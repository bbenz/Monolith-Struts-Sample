# Implementation Plan (SkiShop Monolith - Struts 1.2.9 / Java 5)

<!-- markdownlint-disable MD013 MD029 -->

## Phase Structure

0. Environment & Framework
1. Domain/DAO/DDL
2. Service/Business Logic
3. Web Layer (Action/Form/Validation)
4. View (JSP/Tiles)
5. Security/RequestProcessor
6. Admin Features
7. Notification/Email
8. Non-functional/Operations/Docker
9. Testing/QA
10. Release

---

## Phase 0: Environment & Framework

**Content**

- Create Ant 1.8 project template (`source/target=1.5`, Maven 2 also acceptable).
- Prepare `build.xml` / `build.properties` / `lib/` (dependency JAR placement or Ivy).
- Create package structure/folder structure (`src/main/java`, `src/main/resources`, `src/main/webapp`).
- `web.xml` / `struts-config.xml` skeleton, `context.xml` (Tomcat DataSource), `log4j.properties` template.
- Place dependency JARs (Struts 1.2.9, log4j, dbcp, dbutils, javax.mail, servlet/jsp-api, junit/strutstestcase).
- `.editorconfig` / coding convention memo.

**Exit Criteria**

- `ant war` / `ant test` succeeds and generates empty WAR (if using Maven: `mvn -B -DskipTests package`).
- ActionServlet starts on Tomcat (6 or 8) with startup logs showing no errors other than 404.
- `lib/` has necessary dependencies, no unresolved classes at compile time.

---

## Phase 1: Domain/DAO/DDL

**Content**

- Domain/DTO definition: User, Role, UserSession, Product, ProductImage, Category, Price, Inventory, Cart, Order, OrderItem, OrderShipping, Shipment, Return, Payment, PointAccount, PointTransaction, Coupon, CouponUsage, Campaign, Address, PasswordResetToken, ShippingMethod, SecurityLog, EmailQueue, OAuthAccount (optional).
- Implement `AbstractDao`, `DaoException`, `DataSourceLocator`.
- Create DAO interfaces and implementations: UserDao, RoleDao, UserSessionDao, ProductDao, ProductImageDao, InventoryDao, CouponDao, CouponUsageDao, CartDao, OrderDao, OrderShippingDao, ShipmentDao, ReturnDao, PaymentDao, PointAccountDao, PointTransactionDao, UserAddressDao, PasswordResetTokenDao, ShippingMethodDao, SecurityLogDao, EmailQueueDao, OAuthAccountDao (optional).
- Create DDL scripts (all tables/indexes/constraints, foreign keys/unique constraints, including `coupon_usage`/`security_logs`/`payments`).
- Initial data insertion SQL (sample products/users/addresses/inventory).

**Exit Criteria**

- DDL execution succeeds on H2/PG, DAO CRUD tests are green with JUnit + H2/DBUnit.
- Main DAO methods return expected data (findByEmail, findPaged, reserve, findActiveCouponUsage, lockInventory, recordPayment, etc.).
- DDL includes required indexes, foreign key constraint violations are detected.

---

## Phase 2: Service/Business Logic

**Content**

- Service implementation: AuthService, UserService, ProductService, InventoryService, CouponService, CartService, PaymentService (mock), OrderService, PointService, ShippingService, TaxService, MailService, SecurityLogService, SessionService.
- Implement `OrderFacade` (placeOrder, cancelOrder, returnOrder).
- Business rules:
  - Coupon validation (usage_limit, minimum_amount, period, is_active).
  - Point award/use (1%・365 days).
  - Tax calculation 10%, shipping 800 yen/free for 10,000+ yen.
  - Inventory pessimistic locking `SELECT ... FOR UPDATE`, exception if insufficient.
  - Payment authorization success/failure handling, point/coupon adjustment on cancel/return.
  - Idempotency (`order_number` unique, prevent double charge, prevent duplicate email sending).
  - Define transaction boundaries (per service method, rollback on exception).

**Exit Criteria**

- JUnit service tests (H2 + DBUnit) are green:
  - Success: checkout (coupon+points) → update order/point/coupon_usage/stock/payments.
  - Cancel: return inventory, deduct points, decrement coupon usage count.
  - Return: refund record, point adjustment.
- Concurrency test: pessimistic locking is effective for simultaneous checkouts, no double charge.
- Rollback test: DB consistency maintained on mid-process failure.

---

## Phase 3: Web Layer (Action/Form/Validation)

**Content**

- Action implementation: Login, Register, PasswordForgot, PasswordReset, ProductList, ProductDetail, Cart, Checkout, CouponApply, OrderHistory, OrderCancel, OrderReturn, PointBalance, AddressList, AddressSave, Logout.
- ActionForm implementation: LoginForm, RegisterForm, PasswordResetRequestForm, PasswordResetForm, ProductSearchForm, AddCartForm, CheckoutForm, CouponForm, AddressForm, AdminProductForm.
- Organize `validation.xml` rules (email, password, quantity, postal code, phone, card info).
- Organize `messages.properties` keys.

**Exit Criteria**

- StrutsTestCase shows expected forwards/validations for main Actions (success/failure).
- Input error display based on `validation.xml` can be confirmed in JSP.
- Tests exist for token validation passing/failing with forms containing CSRF tokens.

---

## Phase 4: View (JSP/Tiles)

**Content**

- Tiles layout `base.jsp`, common `header.jsp`, `footer.jsp`, `messages.jsp`.
- Create JSPs: auth/login, auth/register, auth/password/forgot/reset, products/list/detail, cart/view/checkout/confirmation, orders/history/detail, points/balance, account/addresses/address_edit, admin/products/list/edit, admin/orders/list/detail.
- Apply Struts tags (html/bean/logic), `<bean:write filter="true"/>` by default.
- Embed CSRF token (`<html:hidden property="org.apache.struts.taglib.html.TOKEN"/>`).

**Exit Criteria**

- Manual operation check: Pages render on Tomcat with no tag resolution errors.
- UI path from add to cart to checkout completes.
- XSS countermeasure: `<bean:write filter="true"/>` default, no escaping leaks confirmed by visual inspection.

---

## Phase 5: Security / RequestProcessor

**Content**

- `AuthRequestProcessor` extension: authorization (roles), unauthenticated redirect, CSRF verification, session fixation countermeasure (invalidate→new on login).
- Login attempt limit (5 times, record in `security_logs`, `users.status=LOCKED`).
- Password hash (SHA-256 + salt + 1000 iterations).
- Cookie setting `CART_ID` HttpOnly/Secure.

**Exit Criteria**

- Access to protected resources when not logged in→redirect to `/login.do`.
- CSRF invalid time 403 equivalent response.
- Lock after 5 wrong attempts, confirm `security_logs` record, confirm unlock procedure.

---

## Phase 6: Admin Features

**Content**

- Admin Actions/Forms/JSP: product CRUD, inventory update, order status update/refund, coupon management (optional), shipping method management (optional).
- Admin role determination.

**Exit Criteria**

- Login as admin user, can list/edit products, update inventory, update orders.
- Non-admin cannot access `/admin/*`.

---

## Phase 7: Notification / Email

**Content**

- Implement `MailService` (JavaMail 1.4.7), load SMTP configuration.
- `email_queue` DAO and sending job (simple thread or Timer).
- Prepare templates (order confirmation, password reset) as simple strings without JSP/Velocity.

**Exit Criteria**

- Confirm email sending with local SMTP (MailHog, etc.).
- Retry on failure, update `status/retry_count/last_error`.
- Prevent duplicate sending (same `email_queue` record sent only once).

---

## Phase 8: Non-functional / Operations / Docker

**Content**

- log4j MDC (`reqId`), RollingFileAppender configuration.
- Load `app.properties` and integrate with ServiceLocator/Factory.
- DBCP tuning (maxActive/maxIdle/maxWait).
- `Dockerfile` (Tomcat8/JDK8), `Dockerfile.tomcat6`, `docker-compose.yml` (PG) configuration.
- Organize `.dockerignore`.

**Exit Criteria**

- `docker-compose up` starts app+DB, basic flow succeeds.
- WAR size and log rotation as expected.
- `ant war` succeeds from within Docker (build reproducibility).

---

## Phase 9: Testing / QA

**Content**

- StrutsTestCase: Actions coverage.
- JUnit + DBUnit: DAO/Service coverage.
- Scenario test: registration→login→search→cart→checkout→cancel→return.
- Load test (optional) simple check with JMeter/Locust.

**Exit Criteria**

- Test suite green, coverage (Actions/Services/DAO) target 80%.
- Regression test checklist completed.
- SQL injection/input validation negative case tests exist.

---

## Phase 10: Release

**Content**

- WAR versioning, create release notes.
- Deployment procedure document (Tomcat 6/8), organize environment variables and context configuration.
- Backup/restore procedure (DB) memo.

**Exit Criteria**

- Deployment to Tomcat6/Tomcat8 performed and operation confirmed.
- Documentation updated (README/implementation plan/operation procedure).

---

## Supplement (Cross-cutting)

- Comply with coding conventions (Java 1.5 syntax, generics optional, no annotations).
- Review checkpoint list (SQL injection, null check, logging, exception conversion, transaction boundaries, Idempotency).
- Lint/Checkstyle (Java 5 configuration) optional introduction.
- i18n: `messages.properties` / `messages_ja.properties`.
- Runbook: Thread dump acquisition, log rotation check procedure.
- Transaction policy: Start/commit at service layer, DAO layer is transaction-agnostic.
- Idempotency: Prevent duplication with `order_number` and `email_queue` send flag/unique constraint.
