# CBHS STEM colors and branding

This site matches the visual identity of [Casco Bay High School](https://cbhs.portlandschools.org/).

## Logo

- **File:** `src/main/resources/public/images/casco-bay-logo.png`
- **Source:** Official CBHS logo from Portland Public Schools / Finalsite
- **Usage:** Header on every page, linked to home

## Color palette

Extracted from the school's Finalsite theme (`CascoBayHighSchool.css`):

| Name | Hex | Usage |
|------|-----|-------|
| CBHS Teal | `#117c9d` | Primary brand, headings, buttons, badges |
| CBHS Teal Dark | `#00263a` | Links, footer background, logo text |
| CBHS Cyan | `#19aad7` | Accent badges, footer links |
| CBHS Green | `#303632` | Secondary accent (available for future use) |
| Text Primary | `#131313` | Headings |
| Text Secondary | `#373737` | Body text |
| Text Muted | `#636363` | Intro text, prerequisites |
| Background Light | `#f8f8f8` | Cards, course listings |
| Background Alt | `#e5e5e5` | Borders, dividers |

Colors are defined as CSS custom properties in `public/css/variables.css`.

## Typography

The school site uses Helvetica Neue / Arial. This project uses the same web-safe stack for consistency without external font dependencies.

## Do

- Use `--cbhs-teal` for primary actions and section headings
- Keep the logo visible in the header
- Link back to the main CBHS site in the footer

## Don't

- Change logo colors or stretch the logo
- Introduce unrelated color palettes without discussion
- Hot-link the logo from the production site (we include a local copy)
