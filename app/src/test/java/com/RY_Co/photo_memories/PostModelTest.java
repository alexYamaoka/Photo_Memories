package com.RY_Co.photo_memories;
import com.RY_Co.photo_memories.model.Post;

import org.junit.Test;

import static org.junit.Assert.*;

public class PostModelTest
{

    @Test
    public void default_constructor_isCorrect()
    {
        Post post = new Post();
        assertEquals("", post.getDate());
        assertEquals("", post.getDescription());
        assertEquals("", post.getLocation());
        assertEquals("", post.getMemoryId());
        assertEquals("", post.getPostId());
        assertEquals("", post.getPostImage());
    }

    @Test
    public void setDate_isCorrect()
    {
        Post post = new Post();
        post.setDate("12/12/12");
        assertEquals("12/12/12", post.getDate());
    }

    @Test
    public void setDescription_isCorrect()
    {
        Post post = new Post();
        post.setDescription("This is a description");
        assertEquals("This is a description", post.getDescription());
    }

    @Test
    public void setLocation_isCorrect()
    {
        Post post = new Post();
        post.setLocation("This is a location");
        assertEquals("This is a location", post.getLocation());
    }

    @Test
    public void setMemoryId_isCorrect()
    {
        Post post = new Post();
        post.setMemoryId("1234567");
        assertEquals("1234567", post.getMemoryId());
    }


    @Test
    public void setPostId_isCorrect()
    {
        Post post = new Post();
        post.setPostId("1234567");
        assertEquals("1234567", post.getPostId());
    }
}
