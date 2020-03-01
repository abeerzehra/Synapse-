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
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePasswordActivity extends AppCompatActivity
{
    private Button back,home,logout,send;
    private EditText pass1,pass2;
    private TextView emailId;
    private ProgressBar progressBar;
    String email="",pwd1="",pwd2="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setTitle("Change Password");
        this.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.darkred)));

        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        logout = findViewById(R.id.logout);
        emailId = findViewById(R.id.email);
        pass1 = findViewById(R.id.Password);
        pass2 = findViewById(R.id.Password2);
        emailId.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        email = emailId.getText().toString();
        pwd1 = pass1.getText().toString();
        pwd2 = pass2.getText().toString();
        send = findViewById(R.id.send);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                progressBar.setVisibility(View.VISIBLE);
                if(!(pass1.getText().toString().isEmpty()) && !(pass2.getText().toString().isEmpty()))
                {
                    changePassword();
                    progressBar.setVisibility(View.GONE);
                }
                else
                {
                    Toast.makeText(ChangePasswordActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                    emailId.requestFocus();
                    pass1.requestFocus();
                    pass2.requestFocus();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(ChangePasswordActivity.this,ProfileActivity.class);
                startActivity(i);
            }
        });

        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(ChangePasswordActivity.this,HomeActivity.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(ChangePasswordActivity.this,MainActivity.class);
                changeActivity(i);
            }
        });
    }

    private void changeActivity(Intent i)
    {
        finish();
        FirebaseAuth.getInstance().signOut();
        getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void changePassword()
    {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference dref = db.collection("Users").document(emailId.getText().toString());
        if(user!=null)
        {
            if(pass1.getText().toString().equals(pass2.getText().toString()))
            {
                AuthCredential credential = EmailAuthProvider
                        .getCredential(emailId.getText().toString(), pass1.getText().toString());
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                    user.updatePassword(pass1.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {   dref.update("password",pass1.getText().toString());
                                                Toast.makeText(ChangePasswordActivity.this, "Password updated", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(ChangePasswordActivity.this,LoginActivity.class));
                                                finish();
                                                FirebaseAuth.getInstance().signOut();
                                            }
                                            else
                                                {
                                                Toast.makeText(ChangePasswordActivity.this, "Password updated failed try another password", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            }
                        });
            }
            else
            {
                Toast.makeText(this, "Passwords are not equal!", Toast.LENGTH_SHORT).show();
                pass1.setText("");
                pass1.requestFocus();
                pass2.setText("");
                pass2.requestFocus();
            }
        }
        else
        {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
    }
}