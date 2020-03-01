package com.example.android.synapse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PrivateChat extends AppCompatActivity
{
    private Button openList,sendButton,home,back,logout;
    private EditText newMessage;
    private FirebaseAuth mFirebaseAuth;
    private String[] selectedUserData = new String[2];
    private String [] currentUserName = new String[1];
    private String currentUserEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_chat);
        getSupportActionBar().hide();

        Intent intent =getIntent();
        selectedUserData = intent.getStringArrayExtra("selectedUser"); //user to chat with
        //Toast.makeText(PrivateChat.this,"Selected User name : "+ selectedUserData[0] +"\n Selected User email : "+selectedUserData[1], Toast.LENGTH_SHORT).show();

        newMessage = findViewById(R.id.newMessage);
        sendButton = findViewById(R.id.messageSend);
        home = findViewById(R.id.Home);
        back = findViewById(R.id.Back);
        logout = findViewById(R.id.Logout);


        final ArrayList<ChatMessage> list = new ArrayList<ChatMessage>();
        final ChatMessageAdapter listAdapter = new ChatMessageAdapter(this,list,R.color.maroon);
        final ListView listView = findViewById(R.id.messageList);

        mFirebaseAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference memberRef=db.collection("Users");

        currentUserEmail = mFirebaseAuth.getCurrentUser().getEmail();
        final DocumentReference userDocRef= memberRef.document(currentUserEmail);

        userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if(documentSnapshot.exists())
                {
                    currentUserName[0] =documentSnapshot.getString("name");
                }
                else
                    {
                        Toast.makeText(PrivateChat.this,"Document does not exist: ", Toast.LENGTH_SHORT).show();
                    }
            }

        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(PrivateChat.this,"Error: "+ e, Toast.LENGTH_SHORT).show();
            }
        });

        String user1="";
        String user2="";
        user1 = currentUserEmail.substring(0,7); //rollnumber of current
        user2 = selectedUserData[1].substring(0,7); //rollnumber of selected

        String chat1 = user1+user2; //current + selected
        String chat2 = user2+user1; //seleted + current

        final CollectionReference chatRef = db.collection("chatMessages").document(chat1).collection("Messages");

        chatRef.orderBy("timeStamp", Query.Direction.ASCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
        {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots)
            {
                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots)
                {
                    String name = documentSnapshot.getString("name");
                    String message = documentSnapshot.getString("message");
                    String timeStamp = documentSnapshot.getString("timeStamp");
                    String email =documentSnapshot.getString("email");
                    ChatMessage  newMessage = new ChatMessage(email,name,message,timeStamp);
                    list.add(newMessage);
                    listView.setAdapter(listAdapter);
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            { }
        });

        final CollectionReference chat1CRef = db.collection("chatMessages").document(chat1).collection("Messages");
        final CollectionReference chat2CRef = db.collection("chatMessages").document(chat2).collection("Messages");

        sendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String msg = newMessage.getText().toString();
                if(!(msg.isEmpty()))
                {
                    newMessage.setText("");
                    ChatMessage  message = new ChatMessage(currentUserEmail,currentUserName[0],msg,getCurrentTimeStamp());
                    chat1CRef.document(msg).set(message).addOnSuccessListener(new OnSuccessListener<Void>()
                    {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                        }
                    }).addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(PrivateChat.this,"Error : " + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                    chat2CRef.document(msg).set(message).addOnSuccessListener(new OnSuccessListener<Void>()
                    {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                        }
                    }).addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(PrivateChat.this,"Error : " + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                    list.add(message);
                    listView.setAdapter(listAdapter);
                }
            }
        });
        listView.setAdapter(listAdapter);

        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openHome(view);
            }
        });
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                back(view);
            }
        });
        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(PrivateChat.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


    }
    public String getCurrentTimeStamp()
    {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String timeStamp = df.format("dd-MM-yyyy hh:mm", cal).toString();
        return timeStamp;
    }

    public void openHome(View view)
    {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    public void back(View view)
    {
        Intent i = new Intent(this, MembersActivity.class);
        startActivity(i);
    }
}
