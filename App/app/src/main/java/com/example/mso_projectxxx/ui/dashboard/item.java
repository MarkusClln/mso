package com.example.mso_projectxxx.ui.dashboard;

public class item {

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public int getPhotoId() {
        return photoId;
    }

    String name;
    String age;
    int photoId;
    item(String name, String age, int photoId) {
        this.name = name;
        this.age = age;
        this.photoId = photoId;
    }


}
