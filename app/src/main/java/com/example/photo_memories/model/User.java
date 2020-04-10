package com.example.photo_memories.model;

public class User
{
    private String id;
    private String userName;
    private String firstName;
    private String lastName;

    public User(String id, String userName, String firstName, String lastName)
    {
        setId(id);
        setUserName(userName);
        setFirstName(firstName);
        setLastName(lastName);
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

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getId()
    {
        return id;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }
}
