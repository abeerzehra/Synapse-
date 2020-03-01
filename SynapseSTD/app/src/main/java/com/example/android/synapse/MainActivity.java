package com.example.android.synapse;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        final Button signup = findViewById(R.id.signup);
        final Button login =  findViewById(R.id.login);

        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(view.getContext(), "signup!", Toast.LENGTH_SHORT).show();
                openSignupPage(signup);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Toast.makeText(view.getContext(), "Login!", Toast.LENGTH_SHORT).show();
                openLoginPage(login);
                Toast.makeText(view.getContext(), "Login!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    public void openSignupPage(View view)
    {
        Intent i = new Intent(this,Signup.class);
        startActivity(i);
    }

    public void openLoginPage(View view)
    {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
    }

}