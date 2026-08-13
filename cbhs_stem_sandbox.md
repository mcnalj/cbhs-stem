# CBHS STEM Sandbox Architecture Specification

## Project Purpose

The CBHS STEM Sandbox is an educational software platform built around the existing CBHS STEM website.

The goal is to create a stable architecture where students can build, maintain, and extend multiple web applications over multiple years.

The platform should support:

* student software development
* GitHub-based collaboration
* real deployment workflows
* independent student projects
* long-term maintainability

The primary applications will be:

1. CBHS STEM Website (existing)
2. CBHS Flight Tracker (future)
3. CBHS Bank (future)

At this stage, only the **high-level architecture** should be implemented.

Do not implement application-specific functionality for Flight Tracker or CBHS Bank.

---

# Existing Application: CBHS STEM Website

The CBHS STEM Website already exists.

Technology:

* Java
* Javalin
* Maven
* HTML/CSS/JavaScript

Domain:

```
https://cbhsstem.org
```

The existing STEM website should remain the primary public-facing application.

It contains pages such as:

* STEM course descriptions
* STEM endorsement requirements
* STEM learning opportunities
* STEM businesses and enterprises
* STEM programs at colleges and universities
* STEM alumni profiles

The existing application should not be rebuilt.

---

# Future Sandbox Applications

The platform should support additional applications that are independent from the main STEM website.

Initial future applications:

## CBHS Flight Tracker

Subdomain:

```
https://flight.cbhsstem.org
```

Purpose:

A student-developed flight tracking and travel application.

Implementation details will be provided separately.

---

## CBHS Bank

Subdomain:

```
https://bank.cbhsstem.org
```

Purpose:

A student-developed banking simulation application.

Implementation details will be provided separately.

---

# Architecture Principles

The system should prioritize:

1. Simplicity
2. Student understanding
3. Maintainability
4. Clear separation between applications

Avoid unnecessary complexity.

The architecture should resemble a small professional software organization while remaining understandable to high school students.

---

# Domain Architecture

Use subdomains rather than URL paths.

Recommended structure:

```
cbhsstem.org

flight.cbhsstem.org

bank.cbhsstem.org
```

Each application should appear to be an independent website.

Do not use:

```
cbhsstem.org/flight
cbhsstem.org/bank
```

because subdomains provide cleaner separation.

---

# Hosting Architecture

All applications should initially run on the same physical server.

The goal is:

* minimize hosting costs
* simplify administration
* provide a realistic deployment environment

Recommended structure:

```
                    Internet

                       |

                 Reverse Proxy
                     Nginx

                       |

       --------------------------------

       |              |               |

   STEM Website   Flight App      Bank App

   Port 8080      Port 8081       Port 8082


       |              |               |

   STEM DB       Flight DB       Bank DB
```

Nginx should route requests based on subdomain.

Example:

```
cbhsstem.org
        |
        --> STEM application


flight.cbhsstem.org
        |
        --> Flight Tracker application


bank.cbhsstem.org
        |
        --> CBHS Bank application
```

---

# Application Separation

Each application must have:

* separate GitHub repository
* separate backend
* separate frontend
* separate database
* separate deployment process

Do not create one large combined application.

Avoid:

```
CBHSApplication

    STEM
    Flight
    Bank
    Shared Database
```

Instead use:

```
cbhs-stem

cbhs-flight-tracker

cbhs-bank
```

Each application should be understandable by a student team working independently.

---

# Repository Structure

Recommended GitHub repositories:

```
cbhs-stem

cbhs-flight-tracker

cbhs-bank
```

Students should work primarily within their assigned repository.

Future applications should be able to be added without modifying existing applications.

---

# Authentication Strategy

Sandbox applications should eventually use Google authentication.

Do not use a shared password for student access.

Preferred authentication model:

```
User visits application

        |

Google Login

        |

Application verifies identity

        |

Authorization check

        |

Approved user?

        |

Yes → Allow access

No → Deny access
```

Initial authorization can be simple:

* approved email list
* school Google accounts

Future enhancements may include roles:

```
ADMIN

STUDENT_DEVELOPER

USER
```

Authentication implementation should be added after the basic architecture is working.

---

# Student Development Workflow

The system should support a professional development workflow.

Preferred process:

```
Student

   |

Git commit

   |

GitHub repository

   |

Pull Request

   |

Review

   |

Merge

   |

Automatic Deployment

   |

Live Application
```

Students should be able to see their approved changes deployed to the live application.

---

# Deployment Strategy

Each application should eventually support:

* automated builds
* automated deployment
* independent deployment

A student working on Flight Tracker should not risk breaking:

* CBHS STEM Website
* CBHS Bank

---

# Technology Guidelines

Preferred backend:

* Java
* Javalin
* Maven

Preferred frontend:

* HTML
* CSS
* Vanilla JavaScript

Preferred databases:

* Separate database per application

Possible future technologies:

* Bootstrap
* SQLite
* PostgreSQL
* External APIs
* JavaScript frameworks

Do not add technologies unless they solve a specific problem.

---

# Educational Goals

The architecture should allow students to learn:

* web application architecture
* backend/frontend separation
* Java web programming
* JavaScript programming
* databases
* Git and GitHub
* deployment
* authentication
* maintaining existing software

The goal is not to create an enterprise system.

The goal is to create a system that students can understand, improve, and inherit.

---

# Current Development Scope

The current task is to create the sandbox architecture only.

Implement:

* project structure
* hosting approach
* subdomain routing plan
* repository structure
* deployment framework

Do NOT implement:

* Flight Tracker functionality
* CBHS Bank functionality
* complex user management
* application-specific databases
* application features

Future documents will define those applications separately.

---

# Expected Deliverable

The initial implementation should provide:

1. A clear multi-application architecture.
2. Independent application repositories.
3. A deployment approach.
4. A reverse proxy strategy.
5. A foundation that future student teams can extend.

All design decisions should favor simplicity, transparency, and student learning.
