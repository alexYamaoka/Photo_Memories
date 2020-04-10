package com.example.photo_memories.model;

public class Post
{
    private String postId;
    private String memoryId;
    private String postImage;
    private String description;
    private String location;
    private String date;

    public Post(String postId, String memoryId, String postImage, String description, String location, String date)
    {
        setPostId(postId);
        setMemoryId(memoryId);
        setPostImage(postImage);
        setDescription(description);
        setLocation(location);
        setDate(date);
    }

    public Post()
    {
        this("", "", "", "","", "");
    }

    public void setPostId(String postId)
    {
        this.postId = postId;
    }

    public void setMemoryId(String memoryId)
    {
        this.memoryId = memoryId;
    }

    public void setPostImage(String postImage)
    {
        this.postImage = postImage;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getPostId()
    {
        return postId;
    }

    public String getMemoryId()
    {
        return memoryId;
    }

    public String getPostImage()
    {
        return postImage;
    }

    public String getDescription()
    {
        return description;
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
