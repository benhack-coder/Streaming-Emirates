package common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

    public static String getsha512(String password){
        String generatedpassword=null;
        String salt="awerigfizzfvegwizve";
        try{
            MessageDigest md=MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[]bytes=md.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb=new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedpassword=sb.toString();
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedpassword;
    }
}


