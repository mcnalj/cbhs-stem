package org.cbhs.stem;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.cbhs.stem.data.JsonCourseRepository;
import org.cbhs.stem.data.JsonEndorsementRepository;
import org.cbhs.stem.data.JsonOpportunityRepository;
import org.cbhs.stem.web.PageRenderer;

public class App {
    public static void main(String[] args) {
        JsonCourseRepository courseRepository = new JsonCourseRepository();
        JsonEndorsementRepository endorsementRepository = new JsonEndorsementRepository();
        JsonOpportunityRepository opportunityRepository = new JsonOpportunityRepository();
        PageRenderer renderer = new PageRenderer();

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
            config.showJavalinBanner = false;
        });

        app.get("/", ctx -> ctx.html(renderer.renderPage("home.html")));
        app.get("/courses", ctx -> {
            String content = renderer.buildCourseListHtml(courseRepository.findAll());
            ctx.html(renderer.renderPage("courses.html", "CONTENT", content));
        });
        app.get("/endorsement", ctx -> {
            String content = renderer.buildEndorsementHtml(endorsementRepository.getEndorsement());
            ctx.html(renderer.renderPage("endorsement.html", "CONTENT", content));
        });
        app.get("/opportunities", ctx -> {
            String content = renderer.buildOpportunityListHtml(opportunityRepository.findAll());
            ctx.html(renderer.renderPage("opportunities.html", "CONTENT", content));
        });
        app.get("/colleges", ctx -> ctx.html(renderer.renderPage("colleges.html")));
        app.get("/alumni", ctx -> ctx.html(renderer.renderPage("alumni.html")));
        app.get("/partners", ctx -> ctx.html(renderer.renderPage("partners.html")));
        app.get("/maine-app-challenge", ctx -> ctx.html(renderer.renderPage("maine-app-challenge.html")));

        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        app.start(port);
        System.out.println("CBHS STEM site running at http://localhost:" + port);
    }
}
