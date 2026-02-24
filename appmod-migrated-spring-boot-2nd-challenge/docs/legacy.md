# Legacy Struts App

This repository retains the original Struts 1.x app under the root `src/` and `src/main/webapp` for reference/workshop purposes.

## Cleanup Options

1. **Archive to `legacy/`**
   ```bash
   mkdir -p legacy
   git mv src legacy/struts-src
   git mv Dockerfile legacy/Dockerfile.tomcat6
   git mv Dockerfile.tomcat6 legacy/Dockerfile.tomcat6
   git mv docker legacy/docker
   git mv docker-compose.yml legacy/docker-compose.yml
   git mv pom.xml legacy/pom.xml
   ```

2. **Remove entirely**
   ```bash
   git rm -r src src/main/webapp Dockerfile Dockerfile.tomcat6 docker docker-compose.yml pom.xml
   ```

3. **Keep as-is (current)**
   - Boot app lives in `spring-boot-app/`
   - Struts app is not built/run by default

## Verification
- Boot module contains **no** `org.apache.struts` references (see `scripts/verify-no-struts.sh`).

## Notes
- Do **not** mix Struts dependencies into `spring-boot-app/pom.xml`.
- Prefer removing legacy once migration cut-over is complete.
