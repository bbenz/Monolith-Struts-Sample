# UI Modernization Prompt — Duke's Ski Chalet

## Goal

Rebrand and modernize the web UI of the Spring Boot app in `appmod-migrated-java21-spring-boot-2nd-challenge/`. The app was migrated from a Struts app originally branded as "Ski Resort Shop" and may still contain Japanese locale references. Rebrand it to **"Duke's Ski Chalet"** with a consistent color scheme, mascot image, and English-only content.

---

## Branding

- **App name:** "Duke's Ski Chalet" — use this exact name everywhere (title bar, header, footer, nav, etc.).
  > **IMPORTANT:** Decide on ONE name and use it consistently. During this session, the name started as "Duke's Ski Shop" and was later changed to "Duke's Ski Chalet" across 4+ files. Pick the final name up front and apply it everywhere in a single pass.
- **Mascot image:** Use the Java Duke mascot in ski gear. Save as `src/main/resources/static/images/duke-ski.png`. Use it as:
  - The site logo/icon in the header nav bar
  - A hero image on the home page
  - The favicon (if applicable)

---

## Color Scheme

Derive the color scheme from the Duke mascot image (navy/red/white):

| Role | Color | Usage |
|------|-------|-------|
| Primary (navy) | `#1a1a2e` | Navbar background, footer, headings |
| Accent (red) | `#c0392b` | Buttons, hover states, price text, badges |
| Accent hover | `#e74c3c` | Button hover states |
| Background | `#f8f9fa` | Page body |
| Card background | `#ffffff` | Product cards, content areas |
| Text | `#2c3e50` | Body text |
| Muted text | `#7f8c8d` | Secondary text, descriptions |

Apply these colors in `app.css` and inline styles in templates. Override any existing colors from the Struts-era styling.

---

## Files to Update

### 1. `messages.properties`
- Set `app.title=Duke's Ski Chalet`
- Ensure all message keys are in English.

### 2. `templates/fragments/header.html`
- Add the Duke mascot image in the navbar brand area.
- Set the brand text to "Duke's Ski Chalet".
- **Watch for "DukeDuke's" bug:** If you put the image inside an `<a>` tag with adjacent text, the `alt` text concatenates with the visible text. Fix by setting `alt=""` on the image so screen readers skip it and the visible text is not duplicated.
  ```html
  <!-- WRONG — renders as "DukeDuke's Ski Chalet" -->
  <a class="navbar-brand" href="/">
      <img src="/images/duke-ski.png" alt="Duke" /> Duke's Ski Chalet
  </a>

  <!-- CORRECT — renders as "Duke's Ski Chalet" -->
  <a class="navbar-brand" href="/">
      <img src="/images/duke-ski.png" alt="" /> Duke's Ski Chalet
  </a>
  ```

### 3. `templates/` (all Thymeleaf templates)
- Update page titles, headings, and footer text to say "Duke's Ski Chalet".
- Check for hardcoded "Ski Resort Shop" or similar strings in every template.
- Remove any `lang="ja"` attributes from `<html>` tags — change to `lang="en"`.
- Remove any Japanese text, meta charset hints for Japanese, or locale references.

### 4. `static/css/app.css`
- Apply the color scheme above to navbar, buttons, cards, links, footer, etc.
- **Search bar width fix:** The search input in the navbar may render too narrow. Add an explicit width:
  ```css
  .navbar form input[type="text"],
  .navbar form input[type="search"] {
      width: 260px;
  }
  ```

### 5. Product detail template
- If the template uses Thymeleaf conditional/ternary expressions, ensure they are **inside** `${}`:
  ```html
  <!-- CORRECT -->
  <span th:text="${product.salePrice != null ? product.salePrice : product.regularPrice}"></span>

  <!-- WRONG — Thymeleaf parsing error -->
  <span th:text="${product.salePrice} != null ? ${product.salePrice} : ${product.regularPrice}"></span>
  ```

---

## Japanese Language Removal Checklist

The original Struts app had Japanese locale support. Remove all traces:

1. **`<html lang="ja">`** → change to `<html lang="en">` in all templates.
2. **`messages_ja.properties`** — delete this file if it exists.
3. **`<meta>` tags** — remove any `Content-Language: ja` meta tags.
4. **Hardcoded Japanese text** — search all `.html` and `.properties` files for any Japanese characters (Unicode ranges `\u3000-\u9FFF`).
5. **LocaleResolver config** — check `WebConfig.java` or similar configuration classes for Japanese locale defaults and remove them.

> Browsers auto-detect Japanese when they see `lang="ja"` or Japanese characters, triggering the "Translate this page?" prompt. Removing all traces eliminates this.

---

## Thymeleaf Template Notes

- Use `th:text` for plain text content, `th:utext` for HTML content (e.g., AI-generated recommendations rendered from markdown).
- Ternary expressions must be fully inside `${}` — see example above.
- When using fragment layouts (`layout:decorate`), ensure CSS/JS references use Thymeleaf URL syntax: `th:href="@{/css/app.css}"`.
- The app uses `layout/main.html` as the decorator layout — all pages should use `layout:decorate="~{layout/main}"`.

---

## Image Notes

- The Duke mascot image should be a **PNG with transparent background** so it looks good on both the dark navbar and light page backgrounds.
- Recommended size: ~200px wide for the hero image, ~40px height for the navbar icon.
- Save to `src/main/resources/static/images/duke-ski.png`.
- If you need a Duke image, search for "Java Duke mascot skiing" or "CloudSurf Duke" — it's a well-known open-source mascot.

---

## Verification Checklist

After making changes, verify:

- [ ] All pages show "Duke's Ski Chalet" (not "Ski Resort Shop", not "Duke's Ski Shop")
- [ ] Duke mascot image loads on home page and navbar
- [ ] No browser translation prompts appear (no Japanese references remain)
- [ ] Color scheme is consistent: navy navbar, red buttons/accents, white cards
- [ ] Search bar in navbar is wide enough to be usable (~260px)
- [ ] No "DukeDuke's" or similar text duplication in the header
- [ ] Product detail page loads without Thymeleaf parsing errors
- [ ] All nav links work (Home, Products, and any additional pages)
