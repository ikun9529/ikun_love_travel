package com.ikun.model.domain;

public class UserHolder {
    private static final ThreadLocal<User> thread = new ThreadLocal<>();

    public static void saveUser( User user){
        thread.set(user);
    }

    public static  User getUser(){
        return thread.get();
    }

    public static void removeUser(){
        thread.remove();
    }
}