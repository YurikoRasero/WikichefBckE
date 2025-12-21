# Security Architecture Documentation

## Table of Contents
1. [Current Architecture Overview](#current-architecture-overview)
2. [Security Implementation Analysis](#security-implementation-analysis)
3. [Identified Weaknesses](#identified-weaknesses)
4. [Recommended Improvements](#recommended-improvements)
5. [Cloud Deployment Considerations](#cloud-deployment-considerations)

---

## Current Architecture Overview

### Application Structure

The WikiChef backend is a Spring Boot application using JWT-based authentication with the following components:

#### Controllers (API Endpoints)
- **AuthenticationController** (`/api/v1/auth/**`)
  - `POST /api/v1/auth/signup` - User registration
  - `POST /api/v1/auth/signin` - User login

- **RecetaController** (`/api/recetas/**`)
  - `GET /api/recetas` - List all recipes (currently public)
  - `GET /api/recetas/{id}` - Get recipe by ID (currently public)
  - `POST /api/recetas` - Create recipe (requires auth)
  - `PUT /api/recetas/{id}` - Update recipe (requires auth)
  - `DELETE /api/recetas/{id}` - Delete recipe (requires auth)

- **UserController** (`/api/users/**`)
  - `GET /api/users` - List users (requires auth)
  - `GET /api/users/{id}` - Get user by ID (requires auth)
  - `POST /api/users` - Create user (requires auth)
  - `PUT /api/users/{id}` - Update user (requires auth)
  - `DELETE /api/users/{id}` - Delete user (requires auth)

- **ComentarioController** (`/api/comentarios/**`)
  - All CRUD operations require authentication

- **CalificacionController** (`/api/calificaciones/**`)
  - All CRUD operations require authentication

- **AuthorizationController** (`/api/v1/resource/**`)
  - Test endpoint requiring authentication

### Security Flow

```
Request → JwtAuthenticationFilter → SecurityFilterChain → Controller
```

1. **JwtAuthenticationFilter** intercepts all requests
2. Extracts JWT token from `Authorization: Bearer <token>` header
3. Validates token signature and expiration
4. Loads user details from database
5. Sets authentication context if valid
6. **SecurityFilterChain** checks if endpoint requires authentication
7. Allows or denies request based on security rules

### Current Security Configuration

```java
// SecurityConnfiguration.java
.authorizeHttpRequests(request -> request
    .requestMatchers("/api/v1/auth/**").permitAll()
    .requestMatchers(HttpMethod.GET, "/api/recetas/**").permitAll()
    .anyRequest().authenticated())
```

**Current Access Rules:**
- ✅ Public: `/api/v1/auth/**` (signup/signin)
- ✅ Public: `GET /api/recetas/**` (read-only recipe access)
- 🔒 Protected: All other endpoints require valid JWT token

---

## Security Implementation Analysis

### JWT Token Flow

1. **Token Generation** (`JwtServiceImpl.generateToken()`)
   - Uses HMAC-SHA256 algorithm
   - Token expiration: 24 minutes (hardcoded: `1000 * 60 * 24`)
   - Subject: User email
   - Signing key: Base64-encoded secret from configuration

2. **Token Validation** (`JwtServiceImpl.isTokenValid()`)
   - Extracts username from token
   - Checks if username matches UserDetails
   - Verifies token expiration
   - **Note**: Signature verification happens during claim extraction

3. **Filter Processing** (`JwtAuthenticationFilter`)
   - Runs on every request (except those matching `permitAll()`)
   - Database lookup on every authenticated request
   - No token caching or blacklist mechanism

### Authentication Wrapper Pattern

**Current Approach: "All-or-Nothing"**

All controllers are wrapped under the authentication layer by default. The security configuration uses a whitelist approach:

```
Default: AUTHENTICATED
Exceptions: 
  - /api/v1/auth/** (public)
  - GET /api/recetas/** (public)
```

**Problems with this approach:**
1. **Lack of Granularity**: Can't easily distinguish between public read operations and authenticated write operations within the same resource
2. **Maintenance Burden**: Security rules are centralized in one configuration class
3. **No Role-Based Access**: All authenticated users have the same permissions
4. **Database Load**: Every authenticated request triggers a database lookup for user details

---

## Identified Weaknesses

### 1. Token Management Issues

#### Weak Token Validation
- **Issue**: Token validation only checks username match and expiration
- **Risk**: No validation of token revocation, no blacklist mechanism
- **Impact**: Compromised tokens remain valid until expiration

```java
// Current implementation
public boolean isTokenValid(String token, UserDetails userDetails) {
    final String userName = extractUserName(token);
    return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
}
```

#### No Refresh Token Mechanism
- **Issue**: Tokens expire after 24 minutes, users must re-authenticate
- **Risk**: Poor user experience, frequent password entry
- **Impact**: Users may request longer token expiration, increasing security risk

#### Hardcoded Token Expiration
- **Issue**: Token expiration hardcoded in service: `1000 * 60 * 24`
- **Risk**: Not configurable per environment
- **Impact**: Can't adjust expiration for different environments (dev vs prod)

### 2. Security Configuration Weaknesses

#### No Rate Limiting
- **Issue**: Authentication endpoints have no rate limiting
- **Risk**: Brute force attacks on signin endpoint
- **Impact**: Account enumeration, credential stuffing attacks

#### CSRF Disabled Globally
- **Issue**: `http.csrf(AbstractHttpConfigurer::disable)` disables CSRF for all endpoints
- **Risk**: While stateless JWT doesn't need CSRF, this should be explicitly configured
- **Impact**: If session-based auth is added later, CSRF protection is missing

#### No Security Headers
- **Issue**: Missing security headers (HSTS, X-Frame-Options, CSP, etc.)
- **Risk**: XSS attacks, clickjacking, protocol downgrade attacks
- **Impact**: Vulnerable to common web attacks

#### Database Lookup on Every Request
- **Issue**: `JwtAuthenticationFilter` loads user from database on every request
- **Risk**: Performance bottleneck, unnecessary database load
- **Impact**: Slower response times, higher database costs in cloud

### 3. Architecture Issues

#### Monolithic Security Configuration
- **Issue**: All security rules in one class
- **Risk**: Difficult to maintain, test, and understand
- **Impact**: Changes require modifying core security configuration

#### No Role-Based Access Control (RBAC)
- **Issue**: All authenticated users have same permissions
- **Risk**: Users can access/modify any resource
- **Impact**: No way to restrict admin operations, user-specific data access

#### No Endpoint-Level Security Annotations
- **Issue**: Security rules only in configuration class
- **Risk**: Can't easily see which endpoints require what permissions
- **Impact**: Security rules not co-located with endpoint definitions

### 4. Cloud Deployment Concerns

#### No Distributed Token Validation
- **Issue**: No token blacklist/whitelist mechanism
- **Risk**: In multi-instance deployments, revoked tokens may still work
- **Impact**: Can't properly handle logout across instances

#### Configuration Management
- **Issue**: JWT secret in application.yaml (though using env vars in prod)
- **Risk**: Secret management not clearly documented
- **Impact**: Potential secret leakage in version control

#### No Request Logging/Auditing
- **Issue**: No security event logging
- **Risk**: Can't detect or investigate security incidents
- **Impact**: Compliance issues, difficult incident response

---

## Recommended Improvements

### 1. Implement Public/Private Endpoint Pattern

**Better Approach: Explicit Security Annotations**

Instead of wrapping everything and whitelisting exceptions, use explicit security annotations on controllers:

```java
// Recommended approach
@RestController
@RequestMapping("/api/recetas")
public class RecetaController {
    
    @GetMapping
    @PermitAll  // Explicitly public
    public ResponseEntity<List<RecetaDTO>> listarRecetas() {
        // Public read access
    }
    
    @GetMapping("/{id}")
    @PermitAll  // Explicitly public
    public ResponseEntity<RecetaDTO> obtenerReceta(@PathVariable Integer id) {
        // Public read access
    }
    
    @PostMapping
    @PreAuthorize("hasRole('USER')")  // Explicitly requires authentication
    public ResponseEntity<Receta> crearReceta(@RequestBody RecetaDTO recetaDTO) {
        // Requires authenticated user
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') and @recetaService.isOwner(#id, authentication.name)")
    public ResponseEntity<Receta> actualizarReceta(@PathVariable Integer id, ...) {
        // Requires authentication AND ownership
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @recetaService.isOwner(#id, authentication.name)")
    public ResponseEntity<Void> eliminarReceta(@PathVariable Integer id) {
        // Requires admin OR ownership
    }
}
```

**Benefits:**
- ✅ Security rules co-located with endpoints
- ✅ Easy to see what each endpoint requires
- ✅ Supports method-level security
- ✅ Enables role-based access control
- ✅ Better testability

### 2. Enhanced Security Configuration

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // Enable method-level security
public class SecurityConfiguration {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/api/**"))  // API endpoints don't need CSRF
            .sessionManagement(session -> session
                .sessionCreationPolicy(STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .anyRequest().authenticated())  // Default to authenticated
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(customAuthenticationEntryPoint())
                .accessDeniedHandler(customAccessDeniedHandler()))
            .headers(headers -> headers
                .frameOptions(FrameOptionsConfig::deny)
                .contentTypeOptions(ContentTypeOptionsConfig::and)
                .httpStrictTransportSecurity(hsts -> hsts
                    .maxAgeInSeconds(31536000)
                    .includeSubdomains(true)))
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        
        return http.build();
    }
}
```

### 3. Implement Refresh Tokens

```java
// Add to JwtService
String generateRefreshToken(UserDetails userDetails);
boolean isRefreshTokenValid(String token);
String refreshAccessToken(String refreshToken);
```

**Implementation:**
- Store refresh tokens in database or Redis
- Longer expiration (7-30 days)
- Rotate refresh tokens on use
- Revoke refresh tokens on logout

### 4. Add Rate Limiting

```java
@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    
    private final Map<String, RateLimiter> limiters = new ConcurrentHashMap<>();
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) {
        String endpoint = request.getRequestURI();
        
        if (endpoint.startsWith("/api/v1/auth/signin")) {
            String key = getClientIdentifier(request);
            RateLimiter limiter = limiters.computeIfAbsent(key, 
                k -> RateLimiter.create(5.0 / 60.0)); // 5 requests per minute
            
            if (!limiter.tryAcquire()) {
                response.setStatus(429);
                return;
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
```

### 5. Token Caching

Instead of database lookup on every request:

```java
@Cacheable(value = "users", key = "#email")
public UserDetails loadUserByUsername(String email) {
    // Database lookup only on cache miss
}
```

Or use Redis for distributed caching in cloud deployments.

### 6. Role-Based Access Control

```java
// In SecurityConfiguration
@Bean
public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
    DefaultMethodSecurityExpressionHandler handler = 
        new DefaultMethodSecurityExpressionHandler();
    handler.setPermissionEvaluator(new CustomPermissionEvaluator());
    return handler;
}

// Usage in controllers
@PreAuthorize("hasRole('ADMIN')")
@PreAuthorize("hasRole('USER') and #userId == authentication.principal.id")
@PreAuthorize("@recetaService.isOwner(#recetaId, authentication.name)")
```

---

## Cloud Deployment Considerations

### 1. Distributed Token Management

**Problem**: In multi-instance deployments, token revocation doesn't work across instances.

**Solution**: Use Redis for token blacklist:
```java
@Service
public class TokenBlacklistService {
    private final RedisTemplate<String, String> redisTemplate;
    
    public void blacklistToken(String token, long expirationTime) {
        redisTemplate.opsForValue().set(
            "blacklist:" + token, 
            "true", 
            expirationTime, 
            TimeUnit.MILLISECONDS
        );
    }
    
    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(
            redisTemplate.hasKey("blacklist:" + token)
        );
    }
}
```

### 2. Secret Management

**Current**: Environment variables (good for prod)
**Enhancement**: Use cloud secret management services:
- AWS Secrets Manager
- Azure Key Vault
- Google Secret Manager
- HashiCorp Vault

### 3. Request Logging and Monitoring

```java
@Component
public class SecurityAuditFilter extends OncePerRequestFilter {
    
    private final AuditLogger auditLogger;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) {
        String userId = getCurrentUserId();
        String endpoint = request.getRequestURI();
        String method = request.getMethod();
        
        auditLogger.log(new SecurityEvent(userId, endpoint, method, 
            response.getStatus(), getClientIp(request)));
        
        filterChain.doFilter(request, response);
    }
}
```

### 4. Health Checks and Monitoring

Add actuator endpoints for security monitoring:
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus
  endpoint:
    health:
      show-details: when-authorized
```

### 5. Load Balancer Configuration

- Configure health checks on `/actuator/health`
- Use sticky sessions if needed (though stateless JWT doesn't require it)
- Configure SSL/TLS termination at load balancer

### 6. CORS Configuration for Cloud

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(
        "https://wikichef.example.com",  // Production frontend
        "https://staging.wikichef.example.com"  // Staging
    ));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", configuration);
    return source;
}
```

### 7. Environment-Specific Security

```yaml
# application-prod.yaml
spring:
  security:
    jwt:
      expiration: 3600000  # 1 hour
      refresh-expiration: 604800000  # 7 days
  cache:
    type: redis
    redis:
      host: ${REDIS_HOST}
      port: ${REDIS_PORT}

# Rate limiting
security:
  rate-limit:
    signin: 5  # requests per minute
    signup: 3
    api: 100
```

---

## Migration Path

### Phase 1: Quick Wins (Low Risk)
1. Fix class name typo: `SecurityConnfiguration` → `SecurityConfiguration`
2. Add security headers
3. Configure CORS properly
4. Move token expiration to configuration
5. Add rate limiting to auth endpoints

### Phase 2: Enhanced Security (Medium Risk)
1. Implement refresh tokens
2. Add token blacklist (Redis)
3. Implement role-based access control
4. Add method-level security annotations
5. Implement token caching

### Phase 3: Cloud Optimization (Higher Risk)
1. Migrate to distributed token management
2. Implement comprehensive audit logging
3. Add monitoring and alerting
4. Optimize database queries
5. Implement request/response logging

---

## Summary

### Current State
- ✅ Basic JWT authentication working
- ✅ Public endpoints for auth and recipe reading
- ⚠️ Limited security features
- ⚠️ No role-based access control
- ⚠️ Performance concerns for cloud deployment

### Recommended State
- ✅ Explicit public/private endpoint pattern
- ✅ Role-based access control
- ✅ Refresh token mechanism
- ✅ Rate limiting
- ✅ Token blacklist for distributed systems
- ✅ Comprehensive security headers
- ✅ Audit logging
- ✅ Cloud-optimized architecture

### Key Takeaways
1. **Explicit is better than implicit**: Use annotations to make security requirements clear
2. **Separation of concerns**: Security rules should be co-located with endpoints
3. **Cloud-ready**: Design for distributed systems from the start
4. **Defense in depth**: Multiple layers of security (rate limiting, headers, RBAC, etc.)
5. **Observability**: Log security events for monitoring and incident response

---

## References

- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [Spring Security Best Practices](https://spring.io/guides/topicals/spring-security-architecture)
- [JWT Best Practices](https://datatracker.ietf.org/doc/html/rfc8725)
- [Cloud Security Architecture Patterns](https://aws.amazon.com/architecture/security-identity-compliance/)

