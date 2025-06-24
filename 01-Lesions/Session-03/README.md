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

## III. 🧱 Lớp Entity (Model)

### 1. Entity là gì?

Entity là một lớp Java đại diện cho **bảng (table)** trong cơ sở dữ liệu. Mỗi đối tượng của lớp Entity tương ứng với **một dòng (record)** trong bảng đó. Entity thường được chú thích với `@Entity` và ánh xạ sang bảng bằng `@Table`.

Spring Boot kết hợp với JPA (Java Persistence API) để tự động ánh xạ giữa class Java và bảng trong database.

---

### 2. Các `Annotation` thường dùng khi tạo Entity

| Annotation         | Chức năng                                                              |
| ------------------ | ---------------------------------------------------------------------- |
| `@Entity`          | Đánh dấu class là một Entity                                           |
| `@Table(name=...)` | Đặt tên bảng trong DB                                                  |
| `@Id`              | Đánh dấu trường là khóa chính                                          |
| `@GeneratedValue`  | Tự động sinh giá trị (thường dùng cho id)                              |
| `@Column`          | Gắn cột DB với thuộc tính Java, có thể đặt tên, độ dài, nullable, v.v. |
| `@Enumerated`      | Dùng với Enum để chỉ rõ cách lưu (Ordinal/String)                      |
| `@Temporal`        | Với kiểu ngày, chỉ định cách lưu (DATE, TIME, TIMESTAMP)               |
| `@Transient`        | Dùng để bỏ qua trường không lưu DB              |

---

✅ Một số thuộc tính phổ biến của `@Column`

| Thuộc tính           | Ý nghĩa                                                    |
| -------------------- | ---------------------------------------------------------- |
| `name`               | Tên cột trong DB                                           |
| `nullable`           | Có cho phép null không? (`true` mặc định)                  |
| `unique`             | Có cần giá trị duy nhất không? (`false` mặc định)          |
| `length`             | Độ dài chuỗi (dùng với `String`)                           |
| `columnDefinition`   | Tùy chỉnh kiểu SQL cụ thể (ví dụ: `TEXT`, `DECIMAL(10,2)`) |
| `insertable`         | Có cho phép insert dữ liệu vào cột này không?              |
| `updatable`          | Có cho phép cập nhật dữ liệu vào cột này không?            |
| `precision`, `scale` | Dùng cho kiểu số thực (`DECIMAL`, `NUMERIC`)               |

---

### 3. Các kiểu dữ liệu thường gặp trong Entity

| Kiểu dữ liệu Java | Kiểu tương ứng trong DB      | Mô tả                                 |
| ----------------- | ---------------------------- | ------------------------------------- |
| `Long`            | BIGINT                       | Dùng cho ID tự động tăng              |
| `String`          | VARCHAR                      | Lưu chuỗi                             |
| `int` / `Integer` | INT                          | Lưu số nguyên                         |
| `double`          | DOUBLE                       | Lưu số thực                           |
| `boolean`         | BOOLEAN                      | Lưu true/false                        |
| `LocalDate`       | DATE                         | Lưu ngày (năm/tháng/ngày)             |
| `LocalDateTime`   | TIMESTAMP                    | Lưu ngày + giờ                        |
| `Enum`            | VARCHAR / INT (tùy cách lưu) | Lưu giá trị enum (tên hoặc số thứ tự) |

---

### 4. Ví dụ Entity: `Product`

```java
package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id tự tăng

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(name = "in_stock", nullable = false)
    private boolean inStock;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Category category;

    // Constructors
    public Product() {
    }

    public Product(String name, String description, double price, boolean inStock, LocalDateTime createdAt, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.inStock = inStock;
        this.createdAt = createdAt;
        this.category = category;
    }

    // Getters và Setters
    // (Nên dùng Lombok để giảm code lặp nếu muốn)

    // Enum nội bộ
    public enum Category {
        ELECTRONICS, FASHION, FOOD, BOOKS
    }
}
```

---

📌 Gợi ý mở rộng

* Nếu muốn giảm boilerplate, bạn có thể dùng thư viện **Lombok**:

  ```java
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Entity
  public class Product { ... }
  ```

  Đừng quên thêm dependency của Lombok vào `pom.xml`.

---


### 📁 5. Các mối quan hệ trong Entity (Relationship Mapping)

| Mối quan hệ | Annotation                 | Ví dụ                         |
| ----------- | -------------------------- | ----------------------------- |
| 1 - 1       | `@OneToOne`                | Một người dùng – Một hồ sơ    |
| 1 - N       | `@OneToMany`, `@ManyToOne` | Một danh mục – Nhiều sản phẩm |
| N - N       | `@ManyToMany`              | Người dùng – Vai trò          |


Dưới đây là **các ví dụ minh họa** về Relationship Mapping trong JPA/Spring:

#### ✅ 1. One-to-One: Một User có một Profile

Entity: `User`

```java
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    // getters, setters
}
```

Entity: `Profile`

```java
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String address;

    // getters, setters
}
```

> 📝 `@JoinColumn`: xác định tên cột khóa ngoại trong bảng `User` tham chiếu đến bảng `Profile`.

---

#### ✅ 2. One-to-Many & Many-to-One: Một Category có nhiều Product

Entity: `Category`

```java
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products;

    // getters, setters
}
```

Entity: `Product`

```java
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // getters, setters
}
```

> 📝 `mappedBy = "category"` nghĩa là bảng `Product` là bên **sở hữu** mối quan hệ, chứa `category_id`.

---

#### ✅ 3. Many-to-Many: User có nhiều Role và Role có nhiều User

Entity: `User`

```java
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @ManyToMany
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    // getters, setters
}
```

Entity: `Role`

```java
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    // getters, setters
}
```

> 📝 `@JoinTable` tạo bảng trung gian `user_roles` chứa 2 khóa ngoại: `user_id`, `role_id`.

---

#### ⚠️ Lưu ý khi dùng quan hệ

| Vấn đề                      | Lời khuyên                                                                           |
| --------------------------- | ------------------------------------------------------------------------------------ |
| Tránh vòng lặp khi trả JSON | Dùng `@JsonManagedReference` và `@JsonBackReference` hoặc DTO để tránh infinite loop |
| Cascade Type                | Dùng `CascadeType.ALL` khi muốn tự động lưu/xoá liên kết entity con                  |
| Fetch Type                  | `@ManyToOne` mặc định là `EAGER`, `@OneToMany` mặc định là `LAZY`                    |
| mappedBy                    | Chỉ định entity nào là chủ động quản lý liên kết                                     |

---

---

### 6. Thực hành tạo `model/User.java`

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

### ✅ I. Repository là gì?

Trong kiến trúc Spring Boot (và mô hình **MVC**), **Repository** là lớp giao tiếp với **database** (thường thao tác với Entity). Nó đóng vai trò **Data Access Layer**, giúp bạn:

* Truy vấn dữ liệu
* Lưu, cập nhật, xóa dữ liệu
* Tìm kiếm theo điều kiện

Spring Boot sử dụng **Spring Data JPA** để giúp bạn tạo Repository dễ dàng, **không cần viết SQL thủ công**.

---

### ✅ II. Spring Data JPA Repository hoạt động thế nào?

Chỉ cần **kế thừa interface** `JpaRepository<Entity, ID>` là bạn có thể dùng hàng loạt hàm đã có sẵn như:

* `findAll()`, `findById(id)`, `save(entity)`, `deleteById(id)`, `existsById(id)`, ...

👉 Spring Boot sẽ **tự động tạo implementation thật cho bạn**, không cần viết thủ công.

---

### ✅ III. Cách tạo lớp Repository

#### 1. Cấu trúc thư mục đề xuất

```
src/
└── main/
    └── java/
        └── com/example/demo/
            ├── model/           --> Chứa entity
            ├── repository/      --> Chứa repository
            └── controller/      --> REST API controller
```

#### 2. Entity mẫu: `Product`

```java
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;

    // getters/setters
}
```

#### 3. Tạo Repository: `ProductRepository`

```java
package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Các method custom có thể được khai báo theo cú pháp đặt tên
    List<Product> findByName(String name);
    List<Product> findByPriceGreaterThan(double price);
}
```

> 📌 Không cần viết `@Repository` — Spring Boot tự động phát hiện vì `JpaRepository` đã có sẵn annotation nội bộ.

---


### ✅ V. Một số method có thể khai báo thêm

| Tên method                             | Ý nghĩa                               |
| -------------------------------------- | ------------------------------------- |
| `findByName(String name)`              | Tìm sản phẩm theo tên                 |
| `findByPriceGreaterThan(double p)`     | Tìm sản phẩm có giá > p               |
| `findByNameContaining(String keyword)` | Tìm sản phẩm có tên chứa từ khóa      |
| `existsByName(String name)`            | Kiểm tra có tồn tại sản phẩm theo tên |
| `deleteByName(String name)`            | Xoá sản phẩm theo tên                 |

> Tất cả đều dùng cú pháp **Derived Query Methods** → Spring sẽ tự tạo SQL tương ứng.

---

### 📁 Thực hành tạo  `repository/UserRepository.java`

```java
package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
```

---

## VI. ⚙️ Service Layer


### ✅ I. Service Layer là gì?

**Service Layer** là tầng trung gian giữa:

* **Controller (Web/API Layer)** và
* **Repository (Data Access Layer)**.

Nó chứa **logic nghiệp vụ (business logic)** của ứng dụng: xử lý, tính toán, kiểm tra dữ liệu, hoặc điều phối nhiều thao tác với database.

### 🎯 Mục tiêu

* Giúp **tách biệt logic xử lý** ra khỏi Controller
* **Dễ bảo trì**, dễ test
* Tái sử dụng code giữa nhiều controller hoặc use case

---

### 🔧 II. Tạo Service trong Spring Boot

#### 1. Tạo Interface: `ProductService`

```java
public interface ProductService {
    List<Product> getAll();
    Product getById(Long id);
    Product create(Product product);
    Product update(Long id, Product product);
    void delete(Long id);
}
```

#### 2. Tạo Implementation: `ProductServiceImpl`

```java
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired //Dependency Injection
    private ProductRepository productRepository;

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, Product updatedProduct) {
        Product existing = getById(id);
        existing.setName(updatedProduct.getName());
        existing.setPrice(updatedProduct.getPrice());
        return productRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
```


`@Autowired` là một annotation của Spring dùng để tự động tiêm phụ thuộc (dependency injection) vào một class.

> Nói đơn giản: Khi bạn cần dùng một đối tượng nào đó (ví dụ Service, Repository), Spring sẽ tự tạo và đưa vào mà bạn không cần new thủ công.



---


### ⚠️ IV. Lưu ý khi làm việc với Service Layer

| Điều cần nhớ                   | Giải thích                                                                    |
| ------------------------------ | ----------------------------------------------------------------------------- |
| Dùng `@Service`                | Để Spring quản lý lớp Service                                                 |
| Tách interface & impl          | Tốt cho testing, bảo trì, dễ mocking khi test tự động                         |
| Không thao tác DB ở controller | Controller chỉ điều phối request – response, không nên xử lý dữ liệu phức tạp |
| Dùng `@Transactional` nếu cần  | Khi có nhiều thao tác DB liên tiếp cần rollback nếu có lỗi                    |

---

### 🧪 Tuỳ chọn mở rộng

* Trả về DTO thay vì Entity
* Bắt exception và ném `CustomException`
* Validate đầu vào với `@Valid`

---


### 📁 Thực hành tạo `service/UserService.java`

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

    @Autowired //Dependency Injection
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


### 🧑‍💻  Sử dụng `Service` trong Controller

```java
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired //Dependency Injection
    private ProductService productService;

    @GetMapping
    public List<Product> getAll() {
        return productService.getAll();
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return productService.create(product);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        return productService.update(id, product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}
```

---

### 📁 Thực hành tạo `controller/UserController.java`

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

