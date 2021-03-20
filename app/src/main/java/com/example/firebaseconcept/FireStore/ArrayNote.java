package com.example.firebaseconcept.FireStore;

import com.google.firebase.database.Exclude;

import java.util.List;

public class ArrayNote {
    private String documentId;
    private String title;
    private String description;
    private int priority;
    private List<String> tags;
    public ArrayNote() {
        //public no-arg constructor needed
    }
    public ArrayNote(String title, String description, int priority, List<String> tags) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.tags = tags;
    }
    @Exclude
    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public int getPriority() {
        return priority;
    }
    public List<String> getTags() {
        return tags;
    }
}
