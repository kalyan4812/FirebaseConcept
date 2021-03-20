package com.example.firebaseconcept.FirebaseStorage;

import com.google.firebase.database.Exclude;

public class Upload {
    private String title;
    private String imageUrl;
    private String key;
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public Upload() {

    }

    public Upload(String title, String imageUrl) {
        if (title.trim().equals("")) {
            title = "No Name";
        }
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
