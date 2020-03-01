package com.example.android.synapseadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PendingApprovalsActivity extends AppCompatActivity
{
    private ImageView refresh;
    private Button home,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_approvals);

        refresh = findViewById(R.id.refresh);
        home = findViewById(R.id.Home);
        logout = findViewById(R.id.Logout);

        getSupportActionBar().hide();

        final ArrayList<Approval> list = new ArrayList<Approval>();
        final ApprovalsListAdapter listAdapter = new ApprovalsListAdapter(this,list,R.color.darkred);
        final ListView listView = findViewById(R.id.approvalsList);
        listView.setAdapter(listAdapter);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference pendingChannelRef=db.collection("PendingChannels");
        final CollectionReference channelsRef = db.collection("Channel");

        final String[] currentUserName = new String[1];

        pendingChannelRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
        {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots)
            {
                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots)
                {
                    String username = documentSnapshot.getString("creator");
                    String message = documentSnapshot.getString("message");
                    String creationDate = documentSnapshot.getString("timestamp");
                    String channelName = documentSnapshot.getString("channelName");
                    Approval newApproval = new Approval(channelName,username,R.drawable.ic_check_white_24dp,R.drawable.ic_clear_white_24dp);
                    currentUserName[0]=username;
                    list.add(newApproval);
                    listView.setAdapter(listAdapter);
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id)
            {
                Approval newApproval = list.get(position);
                final String newChannelName = newApproval.getChannelName();

                new AlertDialog.Builder(PendingApprovalsActivity.this)
                        .setTitle("Channel Approval")
                        .setMessage("Do you want to approve this channgel?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Channel newChannel = new Channel(newChannelName,currentUserName[0],getDateFormat(),currentUserName[0],true);
                                channelsRef.document(""+newChannelName).set(newChannel);
                                Task<Void> dref = pendingChannelRef.document(""+newChannelName).delete();
                                getToast(newChannelName, newChannelName+"\tApproved");
                                refresh();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                Task<Void> dref = pendingChannelRef.document(""+newChannelName).delete();
                                            refresh();
                                            getToast(newChannelName,newChannelName+"\tDeleted");
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(PendingApprovalsActivity.this,HomeActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(PendingApprovalsActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                refresh();
            }
        });
    }

    private void refresh()
    {
        finish();
        startActivity(getIntent());
    }

    public String getDateFormat()
    {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String timeStamp = df.format("dd-MM-yyyy hh:mm", cal).toString();
        return timeStamp;
    }

    private void getToast(String head, String text)
    {
        LayoutInflater inflater = getLayoutInflater();

        View toastLayout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_root_view));

        TextView header = (TextView) toastLayout.findViewById(R.id.toast_header);
        header.setText(head);

        TextView body = (TextView) toastLayout.findViewById(R.id.toast_body);
        body.setText(text);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastLayout);
        toast.show();
    }

}