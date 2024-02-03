package com.checkconsulting.proepargne.utils;

public class KeycloakUserNameHandler {

    public static String generateUserName(String firstName, String lastName) {
        return firstName.charAt(0) + lastName;
    }
}
