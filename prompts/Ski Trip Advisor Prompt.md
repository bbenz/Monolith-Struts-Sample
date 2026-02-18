# Ski Trip Advisor — Generation Prompt

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
- Display AI equipment recommendations as **rendered HTML** (not raw markdown — see note below).
- Display matching products from the database in a table with links to product detail pages.

---

## Azure OpenAI Configuration

### Dependencies
Use the official OpenAI Java SDK — NOT the deprecated Azure OpenAI SDK:

```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>4.21.0</version>
</dependency>
```

> **WARNING:** Do NOT use `com.openai:openai` version `1.0.0` — that artifact doesn't exist. The correct artifact is `openai-java`.

### Client initialization
```java
OpenAIClient client = OpenAIOkHttpClient.builder()
    .baseUrl(endpoint)
    .credential(AzureApiKeyCredential.create(apiKey))
    .build();
```

### Model-specific constraints (gpt-5.2-chat on Azure)
The `gpt-5.2-chat` deployment has stricter parameter requirements than older models:

- **Use `.maxCompletionTokens()`** — NOT `.maxTokens()`. The model rejects `max_tokens`.
- **Do NOT set `.temperature()`** — the model only supports the default value (1). Setting any other value (e.g., 0.7) causes an error.
- **Use `ChatModel.of(deploymentName)`** to specify the model dynamically.

```java
// CORRECT — works with gpt-5.2-chat
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(ChatModel.of(deploymentName))
    .addSystemMessage(systemPrompt)
    .addUserMessage(userPrompt)
    .maxCompletionTokens(1500)
    .build();

// WRONG — will fail at runtime
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(ChatModel.of(deploymentName))
    .addSystemMessage(systemPrompt)
    .addUserMessage(userPrompt)
    .maxTokens(1500)          // ERROR: 'max_tokens' not supported
    .temperature(0.7)         // ERROR: 'temperature' only supports default (1)
    .build();
```

### Configuration in application.yml
```yaml
openai:
  endpoint: ${OPENAI_ENDPOINT:<your-azure-openai-endpoint>}
  apikey: ${OPENAI_API_KEY:<your-api-key>}
  deployment: ${OPENAI_DEPLOYMENT:<your-deployment-name>}
```

Use `@Value("${openai.endpoint}")` etc. in the service class, with environment variable fallbacks so the Docker container can be configured without rebuilding.

---

## Markdown-to-HTML Rendering

The OpenAI response comes back as **markdown**. To display it properly in the Thymeleaf template:

1. Add the **commonmark** dependency to `pom.xml`:
```xml
<dependency>
    <groupId>org.commonmark</groupId>
    <artifactId>commonmark</artifactId>
    <version>0.22.0</version>
</dependency>
```

2. In `SkiOpenAIService`, parse the markdown response and convert to HTML before returning it:
```java
private final Parser markdownParser = Parser.builder().build();
private final HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();

// After getting the AI response:
String markdown = completion.choices().get(0).message().content().orElse("No content");
Node document = markdownParser.parse(markdown);
return htmlRenderer.render(document);  // Returns HTML string
```

3. In the Thymeleaf template, use `th:utext` (not `th:text`) to render the HTML:
```html
<div class="ai-recommendations" th:utext="${result.recommendations}"></div>
```

4. Add CSS styling for the rendered HTML (headers, lists, bold, horizontal rules, etc.).

---

## Java & Build Requirements

- **Java 21** — all code must compile with Java 21. Use `<java.version>21</java.version>` in pom.xml.
- **Spring Boot 3.2.2** — match the existing app version.
- **Maven** — the project uses Maven (no Gradle, no mvnw wrapper).
- **WSL on Windows** — use forward slashes in paths when running terminal commands. The workspace is at `/mnt/c/githublocal/Monolith-Struts-Sample/`.

---

## Database Schema Notes

The existing database uses these relevant tables:
- `categories` — has `id`, `name`, `parent_id` columns. Ski categories include Snowboards, Boots, Bindings, etc.
- `products` — has `id`, `name`, `description`, `regular_price`, `status`, `category_id`, `quantity` columns.
- `prices` — has `product_id`, `regular_price` columns (joined for pricing).

> **IMPORTANT:** Read the actual `01-schema.sql` and `02-data.sql` files before writing queries. Previous attempts assumed wrong table/column names (e.g., `price` vs `regular_price`, `stock` vs `quantity`). Always verify against the real schema.

---

## Docker Notes

- The app runs via Docker Compose with services `app` and `db`.
- After code changes, rebuild with `docker compose build --no-cache` (the `--no-cache` flag is important — without it, source changes may not be picked up).
- PostgreSQL init scripts in `docker-entrypoint-initdb.d` run in **alphabetical order** — prefix files with `01-`, `02-` etc. to control ordering. (`data.sql` runs before `schema.sql` alphabetically, causing failures.)
- Shell scripts (like `entrypoint.sh`) must have **Unix LF line endings**, not Windows CRLF. If you see `'sh\r': No such file or directory`, fix with: `sed -i 's/\r$//' <file>`.

---

## Branding

- The app is branded as **"Duke's Ski Chalet"** (not "Duke's Ski Shop").
- Use the existing color scheme: navy primary (`#1a1a2e`), red accent (`#c0392b`).
- The Duke mascot image is at `/images/duke-ski.png`.

---

## Sample OpenAI Code Reference

```java
package com.sample;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionMessage;
import com.openai.azure.credential.AzureApiKeyCredential;

public final class Chat {
    public static void main(String[] args) {
        String endpoint = "<your-azure-openai-endpoint>";
        String deploymentName = "<your-deployment-name>";

        OpenAIClient client = OpenAIOkHttpClient.builder()
            .baseUrl(endpoint)
            .credential(AzureApiKeyCredential.create("<your-api-key>"))
            .build();

        ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
            .model(ChatModel.of(deploymentName))
            .addSystemMessage("You are a helpful assistant.")
            .addUserMessage("Can you help me?")
            .maxCompletionTokens(1500)
            .build();

        ChatCompletion chatCompletion = client.chat().completions().create(createParams);

        for (ChatCompletion.Choice choice : chatCompletion.choices()) {
            ChatCompletionMessage message = choice.message();
            System.out.println(message.content());
        }
    }
}
```