Dưới đây là **hướng dẫn từng bước thiết lập dự án Spring Boot RESTful API** cho người mới bắt đầu, từ tạo project đến chạy endpoint đầu tiên:

---

## 🛠️ Bước 1: Cài đặt công cụ cần thiết

| Công cụ        | Gợi ý                   |
| -------------- | ----------------------- |
| Java JDK       | JDK 17 hoặc cao hơn     |
| IDE            | IntelliJ IDEA / Eclipse |
| Build tool     | Maven hoặc Gradle       |
| Postman / curl | Để test API             |

✅ *Kiểm tra JDK:*

```bash
java -version
```

---

## 🧱 Bước 2: Tạo project bằng [Spring Initializr](https://start.spring.io)

**Cấu hình như sau:**

* Project: Maven / Gradle
* Language: Java
* Spring Boot: (latest stable)
* Group: `com.example`
* Artifact: `demo`
* Name: `demo`
* Packaging: `Jar`
* Java: 17+

**Dependencies nên chọn:**

* Spring Web
* Spring Boot DevTools (reload nhanh)
* Spring Data JPA
* H2 Database (hoặc MySQL nếu bạn đã cài)
* Lombok (giảm code boilerplate)

👉 Ấn **Generate**, tải file `.zip`, giải nén và mở bằng IDE (IntelliJ / Eclipse)

---

## 🔃 Bước 3: Cấu trúc project cơ bản

```
src
└── main
    └── java
        └── com.example.demo
            ├── DemoApplication.java   ← file main
            ├── controller              ← chứa REST controller
            ├── model                   ← chứa entity class
            ├── repository              ← interface kết nối DB
            └── service (tùy chọn)      ← xử lý logic trung gian
```

---

## 🌐 Bước 4: Tạo REST Controller đầu tiên

```java
@RestController
@RequestMapping("/api/hello")
public class HelloController {

    @GetMapping
    public String sayHello() {
        return "Hello, Spring Boot!";
    }
}
```

📌 Thêm annotation `@RestController` để tự động trả JSON.

---

## 🧪 Bước 5: Chạy ứng dụng

Chạy `DemoApplication.java` bằng IDE hoặc:

```bash
./mvnw spring-boot:run
```

Mở trình duyệt hoặc Postman:

```
GET http://localhost:8080/api/hello
```

✅ Kết quả: `"Hello, Spring Boot!"`

---

## ⚙️ Bước 6: Cấu hình ứng dụng (`application.properties`)

```properties
# Server
server.port=8080

# H2 Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true

# JPA
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```

Mở H2 Console tại `http://localhost:8080/h2-console`

---

## 💾 Bước 7: Kết nối database + CRUD đơn giản

### Tạo Entity:

```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
}
```

### Repository:

```java
public interface UserRepository extends JpaRepository<User, Long> {
}
```

### Controller:

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
```

---

## ✅ Kết quả

Bạn đã có một **Spring Boot RESTful API** đơn giản:

* Endpoint `POST /api/users` tạo user
* Endpoint `GET /api/users` lấy danh sách user

---

## 🧠 Gợi ý tiếp theo

* Thêm validation (`@Valid`, `@NotNull`)
* Thêm xử lý lỗi (`@ExceptionHandler`)
* Thêm Swagger (`springdoc-openapi-ui`)
* Tạo service layer để tách logic

---

Nếu bạn muốn mình tạo **starter project**, **project mẫu**, hoặc **video hướng dẫn**, mình có thể làm tiếp!
