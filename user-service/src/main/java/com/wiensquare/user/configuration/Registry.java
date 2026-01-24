package com.wiensquare.user.configuration;

import com.wiensquare.user.domain.entity.UserEntity;

public class Registry {

    private static UserEntity CURRENT_USER;

    public static void setCurrentUser(UserEntity currentUser) {
        CURRENT_USER = currentUser;
    }

    public static UserEntity getCurrentUser() {
        return CURRENT_USER;
    }

}
