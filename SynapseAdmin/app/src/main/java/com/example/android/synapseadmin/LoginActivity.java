package com.example.android.synapseadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class LoginActivity extends AppCompatActivity
{
    private Button submit,back;
    private EditText passwordView,emailView;
    private ProgressBar progressBar;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Admin Login");
        this.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.darkblue)));
        mFirebaseAuth = FirebaseAuth.getInstance();

        submit =findViewById(R.id.loginButton);
        emailView = findViewById(R.id.loginEmail);
        passwordView = findViewById(R.id.loginPassword);
        back = findViewById(R.id.loginBack);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.darkpurple)));

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openMain(view);
            }
        });

        if(mFirebaseUser!=null)
        {
            Toast.makeText(LoginActivity.this,""+mFirebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
            DocumentReference drefUser = db.collection("Users").document("Admin");
            drefUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
            {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot)
                {
                    if(documentSnapshot.exists())
                    {
                        String role=documentSnapshot.getString("role");
                        Toast.makeText(LoginActivity.this,"Admin Logged in", Toast.LENGTH_SHORT).show();
                        Intent intToHome= new Intent(LoginActivity.this, HomeActivity.class);
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
                {    progressBar.setVisibility(View.VISIBLE);
                     mFirebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            progressBar.setVisibility(View.GONE);
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(LoginActivity.this,"Incorrect Email or Password, Please Login Again ", Toast.LENGTH_SHORT).show();
                                emailView.requestFocus();
                                passwordView.requestFocus();
                            }
                            else
                            {
                                DocumentReference dref = db.collection("Users").document("cr.kumail.79@gmail.com");
                                dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                                {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot)
                                    {
                                        if(documentSnapshot.exists())
                                        {
                                            if(mFirebaseAuth.getCurrentUser().getEmail().equals("admin@synapse.com"))
                                            {
                                                Toast.makeText(LoginActivity.this,"Admin Login Successful!", Toast.LENGTH_SHORT).show();
                                                Intent intToHome= new Intent(LoginActivity.this, HomeActivity.class);
                                                intToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intToHome);
                                                finish();
                                            }
                                            else
                                            {
                                                Toast.makeText(LoginActivity.this,"Not a registered Admin", Toast.LENGTH_SHORT).show();
                                                emailView.setText("");
                                                passwordView.setText("");
                                                FirebaseAuth.getInstance().signOut();
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(LoginActivity.this,"Not a registered Admin", Toast.LENGTH_SHORT).show();
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
                        }
                    });
                }
                else {
                    Toast.makeText(LoginActivity.this,"Error Occurred!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void openMain(View view)
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
