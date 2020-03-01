package com.example.android.synapse;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity
{
    private TextView channel,members;
    private Button logout,profile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        channel =  findViewById(R.id.Channel);
        members =  findViewById(R.id.members);
        logout = findViewById(R.id.homeLogout);
        profile = findViewById(R.id.profile);

        this.getSupportActionBar().hide();

        channel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(view.getContext(),"Your channels!", Toast.LENGTH_SHORT).show();
                openChannels(view);
            }
        });

        members.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(view.getContext(),"Members!", Toast.LENGTH_SHORT).show();
                openMembers(view);
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

        profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
    }

    public void logout()
    {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(HomeActivity.this,LoginActivity.class);
        startActivity(i);
    }

    public void openChannels(View view)
    {
        Intent i = new Intent(this, ChannelsActivity.class);
        startActivity(i);
    }


    public void openMembers(View view)
    {
        Intent i = new Intent(this,MembersActivity.class);
        startActivity(i);
    }
}