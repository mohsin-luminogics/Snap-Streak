package com.snapchat.streak.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "Users")
public class User {

    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    private String fullName;
    private List<Friend> friendsList;
    private String password;
    private List<String> sendByStreakList;

    public User() {
    }

    public User(String id, String username, String fullName, List<Friend> friendsList, String password, List<String> sendByStreakList) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.friendsList = friendsList;
        this.password = password;
        this.sendByStreakList = sendByStreakList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<Friend> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<Friend> friendsList) {
        this.friendsList = friendsList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getSendByStreakList() {
        return sendByStreakList;
    }

    public void setSendByStreakList(List<String> sendByStreakList) {
        this.sendByStreakList = sendByStreakList;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", friendsList=" + friendsList +
                ", password='" + password + '\'' +
                ", sendBySteakList=" + sendByStreakList +
                '}';
    }

}
