package org.cbhs.stem.web;

import org.cbhs.stem.model.Course;
import org.cbhs.stem.model.Endorsement;
import org.cbhs.stem.model.Opportunity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Loads HTML page templates and replaces {{PLACEHOLDER}} tokens with content.
 */
public class PageRenderer {
    public String renderPage(String templateName) {
        return loadTemplate("public/pages/" + templateName);
    }

    public String renderPage(String templateName, String placeholder, String content) {
        String template = loadTemplate("public/pages/" + templateName);
        return template.replace("{{" + placeholder + "}}", content);
    }

    public String loadPublicResource(String resourceName) {
        return loadTemplate("public/" + resourceName);
    }

    public String renderReferencePage(String technology, String topic) {
        String content = loadTemplate("public/pages/" + technology + "/" + topic + ".html");
        return loadTemplate("public/pages/web-reference.html")
                .replace("{{TECH_PATH}}", technology)
                .replace("{{TECH_NAME}}", technology.toUpperCase())
                .replace("{{TECH_TITLE}}", technology.toUpperCase())
                .replace("{{CONTENT}}", content);
    }

    public String buildCourseListHtml(List<Course> courses) {
        StringBuilder html = new StringBuilder();

        String currentDepartment = "";
        for (Course course : courses) {
            if (!course.getDepartment().equals(currentDepartment)) {
                if (!currentDepartment.isEmpty()) {
                    html.append("</section>\n");
                }
                currentDepartment = course.getDepartment();
                html.append("<section class=\"department-section\">\n");
                html.append("<h2>").append(escapeHtml(currentDepartment)).append("</h2>\n");
                if ("Math".equals(currentDepartment)) {
                    html.append("<p class=\"department-intro\">As Galileo notes, the language of the universe is mathematics. ");
                    html.append("CBHS mathematicians learn this language through rigorous study of essential concepts and authentic applications. ");
                    html.append("All math courses are year-long and worth two UPs.</p>\n");
                } else if ("Science".equals(currentDepartment)) {
                    html.append("<p class=\"department-intro\">CBHS scientists learn scientific knowledge and processes that enable them to understand how our world works. ");
                    html.append("All science courses have a significant lab component and are year-long (two UPs), except STEM Topics semester courses. ");
                    html.append("Typical sequence: 9th Biology → 10th Physics → 11th Chemistry → 12th Engineering, USM Environmental Science, or STEM Topics.</p>\n");
                } else if ("Computer Science".equals(currentDepartment)) {
                    html.append("<p class=\"department-intro\">CBHS computer science courses introduce programming and computational thinking, ");
                    html.append("building toward AP Computer Science and real-world applications.</p>\n");
                }
            }

            html.append("<article class=\"course-card\" id=\"").append(escapeHtml(course.getId())).append("\">\n");
            html.append("<h3>").append(escapeHtml(course.getName())).append("</h3>\n");
            html.append("<div class=\"course-meta\">\n");
            html.append("<span class=\"badge\">Grades ").append(String.join(", ", course.getGradeLevels())).append("</span>\n");
            if (course.getCredits() != null && !course.getCredits().isEmpty()) {
                html.append("<span class=\"badge badge-muted\">").append(escapeHtml(course.getCredits())).append("</span>\n");
            }
            if (course.isStemSequence()) {
                html.append("<span class=\"badge badge-accent\">STEM Sequence</span>\n");
            }
            html.append("</div>\n");
            html.append("<p>").append(escapeHtml(course.getDescription())).append("</p>\n");
            if (course.getPrerequisites() != null && !course.getPrerequisites().isEmpty()) {
                html.append("<p class=\"prerequisites\"><strong>Prerequisites:</strong> ");
                html.append(escapeHtml(String.join(", ", course.getPrerequisites())));
                html.append("</p>\n");
            }
            html.append("</article>\n");
        }

        if (!currentDepartment.isEmpty()) {
            html.append("</section>\n");
        }

        return html.toString();
    }

    public String buildEndorsementHtml(Endorsement endorsement) {
        StringBuilder html = new StringBuilder();
        html.append("<p class=\"lead\">").append(escapeHtml(endorsement.getOverview())).append("</p>\n");

        if (endorsement.getPurposes() != null) {
            html.append("<h2>Purpose</h2>\n<ul>\n");
            for (String purpose : endorsement.getPurposes()) {
                html.append("<li>").append(escapeHtml(purpose)).append("</li>\n");
            }
            html.append("</ul>\n");
        }

        html.append("<h2>Requirements</h2>\n<ol class=\"requirement-list\">\n");
        for (Endorsement.Requirement req : endorsement.getRequirements()) {
            html.append("<li>").append(escapeHtml(req.getText())).append("</li>\n");
        }
        html.append("</ol>\n");

        html.append("<h2>Extended STEM Learning</h2>\n");
        html.append("<p>Students typically demonstrate substantial commitment through at least 50 hours of STEM learning ");
        html.append("in at least 2 of the following categories:</p>\n<ul>\n");
        for (Endorsement.ExtendedLearningCategory category : endorsement.getExtendedLearning()) {
            html.append("<li><strong>").append(escapeHtml(category.getName())).append("</strong> ");
            html.append("(").append(escapeHtml(category.getMinimumHours())).append(")</li>\n");
        }
        html.append("</ul>\n");

        if (endorsement.getTrackingNote() != null) {
            html.append("<p class=\"note\">").append(escapeHtml(endorsement.getTrackingNote())).append("</p>\n");
        }

        return html.toString();
    }

    public String buildOpportunityListHtml(List<Opportunity> opportunities) {
        StringBuilder html = new StringBuilder();
        for (Opportunity opportunity : opportunities) {
            html.append("<article class=\"opportunity-card\">\n");
            html.append("<h3>").append(escapeHtml(opportunity.getTitle())).append("</h3>\n");
            if (opportunity.getType() != null && !opportunity.getType().isEmpty()) {
                html.append("<span class=\"badge\">").append(escapeHtml(opportunity.getType())).append("</span>\n");
            }
            html.append("<p>").append(escapeHtml(opportunity.getDescription())).append("</p>\n");
            if (opportunity.getMoreInfoLink() != null && !opportunity.getMoreInfoLink().isEmpty()) {
                html.append("<p><a href=\"").append(escapeHtml(opportunity.getMoreInfoLink()))
                        .append("\" class=\"btn btn-outline\">More info</a></p>\n");
            }
            html.append("</article>\n");
        }
        return html.toString();
    }

    private String loadTemplate(String resourcePath) {
        InputStream input = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (input == null) {
            throw new IllegalStateException("Could not find template: " + resourcePath);
        }
        try {
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read template: " + resourcePath, e);
        }
    }

    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
