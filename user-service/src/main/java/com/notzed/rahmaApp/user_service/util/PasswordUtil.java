package com.notzed.rahmaApp.user_service.util;

import org.mindrot.jbcrypt.BCrypt;


public class PasswordUtil {

    public static String hashPassword(String textPlainPassword){
        return BCrypt.hashpw(textPlainPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String textPlainPassword, String hashedPassword){
        return BCrypt.checkpw(textPlainPassword, hashedPassword);
    }
}
