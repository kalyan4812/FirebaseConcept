package com.example.firebaseconcept.FireStore;

import com.google.firebase.database.Exclude;

public class PageNote {
    private String title, descrption;
    private int priority;
    private String documentId;

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    @Exclude
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public PageNote() {

    }

    public PageNote(String title, String descrption, int priority) {
        this.title = title;
        this.descrption = descrption;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
