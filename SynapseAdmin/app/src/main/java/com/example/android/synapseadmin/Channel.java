package com.example.android.synapseadmin;

public class Channel
{
    String channelName,username,creationDate,creator;
    boolean approved;

    public Channel()
    {

    }

    public Channel(String name,String username, String date, String creator)
    {
        this.channelName=name;
        this.username=username;
        creationDate=date;
        this.creator=creator;
    }
    public Channel(String name,String username, String date, String creator,boolean approved)
    {
        this.channelName=name;
        this.username=username;
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