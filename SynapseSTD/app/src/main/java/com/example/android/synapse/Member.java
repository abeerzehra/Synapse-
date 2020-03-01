package com.example.android.synapse;

public class Member
{
    String name,email,role,creationDate;

    public Member()
    {

    }

    public Member(String name,String email,String role,String creationDate)
    {
        this.name=name;
        this.email=email;
        this.role=role;
        this.creationDate=creationDate;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getRole()
    {
        return role;
    }

}