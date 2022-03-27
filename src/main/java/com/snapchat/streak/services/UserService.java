package com.snapchat.streak.services;

import com.snapchat.streak.entity.User;

import java.util.List;

public interface UserService {

    boolean signUp(User user);

    boolean signIn(User user);

    List<User> fetchUnFriendUsers(String username);
    List<String> fetchSendByStreakList(String username);
}
