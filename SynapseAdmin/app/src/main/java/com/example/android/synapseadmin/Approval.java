package com.example.android.synapseadmin;

public class Approval
{
    String channelName,creator;
    int check,clear;
    Approval()
    {

    }

    Approval(String channelName,String creator,int check,int clear)
    {
        this.channelName=channelName;
        this.creator=creator;
        this.check=check;
        this.clear=clear;
    }

    public int getCheck()
    {
        return check;
    }

    public void setCheck(int check)
    {
        this.check = check;
    }

    public int getClear()
    {
        return clear;
    }

    public void setClear(int clear) {
        this.clear = clear;
    }

    public String getChannelName()
    {
        return channelName;
    }

    public void setChannelName(String channelName)
    {
        this.channelName = channelName;
    }

    public String getCreator()
    {
        return creator;
    }

    public void setCreator(String creator)
    {
        this.creator = creator;
    }
}
