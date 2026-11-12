package org.cbhs.stem;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.staticfiles.Location;
import org.cbhs.stem.data.JsonCourseRepository;
import org.cbhs.stem.data.JsonEndorsementRepository;
import org.cbhs.stem.data.JsonOpportunityRepository;
import org.cbhs.stem.web.PageRenderer;

import java.util.Set;

public class App {
    private static final Set<String> HTML_TOPICS = Set.of(
            "getting-started", "writing-tags", "self-closing-elements", "attributes",
            "coding-style", "images", "links", "tag-list", "planets", "troubleshooting", "tips");
    private static final Set<String> CSS_TOPICS = Set.of(
            "getting-started", "writing-rules", "selectors", "colors", "typography",
            "box-model", "spacing", "flexbox", "responsive-design");

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
        app.get("/web", ctx -> ctx.html(renderer.renderPage("web.html")));
        app.get("/html", ctx -> ctx.html(renderer.renderPage("html.html")));
        app.get("/css", ctx -> ctx.html(renderer.renderPage("css.html")));
        app.get("/css/style.css", ctx -> ctx.contentType("text/css").result(renderer.loadPublicResource("css/style.css")));
        app.get("/css/variables.css", ctx -> ctx.contentType("text/css").result(renderer.loadPublicResource("css/variables.css")));
        app.get("/html/{topic}", ctx -> renderReferencePage(ctx, "html", HTML_TOPICS, renderer));
        app.get("/css/{topic}", ctx -> renderReferencePage(ctx, "css", CSS_TOPICS, renderer));
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

    private static void renderReferencePage(Context ctx, String technology, Set<String> topics, PageRenderer renderer) {
        if (!topics.contains(ctx.pathParam("topic"))) {
            ctx.status(404);
            return;
        }
        ctx.html(renderer.renderReferencePage(technology, ctx.pathParam("topic")));
    }
}
