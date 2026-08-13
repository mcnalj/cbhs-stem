package org.cbhs.stem.model;

import java.util.List;

/**
 * An extended STEM learning opportunity.
 */
public class Opportunity {
    private String id;
    private String title;
    private String type;
    private String description;
    private List<String> gradeLevels;
    private String moreInfoLink;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getGradeLevels() {
        return gradeLevels;
    }

    public String getMoreInfoLink() {
        return moreInfoLink;
    }
}
