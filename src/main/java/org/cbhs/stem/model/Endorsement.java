package org.cbhs.stem.model;

import java.util.List;

/**
 * STEM Endorsement requirements from the CBHS Family Handbook.
 */
public class Endorsement {
    private String title;
    private String overview;
    private List<String> purposes;
    private List<Requirement> requirements;
    private List<ExtendedLearningCategory> extendedLearning;
    private String trackingNote;

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public List<String> getPurposes() {
        return purposes;
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public List<ExtendedLearningCategory> getExtendedLearning() {
        return extendedLearning;
    }

    public String getTrackingNote() {
        return trackingNote;
    }

    public static class Requirement {
        private int number;
        private String text;

        public int getNumber() {
            return number;
        }

        public String getText() {
            return text;
        }
    }

    public static class ExtendedLearningCategory {
        private String name;
        private String minimumHours;

        public String getName() {
            return name;
        }

        public String getMinimumHours() {
            return minimumHours;
        }
    }
}
