package com.example.photo_memories;

import com.example.photo_memories.model.User;

import org.junit.Test;
import static org.junit.Assert.*;

public class UserModelTest
{
    @Test
    public void constructor_with_parameter_isCorrect()
    {
        String firstName = "John";
        String lastName = "Doe";
        String id = "1234567";
        String username = "JDoe123";

        User user = new User(id, username, firstName, lastName);
        assertEquals(id, user.getId());
        assertEquals(username, user.getUserName());
    }

    @Test
    public void default_constructor_isCorrect()
    {
        User user = new User();
        assertEquals("", user.getId());
        assertEquals("", user.getUserName());
    }

    @Test
    public void setId_method_isCorrect()
    {
        User user = new User("12345", "", "", "");
        assertEquals("12345", user.getId());
    }

    @Test
    public void getID_method_isCorrect()
    {
        User user = new User();
        user.setId("12345");
        assertEquals("12345", user.getId());
    }
}
