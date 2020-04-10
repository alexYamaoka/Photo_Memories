package com.example.photo_memories.model;

public class User
{
    private String id;
    private String userName;

    public User(String id, String userName, String firstName, String lastName)
    {
        setId(id);
        setUserName(userName);
    }

    public User()
    {
        this("", "", "", "");
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getId()
    {
        return id;
    }

    public String getUserName()
    {
        return userName;
    }
}
