package com.example.android.synapse;

public class User
{
    public String email,password,name,role,creationDate;

    public User()
    {

    }

    public User(String name, String email, String password,String time)
    {
        this.name=name;
        this.email = email;
        this.password=password;
        this.role="user";
        this.creationDate=time;
    }

    public String getCreationDate()
    {
        return creationDate;
    }

    public String getEmail()
    {
        return email;
    }

    public String getName()
    {
        return name;
    }

    public String getRole()
    {
        return role;
    }
}