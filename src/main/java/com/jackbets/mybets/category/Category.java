package com.jackbets.mybets.category;

public enum Category {

    NBA("NBA"),
    NFL("NFL"),
    MLB("MLB"),
    NHL("NHL"),
    COLLEGE_FOOTBALL("College Football"),
    COLLEGE_BASKETBALL("College Basketball");

    private final String category;

    Category (final String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

}
