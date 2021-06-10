package it.polito.ezshop.data.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserClassTest {

    private static UserClass user = new UserClass(1, "username", "password", "salt", "Administrator");

    @Test
    public void testSetId() {
        user.setId(2);
        assertEquals(user.getId(), Integer.valueOf(2));

    }

    @Test
    public void testSetUsername() {
        user.setUsername("newName");
        assertEquals(user.getUsername(), "newName");
    }

    @Test
    public void testSetPassword() {
        user.setPassword("123");
        assertEquals(user.getPassword(), "123");
    }

    @Test
    public void testSetRole() {
        user.setRole("Cashier");
        assertEquals(user.getRole(), "Cashier");
    }

    @Test
    public void testSetSalt() {
        user.setSalt("abc");
        assertEquals(user.getSalt(), "abc");
    }
}