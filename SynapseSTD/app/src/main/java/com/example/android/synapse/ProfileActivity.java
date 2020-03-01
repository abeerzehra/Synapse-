package com.example.android.synapse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity
{
    private Button back,home,logout,changePassword,deleteAcc;
    private String naam="";
    private TextView emailId,name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle("User Profile");
        this.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.darkred)));

        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        logout = findViewById(R.id.logout);
        emailId = findViewById(R.id.email);
        name = findViewById(R.id.name);
        changePassword = findViewById(R.id.changePass);
        deleteAcc = findViewById(R.id.delete);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //DocumentReference dref = db.document("Users/"+emailId.getText().toString());
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference dref = db.collection("Users").document(""+ email);
        emailId.setText(email);

        dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if(documentSnapshot.exists())
                {
                    naam = documentSnapshot.getString("name");
                    name.setText(naam);
                }
            }

        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            { }
        });



        changePassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ProfileActivity.this,ChangePasswordActivity.class));
            }
        });

        deleteAcc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                delete();
            }
        });

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(ProfileActivity.this,HomeActivity.class);
                startActivity(i);
            }
        });

        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(ProfileActivity.this,HomeActivity.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(ProfileActivity.this,MainActivity.class);
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

    private void delete()
    {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference dref = db.collection("Users").document(emailId.getText().toString());
        if(user!=null)
        {
            FirebaseAuth.getInstance().signOut();
            new AlertDialog.Builder(this)
                    .setTitle("Delete user")
                    .setMessage("Are you sure you want to delete your account?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // Continue with delete operation
                            dref.delete();
                            user.delete();
                            Toast.makeText(ProfileActivity.this, "Your account has been deleted", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                            finish();
                            getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}