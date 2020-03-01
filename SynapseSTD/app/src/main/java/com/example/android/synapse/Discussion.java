package com.example.android.synapse;

import android.util.Log;

public class Discussion
{
    private String userName,channelName,message,timestamp;
    public Discussion()
    {

    }

    public Discussion(String channel,String name, String msg, String timeStamp)
    {
        this.userName=name;
        this.channelName=channel;
        Log.d("tag","ok");
        message=msg;
        this.timestamp=timeStamp;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getChannelName()
    {
        return channelName;
    }

    public void setChannelName(String channelName)
    {
        this.channelName = channelName;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

}
