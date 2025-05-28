# 🗃️ **Phần 3: RESTful API CRUD với Spring Data JPA & MySQL**

---

## I. 🎯 Mục tiêu

* Hiểu cách ánh xạ **Entity** với bảng trong database.
* Kết nối Spring Boot với **MySQL** thật.
* Tạo REST API đầy đủ các chức năng **CRUD** (Create, Read, Update, Delete).
* Sử dụng **DTO** để tách biệt dữ liệu từ client với Entity.
* Tổ chức code theo mô hình: **Controller – Service – Repository – DTO – Model**.

---

## II. 🛠 Cài đặt và chuẩn bị MySQL

1. **Tạo database MySQL**

```sql
CREATE DATABASE spring_demo;
```

2. **Cấu hình kết nối trong `application.properties`**

```properties
# MySQL Connection
spring.datasource.url=jdbc:mysql://localhost:3306/spring_demo
spring.datasource.username=root
spring.datasource.password=your_password

# Hibernate JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

> 🔐 Thay `your_password` bằng mật khẩu thực tế.

---

## III. 🧱 Tạo Entity: `User`

### 📁 `model/User.java`

```java
package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
}
```

---

## IV. 📦 Tạo DTO: `UserDTO`

### 📁 `dto/UserDTO.java`

```java
package com.example.demo.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String email;
}
```

---

## V. 🧩 Repository Interface

### 📁 `repository/UserRepository.java`

```java
package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
```

---

## VI. ⚙️ Service Layer

### 📁 `service/UserService.java`

```java
package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(UserDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(Long id, UserDTO dto) {
        return userRepository.findById(id).map(user -> {
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            return userRepository.save(user);
        }).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
```

---

## VII. 🌐 REST Controller

### 📁 `controller/UserController.java`

```java
package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody UserDTO dto) {
        return userService.createUser(dto);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody UserDTO dto) {
        return userService.updateUser(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
```

---

## VIII. 🧪 Kiểm thử với Postman

| Method | Endpoint          | Mô tả              |
| ------ | ----------------- | ------------------ |
| POST   | `/api/users`      | Tạo user mới       |
| GET    | `/api/users`      | Lấy danh sách user |
| GET    | `/api/users/{id}` | Lấy user theo ID   |
| PUT    | `/api/users/{id}` | Cập nhật user      |
| DELETE | `/api/users/{id}` | Xoá user theo ID   |

### Ví dụ POST

```json
POST /api/users
Content-Type: application/json

{
  "name": "John",
  "email": "john@example.com"
}
```

---

## IX. ✅ Tổng kết

| Thành phần | Vai trò                          |
| ---------- | -------------------------------- |
| Entity     | Ánh xạ với bảng trong MySQL      |
| DTO        | Nhận dữ liệu từ request          |
| Repository | Truy vấn DB qua JPA              |
| Service    | Xử lý logic nghiệp vụ            |
| Controller | Nhận yêu cầu HTTP và trả kết quả |

---

## 🎯 Bài tập mở rộng

* [ ] Thêm trường `age`, `address` cho `User`.
* [ ] Viết API tìm kiếm user theo email.
* [ ] Bổ sung xử lý lỗi khi không tìm thấy user.
* [ ] Tách DTO cho `UserRequestDTO` và `UserResponseDTO`.

---

