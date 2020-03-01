package com.example.android.synapse;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class ResetLogin extends AppCompatActivity
{
    private Button submit,back,signup;
    private TextView resetPassword;
    private EditText passwordView,emailView;
    private ProgressBar progressBar;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.darkblue)));

        submit =findViewById(R.id.loginButton);
        emailView = findViewById(R.id.loginEmail);
        passwordView = findViewById(R.id.loginPassword);
        back = findViewById(R.id.Back);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        signup = findViewById(R.id.signup);
        resetPassword = findViewById(R.id.forgotpassword);

        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.darkgreen)));

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openHome(view);
            }
        });

        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(ResetLogin.this,Signup.class);
                startActivity(i);
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(ResetLogin.this,ResetPassword.class);
                startActivity(i);
            }
        });

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        mFirebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser!=null && mFirebaseUser.isEmailVerified())
        {
            Toast.makeText(ResetLogin.this,""+mFirebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
            DocumentReference drefUser = db.collection("Users").document(""+mFirebaseUser.getEmail());
            drefUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
            {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot)
                {
                    if(documentSnapshot.exists())
                    {
                        Toast.makeText(ResetLogin.this,"User Logged in", Toast.LENGTH_SHORT).show();
                        Intent intToHome= new Intent(ResetLogin.this, HomeActivity.class);
                        finish();
                        getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intToHome);
                    }
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(ResetLogin.this,"Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        submit.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                final String email= emailView.getText().toString();
                final String pwd= passwordView.getText().toString();
                if(email.isEmpty())
                {
                    emailView.setError("Please enter email id");
                    emailView.requestFocus();
                }
                else if(pwd.isEmpty())
                {
                    passwordView.setError("Please enter your password");
                    passwordView.requestFocus();
                }
                else if(email.isEmpty() && pwd.isEmpty())
                {
                    emailView.setError("Please enter email id");
                    emailView.requestFocus();
                    passwordView.setError("Please enter your password");
                    passwordView.requestFocus();
                }
                else if(!(email.isEmpty() && pwd.isEmpty()))
                {   progressBar.setVisibility(View.VISIBLE);
                    mFirebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(ResetLogin.this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            progressBar.setVisibility(View.GONE);
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(ResetLogin.this,"Incorrect Email or Password, Please Login Again ", Toast.LENGTH_SHORT).show();
                                emailView.requestFocus();
                                passwordView.requestFocus();
                            }
                            else
                            {   if(mFirebaseAuth.getCurrentUser().isEmailVerified())
                            {
                                final DocumentReference dref = db.collection("Users").document(""+email);
                                dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                                {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot)
                                    {
                                        if(documentSnapshot.exists())
                                        {
                                            Toast.makeText(ResetLogin.this,"User Login Successful!", Toast.LENGTH_SHORT).show();
                                            dref.update("password",pwd);
                                            Intent intToHome= new Intent(ResetLogin.this, HomeActivity.class);
                                            intToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intToHome);
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(ResetLogin.this,"Not a registered User", Toast.LENGTH_SHORT).show();
                                            emailView.setText("");
                                            passwordView.setText("");
                                            FirebaseAuth.getInstance().signOut();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener()
                                {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {
                                        Toast.makeText(ResetLogin.this,"Error Occurred!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else
                            {
                                FirebaseAuth.getInstance().signOut();
                                Toast.makeText(ResetLogin.this,"Please Verify Email!", Toast.LENGTH_SHORT).show();
                            }
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(ResetLogin.this,"Error Occurred!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void openHome(View view)
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}