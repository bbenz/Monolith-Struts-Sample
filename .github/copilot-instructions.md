# Duke's Ski Chalet — Project Instructions

These are shared project-wide rules for the Spring Boot application in `appmod-migrated-java21-spring-boot-2nd-challenge/`. Task-specific prompts are in the `prompts/` directory and build on these shared conventions.

---

## Branding

- **App name:** "Duke's Ski Chalet" — use this exact name everywhere (title bar, header, footer, nav, etc.). Not "Duke's Ski Shop", not "Ski Resort Shop".
  > **IMPORTANT:** Decide on ONE name and use it consistently. Pick "Duke's Ski Chalet" and apply it everywhere in a single pass.
- **Mascot image:** Java Duke mascot in ski gear, saved at `src/main/resources/static/images/duke-ski.png`. Used as:
  - The site logo/icon in the header nav bar
  - A hero image on the home page
  - The favicon (if applicable)
- **Image requirements:** PNG with transparent background (~200px wide for hero, ~40px height for navbar icon).
- **"DukeDuke's" bug:** When placing the mascot image inside an `<a>` tag with adjacent text, set `alt=""` on the image to prevent screen reader text concatenation:
  ```html
  <!-- CORRECT -->
  <a class="navbar-brand" href="/">
      <img src="/images/duke-ski.png" alt="" /> Duke's Ski Chalet
  </a>

  <!-- WRONG — renders as "DukeDuke's Ski Chalet" -->
  <a class="navbar-brand" href="/">
      <img src="/images/duke-ski.png" alt="Duke" /> Duke's Ski Chalet
  </a>
  ```

---

## Color Scheme

Derived from the Duke mascot image (navy/red/white):

| Role | Color | Usage |
|------|-------|-------|
| Primary (navy) | `#1a1a2e` | Navbar background, footer, headings |
| Accent (red) | `#c0392b` | Buttons, hover states, price text, badges |
| Accent hover | `#e74c3c` | Button hover states |
| Background | `#f8f9fa` | Page body |
| Card background | `#ffffff` | Product cards, content areas |
| Text | `#2c3e50` | Body text |
| Muted text | `#7f8c8d` | Secondary text, descriptions |

---

## Tech Stack & Build Requirements

- **Java 21** — all code must compile with Java 21. Use `<java.version>21</java.version>` in pom.xml.
- **Spring Boot 3.2.2** — match the existing app version.
- **Maven** — the project uses Maven (no Gradle, no mvnw wrapper).
- **Package structure:** `com.skishop` — follow existing patterns: Spring MVC controllers, `@Service` classes, Spring Data JPA repositories.
- **WSL on Windows** — use forward slashes in paths when running terminal commands. The workspace is at `/mnt/c/githublocal/Monolith-Struts-Sample/`.

---

## Thymeleaf Template Notes

- Use `th:text` for plain text content, `th:utext` for HTML content (e.g., AI-generated recommendations rendered from markdown).
- Ternary expressions must be fully inside `${}`:
  ```html
  <!-- CORRECT -->
  <span th:text="${product.salePrice != null ? product.salePrice : product.regularPrice}"></span>

  <!-- WRONG — Thymeleaf parsing error -->
  <span th:text="${product.salePrice} != null ? ${product.salePrice} : ${product.regularPrice}"></span>
  ```
- When using fragment layouts (`layout:decorate`), ensure CSS/JS references use Thymeleaf URL syntax: `th:href="@{/css/app.css}"`.
- The app uses `layout/main.html` as the decorator layout — all pages should use `layout:decorate="~{layout/main}"`.

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

When rendering AI-generated markdown in Thymeleaf templates:

1. Add the **commonmark** dependency to `pom.xml`:
```xml
<dependency>
    <groupId>org.commonmark</groupId>
    <artifactId>commonmark</artifactId>
    <version>0.22.0</version>
</dependency>
```

2. Parse the markdown response and convert to HTML:
```java
private final Parser markdownParser = Parser.builder().build();
private final HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();

String markdown = completion.choices().get(0).message().content().orElse("No content");
Node document = markdownParser.parse(markdown);
return htmlRenderer.render(document);  // Returns HTML string
```

3. In templates, use `th:utext` (not `th:text`) to render the HTML:
```html
<div class="ai-recommendations" th:utext="${result.recommendations}"></div>
```

4. Add CSS styling for the rendered HTML (headers, lists, bold, horizontal rules, etc.).

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
