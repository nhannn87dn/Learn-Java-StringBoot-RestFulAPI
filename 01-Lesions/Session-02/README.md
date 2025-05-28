# 🧱 **Phần 2: Xây dựng REST API đơn giản**

---

## I. 🎯 Mục tiêu

* Làm quen với cách tạo một REST API đơn giản trong Spring Boot.
* Hiểu các annotation như `@RestController`, `@RequestMapping`, `@GetMapping`, `@PostMapping`.
* Làm quen với cách truyền dữ liệu vào API thông qua `@PathVariable`, `@RequestParam`, `@RequestBody`.
* Xây dựng cấu trúc thư mục chuẩn hóa theo mô hình: **controller – service – repository – model – dto**.

---

## II. 🚀 Tạo ứng dụng Hello World

### 1. Tạo lớp `HelloController.java`

```java
package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, World!";
    }
}
```

> ✅ `@RestController` là sự kết hợp giữa `@Controller` và `@ResponseBody` giúp trả JSON.
>
> ✅ `@GetMapping("/hello")` tạo một endpoint GET tại đường dẫn `/hello`.

---

## III. 📁 Routes Mapping

### 1. Các Annotation cơ bản

```java
@RestController
@RequestMapping("/api")  // Base URL cho toàn bộ controller
public class UserController {

    // GET /api/users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    // GET /api/users/{id}
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    // POST /api/users
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }

    // PUT /api/users/{id}
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.update(id, user);
    }

    // DELETE /api/users/{id}
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
```

### 2. Các cách định nghĩa route khác nhau

#### a. Sử dụng `@RequestMapping` với method
```java
@RequestMapping(value = "/users", method = RequestMethod.GET)
public List<User> getAllUsers() {
    return userService.findAll();
}
```

#### b. Kết hợp nhiều HTTP methods
```java
@RequestMapping(value = "/users", method = {RequestMethod.GET, RequestMethod.POST})
public List<User> handleUsers() {
    return userService.findAll();
}
```

#### c. Sử dụng `@RequestMapping` với params
```java
@RequestMapping(value = "/search", params = "name")
public List<User> searchByName(@RequestParam String name) {
    return userService.findByName(name);
}
```

#### d. Sử dụng `@RequestMapping` với headers
```java
@RequestMapping(value = "/users", headers = "API-Version=1")
public List<User> getUsersV1() {
    return userService.findAll();
}
```

### 3. Các annotation mapping phổ biến

| Annotation     | HTTP Method | Ví dụ                    |
|---------------|-------------|--------------------------|
| `@GetMapping` | GET         | `@GetMapping("/users")`  |
| `@PostMapping`| POST        | `@PostMapping("/users")` |
| `@PutMapping` | PUT         | `@PutMapping("/users")`  |
| `@DeleteMapping`| DELETE    | `@DeleteMapping("/users")`|
| `@PatchMapping`| PATCH      | `@PatchMapping("/users")`|

### 4. Best Practices

1. **Sử dụng base URL**: Định nghĩa base URL cho controller
```java
@RestController
@RequestMapping("/api/v1")
public class UserController {
    // Các endpoints sẽ có prefix /api/v1
}
```

2. **Đặt tên resource theo danh từ số nhiều**
```java
@GetMapping("/users")        // ✅ Tốt
@GetMapping("/user")         // ❌ Không nên
```

3. **Sử dụng HTTP methods đúng mục đích**
- GET: Lấy dữ liệu
- POST: Tạo mới
- PUT: Cập nhật toàn bộ
- PATCH: Cập nhật một phần
- DELETE: Xóa

4. **Versioning API**
```java
@RestController
@RequestMapping("/api/v1/users")  // Version 1
public class UserControllerV1 { }

@RestController
@RequestMapping("/api/v2/users")  // Version 2
public class UserControllerV2 { }
```

### 5. Path Route và Các Pattern Matching

#### a. Path Variables
```java
// Cơ bản
@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) {
    return userService.findById(id);
}

// Nhiều path variables
@GetMapping("/users/{id}/orders/{orderId}")
public Order getUserOrder(@PathVariable Long id, @PathVariable Long orderId) {
    return orderService.findByUserIdAndOrderId(id, orderId);
}

// Đặt tên khác cho path variable
@GetMapping("/users/{userId}")
public User getUser(@PathVariable("userId") Long id) {
    return userService.findById(id);
}
```

#### b. Path Pattern Matching
```java
// Match nhiều segments
@GetMapping("/users/**")
public List<User> getAllUsersInPath() {
    return userService.findAll();
}

// Match một segment
@GetMapping("/users/*/orders")
public List<Order> getAllOrders() {
    return orderService.findAll();
}

// Match với regex
@GetMapping("/users/{id:[\\d]+}")
public User getUserById(@PathVariable Long id) {
    return userService.findById(id);
}
```

#### c. Path Parameters với Validation
```java
@GetMapping("/users/{id}")
public User getUser(@PathVariable @Min(1) Long id) {
    return userService.findById(id);
}

@GetMapping("/users/{name}")
public User getUserByName(@PathVariable @Size(min=2, max=50) String name) {
    return userService.findByName(name);
}
```

#### d. Path Parameters với Optional
```java
// Optional path variable
@GetMapping({"/users", "/users/{id}"})
public Object getUsers(@PathVariable(required = false) Long id) {
    if (id != null) {
        return userService.findById(id);
    }
    return userService.findAll();
}
```

#### e. Path Parameters với Enum
```java
public enum UserType {
    ADMIN, USER, GUEST
}

@GetMapping("/users/type/{type}")
public List<User> getUsersByType(@PathVariable UserType type) {
    return userService.findByType(type);
}
```

#### f. Path Parameters với Custom Converter
```java
@Component
public class StringToUserConverter implements Converter<String, User> {
    @Override
    public User convert(String source) {
        // Logic chuyển đổi từ String sang User
        return new User(source);
    }
}

@GetMapping("/users/{user}")
public User getUser(@PathVariable User user) {
    return user;
}
```

### 6. Lưu ý khi sử dụng Path Route

1. **Thứ tự ưu tiên của routes**:
```java
@GetMapping("/users/new")        // Route cụ thể
@GetMapping("/users/{id}")       // Route với path variable
@GetMapping("/users/**")         // Route với wildcard
```

2. **Tránh xung đột routes**:
```java
// ❌ Không nên - Gây nhầm lẫn
@GetMapping("/users/{id}")
@GetMapping("/users/new")

// ✅ Nên làm - Thêm prefix để phân biệt
@GetMapping("/users/{id}")
@GetMapping("/users/create/new")
```

3. **Sử dụng constants cho paths**:
```java
public class UserController {
    private static final String BASE_PATH = "/api/v1/users";
    
    @GetMapping(BASE_PATH + "/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }
}
```

---


## IV. 📁 Cấu trúc thư mục cơ bản (gợi ý)

```
src
└── main
    └── java
        └── com.example.demo
            ├── controller      // Xử lý yêu cầu HTTP
            ├── service         // Xử lý logic nghiệp vụ
            ├── repository      // Kết nối database (sau này dùng JPA)
            ├── model           // Định nghĩa Entity hoặc class đơn giản
            └── dto             // Các lớp trung gian truyền dữ liệu
```

---

## V. 📦 Tạo DTO – Data Transfer Object

### 1. DTO là gì?

DTO (Data Transfer Object) là một design pattern được sử dụng để truyền dữ liệu giữa các layer của ứng dụng. Nó giúp:
- Tách biệt dữ liệu hiển thị (presentation) khỏi dữ liệu lưu trữ (persistence)
- Giảm số lượng request/response
- Tăng tính bảo mật bằng cách chỉ expose những field cần thiết
- Linh hoạt trong việc thay đổi cấu trúc dữ liệu

### 2. Tại sao cần DTO?

1. **Bảo mật dữ liệu**:
```java
// Entity
@Entity
public class User {
    private Long id;
    private String username;
    private String password;  // Không nên expose
    private String email;
    private String role;
    // ... other sensitive fields
}

// DTO - Chỉ expose những field cần thiết
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    // Không có password và role
}
```

2. **Tối ưu hiệu suất**:
```java
// Không dùng DTO - Lấy toàn bộ dữ liệu không cần thiết
@Entity
public class Order {
    private List<OrderItem> items;
    private Customer customer;
    private Payment payment;
    // ... many other fields
}

// Dùng DTO - Chỉ lấy dữ liệu cần thiết
public class OrderSummaryDTO {
    private Long orderId;
    private BigDecimal totalAmount;
    private String status;
    // Chỉ những thông tin cần thiết cho summary
}
```

3. **Linh hoạt trong versioning**:
```java
// DTO cho API v1
public class UserDTOV1 {
    private String name;
    private String email;
}

// DTO cho API v2
public class UserDTOV2 {
    private String name;
    private String email;
    private String phone;
    private AddressDTO address;
}
```

### 3. Các loại DTO phổ biến

1. **Request DTO**:
```java
public class CreateUserRequestDTO {
    @NotBlank
    private String username;
    
    @Email
    private String email;
    
    @Size(min = 6)
    private String password;
}
```

2. **Response DTO**:
```java
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String status;
    private LocalDateTime createdAt;
}
```

3. **Update DTO**:
```java
public class UpdateUserDTO {
    private String email;
    private String phone;
    // Không có username vì không cho phép update
}
```

### 4. Best Practices khi sử dụng DTO

1. **Sử dụng validation**:
```java
public class UserDTO {
    @NotBlank(message = "Username is required")
    private String username;
    
    @Email(message = "Invalid email format")
    private String email;
    
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
```

2. **Sử dụng Builder Pattern**:
```java
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    
    // Builder giúp tạo object dễ dàng hơn
    public static void main(String[] args) {
        UserDTO user = UserDTO.builder()
            .username("john")
            .email("john@example.com")
            .build();
    }
}
```

3. **Sử dụng MapStruct để mapping**:
```java
@Mapper
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO dto);
    List<UserDTO> toDTOList(List<User> users);
}
```

### 5. Ví dụ thực tế

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @PostMapping
    public UserResponseDTO createUser(@Valid @RequestBody CreateUserRequestDTO requestDTO) {
        // 1. Validate dữ liệu đầu vào
        // 2. Chuyển đổi DTO sang Entity
        User user = userMapper.toEntity(requestDTO);
        // 3. Lưu vào database
        User savedUser = userService.save(user);
        // 4. Chuyển đổi Entity sang Response DTO
        return userMapper.toDTO(savedUser);
    }
    
    @GetMapping("/{id}")
    public UserResponseDTO getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return userMapper.toDTO(user);
    }
}
```

### 6. Lợi ích của việc sử dụng DTO

1. **Tách biệt các layer**:
   - Controller layer chỉ làm việc với DTO
   - Service layer làm việc với Entity
   - Repository layer làm việc với Entity

2. **Dễ dàng maintain**:
   - Thay đổi cấu trúc database không ảnh hưởng đến API
   - Thay đổi API không ảnh hưởng đến database

3. **Tăng tính bảo mật**:
   - Kiểm soát được dữ liệu được expose ra ngoài
   - Validate dữ liệu đầu vào một cách chặt chẽ

4. **Tối ưu hiệu suất**:
   - Giảm kích thước payload
   - Chỉ lấy những dữ liệu cần thiết

