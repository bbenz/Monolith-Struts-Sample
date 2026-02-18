# Ski Trip Advisor — Generation Prompt (Slim)

> **Shared project rules** (branding, color scheme, tech stack, database schema, Docker, Azure OpenAI config, Thymeleaf notes) are in [`.github/copilot-instructions.md`](../.github/copilot-instructions.md). This prompt covers only task-specific requirements.

---

## Goal

Add an AI-powered "Ski Trip Advisor" feature to the Spring Boot web application in `appmod-migrated-java21-spring-boot-2nd-challenge/`. The feature should be a new web page (not a separate CLI app) that lets users enter trip details and receive AI-generated equipment recommendations based on weather, ski conditions, and the product catalog from the database.

---

## Key Requirements

### 1. Integrate into the existing Spring Boot app
- Add the feature as a new page in the **existing** `appmod-migrated-java21-spring-boot-2nd-challenge/` Spring Boot app — do NOT create a separate project or CLI app.
- Create a new controller, service classes, DTOs, and Thymeleaf templates within the existing package structure (`com.skishop`).
- Add a nav link in the shared header template (`fragments/header.html`).
- Follow the existing app's patterns: Spring MVC controller, `@Service` classes, Thymeleaf templates using the existing `layout/main.html` layout.

### 2. Web form with these fields
- **Location** (text input — ski resort name)
- **Skill Level** (dropdown: Beginner, Intermediate, Advanced, Expert)
- **Skiing Type** (dropdown: All-Mountain, Freestyle, Freeride, Carving, Backcountry)
- **Budget** (dropdown: Low, Medium, High, No Limit)

### 3. Services to create
- **SkiWeatherService** — provides weather data and ski conditions. Use mock/simulated data (random within realistic ranges) since we don't have a real weather API key. Generate temperature, wind, snowfall, humidity, snow depth, open slopes count, snow quality, and difficulty level.
- **SkiOpenAIService** — calls Azure OpenAI to generate equipment recommendations. Takes weather data, ski conditions, matching products from the database, and user preferences as input.
- **SkiAdvisorService** — orchestrator that combines weather service + database product lookup + budget filtering + AI recommendations.

### 4. Database product lookup
- Query products from the existing `products` and `categories` tables using the existing Spring Data JPA repositories.
- Use the existing `CategoryRepository` (add `findByNameIgnoreCase` and `findByParentId` methods if needed) and `ProductRepository` (add `findByCategoryIdInAndStatus` if needed).
- Map relevant ski categories (Snowboards, Boots, Bindings, etc.) based on the user's skiing type.

### 5. Results page
- Display trip summary (location, skill, type, budget) in a card.
- Display weather conditions (temperature, wind, snowfall, humidity) in a card.
- Display ski conditions (open slopes, snow depth, quality, difficulty, powder day flag) in a card.
- Display AI equipment recommendations as **rendered HTML** (not raw markdown — see Markdown-to-HTML Rendering in shared instructions).
- Display matching products from the database in a table with links to product detail pages.
