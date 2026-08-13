package org.cbhs.stem.model;

import java.util.List;

/**
 * Represents a STEM-related course at CBHS.
 * Fields match the structure in data/courses.json.
 */
public class Course {
    private String id;
    private String name;
    private String department;
    private List<String> gradeLevels;
    private String description;
    private List<String> prerequisites;
    private boolean stemSequence;
    private String credits;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public List<String> getGradeLevels() {
        return gradeLevels;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public boolean isStemSequence() {
        return stemSequence;
    }

    public String getCredits() {
        return credits;
    }
}
