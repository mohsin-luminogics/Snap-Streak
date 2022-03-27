package com.snapchat.streak.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Friend {

    private String name;
    private List<Streak> streak;
    private int uniqueId;
    private boolean sendStreak;
    private String startPresentTime;
    private String endPresentTime;
    private String nextStartTime;

    public Friend() {
    }

    public Friend(String name, List<Streak> streak, int uniqueId, boolean sendStreak, String startPresentTime, String endPresentTime, String nextStartTime) {
        this.name = name;
        this.streak = streak;
        this.uniqueId = uniqueId;
        this.sendStreak = sendStreak;
        this.startPresentTime = startPresentTime;
        this.endPresentTime = endPresentTime;
        this.nextStartTime = nextStartTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Streak> getStreak() {
        return streak;
    }

    public void setStreak(List<Streak> streak) {
        this.streak = streak;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public boolean isSendStreak() {
        return sendStreak;
    }

    public void setSendStreak(boolean sendStreak) {
        this.sendStreak = sendStreak;
    }

    public String getStartPresentTime() {
        return startPresentTime;
    }

    public void setStartPresentTime(String startPresentTime) {
        this.startPresentTime = startPresentTime;
    }

    public String getEndPresentTime() {
        return endPresentTime;
    }

    public void setEndPresentTime(String endPresentTime) {
        this.endPresentTime = endPresentTime;
    }

    public String getNextStartTime() {
        return nextStartTime;
    }

    public void setNextStartTime(String nextStartTime) {
        this.nextStartTime = nextStartTime;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "name='" + name + '\'' +
                ", streak=" + streak +
                ", uniqueId=" + uniqueId +
                ", sendStreak=" + sendStreak +
                ", startPresentTime=" + startPresentTime +
                ", endPresentTime=" + endPresentTime +
                ", nextStartTime=" + nextStartTime +
                '}';
    }
}
