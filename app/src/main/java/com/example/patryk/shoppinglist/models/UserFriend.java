package com.example.patryk.shoppinglist.models;

import java.util.ArrayList;
import java.util.List;

public class UserFriend {
    public static List<User> currentFriends = new ArrayList<>();
    private User user;
    private User friend;
    private int id;

    public UserFriend(User user, User friend, int id) {
        this.user = user;
        this.friend = friend;
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
