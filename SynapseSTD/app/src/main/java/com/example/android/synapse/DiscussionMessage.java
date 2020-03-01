package com.example.android.synapse;

public class DiscussionMessage
{

    private String userName,channelName,message,timestamp;
    public DiscussionMessage()
    {

    }

    public DiscussionMessage(String name, String msg, String timeStamp,String channelName)
    {
        this.userName=name;
        this.channelName=channelName;
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

    public String getTimeStamp()
    {
        return timestamp;
    }
}
