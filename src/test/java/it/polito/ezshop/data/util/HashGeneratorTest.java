package it.polito.ezshop.data.util;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import it.polito.ezshop.data.util.HashGenerator;

import static org.junit.Assert.*;

public class HashGeneratorTest {

    private static final int iterations = 100;
    private static final int keyLength = 64;


    @Test
    public void testPasswordMatches() {
        String[] hashedBytes = HashGenerator.getPasswordHashAndSalt("newPass");
        String hashedPass = hashedBytes[0];
        String salt = hashedBytes[1];
        assertTrue(HashGenerator.passwordMatches(hashedPass, "newPass" ,salt));
        assertFalse(HashGenerator.passwordMatches(hashedPass, "NewPass", salt));
    }
    @Test
    public void testHashPassword() {
        char[] passwordChars = "newPass".toCharArray();
        byte[] saltBytes = HashGenerator.generateSalt();
        assertEquals(byte[].class,HashGenerator.hashPassword(passwordChars, saltBytes, iterations, keyLength).getClass());
    }
}