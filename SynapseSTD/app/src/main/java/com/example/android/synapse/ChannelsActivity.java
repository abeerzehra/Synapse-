package com.example.android.synapse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

public class ChannelsActivity extends AppCompatActivity
{
    private TextView home,back,logout;
    private Button addChannel;
    private ImageView refresh;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channels_list);
        this.getSupportActionBar().hide();

         home = findViewById(R.id.channelHome);
         logout = findViewById(R.id.channelLogout);
         addChannel = findViewById(R.id.addChannel);
         refresh = findViewById(R.id.refresh);

         final ArrayList<Channel> list = new ArrayList<Channel>();

        mFirebaseAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference channelRef=db.collection("Channel");

        final ChannelAdapter listAdapter = new ChannelAdapter(this,list,R.color.darkpurple);
        final ListView listView = findViewById(R.id.channelList);

        channelRef.orderBy("channelName", Query.Direction.ASCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
        {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots)
            {
                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots)
                {
                    String channelName = documentSnapshot.getString("channelName");
                    String creator = documentSnapshot.getString("creator");
                    boolean approved = documentSnapshot.getBoolean("approved");
                    String creationDate = documentSnapshot.getString("creationDate");
                    Channel newChannel = new Channel(channelName.toUpperCase(),creationDate,creator,approved);
                    if(approved)
                    {
                        list.add(newChannel);
                    }
                    listView.setAdapter(listAdapter);
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            { }
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
                Intent intent = new Intent(ChannelsActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        addChannel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openAddChannel();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                refresh();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id)
            {
                Channel channel = list.get(position);
                String currentChannelName = channel.getChannelName();
                Intent i=new Intent(ChannelsActivity.this,DiscussionActivity.class);
                i.putExtra("channelName",currentChannelName);
                startActivity(i);
            }
        });
    }

    private void refresh()
    {
        finish();
        startActivity(getIntent());
    }

    private void openHome()
    {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    private void openAddChannel()
    {
        Intent i = new Intent(this, AddChannelActivity.class);
        startActivity(i);
    }
}