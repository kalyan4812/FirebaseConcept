package com.example.firebaseconcept.FireStore;

import com.google.firebase.database.Exclude;

public class Note {
    private String title;
    private String description;
    private String documentId;
    @Exclude
    public String getDocumentId() {
        return documentId;
    }
    @Exclude
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Note() {

    }

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
