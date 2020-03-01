package com.example.android.synapseadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity
{
    private TextView pendingRequests,channels,members;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logout = findViewById(R.id.homeLogout);
        channels = findViewById(R.id.Channels);
        members = findViewById(R.id.Members);

        this.getSupportActionBar().setTitle("\tSynapse Admin");
        this.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.darkblue)));

        channels.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(HomeActivity.this,ChannelsActivity.class);
                startActivity(i);
            }
        });

        members.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(HomeActivity.this,MembersActivity.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        pendingRequests = findViewById(R.id.pendingApprovals);
        pendingRequests.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(HomeActivity.this,PendingApprovalsActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
    }
}