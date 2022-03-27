package com.snapchat.streak.entity;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class CommonData {

    @Id
    private String id;
    private int streakCount;
    private int uniqueId;

    public CommonData() {
    }

    public CommonData(String id, int streakCount, int uniqueId) {
        this.id = id;
        this.streakCount = streakCount;
        this.uniqueId = uniqueId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStreakCount() {
        return streakCount;
    }

    public void setStreakCount(int streakCount) {
        this.streakCount = streakCount;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public String toString() {
        return "CommonData{" +
                "id='" + id + '\'' +
                ", streakCount=" + streakCount +
                ", uniqueId=" + uniqueId +
                '}';
    }
}
