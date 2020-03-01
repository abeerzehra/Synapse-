package com.example.android.synapse;

public class Channel
{
    String channelName,creationDate,creator;
    boolean approved;

    public Channel()
    {

    }

    public Channel(String name,String date,String creator,boolean approved)
    {
        this.channelName=name;
        creationDate=date;
        this.creator=creator;
        this.approved=approved;
    }

    public String getChannelName()
    {
        return channelName;
    }

    public void setChannelName(String name)
    {
        this.channelName= name;
    }

    public String getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(String creationDate)
    {
        this.creationDate = creationDate;
    }

    public String getCreator()
    {
        return creator;
    }

    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    public boolean isApproved()
    {
        return approved;
    }

    public void setApproved(boolean approved)
    {
        this.approved = approved;
    }
}