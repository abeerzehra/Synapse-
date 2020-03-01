package com.example.android.synapse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity
{
    private EditText emailId;
    private Button send,back,login,signup;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        getSupportActionBar().setTitle("Reset Password");
        this.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.darkred)));

        emailId = findViewById(R.id.email);
        send = findViewById(R.id.send);
        back = findViewById(R.id.back);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);

        mFirebaseAuth=FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ResetPassword.this,LoginActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ResetPassword.this,LoginActivity.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ResetPassword.this,Signup.class));
            }
        });

        send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!(emailId.getText().toString().isEmpty()))
                {
                    String e= emailId.getText().toString();
                    mFirebaseAuth.sendPasswordResetEmail(e).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ResetPassword.this, "Password sent to your email!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetPassword.this, ResetLogin.class));
                                finish();
                                getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            }
                            else
                            {
                                Toast.makeText(ResetPassword.this, "Incorrect email!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                        }
                    });
                }
                else
                {
                    emailId.setError("Enter email");
                    emailId.requestFocus();
                }
            }
        });

    }
}
