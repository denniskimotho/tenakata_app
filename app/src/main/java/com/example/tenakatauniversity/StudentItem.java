package com.example.tenakatauniversity;

public class StudentItem {

    private String name;
    private String gender;
    private int age;
    private int iq;
    private String marital_status;
    private String location;
    private int adm_score;
    private String photo_url;
    public StudentItem(){

    };
    public StudentItem(String name,String gender,String marital_status,int iq,String location,int adm_score,String photo_url,int age){
        this.name = name;
        this.gender = gender;
        this.adm_score = adm_score;
        this.age = age;
        this.marital_status = marital_status;
        this.iq = iq;
        this.location = location;
        this.photo_url = photo_url;
    }

    public int getAge() {
        return age;
    }
    public int getIq() {
        return iq;
    }

    public String getGender() {
        return gender;
    }

    public String getLocation() {
        return location;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public String getName() {
        return name;
    }

    public int getAdm_score() {
        return adm_score;
    }

    public void setAdm_score(int adm_score) {
        this.adm_score = adm_score;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setIq(int iq) {
        this.iq = iq;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }
}

