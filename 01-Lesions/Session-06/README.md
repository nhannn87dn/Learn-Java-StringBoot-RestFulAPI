# 🔐 **Phần 6: Bảo mật API với Spring Security (Cơ bản)**

## 🎯 Mục tiêu

* Hiểu vai trò của Spring Security trong bảo vệ API.
* Cấu hình bảo mật cơ bản với HTTP Basic Auth.
* Giới thiệu khái niệm JWT và vì sao cần thiết.

---

## I. 🔐 Spring Security là gì?

**Spring Security** là một **framework bảo mật mạnh mẽ** dành cho ứng dụng Java, đặc biệt tích hợp tốt với Spring Boot.

### Tính năng chính:

* Xác thực (authentication)
* Phân quyền (authorization)
* Ngăn chặn truy cập trái phép
* Bảo vệ CSRF, CORS, session, password…

> 📌 Mặc định, Spring Security **bảo vệ tất cả các endpoint HTTP** nếu không cấu hình khác.

---

## II. ⚙️ Cấu hình Spring Security mặc định

### 1. Thêm dependency

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### 2. Chạy app và kiểm tra

Khi bạn khởi động ứng dụng lần đầu sau khi thêm Spring Security:

* Tất cả endpoint `/api/...` sẽ bị bảo vệ.
* Spring tạo sẵn một user mặc định: `user`
* Password mặc định được in ra console như:

```
Using generated security password: 39e5c0df-1234-5678...
```

---

## III. 🔐 HTTP Basic Authentication

**HTTP Basic Auth** là cơ chế xác thực đơn giản:

* Client gửi `username:password` mã hóa base64 trong header:

  ```
  Authorization: Basic dXNlcjpwYXNz
  ```

### 1. Tạo cấu hình bảo mật cho phép Basic Auth

📄 `SecurityConfig.java`

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Tắt CSRF cho API
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(); // Kích hoạt HTTP Basic Auth

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("admin")
            .password(passwordEncoder().encode("admin123"))
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 2. Test bằng Postman hoặc curl

* Truy cập `/api/secure` ➜ bị yêu cầu xác thực.
* Truy cập `/api/public/hello` ➜ truy cập tự do.

---

## IV. 🔄 So sánh với JWT – Giới thiệu

| HTTP Basic Auth                   | JWT (JSON Web Token)                      |
| --------------------------------- | ----------------------------------------- |
| Gửi username/password mỗi lần     | Gửi 1 token duy nhất                      |
| Đơn giản, nhưng không bảo mật cao | Bảo mật hơn, scalable hơn                 |
| Không thích hợp cho SPA/mobile    | Rất phù hợp cho API và frontend tách biệt |
| Stateless: ❌ (phụ thuộc session)  | Stateless: ✅                              |

---

## V. 🧠 JWT là gì? (Giới thiệu nhẹ)

### 🔐 JWT (JSON Web Token)

* Là một **chuỗi token nhỏ** chứa thông tin người dùng đã mã hoá.
* Không cần lưu session, thích hợp cho ứng dụng SPA (React, Angular, Mobile...).

Ví dụ token:

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
.eyJzdWIiOiJ1c2VySWQiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTY... (rút gọn)
```

### ✨ Lợi ích:

* Gửi 1 lần → dùng nhiều lần
* Không lưu trạng thái phía server
* Hỗ trợ dễ dàng phân quyền
* Truyền qua Header dễ dàng


### 🔄 Tổng quan về cách hoạt động của JWT

1. Người dùng gửi `username` & `password` → `/api/auth/login`
2. Server xác thực và trả về một **JWT Token**
3. Client lưu token này và đính kèm vào header ở các request sau:

   ```
   Authorization: Bearer <JWT_TOKEN>
   ```
4. Server kiểm tra token hợp lệ và cho phép truy cập.

---

### ⚙️ Cách 1: Cấu hình JWT **thủ công (custom implementation)**

#### 1. Thêm thư viện JWT

```xml
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-api</artifactId>
  <version>0.11.5</version>
</dependency>
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-impl</artifactId>
  <version>0.11.5</version>
  <scope>runtime</scope>
</dependency>
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-jackson</artifactId>
  <version>0.11.5</version>
  <scope>runtime</scope>
</dependency>
```

---

#### 2. Tạo lớp tiện ích JWT (JwtUtils.java)

```java
@Component
public class JwtUtils {
    private final String jwtSecret = "your-secret-key";
    private final long jwtExpirationMs = 86400000; // 1 ngày

    public String generateJwtToken(UserDetails userDetails) {
        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
            .compact();
    }

    public String getUsernameFromJwt(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(jwtSecret.getBytes())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public boolean validateJwt(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(jwtSecret.getBytes())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
```

---

#### 3. Tạo API Login để sinh JWT

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken((UserDetails) authentication.getPrincipal());

        return ResponseEntity.ok(new JwtResponse(token));
    }
}
```

---

#### 4. Tạo JWT Filter để kiểm tra token

```java
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtUtils.validateJwt(token)) {
                String username = jwtUtils.getUsernameFromJwt(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
```

---

#### 5. Thêm filter vào cấu hình Spring Security

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
```

---

### ⚙️ Cách 2: Dùng thư viện hỗ trợ (ví dụ: **spring-boot-starter-oauth2-resource-server**)

#### Ưu điểm

* Cấu hình nhanh hơn, tuân thủ chuẩn OAuth2.
* Dùng tốt với Auth0, Keycloak, Google…

#### Thêm dependency

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

#### Cấu hình `application.yml`

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://your-auth-provider.com
```

➡️ Spring Boot sẽ tự động kiểm tra token và phân quyền theo chuẩn JWT.

---

### 🔁 So sánh hai cách

| Tiêu chí                     | Thủ công (JwtUtils + Filter) | Thư viện OAuth2      |
| ---------------------------- | ---------------------------- | -------------------- |
| Linh hoạt                    | ✅ Cao (custom tùy ý)         | ❌ Bị ràng buộc       |
| Cấu hình nhanh               | ❌ Tốn thời gian cấu hình     | ✅ Nhanh chóng        |
| Tích hợp với Auth0/OIDC      | ❌ Khó                        | ✅ Dễ                 |
| Kiểm soát chi tiết (role...) | ✅ Tùy chỉnh sâu              | ⚠️ Cần cấu hình thêm |

---


## VI. 📌 Gợi ý thực hành

1. Viết API `/api/public/hello` không cần đăng nhập.
2. Viết API `/api/secure/profile` chỉ cho truy cập nếu đã xác thực.
3. Tạo user trong memory với vai trò `USER`, dùng Postman gửi Basic Auth.

