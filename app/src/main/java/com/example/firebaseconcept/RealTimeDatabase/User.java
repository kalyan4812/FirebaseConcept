package com.example.firebaseconcept.RealTimeDatabase;

public class User {
    private String name;
    private String email;

    public String getName_key() {
        return name_key;
    }

    public void setName_key(String name_key) {
        this.name_key = name_key;
    }

    private String name_key;

    public User() {

    }

    public User(String name, String email,String name_key) {
        this.name = name;
        this.email = email;
        this.name_key=name_key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
