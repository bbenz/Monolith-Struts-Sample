# UI Modernization Prompt — Duke's Ski Chalet (Slim)

> **Shared project rules** (branding, color scheme, tech stack, database schema, Docker, Azure OpenAI config, Thymeleaf notes) are in [`.github/copilot-instructions.md`](../.github/copilot-instructions.md). This prompt covers only task-specific requirements.

---

## Goal

Rebrand and modernize the web UI of the Spring Boot app in `appmod-migrated-java21-spring-boot-2nd-challenge/`. The app was migrated from a Struts app originally branded as "Ski Resort Shop" and may still contain Japanese locale references. Rebrand it to **"Duke's Ski Chalet"** with a consistent color scheme, mascot image, and English-only content.

---

## Files to Update

### 1. `messages.properties`
- Set `app.title=Duke's Ski Chalet`
- Ensure all message keys are in English.

### 2. `templates/fragments/header.html`
- Add the Duke mascot image in the navbar brand area.
- Set the brand text to "Duke's Ski Chalet".
- Watch for the "DukeDuke's" bug — see branding section in shared instructions.

### 3. `templates/` (all Thymeleaf templates)
- Update page titles, headings, and footer text to say "Duke's Ski Chalet".
- Check for hardcoded "Ski Resort Shop" or similar strings in every template.
- Remove any `lang="ja"` attributes from `<html>` tags — change to `lang="en"`.
- Remove any Japanese text, meta charset hints for Japanese, or locale references.

### 4. `static/css/app.css`
- Apply the color scheme (see shared instructions) to navbar, buttons, cards, links, footer, etc.
- **Search bar width fix:** The search input in the navbar may render too narrow. Add an explicit width:
  ```css
  .navbar form input[type="text"],
  .navbar form input[type="search"] {
      width: 260px;
  }
  ```

### 5. Product detail template
- If the template uses Thymeleaf conditional/ternary expressions, ensure they follow the correct syntax (see Thymeleaf notes in shared instructions).

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
