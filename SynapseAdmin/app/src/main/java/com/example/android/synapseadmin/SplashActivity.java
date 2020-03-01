package com.example.android.synapseadmin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity
{
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mFirebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        final boolean[] AdminLoggedIn = {false};

        if(mFirebaseUser!=null)
        {
                        if(mFirebaseAuth.getCurrentUser().getEmail().equals("admin@synapse.com"))
                        {
                            Intent intToHome= new Intent(SplashActivity.this, HomeActivity.class);
                            AdminLoggedIn[0] =true;
                        }
                        else
                        {
                            FirebaseAuth.getInstance().signOut();
                        }
        }
        else
        {
            AdminLoggedIn[0]=false;
        }

        Thread myThread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    sleep(2000);
                    if(AdminLoggedIn[0])
                    {
                        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    finish();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                super.run();
            }
        };
        myThread.start();
    }
}
