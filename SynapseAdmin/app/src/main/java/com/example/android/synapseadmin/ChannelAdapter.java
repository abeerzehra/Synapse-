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


public class ChannelAdapter extends ArrayAdapter<Channel>
{
    int Color;
    public ChannelAdapter(Activity context, ArrayList<Channel> channels, int color)
    {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, channels);
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
                    R.layout.list_item, parent, false);
            Log.d("yahan","hoon yahan");
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Channel currentChannel= getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.name);
        nameTextView.setText(currentChannel.getChannelName());
        TextView creatorTextView = (TextView) listItemView.findViewById(R.id.creator);
        creatorTextView.setText("Created By : "+currentChannel.getCreator());
        TextView creationDateTextVIew = (TextView) listItemView.findViewById(R.id.timeStamp);
        creationDateTextVIew.setText("Date created : "+currentChannel.getCreationDate());

        LinearLayout linearLayout = (LinearLayout) listItemView.findViewById(R.id.text);
        int bgColor = ContextCompat.getColor(getContext(),Color);
        linearLayout.setBackgroundColor(bgColor);

        return listItemView;
    }

}
