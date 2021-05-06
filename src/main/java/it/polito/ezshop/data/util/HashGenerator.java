package it.polito.ezshop.data.util;

import org.apache.commons.codec.binary.Hex;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class HashGenerator {
    private static int iterations = 100;
    private static int keyLength = 64;

    public static byte[] hashPassword( final char[] password, final byte[] salt, final int iterations, final int keyLength ){

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            PBEKeySpec spec = new PBEKeySpec( password, salt, iterations, keyLength );
            SecretKey key = skf.generateSecret( spec );
            byte[] res = key.getEncoded( );
            return res;
        } catch ( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
    }

    public static String[] getPasswordHashAndSalt(String password){
        char[] passwordChars = password.toCharArray();
        byte[] saltBytes = generateSalt();
        byte[] hashedBytes = hashPassword(passwordChars, saltBytes, iterations, keyLength);
        String hashedString = Hex.encodeHexString(hashedBytes);
        return new String[] {hashedString, Hex.encodeHexString(saltBytes)};
    }

    public static byte[] generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static boolean passwordMatches(String dbHashedPass, String password, String salt){
        try {
            char[] passwordChars = password.toCharArray();
            byte[] saltBytes = Hex.decodeHex(salt.toCharArray());
            byte[] hashedBytes = hashPassword(passwordChars, saltBytes, iterations, keyLength);
            String hashedString = Hex.encodeHexString(hashedBytes);
            if (dbHashedPass.equals(hashedString))
                return true;
            return false;
        }catch(org.apache.commons.codec.DecoderException e){
            e.printStackTrace();
        }
        return false;
    }
}