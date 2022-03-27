package com.snapchat.streak.entity;

public class Streak {
    private String text;
    private int order;

    public Streak() {
    }

    public Streak(String text, int order) {
        this.text = text;
        this.order = order;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "Streak{" +
                "text='" + text + '\'' +
                ", order=" + order +
                '}';
    }
}
