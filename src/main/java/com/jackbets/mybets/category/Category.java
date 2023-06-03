package com.jackbets.mybets.category;

public enum Category {

    NBA("NBA"),
    NFL("NFL"),
    MLB("MLB"),
    NHL("NHL"),
    COLLEGE_FOOTBALL("College Football"),
    COLLEGE_BASKETBALL("College Basketball"),
    OTHER("Other");

    private String sport;
    
    private Category(String sport) {
        this.sport = sport;
    }

    public String getSport() {
        return sport;
    }

}
