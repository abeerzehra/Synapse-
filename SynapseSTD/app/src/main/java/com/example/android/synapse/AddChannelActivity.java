package com.example.android.synapse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddChannelActivity extends AppCompatActivity
{
    private Button back,home,logout,addChannelSubmit;
    private FirebaseAuth mFirebaseAuth;
    private EditText newChannelView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_channel);
        getSupportActionBar().hide();

        back = findViewById(R.id.addChannelBack);
        home = findViewById(R.id.addChannelHome);
        logout = findViewById(R.id.addChannelLogout);
        addChannelSubmit= findViewById(R.id.addChannelSubmit);
        newChannelView = findViewById(R.id.newChannelName);

        mFirebaseAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference channelRef=db.collection("Channel");
        final CollectionReference pendingChannelRef=db.collection("PendingChannels");

        final String currentUserEmail = mFirebaseAuth.getCurrentUser().getEmail();
        final DocumentReference docRef=channelRef.document(""+currentUserEmail);
        CollectionReference usersRef= db.collection("Users");
        DocumentReference userRef = usersRef.document(""+currentUserEmail);
        final String[] currentUserName = new String[1];
        final User[] currentUser = new User[1];

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                    if(documentSnapshot.exists())
                    {
                        currentUser[0] = documentSnapshot.toObject(User.class);
                        currentUserName[0] =currentUser[0].getName();
                    }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {

            }
        });
        addChannelSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String newChannelName = newChannelView.getText().toString();
                if(!(newChannelName.isEmpty()))
                {
                    String creator = currentUserName[0];
                    String date = getDateFormat();
                    Channel newChannel = new Channel(newChannelName,date,creator,false);
                    pendingChannelRef.document(""+newChannelName).set(newChannel);
                    newChannelView.setText("");
                    Toast.makeText(AddChannelActivity.this,"Wait for admin's approval", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddChannelActivity.this,ChannelsActivity.class));
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openChannels();
            }
        });

        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openHome();
            }
        });

        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(AddChannelActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private String getDateFormat()
    {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String timeStamp = df.format("dd-MM-yyyy hh:mm", cal).toString();
        return timeStamp;
    }

    private void openHome()
    {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    private void openChannels()
    {
        Intent i = new Intent(this, ChannelsActivity.class);
        startActivity(i);
    }
}