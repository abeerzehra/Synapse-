package com.example.android.synapseadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.synapseadmin.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MembersActivity extends AppCompatActivity
{

    private Button home,logout;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        home = findViewById(R.id.memberHome);
        logout =  findViewById(R.id.memberLogout);
        this.getSupportActionBar().hide();

        final ArrayList<Member> list = new ArrayList<Member>();
        final MemberAdapter listAdapter = new MemberAdapter(this,list,R.color.darkskyblue);
        final ListView listView = findViewById(R.id.membersList);

        mFirebaseAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference memberRef=db.collection("Users");

        memberRef.orderBy("name")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
        {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots)
            {
                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots)
                {
                    String memberName = documentSnapshot.getString("name");
                    String memberEmail = documentSnapshot.getString("email");
                    String memberRole = documentSnapshot.getString("role");
                    String memberCreationDate = documentSnapshot.getString("creationDate");
                    Member newMember = new Member(memberName,memberEmail,memberRole,memberCreationDate);
                    if(newMember.getRole().equals("user") && !(mFirebaseAuth.getCurrentUser().getEmail().equals(memberEmail)))
                    {
                        list.add(newMember);
                    }
                    listView.setAdapter(listAdapter);
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(MembersActivity.this,"Error: "+ e, Toast.LENGTH_SHORT).show();
            }
        });

        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openHome(view);
            }
        });

        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(MembersActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id)
            {

            }
        });
    }

    public void openHome(View view)
    {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    public void openMain(View view)
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public String getCurrentTimeStamp()
    {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String timeStamp = df.format("dd-MM-yyyy hh:mm", cal).toString();
        return timeStamp;
    }
}