package com.example.android.synapse;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Locale;

public class Signup extends AppCompatActivity
{
    private Button submit,back,login;
    private EditText name,emailId,password,password2;
    private ProgressBar progressBar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mFirebaseAuth;
    private String naam;
    private String email;
    private String pwd;
    private String pwd2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("User Registeration");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.darkblue)));

        mFirebaseAuth = FirebaseAuth.getInstance();

        submit =  findViewById(R.id.Submit);
        name = findViewById(R.id.Name);
        emailId =  findViewById(R.id.Email);
        password= findViewById(R.id.Password);
        password2= findViewById(R.id.Password2);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        back =  findViewById(R.id.signupBack);
        login = findViewById(R.id.login);

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openMain();
            }
        });

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openLoginPage();
            }
        });

        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                naam = name.getText().toString();
                email = emailId.getText().toString().toLowerCase();
                pwd = password.getText().toString();
                pwd2 = password2.getText().toString();

                progressBar.setVisibility(View.VISIBLE);
                if(pwd.isEmpty() && pwd2.isEmpty() && email.isEmpty() && naam.isEmpty())
                {
                    Toast.makeText(Signup.this,
                            "Name : " + name.getText().toString() + "Email : "+email+"Password : "+pwd,
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    emailId.setError("Please enter email");
                    name.setError("Please enter email");
                    password.setError("Please enter password!");
                    password2.setError("Please enter password!");
                    name.requestFocus();
                    emailId.requestFocus();
                    password.requestFocus();
                    password2.requestFocus();
                }
                else
                {
                    addUser();
                }
            }
        });
    }

    private void addUser()
    {
        String naam = name.getText().toString();
        final String email = emailId.getText().toString().toLowerCase();
        String nuEmail = "@nu.edu.pk";
        String subEmail="";
        String pwd = password.getText().toString();
        String pwd2 = password2.getText().toString();
        final User user = new User(naam,email,pwd,getCurrentTimeStamp());

        if(email.isEmpty())
        {
            progressBar.setVisibility(View.GONE);
            emailId.setError("Please enter email id");
            emailId.requestFocus();
        }
        if(email.length()!=17)
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(Signup.this,
                    "Enter correct nu-email",
                    Toast.LENGTH_SHORT).show();
        }
        else if(email.length()==17)
        {
            progressBar.setVisibility(View.GONE);
            subEmail=email.substring(7,17);
        }
        else if(pwd.isEmpty() && pwd2.isEmpty())
        {
            progressBar.setVisibility(View.GONE);
            password.setError("Please enter password!");
            password2.setError("Please enter password!");
            password.requestFocus();
            password2.requestFocus();
        }
        if(pwd.isEmpty())
        {
            progressBar.setVisibility(View.GONE);
            password.setError("Please enter password!");
            password.requestFocus();
        }
        else if(pwd2.isEmpty())
        {
            progressBar.setVisibility(View.GONE);
            password2.setError("Please enter password!");
            password2.requestFocus();
        }
        else if(email.isEmpty() && (pwd.isEmpty()|| pwd2.isEmpty()))
        {
            progressBar.setVisibility(View.GONE);
            emailId.setError("Please enter all fields!");
            emailId.requestFocus();
            password.requestFocus();
            password2.requestFocus();
        }
        else if(email.length()!=17)
        {
            progressBar.setVisibility(View.GONE);
            emailId.setError("Incorrect nu-email");
            emailId.requestFocus();
        }
        else if(!(pwd.equals(pwd2)))
        {
            progressBar.setVisibility(View.GONE);
            password.setError("Passwords do not match");
            password2.setError("Passwords do not match");
            password.requestFocus();
            password2.requestFocus();
            password.setText("");
            password2.setText("");
        }
        else if(!(nuEmail.equals(subEmail)))
        {
            progressBar.setVisibility(View.GONE);
            emailId.setError("Enter Correct nu-email");
            emailId.setText("");
            emailId.requestFocus();
        }
        else if(!(email.isEmpty() && pwd.isEmpty() && pwd2.isEmpty()) && (pwd.equals(pwd2)))
        {
            progressBar.setVisibility(View.GONE);
            mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        mFirebaseAuth.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(Signup.this,
                                                    "Verify email!", //ADD THIS
                                                    Toast.LENGTH_SHORT).show();
                                            db.collection("Users").document(""+email).set(user);
                                            emailId.setText("");
                                            password.setText("");
                                            emailId.setText("");
                                            password2.setText("");
                                            mFirebaseAuth.getInstance().signOut();
                                            startActivity(new Intent(Signup.this, LoginActivity.class));
                                        }
                                        else
                                        {
                                            Toast.makeText(Signup.this,
                                                    "Signup unsuccessful : " + task.getException().getMessage(), //ADD THIS
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else
                    {
                        Toast.makeText(Signup.this,
                                "Signup unsuccessful: " + task.getException().getMessage(), //ADD THIS
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(Signup.this,"Enter correct nu-email", Toast.LENGTH_SHORT).show();
        }
    }

    private void openLoginPage()
    {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
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

    private String getCurrentTimeStamp()
    {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String timeStamp = df.format("dd-MM-yyyy hh:mm", cal).toString();
        return timeStamp;
    }
}