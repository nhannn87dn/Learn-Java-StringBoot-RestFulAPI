Dưới đây là nội dung **chi tiết Phần 1: Giới thiệu & Chuẩn bị môi trường** trong lộ trình học **Spring Boot RESTful API cho người mới bắt đầu** – đầy đủ lý thuyết, hướng dẫn cài đặt, và phần thực hành mở đầu.

---

# 📚 PHẦN 1: GIỚI THIỆU & CHUẨN BỊ MÔI TRƯỜNG

---

## I. 💡 RESTful API là gì?

### 1. REST (Representational State Transfer)

Là **kiến trúc thiết kế API** phổ biến, sử dụng các phương thức HTTP như `GET`, `POST`, `PUT`, `DELETE`.

| Phương thức | Chức năng   | Ví dụ                 |
| ----------- | ----------- | --------------------- |
| GET         | Lấy dữ liệu | `GET /api/users`      |
| POST        | Tạo mới     | `POST /api/users`     |
| PUT         | Cập nhật    | `PUT /api/users/1`    |
| DELETE      | Xoá         | `DELETE /api/users/1` |

> ✅ RESTful API trả dữ liệu thường ở định dạng **JSON**.

---

## II. ☕ Giới thiệu Spring Boot

### Spring Boot là gì?

Spring Boot là **framework Java** giúp xây dựng ứng dụng web/API nhanh, với cấu hình tối giản.

### Ưu điểm:

* Tạo ứng dụng RESTful API dễ dàng
* Tích hợp sẵn các thành phần như Spring MVC, Spring Data JPA, Security...
* Ít cấu hình, nhanh chóng chạy được server (`Embedded Tomcat`)
* Dễ dàng kết nối với database

> ✅ **Spring Boot** giúp bạn tập trung vào business logic, không phải cấu hình phức tạp.

---

## III. 🧰 Cài đặt công cụ cần thiết

### 1. Cài Java JDK

* Cài Java 17 hoặc mới hơn:

  * [OpenJDK](https://jdk.java.net/)
  * [Oracle JDK](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
* Kiểm tra:

```bash
java -version
```

---

### 2. Cài đặt IDE

* [IntelliJ IDEA Community (free)](https://www.jetbrains.com/idea/download/)
* Hoặc Eclipse

---

### 3. Cài Postman

* Để test các REST API: [https://www.postman.com/downloads/](https://www.postman.com/downloads/)

---

### 4. (Tuỳ chọn) Cài Git

* Để quản lý mã nguồn: [https://git-scm.com/](https://git-scm.com/)

---

## IV. 🌐 Tạo Project đầu tiên với Spring Initializr

Truy cập [https://start.spring.io](https://start.spring.io) và cấu hình:

| Trường          | Giá trị đề xuất        |
| --------------- | ---------------------- |
| Project         | Maven / Gradle         |
| Language        | Java                   |
| Spring Boot     | Mới nhất (e.g., 3.x.x) |
| Group           | `com.example`          |
| Artifact / Name | `demo`                 |
| Packaging       | Jar                    |
| Java            | 17 hoặc cao hơn        |

### Dependencies nên chọn:

* ✅ Spring Web (REST API)
* ✅ Spring Boot DevTools (reload nhanh)
* ✅ Spring Data JPA (ORM)
* ✅ H2 Database (test DB)
* ✅ Lombok (giảm code Java)

Ấn **Generate**, tải về `.zip`, giải nén và mở trong IDE.

---

## V. 📁 Cấu trúc thư mục ban đầu

```text
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

## VI. 🚀 Chạy thử ứng dụng

### 1. Mở file `DemoApplication.java`:

```java
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### 2. Chạy ứng dụng

* Trong IntelliJ: Chuột phải → Run `DemoApplication`
* Hoặc dùng terminal:

```bash
./mvnw spring-boot:run
```

---

