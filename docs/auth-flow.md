# Authentication & JWT Flow (request → validation → response)

This document describes how an incoming HTTP request is processed, how the JWT is validated, and how authenticated requests produce responses in this project.

1) Incoming request
- Client sends an HTTP request to an API endpoint (e.g., `/students`, `/auth/login`).
- If the endpoint is under `/auth/**` (see Security configuration), it is permitted without a JWT.
  - See Security configuration: [src/main/java/com/example/demo/configs/SecurityConfiguration.java](src/main/java/com/example/demo/configs/SecurityConfiguration.java)

2) Authentication endpoints (login / signup)
- Login: client calls `POST /auth/login` with credentials.
  - Controller: [src/main/java/com/example/demo/controller/AuthenticationController.java](src/main/java/com/example/demo/controller/AuthenticationController.java)
  - The `AuthenticationService` authenticates the user and returns a `Student`.
  - `JwtService.generateToken(...)` is called to create a signed JWT. See: [src/main/java/com/example/demo/service/impl/JwtService.java](src/main/java/com/example/demo/service/impl/JwtService.java)
  - Response contains `token` and `expiresIn`.

3) Protected endpoints
- All other endpoints require authentication per `SecurityConfiguration` (anyRequest().authenticated()).
  - See: [src/main/java/com/example/demo/configs/SecurityConfiguration.java](src/main/java/com/example/demo/configs/SecurityConfiguration.java)

4) How the JWT is supplied
- Client must set HTTP header `Authorization: Bearer <token>` on requests to protected endpoints.

5) JWT extraction & validation (filter)
- The filter `JwtAuthenticationFilter` runs once per request and executes the following logic:
  - Reads the `Authorization` header. If missing or not starting with `Bearer `, the filter lets the request continue without setting authentication. See: [src/main/java/com/example/demo/configs/JwtAuthenticationFilter.java](src/main/java/com/example/demo/configs/JwtAuthenticationFilter.java)
  - Extracts the token substring after `Bearer ` and calls `JwtService.extractUsername(jwt)` to read the subject (username/email) from token claims.
  - If a username is present and there is no existing `Authentication` in `SecurityContextHolder`, it loads `UserDetails` via the configured `UserDetailsService`.
  - Calls `JwtService.isTokenValid(jwt, userDetails)` which checks that the token subject equals the user and that the token is not expired. Implementation details:
    - Signature validated using the configured secret key (decoded from base64) and HS256.
    - Expiration checked against the token's `exp` claim.
    - See: [src/main/java/com/example/demo/service/impl/JwtService.java](src/main/java/com/example/demo/service/impl/JwtService.java)
  - If valid, constructs a `UsernamePasswordAuthenticationToken` with the user's authorities and sets it into `SecurityContextHolder.getContext().setAuthentication(...)` so downstream handlers see the authenticated principal.
  - If any exception occurs (parsing, validation, user lookup), the filter delegates to the `HandlerExceptionResolver` to produce an appropriate response (see filter error handling).

6) Response flow for authenticated requests
- Once the `SecurityContext` contains an authenticated principal, controller methods can access it (via `@AuthenticationPrincipal` or `SecurityContextHolder`) and return protected resources.
- If the token is invalid or missing, the request will either be denied by downstream security/authorization checks or proceed as anonymous (depending on endpoint configuration).

7) Configuration points you may want to change
- Expose Actuator endpoints: set `management.endpoints.web.exposure.include` in `src/main/resources/application.properties`.
- Permit or secure actuator endpoints in `SecurityConfiguration` (currently `"/actuator/**"` is permitted in the existing config).
- Adjust CORS allowed origins/methods in `SecurityConfiguration.corsConfigurationSource()`.
- Control token lifetime and secret via properties referenced by `JwtService`:
  - `security.jwt.secret-key`
  - `security.jwt.expiration-time`

8) Relevant files
- Security config: [src/main/java/com/example/demo/configs/SecurityConfiguration.java](src/main/java/com/example/demo/configs/SecurityConfiguration.java)
- JWT filter: [src/main/java/com/example/demo/configs/JwtAuthenticationFilter.java](src/main/java/com/example/demo/configs/JwtAuthenticationFilter.java)
- JWT service: [src/main/java/com/example/demo/service/impl/JwtService.java](src/main/java/com/example/demo/service/impl/JwtService.java)
- Auth controller: [src/main/java/com/example/demo/controller/AuthenticationController.java](src/main/java/com/example/demo/controller/AuthenticationController.java)

9) Quick troubleshooting
- If `/actuator/**` returns 401/403: ensure `management.endpoints.web.exposure.include` includes the endpoints you need and that `SecurityConfiguration` permits `/actuator/**` (this project currently permits it).
- If tokens are rejected: verify `security.jwt.secret-key` matches the key used to sign tokens and that `security.jwt.expiration-time` is large enough.

---
Document created by the dev assistant to describe request → token validation → response. If you want, I can add sequence diagrams or inline code excerpts with exact line numbers.