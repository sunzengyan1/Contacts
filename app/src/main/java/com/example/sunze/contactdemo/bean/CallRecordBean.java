package com.example.sunze.contactdemo.bean;

/**
 * Created by Dell on 2018/1/25.
 */

public class CallRecordBean {
    private String number;
    private String name;
    private String type;
    private String date;
    private String duration;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {

        this.duration = duration;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public void setNumber(String number) {

        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CallRecordBean{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
