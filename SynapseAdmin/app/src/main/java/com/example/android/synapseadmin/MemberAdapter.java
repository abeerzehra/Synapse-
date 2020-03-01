package com.example.android.synapseadmin;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;

public class MemberAdapter extends ArrayAdapter<Member>
{
    int Color;
    public MemberAdapter(Activity context, ArrayList<Member> members, int color)
    {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, members);
        Color=color;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.member_list_item, parent, false);
            Log.d("yahan","hoon yahan");
        }

        Member currentMember = getItem(position);

        TextView nameTextView = listItemView.findViewById(R.id.memberName);
        nameTextView.setText(currentMember.getName());

        TextView emailTextView = listItemView.findViewById(R.id.memberEmail);
        emailTextView.setText(currentMember.getEmail());

        TextView creationDateTextView = listItemView.findViewById(R.id.timeStamp);
        creationDateTextView.setText("Member from : "+currentMember.getCreationDate());

        LinearLayout linearLayout = listItemView.findViewById(R.id.text);
        int bgColor = ContextCompat.getColor(getContext(),Color);
        linearLayout.setBackgroundColor(bgColor);

        return listItemView;
    }
}