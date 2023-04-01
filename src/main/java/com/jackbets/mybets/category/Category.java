package com.jackbets.mybets.category;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public enum Category {

    NBA("NBA"),
    NFL("NFL"),
    MLB("MLB"),
    NHL("NHL"),
    COLLEGE_FOOTBALL("College Football"),
    COLLEGE_BASKETBALL("College Basketball"),
    OTHER("Other");

    private final String category;
    
    @Id
    @SequenceGenerator(name = "category_sequence", sequenceName = "category_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_sequence")
    private Long id;

    Category (final String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

}
