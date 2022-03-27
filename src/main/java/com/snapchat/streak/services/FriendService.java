package com.snapchat.streak.services;

import com.snapchat.streak.entity.Friend;
import com.snapchat.streak.entity.Streak;
import com.snapchat.streak.entity.StreakCount;

import java.util.ArrayList;
import java.util.List;

public interface FriendService {

    boolean addFriend(String friend, String username);
    List<Friend> userFriends(String username);
    List<StreakCount> getFriendsAndStreakCount(String username);
    boolean sendStreak(String streakMsg,List<String> myFriends,String username);
    List<Streak> getStreaks(String username, String friend);
    String deleteStreakAndSendMessage(String username, String friend, int order);
}
