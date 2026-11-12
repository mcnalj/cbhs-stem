# CBHS STEM Program Website

A teaching project for Casco Bay High School's STEM program. Built with Java (Javalin), vanilla HTML/CSS/JavaScript, and JSON data files.

**Production:** [https://cbhsstem.org](https://cbhsstem.org)

This is one application in the [CBHS STEM Sandbox](https://github.com/mcnalj/cbhs-stem-sandbox). Flight Tracker and CBHS Bank are separate repositories.

## Prerequisites

- **JDK 17+** — [Adoptium](https://adoptium.net/) or similar
- **Maven 3.8+** — [maven.apache.org](https://maven.apache.org/)

Check your setup:

```bash
java -version
mvn -version
```

## Run locally

```bash
git clone git@github.com:mcnalj/cbhs-stem.git
cd cbhs-stem
mvn package
java -jar target/cbhs-stem-1.0-SNAPSHOT.jar
```

Open [http://localhost:8080](http://localhost:8080) in your browser.

To use a different port:

```bash
PORT=9090 java -jar target/cbhs-stem-1.0-SNAPSHOT.jar
```

## Project structure

```
src/main/java/org/cbhs/stem/     Java source code
  App.java                         Entry point and routes
  model/                           Data classes (Course, Endorsement, etc.)
  data/                            Read JSON files into Java objects
  web/                             Build HTML pages from templates

src/main/resources/
  data/                            JSON content (edit these to update the site)
  public/
    css/                           Stylesheets
    js/                            JavaScript
    images/                        Logo and images
    pages/                         HTML page templates
      html/                        Editable HTML topic content
      css/                         Editable CSS topic content
```

## Pages

| URL | Description |
|-----|-------------|
| `/` | Home page with links to all sections |
| `/courses` | Math, science, and CS course descriptions |
| `/endorsement` | STEM diploma endorsement requirements |
| `/opportunities` | Extended learning opportunities (stub) |
| `/colleges` | Blank scaffold — students build this |
| `/alumni` | Blank scaffold — students build this |
| `/partners` | Blank scaffold — students build this |

### Edit web design topics

Each HTML and CSS reference topic has its own editable HTML content file:

- HTML topics: `src/main/resources/public/pages/html/`
- CSS topics: `src/main/resources/public/pages/css/`
- Shared navigation and page frame: `src/main/resources/public/pages/web-reference.html`

Edit a topic file's HTML to change its explanations, examples, or links. The shared template supplies the site navigation and styles.

## How to add content

### Edit course descriptions

Edit `src/main/resources/data/courses.json`, rebuild, and restart:

```bash
mvn package
java -jar target/cbhs-stem-1.0-SNAPSHOT.jar
```

### Edit page layout or styling

- HTML templates: `src/main/resources/public/pages/`
- CSS: `src/main/resources/public/css/`
- JavaScript: `src/main/resources/public/js/`

### Edit STEM endorsement requirements

Edit `src/main/resources/data/endorsement.json`.

## Contributing (students)

1. Create a branch: `git checkout -b your-feature-name`
2. Make your changes
3. Test locally (see above)
4. Commit and push
5. Open a pull request (or ask your teacher to merge)

See `PLAN.md` for the full project plan and `docs/BRANDING.md` for school colors and logo usage.

## Tech stack

- **Java 17** + **Javalin 6** — web server and routing
- **Gson** — read JSON data files
- **Vanilla HTML/CSS/JS** — frontend (no frameworks)
- **Maven** — build tool

Future phases: basic JavaScript filters, SQLite database, admin interface.
