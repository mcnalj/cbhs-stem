# CBHS STEM Sandbox — Implementation Plan (Refined)

> **Superseded.** Hosting is a DigitalOcean droplet, not DreamHost. The current plan lives in [cbhs-stem-sandbox](https://github.com/mcnalj/cbhs-stem-sandbox) (`IMPLEMENTATION_PLAN.md`).

**Last updated:** July 29, 2026  
**Status:** Superseded — see `cbhs-stem-sandbox`  
**Launch target:** ~4 weeks (all three subdomains live)  
**Source document:** `cbhs_stem_sandbox.md`  
**Related documents:** `PLAN.md`, `docs/BRANDING.md`

---

## 1. Executive summary

The **CBHS STEM Sandbox** is a multi-application platform on one DreamHost VPS. It keeps the **existing CBHS STEM website** intact while adding independent homes for **Flight Tracker** and **CBHS Bank** as minimal “under development” Javalin apps at launch.

**Confirmed scope for initial implementation:**

- Four GitHub repositories (three apps + platform infra)
- Nginx subdomain routing + SSL
- Manual deploy only (GitHub Actions deferred to a later semester)
- All three subdomains live at launch
- STEM default port changed from 7000 → 8080
- Skeleton apps reuse CBHS logo and colors

**Out of scope for now:** Flight/Bank features, databases, Google authentication (documented for future).

**Implementation will not begin until you:**

1. Register `cbhsstem.org` and notify that DNS is under your control
2. Provision the DreamHost VPS
3. Create the four GitHub repositories (see Section 3)
4. Explicitly tell the agent/team to begin implementation

---

## 2. Confirmed decisions

| Topic | Decision |
|-------|----------|
| **Domain `cbhsstem.org`** | Not registered yet — teacher will notify when ready |
| **DreamHost VPS** | Not provisioned yet — required before deploy |
| **`www.cbhsstem.org`** | **Redirect** to `cbhsstem.org` (not duplicate content) |
| **GitHub repos** | Do **not** exist yet — **create before any implementation code** |
| **Platform repo** | **Yes** — create `cbhs-stem-platform` before implementation |
| **Flight / Bank at launch** | Minimal Javalin “Under development” pages, **live on subdomains** |
| **Deployment** | **Manual SSH deploy first**; GitHub Actions in a later semester |
| **STEM port** | Change default from **7000 → 8080** |
| **VPS SSH access** | **Teacher only** |
| **Future Google auth** | Allow **school Google accounts and personal Gmail** (implementation deferred) |
| **Skeleton branding** | **Reuse CBHS logo and colors** from STEM site |
| **Launch deadline** | **4 weeks** from plan approval (~August 26, 2026) |

---

## 3. Pre-implementation checklist (teacher — before any code)

Complete these **before** telling the team to start implementation.

### 3.1 Domain and DNS

- [ ] Register **`cbhsstem.org`**
- [ ] Note registrar login and renewal date
- [ ] After VPS IP is known, create DNS **A records** pointing to VPS:
  - `cbhsstem.org`
  - `flight.cbhsstem.org`
  - `bank.cbhsstem.org`
- [ ] **`www.cbhsstem.org`** → redirect to `cbhsstem.org` (configured in Nginx after SSL)
- [ ] Notify team when DNS is under your control and propagated

### 3.2 DreamHost VPS

- [ ] Purchase/provision **DreamHost self-managed VPS** (not shared hosting)
- [ ] Choose **Ubuntu LTS**
- [ ] Record **public IP address**
- [ ] Confirm **teacher-only SSH** access (key-based login)
- [ ] Notify team when SSH works

### 3.3 GitHub repositories — create these first

Create **four empty repositories** (GitHub organization optional but recommended):

| Repository | Description |
|------------|-------------|
| **`cbhs-stem`** | Existing STEM website (push from current `CBHSSTEM` folder) |
| **`cbhs-stem-platform`** | Nginx, systemd, deploy scripts, infra docs |
| **`cbhs-flight-tracker`** | Flight Tracker skeleton app |
| **`cbhs-bank`** | CBHS Bank skeleton app |

Suggested settings for all repos:

- Default branch: `main`
- Add README (can be placeholder until implementation)
- **Do not** initialize with license/template that conflicts with planned structure
- Branch protection on `main` (teacher approval required) — configure before students contribute

> **Reminder:** Repos must exist **before** implementation code is written. The agent will not scaffold flight, bank, or platform code until you confirm repos are created.

### 3.4 Go-ahead signal

When prerequisites are ready, tell the team:

> “Prerequisites are done — begin sandbox implementation.”

Include: VPS IP, confirmation that DNS is pointed, and GitHub org/repo URLs.

---

## 4. Target architecture

```
                         Internet
                             |
              cbhsstem.org / flight / bank (DNS → VPS)
                             |
                      Nginx :443 / :80
                 SSL + www → apex redirect
                    Host-based routing
                             |
         +-------------------+-------------------+
         |                   |                   |
   cbhsstem.org      flight.cbhsstem.org   bank.cbhsstem.org
         |                   |                   |
   cbhs-stem :8080    cbhs-flight :8081     cbhs-bank :8082
         |                   |                   |
   JSON (→ SQLite)     (no DB yet)          (no DB yet)
```

- Each app: **separate repo**, **separate JAR**, **separate systemd service**
- **No** monorepo, **no** shared database
- STEM website codebase is **not rebuilt** — only port default and deploy wiring change

---

## 5. Repository responsibilities

| Repository | Owner / editors | Contents |
|------------|-----------------|----------|
| **`cbhs-stem`** | STEM student teams + teacher | Current STEM site; ongoing feature work |
| **`cbhs-stem-platform`** | Teacher (primarily) | `nginx/`, `systemd/`, `scripts/deploy-*.sh`, VPS docs |
| **`cbhs-flight-tracker`** | Future Flight team | Skeleton → future app |
| **`cbhs-bank`** | Future Bank team | Skeleton → future app |

Local folder `/Users/mcnalj/Documents/CBHSSTEM` maps to **`cbhs-stem`**.

---

## 6. Skeleton app standard (Flight + Bank)

Minimal Javalin apps at launch:

```
cbhs-{app}/
├── README.md              # Port, subdomain, how to run locally
├── pom.xml
├── src/main/java/org/cbhs/{app}/App.java
└── src/main/resources/public/
    ├── css/
    │   ├── variables.css  # Copy from STEM (CBHS colors)
    │   └── style.css
    ├── images/
    │   └── casco-bay-logo.png
    └── pages/home.html    # “Under development” + link back to cbhsstem.org
```

| App | Subdomain | Default port | systemd service |
|-----|-----------|--------------|-----------------|
| STEM | `cbhsstem.org` | 8080 | `cbhs-stem.service` |
| Flight | `flight.cbhsstem.org` | 8081 | `cbhs-flight-tracker.service` |
| Bank | `bank.cbhsstem.org` | 8082 | `cbhs-bank.service` |

No database, no auth, no business logic in skeletons.

---

## 7. Hosting and Nginx

### 7.1 VPS software stack

Install on Ubuntu VPS:

- OpenJDK 17+ (runtime only — builds happen on teacher machine or later CI)
- Nginx
- certbot (Let’s Encrypt)

Maven is **not** required on the server for manual deploy (build JAR locally, `scp` to VPS).

### 7.2 Nginx behavior

| Hostname | Action |
|----------|--------|
| `cbhsstem.org` | Proxy → `127.0.0.1:8080` |
| `www.cbhsstem.org` | **301 redirect** → `https://cbhsstem.org` |
| `flight.cbhsstem.org` | Proxy → `127.0.0.1:8081` |
| `bank.cbhsstem.org` | Proxy → `127.0.0.1:8082` |

Single SSL certificate covering all four hostnames via certbot.

### 7.3 Server layout

```
/opt/cbhs/
├── apps/
│   ├── stem/cbhs-stem.jar
│   ├── flight/cbhs-flight-tracker.jar
│   └── bank/cbhs-bank.jar
└── data/               # empty subdirs for future SQLite per app
    ├── stem/
    ├── flight/
    └── bank/
```

Config files live in **`cbhs-stem-platform`** repo and are copied to the server during setup.

---

## 8. Deployment (manual only — initial launch)

### 8.1 Per-app deploy flow

```text
Teacher machine                         VPS
      |                                   |
  mvn package                             |
      |                                   |
  scp JAR → /opt/cbhs/apps/{app}/ ------->|
      |                                   |
      |                          systemctl restart cbhs-{app}
      |                                   |
  Browser → subdomain ------------> Nginx → localhost:PORT
```

### 8.2 Platform repo deliverables

| File | Purpose |
|------|---------|
| `scripts/server-init.sh` | Create `/opt/cbhs/` dirs, install dependencies |
| `scripts/deploy-stem.sh` | Build hint + scp + restart STEM |
| `scripts/deploy-flight.sh` | Same for Flight |
| `scripts/deploy-bank.sh` | Same for Bank |
| `nginx/cbhsstem.conf` | Full Nginx config |
| `systemd/cbhs-stem.service` | systemd unit templates (×3) |
| `docs/vps-setup.md` | Step-by-step VPS setup |
| `docs/dns-and-ssl.md` | DNS + certbot runbook |
| `docs/manual-deploy.md` | Teacher deploy cheat sheet |

### 8.3 GitHub Actions — deferred

Automated deploy (Phase 4 in original plan) moves to a **later semester** after manual deploy is stable and students understand the workflow. Document the target design in `cbhs-stem-platform/docs/future-ci-cd.md` but do not implement yet.

---

## 9. Authentication (future)

**Not implemented at launch.**

When Flight/Bank need login (STEM site likely stays public):

- **Google OAuth**
- Allowlist includes **`@portlandschools.org`** and **personal Gmail** addresses you approve
- Simple email allowlist file or config per app initially; roles (`ADMIN`, `STUDENT_DEVELOPER`, `USER`) later
- Each app implements its own Javalin auth filter — no shared auth server required at first

Document hook points in platform repo; implement when a separate spec requests it.

---

## 10. Database (future)

| App | At launch | Later |
|-----|-----------|-------|
| STEM | JSON files (current) | SQLite in `/opt/cbhs/data/stem/` |
| Flight | None | Per Flight Tracker spec |
| Bank | None | Per CBHS Bank spec |

Create empty `data/` subdirectories on VPS at setup; no DB code at launch.

---

## 11. Four-week implementation schedule

**Start date:** When teacher confirms prerequisites + gives go-ahead  
**Target live date:** ~4 weeks after start (all three subdomains)

| Week | Focus | Repos | Deliverables |
|------|-------|-------|--------------|
| **Week 1** | Prerequisites + platform scaffolding | `cbhs-stem-platform` | VPS provisioned; DNS pointed; `server-init.sh`; Nginx + systemd templates; docs |
| **Week 2** | STEM production + platform deploy | `cbhs-stem`, `cbhs-stem-platform` | Port 8080; STEM pushed to GitHub; manual deploy; **`https://cbhsstem.org` live**; www redirect working |
| **Week 3** | Skeleton apps | `cbhs-flight-tracker`, `cbhs-bank` | Minimal Javalin stubs with CBHS branding; run locally on 8081/8082 |
| **Week 4** | Full sandbox live + hardening | All four | Deploy Flight + Bank; SSL verified on all subdomains; `manual-deploy.md` tested; smoke-test checklist passed |

### Week-by-week task detail

#### Week 1 — Infrastructure (blocked until VPS + DNS)

- [ ] Run `server-init.sh` on VPS (JDK, Nginx, certbot, directories)
- [ ] Install Nginx config (HTTP only first, then SSL after DNS propagates)
- [ ] Create systemd units (STEM, Flight, Bank) — services not started until JARs exist
- [ ] Obtain SSL cert once DNS resolves
- [ ] Configure `www` → apex redirect

#### Week 2 — STEM live

- [ ] Change `App.java` default port to `8080`
- [ ] Push `cbhs-stem` to GitHub
- [ ] Build JAR locally; run `deploy-stem.sh`
- [ ] Verify `https://cbhsstem.org` — all existing pages work
- [ ] Update STEM `README.md` with production URL and sandbox context

#### Week 3 — Skeleton apps (can start locally before VPS is ready)

- [ ] Scaffold `cbhs-flight-tracker` from template
- [ ] Scaffold `cbhs-bank` from template
- [ ] Copy branding assets (logo, `variables.css`) from STEM
- [ ] Push both repos to GitHub
- [ ] Local smoke test: three apps on 8080, 8081, 8082 simultaneously

#### Week 4 — Full launch

- [ ] Deploy Flight and Bank JARs to VPS
- [ ] Start and enable all three systemd services
- [ ] Verify:
  - `https://cbhsstem.org` — STEM site
  - `https://flight.cbhsstem.org` — under development page
  - `https://bank.cbhsstem.org` — under development page
  - `https://www.cbhsstem.org` — redirects to apex
- [ ] Teacher runs through `manual-deploy.md` end-to-end once per app
- [ ] Mark architecture phase complete

### Parallel work if domain/VPS are delayed

Weeks 3 tasks (skeleton apps, port change, local testing) can proceed **without** VPS. Production deploy (Weeks 1–2, 4) **requires** domain and VPS. If DNS slips, prioritize STEM on `cbhsstem.org` first, then Flight/Bank subdomains in same week once DNS is ready.

---

## 12. Implementation phases (ordered)

| Phase | Name | Blocked by | Repo(s) |
|-------|------|------------|---------|
| **0** | Prerequisites | — | GitHub repos, domain, VPS |
| **1** | Platform foundation | VPS + DNS | `cbhs-stem-platform` |
| **2** | STEM alignment + deploy | Phase 1 | `cbhs-stem` |
| **3** | Skeleton apps | Can start locally anytime | `cbhs-flight-tracker`, `cbhs-bank` |
| **4** | Full sandbox launch | Phases 1–3 | All |
| **5** | CI/CD automation | Later semester | Per-app repos |
| **6** | Google auth | Separate spec | Flight, Bank |

**Do not start Phase 1 code until Phase 0 checklist is complete.**

---

## 13. Risk register (updated)

| Risk | Likelihood | Mitigation |
|------|------------|------------|
| Domain not ready within 4 weeks | Medium | Build skeleton apps locally; teacher notifies when DNS ready |
| VPS provisioning delays | Medium | Week 3 work is local-only; compress Week 4 deploy |
| DNS propagation slow | Low | Start DNS early in Week 1; use `dig` to verify before certbot |
| 4-week deadline at risk | Medium | STEM live on `cbhsstem.org` is priority; subdomains same week if needed |
| Teacher-only deploy bottleneck | Expected | Manual deploy docs; CI/CD later |
| Personal Gmail auth abuse (future) | Low | Explicit allowlist; teacher maintains approved emails |

---

## 14. Launch success criteria

At the end of the 4-week window:

- [ ] `https://cbhsstem.org` serves the existing STEM website (all current pages)
- [ ] `https://www.cbhsstem.org` redirects to `https://cbhsstem.org`
- [ ] `https://flight.cbhsstem.org` shows branded “Under development” page
- [ ] `https://bank.cbhsstem.org` shows branded “Under development” page
- [ ] Four GitHub repos exist and are documented
- [ ] Manual deploy works for all three apps (teacher-tested)
- [ ] Each app runs locally on its documented port
- [ ] No Flight/Bank business features; no auth; no databases
- [ ] Students can clone a single repo and develop without affecting other apps

---

## 15. What happens when you say “begin implementation”

Assuming prerequisites are done, work proceeds in this order:

1. **`cbhs-stem-platform`** — Nginx, systemd, scripts, docs  
2. **`cbhs-stem`** — port 8080, GitHub push, first deploy  
3. **`cbhs-flight-tracker`** — skeleton with CBHS branding  
4. **`cbhs-bank`** — skeleton with CBHS branding  
5. Deploy all three; run launch checklist  

**No code will be written until you confirm prerequisites and give the go-ahead.**

---

## 16. Reminders for the teacher

Before implementation begins, please:

1. **Register `cbhsstem.org`** and notify when DNS is under your control  
2. **Provision DreamHost VPS** and confirm SSH access  
3. **Create GitHub repositories:**
   - `cbhs-stem`
   - `cbhs-stem-platform`
   - `cbhs-flight-tracker`
   - `cbhs-bank`
4. Send repo URLs (and GitHub org name if used)  
5. Say **“begin sandbox implementation”** when ready  

---

## 17. Open items (waiting on teacher)

| Item | Status |
|------|--------|
| Domain registered | ⏳ Pending — teacher will notify |
| VPS provisioned | ⏳ Pending |
| GitHub repos created | ⏳ Pending — **required before code** |
| Implementation go-ahead | ⏳ Waiting for explicit approval |

All other architectural questions are **resolved** (see Section 2).
