package com.example.photo_memories.model;

public class Post
{
    private String postId;
    private String postImage;
    private String description;
    private String date;

    public Post(String postId, String postImage, String description, String date)
    {
        setPostId(postId);
        setPostImage(postImage);
        setDescription(description);
        setDate(date);
    }

    public Post()
    {
        this("", "", "", "");
    }

    public void setPostId(String postId)
    {
        this.postId = postId;
    }

    public void setPostImage(String postImage)
    {
        this.postImage = postImage;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getPostId()
    {
        return postId;
    }

    public String getPostImage()
    {
        return postImage;
    }

    public String getDescription()
    {
        return description;
    }

    public String getDate()
    {
        return date;
    }
}
