package com.example.andylin.homepackagemonitor.Model;

/**
 * Created by Andy Lin on 2017-03-15.
 */

public class BoxHistory {
    private String dateAccessed;
    private boolean wasGranted;
    private String imageBytes;
    private boolean hasAcknowledged;
    private int id;

    public BoxHistory(String dateAccessed, boolean wasGranted, String imageBytes, boolean hasAcknowledged, int id){
        this.dateAccessed = dateAccessed;
        this.wasGranted = wasGranted;
        this.imageBytes = imageBytes;
        this.hasAcknowledged = hasAcknowledged;
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

    public boolean getHasAcknowledged(){
        return hasAcknowledged;
    }

    public int getId(){
        return id;
    }
}
