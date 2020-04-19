package com.RY_Co.photo_memories.model;

import java.util.UUID;

public class Memory
{
    private String id;
    private String title;
    private String location;
    private String date;

    public Memory(String id, String title, String location, String date)
    {
        setId(id);
        setTitle(title);
        setLocation(location);
        setDate(date);
    }

    public Memory(String title, String location, String date)
    {
        createId();
        setTitle(title);
        setLocation(location);
        setDate(date);
    }

    public Memory()
    {
        this( "", "", "", "");
    }

    public void createId()
    {
        this.id = UUID.randomUUID().toString();
    }

    private void setId(String id)
    {

        this.id = id;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getLocation()
    {
        return location;
    }

    public String getDate()
    {
        return date;
    }
}
