package com.example.android.synapseadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DiscussionActivity extends AppCompatActivity
{
    private TextView home,back,logout,heading;
    private Button newMsg;
    private ListView list;
    final Context context = this;
    private FirebaseAuth mFirebaseAuth;
    String channelName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);
        this.getSupportActionBar().hide();


        Intent intent =getIntent();
        channelName=intent.getStringExtra("channelName"); //selected channel

        Toast.makeText(DiscussionActivity.this,""+channelName, Toast.LENGTH_SHORT).show();

        mFirebaseAuth = FirebaseAuth.getInstance();

        home = findViewById(R.id.Home);
        back = findViewById(R.id.Back);
        logout = findViewById(R.id.Logout);
        list = findViewById(R.id.discussionList);
        heading = findViewById(R.id.channelName);
        heading.setText(channelName);

        final ArrayList<Discussion> list = new ArrayList<Discussion>();
        final DiscussionAdapter listAdapter = new DiscussionAdapter(this,list,R.color.darkpurple);
        final ListView listView = findViewById(R.id.discussionList);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference discussionRef = db.collection("Discussions");

        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openHome();
            }
        });
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                back();
            }
        });
        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(DiscussionActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        discussionRef.whereEqualTo("channelName",channelName).orderBy("timestamp", Query.Direction.ASCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
        {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots)
            {
                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots)
                {
                    String username = documentSnapshot.getString("userName");
                    String message = documentSnapshot.getString("message");
                    String creationDate = documentSnapshot.getString("timestamp");
                    Discussion currentDiscussionList = new Discussion(channelName,username,message,creationDate);
                    list.add(currentDiscussionList);
                    listView.setAdapter(listAdapter);
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {

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
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(DiscussionActivity.this,ChannelsActivity.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(DiscussionActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id)
            {
                Discussion message = list.get(position);
                Toast.makeText(DiscussionActivity.this,"Message  : "+ message.getMessage()+"\nMember name : " + message.getUserName() +"\n"+message.getTimestamp(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openHome()
    {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    public void back()
    {
        Intent i = new Intent(this, ChannelsActivity.class);
        startActivity(i);
    }
}