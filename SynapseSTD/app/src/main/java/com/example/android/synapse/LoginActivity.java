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

public class LoginActivity extends AppCompatActivity
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
        getSupportActionBar().setTitle("\t\tLogin");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.darkblue)));

        submit =findViewById(R.id.loginButton);
        emailView = findViewById(R.id.loginEmail);
        passwordView = findViewById(R.id.loginPassword);
        back = findViewById(R.id.Back);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        signup = findViewById(R.id.signup);
        resetPassword = findViewById(R.id.forgotpassword);

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openMain();
            }
        });

        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(LoginActivity.this,Signup.class);
                startActivity(i);
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(LoginActivity.this,ResetPassword.class);
                startActivity(i);
            }
        });

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        mFirebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser!=null && mFirebaseUser.isEmailVerified())
        {
            Toast.makeText(LoginActivity.this,""+mFirebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
            DocumentReference drefUser = db.collection("Users").document(""+mFirebaseUser.getEmail());
            drefUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
            {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot)
                {
                    if(documentSnapshot.exists())
                    {
                        Toast.makeText(LoginActivity.this,"User Logged in", Toast.LENGTH_SHORT).show();
                        Intent intToHome= new Intent(LoginActivity.this, HomeActivity.class);
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
                    Toast.makeText(LoginActivity.this,"Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        submit.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                final String email= emailView.getText().toString();
                String pwd= passwordView.getText().toString();
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
                    mFirebaseAuth.signInWithEmailAndPassword(email.toLowerCase(),pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            progressBar.setVisibility(View.GONE);
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(LoginActivity.this,"Incorrect email or password", Toast.LENGTH_SHORT).show();
                                emailView.setText("");
                                passwordView.setText("");
                                emailView.requestFocus();
                                passwordView.requestFocus();
                            }
                            else
                            {   if(mFirebaseAuth.getCurrentUser().isEmailVerified())
                                {
                                    DocumentReference dref = db.collection("Users").document(""+email);
                                    dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                                    {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot)
                                        {
                                            if(documentSnapshot.exists())
                                            {
                                                Toast.makeText(LoginActivity.this,"User Login Successful!", Toast.LENGTH_SHORT).show();
                                                Intent intToHome= new Intent(LoginActivity.this, HomeActivity.class);
                                                intToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intToHome);
                                                finish();
                                            }
                                            else
                                            {
                                                Toast.makeText(LoginActivity.this,"Not a registered User", Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(LoginActivity.this,"Error Occurred!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                }
                                else
                                {
                                    FirebaseAuth.getInstance().signOut();
                                    Toast.makeText(LoginActivity.this,"Please Verify Email!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(LoginActivity.this,"Error Occurred!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void openMain()
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed()
    {
        openMain();
    }
}