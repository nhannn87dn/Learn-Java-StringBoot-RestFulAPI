Dưới đây là phần soạn chi tiết cho người mới về:

---

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

#### Ví dụ token:

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
.eyJzdWIiOiJ1c2VySWQiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTY... (rút gọn)
```

### ✨ Lợi ích:

* Gửi 1 lần → dùng nhiều lần
* Không lưu trạng thái phía server
* Hỗ trợ dễ dàng phân quyền
* Truyền qua Header dễ dàng

> 🧩 JWT chi tiết (tạo token, validate token, filter…) sẽ được trình bày ở phần nâng cao sau.

---

## VI. ✅ Tổng kết

| Chủ đề          | Ghi chú                                       |
| --------------- | --------------------------------------------- |
| Spring Security | Framework bảo mật trong Spring                |
| Basic Auth      | Dùng cho các API đơn giản, học tập            |
| JWT             | Dùng cho các hệ thống production REST API     |
| Cấu hình cơ bản | Dùng `httpBasic()` và `authorizeHttpRequests` |
| User In-Memory  | Tạo nhanh user test với Spring                |

---

## 📌 Gợi ý thực hành

1. Viết API `/api/public/hello` không cần đăng nhập.
2. Viết API `/api/secure/profile` chỉ cho truy cập nếu đã xác thực.
3. Tạo user trong memory với vai trò `USER`, dùng Postman gửi Basic Auth.

---

Bạn muốn tiếp tục sang **Phần 7: Quan hệ bảng (OneToMany, ManyToOne)** không? Đây là bước kế tiếp quan trọng để xây dựng hệ thống nhiều bảng như Blog, Quản lý sản phẩm - danh mục, v.v.
