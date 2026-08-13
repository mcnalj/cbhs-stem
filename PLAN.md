# CBHS STEM Program Website — Project Plan

**Last updated:** July 29, 2026  
**Teacher:** AP Computer Science A / App Lab  
**Audience:** Current CBHS students and prospective families (primary); parents and counselors (secondary)

---

## 1. Purpose

This document plans a school STEM program website built **primarily as a teaching project**. The site should be real, public-facing, and incrementally useful — but the top priority is giving students a coherent, maintainable codebase to learn from and extend.

### Pedagogical goals

- AP CSA students practice Java (classes, collections, file I/O, eventually SQL) on a real project.
- App Lab / intro students contribute HTML, CSS, and basic JavaScript without needing Java.
- All changes flow through **Git commits**, mirroring professional workflow.
- Complexity grows in phases so beginners (post-arrays) are not overwhelmed.

### Product goals (v1)

Launch by **start of school year** with:

1. **Course listings** — full content
2. **STEM Endorsement requirements** — full content
3. **Extended learning opportunities** — stub page with placeholder structure
4. **Blank scaffold pages** for:
   - Regional college/university STEM programs
   - Alumni STEM profiles
   - Community STEM businesses and enterprises

Later phases add interactivity (basic filters), more content, SQLite, and eventually an admin UI (advanced student project, years 2+).

---

## 2. Tech stack (confirmed)

| Layer | Choice | Why |
|-------|--------|-----|
| Backend | **Java 17+** | Aligns with AP CSA; students already know the language |
| HTTP framework | **Javalin 6.x** | Minimal boilerplate; one dependency; easy to read |
| Frontend | **Vanilla HTML, CSS, JavaScript** | No build tools; visible in browser DevTools |
| Data (phase 1) | **JSON files** on disk | Teaches file I/O and object modeling before SQL |
| Data (phase 2) | **SQLite** | Single file DB; no separate server; natural CSA capstone |
| Version control | **Git + GitHub** (recommended) | Students submit via branches/PRs or direct commits |
| Hosting | **DreamHost VPS** | Shared hosting does **not** support Java apps; VPS required |

### What we are deliberately avoiding (for now)

- React, Vue, or any frontend framework
- Spring Boot
- npm / webpack / build pipelines
- Separate API-first architecture in v1 (see architecture below)

---

## 3. Architecture

### Phase 1 — Server-rendered pages (launch target)

Javalin serves **complete HTML pages**. Java reads JSON data files, builds model objects, and injects content into HTML templates.

```
Browser  →  GET /courses  →  Javalin route  →  read courses.json  →  render courses.html  →  HTML response
```

**Why this first:** Students already understand printing and strings. Serving HTML from Java keeps the mental model simple. Frontend students edit `.html` and `.css` files directly; backend students wire routes and data.

### Phase 2 — Client-side interactivity (mid-year)

Add JavaScript for **basic filters** (e.g., filter courses by department, filter opportunities by grade level). Two acceptable patterns:

- **A. Query params:** JavaScript sets `?department=Engineering`, page reloads, Java filters server-side.
- **B. JSON endpoint:** Java exposes `GET /api/courses`, JavaScript fetches and re-renders a section of the page.

Start with **A** (simpler); move to **B** when students understand HTTP and JSON.

### Phase 3 — SQLite (late year or year 2)

Replace JSON file reads with JDBC + SQLite. Same Java classes; only the data-access layer changes. Excellent unit on interfaces: `CourseRepository` with `JsonCourseRepository` and `SqliteCourseRepository` implementations.

### Phase 4 — Admin interface (year 2+, optional advanced project)

Simple authenticated pages for CRUD on alumni, businesses, etc. Not in scope for v1.

---

## 4. Hosting on DreamHost

### Important constraint

DreamHost **shared hosting does not run Java web applications** (no servlets, Tomcat, or persistent Java processes). You will need a **DreamHost VPS** (self-managed, ~$10–20/month) where you:

1. Install JDK 17+
2. Build a runnable JAR (`maven` or `gradle`)
3. Run the JAR as a background service (systemd)
4. Configure **Nginx** as a reverse proxy (port 443 → localhost:7000)
5. Point your custom domain DNS to the VPS
6. Enable SSL (Let's Encrypt via DreamHost or certbot)

### Action items for teacher (before school year)

- [ ] Confirm DreamHost plan supports VPS (not shared-only)
- [ ] Choose domain/subdomain (e.g., `stem.cbhs.org`)
- [ ] Document deployment steps so students are not blocked on server access
- [ ] Decide who has SSH access (teacher only at first is fine)

### Fallback for early development

Students run locally: `java -jar cbhs-stem.jar` → `http://localhost:7000`. Deployment can lag slightly behind local dev if VPS setup takes time.

---

## 5. Site map and release phases

### Navigation (all pages)

```
Home
├── Courses
├── STEM Endorsement
├── Extended Learning        (stub at launch)
├── Colleges & Universities  (blank scaffold)
├── Alumni Profiles          (blank scaffold)
└── Community Partners       (blank scaffold)
```

### Phase 0 — Foundation (teacher, summer)

- Project skeleton: Maven/Gradle, Javalin app, static assets
- Shared layout: header, nav, footer, school branding
- Home page with welcome text and links to all sections
- Git repo initialized; README with run instructions

### Phase 1 — Launch content (start of school year)

| Page | Status at launch | Data source |
|------|------------------|-------------|
| Home | Brief intro + nav | Static HTML |
| Courses | **Full content** | `data/courses.json` |
| STEM Endorsement | **Full content** | `data/endorsement.json` or static HTML |
| Extended Learning | **Stub** — heading, intro paragraph, "coming soon" or 1–2 placeholder items | `data/opportunities.json` (minimal) |
| Colleges | Blank scaffold — page title, nav works, placeholder message | None |
| Alumni | Blank scaffold | None |
| Community Partners | Blank scaffold | None |

### Phase 2 — Student expansion (fall semester)

- App Lab students: styling, layout improvements, blank page designs
- CSA students: populate JSON files; add routes; implement first filters
- Colleges, Alumni, Community pages get structure designed by students

### Phase 3 — Interactivity + database (spring semester)

- Filter UI on Courses and/or Opportunities
- Migrate one entity (e.g., courses) to SQLite as a class project

---

## 6. Repository structure

```
CBHSSTEM/
├── PLAN.md                          ← this document
├── README.md                        ← how to run locally, how to contribute
├── pom.xml                          ← Maven (or build.gradle)
├── src/
│   └── main/
│       ├── java/
│       │   └── org/cbhs/stem/
│       │       ├── App.java                 ← Javalin entry point, route registration
│       │       ├── model/
│       │       │   ├── Course.java
│       │       │   ├── EndorsementRequirement.java
│       │       │   └── Opportunity.java
│       │       ├── data/
│       │       │   ├── CourseRepository.java
│       │       │   └── JsonCourseRepository.java
│       │       └── web/
│       │           └── PageRenderer.java    ← loads HTML template, replaces placeholders
│       └── resources/
│           ├── data/
│           │   ├── courses.json
│           │   ├── endorsement.json
│           │   └── opportunities.json
│           └── public/                      ← served as static files
│               ├── css/
│               │   ├── style.css            ← shared styles + CBHS branding
│               │   └── variables.css        ← school colors, fonts
│               ├── js/
│               │   └── main.js              ← nav highlight, future filters
│               ├── images/
│               │   └── logo.png
│               └── pages/
│                   ├── home.html
│                   ├── courses.html
│                   ├── endorsement.html
│                   ├── opportunities.html
│                   ├── colleges.html
│                   ├── alumni.html
│                   └── partners.html
├── scripts/
│   └── deploy.sh                    ← teacher-maintained deploy script (optional)
└── .gitignore
```

### Design conventions

- **One HTML file per feature area** — easy to assign ownership
- **Shared CSS** — single `style.css` + branding variables; avoids duplication
- **Placeholder syntax** in HTML templates: `{{COURSE_LIST}}` replaced by Java before sending response
- **JSON files** edited by students for content changes (no recompile needed if hot-reload or restart is documented)

---

## 7. Data models (phase 1)

### Course (`courses.json`)

```json
{
  "courses": [
    {
      "id": "ap-csa",
      "name": "AP Computer Science A",
      "gradeLevels": ["10", "11", "12"],
      "department": "Computer Science",
      "description": "...",
      "prerequisites": ["Algebra II"],
      "stemSequence": true
    }
  ]
}
```

### Endorsement (`endorsement.json`)

```json
{
  "title": "CBHS STEM Endorsement",
  "overview": "...",
  "requirements": [
    {
      "category": "Core Courses",
      "items": ["...", "..."]
    }
  ],
  "totalCredits": 6
}
```

### Opportunity — stub (`opportunities.json`)

```json
{
  "opportunities": [
    {
      "id": "placeholder-1",
      "title": "More opportunities coming soon",
      "type": "TBD",
      "description": "Check back as our STEM program grows.",
      "gradeLevels": []
    }
  ]
}
```

Colleges, alumni, and partners: **no data files until students define the schema** as a design exercise.

---

## 8. AP CSA curriculum alignment

| Topic | Where it appears in this project |
|-------|----------------------------------|
| Classes & objects | `Course`, `EndorsementRequirement` model classes |
| Arrays / ArrayList | Lists of courses, requirements, opportunities |
| File I/O | `JsonCourseRepository` reads JSON from disk |
| String manipulation | Template placeholder replacement in `PageRenderer` |
| Encapsulation | Private fields, getters, repository interface |
| Interfaces (later) | `CourseRepository` → JSON vs SQLite implementations |
| SQL (extension) | SQLite migration unit |
| HTTP (conceptual) | Javalin routes; optional `fetch()` in phase 2 |

Suggested pacing: introduce the repo **after arrays and ArrayList** (~week 4–6 of your web unit). Before that, App Lab students can work on HTML/CSS while you scaffold the Java layer.

---

## 9. Student roles and tasks

~20 AP CSA students + App Lab contributors. Assign by interest and readiness.

### App Lab / intro students (HTML/CSS/JS)

- Apply CBHS branding to `variables.css` and shared layout
- Design blank scaffold pages (colleges, alumni, partners)
- Improve typography, spacing, mobile layout
- Write copy for home page and section intros
- Later: simple JavaScript filter UI (with template code provided)

### AP CSA students — tier 1 (post-arrays)

- Add entries to `courses.json` and `opportunities.json`
- Fix typos and content updates via Git commits
- Create model class fields matching JSON structure
- Read and explain existing Java routes

### AP CSA students — tier 2

- Implement `JsonCourseRepository`
- Add new Javalin routes for new pages
- Wire template rendering for Courses and Endorsement pages
- Implement server-side filter via query parameters

### AP CSA students — tier 3 (advanced / year 2)

- SQLite migration
- REST JSON endpoints
- Admin CRUD pages
- Alumni/partners data models and repositories

### Suggested Git workflow for 20 students

- **Protected `main` branch** — teacher merges
- Students work on named branches: `add-course-robotics`, `style-alumni-page`
- Small PRs (one feature or one page) to avoid merge conflicts
- Pair programming for first few commits to model the workflow

---

## 10. Page specifications

### Home (`/`)

- School STEM program headline
- Short paragraph for prospective families
- Card or link grid to all six sections
- CBHS logo and brand colors

### Courses (`/courses`)

- List all courses from `courses.json`
- Each course: name, grades, department, description, prerequisites
- Optional badge for "STEM sequence" courses
- **Future:** filter by department or grade (phase 2)

### STEM Endorsement (`/endorsement`)

- Overview paragraph
- Requirements grouped by category
- Clear checklist-style layout for students/parents
- Credit total

### Extended Learning (`/opportunities`)

- Section header and intro
- 1–2 stub items OR "content coming soon"
- Structure ready for students to add internships, clubs, competitions, etc.

### Colleges (`/colleges`) — blank scaffold

- Page title and nav
- Message: "Our students are building this section."
- Optional: student-submitted wireframe in HTML comments for planning

### Alumni (`/alumni`) — blank scaffold

- Same pattern; students will later define fields (name, year, field, college, quote, photo)

### Community Partners (`/partners`) — blank scaffold

- Same pattern; students will later define fields (business name, industry, link, description)

---

## 11. Branding integration

You have existing branding. Integrate early in phase 0:

- Add `variables.css` with school colors as CSS custom properties
- Logo in `public/images/`
- Font choices (web-safe fallbacks if custom fonts are restricted)
- Header/footer consistent across all pages

Provide students a **one-page brand guide** (hex colors, logo file, do/don't) in the repo as `docs/BRANDING.md`.

---

## 12. Milestone calendar (suggested)

| When | Milestone | Owner |
|------|-----------|-------|
| Summer (weeks 1–2) | Repo skeleton, Javalin runs locally, shared layout, branding | Teacher |
| Summer (week 3) | Courses + Endorsement content in JSON; pages render | Teacher |
| Summer (week 4) | Stub + blank pages; README; test on VPS | Teacher |
| **School year start** | **v1 live on custom domain** | Teacher |
| Weeks 1–4 | App Lab: styling pass; CSA: tour codebase, first JSON edits | Students |
| Weeks 5–8 | CSA: model classes + repository; populate Opportunities | Students |
| Weeks 9–12 | Design and build Colleges, Alumni, Partners pages | Mixed teams |
| Spring | Filters, optional SQLite unit | CSA advanced |

Adjust to your calendar; incremental releases mean partial pages can go live as students finish them.

---

## 13. Local development setup

Document in `README.md`:

```bash
# Prerequisites: JDK 17+, Maven 3.8+
git clone <repo-url>
cd CBHSSTEM
mvn package
java -jar target/cbhs-stem.jar
# Open http://localhost:7000
```

Students on school Chromebooks may need cloud dev environments or your lab machines — note your school's setup in README.

---

## 14. Testing and quality (lightweight)

Keep this minimal for teaching clarity:

- Manual checklist before each deploy: all nav links work, pages render on mobile
- Optional: one JUnit test per repository (`JsonCourseRepositoryTest` loads real JSON)
- Teacher reviews all merges to `main` before deploy

No requirement for full test coverage in v1.

---

## 15. Open questions and future issues

Track these; not blockers for v1:

| Item | Status |
|------|--------|
| DreamHost VPS provisioning and SSH access | Teacher to confirm |
| Exact custom domain and DNS | Teacher to confirm |
| School IT firewall / port restrictions | Unknown — test early |
| Photo consent for alumni | Future |
| Public contact info on alumni profiles | Future — default to no contact info |
| Accessibility (WCAG) requirements | Future — use semantic HTML from start |

---

## 16. Success criteria

### v1 launch (start of school year)

- [ ] Site reachable at custom domain over HTTPS
- [ ] Courses page shows real course data from JSON
- [ ] Endorsement page shows real requirements
- [ ] Opportunities page exists with stub content
- [ ] Three blank scaffold pages exist and are linked from nav
- [ ] README allows a new student to run the project locally in under 15 minutes
- [ ] Git repo ready for student contributions

### End of fall semester

- [ ] At least one student-authored content merge to `main`
- [ ] At least one blank page redesigned by App Lab students
- [ ] Opportunities section has real entries

### End of school year

- [ ] Basic filter on at least one page OR SQLite migration demo complete
- [ ] Alumni or Partners page has student-designed schema and sample data

---

## 17. Next steps

1. **Teacher:** Confirm DreamHost VPS plan and domain DNS
2. **Teacher:** Gather course and endorsement content into draft JSON or doc
3. **Teacher:** Export branding assets (logo, colors) into repo
4. **Build phase 0 skeleton** — scaffold project structure, Javalin app, shared layout
5. **Deploy v1 to VPS** before first day of school
6. **Intro lesson:** Git workflow + codebase tour for AP CSA class

When ready, the next implementation step is **phase 0: project skeleton** — Maven project, Javalin hello-world, shared HTML layout, and branding placeholders.
