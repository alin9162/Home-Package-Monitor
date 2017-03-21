package com.example.andylin.homepackagemonitor.Model;

/**
 * Created by Andy Lin on 2017-03-15.
 */

public class BoxHistory {
    private String dateAccessed;
    private boolean wasGranted;
    private String imageBytes;
    private int id;

    public BoxHistory(String dateAccessed, boolean wasGranted, String imageBytes, int id){
        this.dateAccessed = dateAccessed;
        this.wasGranted = wasGranted;
        this.imageBytes = imageBytes;
        this.id = id;
    }

    public String getDateAccessed(){
        return dateAccessed;
    }

    public boolean getWasGranted(){
        return wasGranted;
    }

    public String getImageBytes(){
        return imageBytes;
    }

    public int getId(){
        return id;
    }
}
