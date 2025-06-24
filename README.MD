# 📚 **Spring Boot RESTful API - Lộ trình cho người mới bắt đầu**

## 🧩 **Phần 1: Giới thiệu & Chuẩn bị môi trường**

* Giới thiệu RESTful API là gì?

  * REST principles: stateless, client-server, cacheable
  * HTTP Methods: GET, POST, PUT, DELETE
  * JSON là gì?
* Giới thiệu Spring Boot

  * So sánh với Spring truyền thống
  * Ưu điểm khi phát triển REST API
* Cài đặt công cụ:

  * Java JDK (17+)
  * IntelliJ IDEA hoặc Eclipse
  * Maven hoặc Gradle
  * Postman hoặc curl
* Tạo project với [Spring Initializr](https://start.spring.io)

---

## 🧱 **Phần 2: Xây dựng REST API đơn giản**

* Tạo ứng dụng "Hello World"

  * Sử dụng `@RestController` và `@GetMapping`
* Cấu trúc thư mục cơ bản: `controller`, `model`, `service`, `repository`
* Viết các endpoint GET/POST:

  * `@RequestMapping`, `@GetMapping`, `@PostMapping`
  * Sử dụng `@RequestBody`, `@PathVariable`, `@RequestParam`
* Tạo `DTO` (Data Transfer Object)

---

## 🗃️ **Phần 3: Kết nối CSDL với Spring Data JPA**

* Cấu hình kết nối Spring Data JPA + MySQL
* Entity & Repository:

  * Tạo entity với `@Entity`, `@Id`, `@GeneratedValue`
  * Interface repository kế thừa `JpaRepository`
* CRUD cơ bản (Create, Read, Update, Delete)

---

## 🧪 **Phần 4: Validation & Xử lý lỗi**

* Validate input:

  * Dùng `@Valid`, `@NotNull`, `@Size`, `@Email`, v.v.
* Custom Exception Handling:

  * `@ControllerAdvice` và `@ExceptionHandler`
* Trả về lỗi chuẩn REST (status code + message)

---

## 🔄 **Phần 5: Nâng cao REST API**

* Pagination & Sorting:

* Sử dụng `Pageable`, `Sort`
* Tìm kiếm với query params
* Mapping giữa Entity và DTO (sử dụng MapStruct hoặc tự code)

---

## 🛡️ **Phần 6: Bảo mật API với Spring Security (Cơ bản)**

* Cấu hình Spring Security mặc định
* HTTP Basic Auth
* Giới thiệu về JWT (JWT chi tiết có thể để ở phần nâng cao)

---

## 📄 **Phần 7: Swagger & API Documentation**

* Cài đặt và cấu hình Swagger (springdoc-openapi)
* Truy cập giao diện Swagger UI

---

## ✅ **Phần 8: Testing**

* Unit test với `@WebMvcTest`
* Integration test với `@SpringBootTest`
* Dùng MockMvc để test API endpoint

---

## 🧠 **Phần 9: Thực hành dự án mini**

> Ví dụ: Xây dựng API quản lý sinh viên / sản phẩm / người dùng

* Các tính năng:

  * CRUD đầy đủ
  * Validation & error handling
  * Pagination & tìm kiếm
  * Swagger doc
  * Testing cơ bản

---

## 📦 **Phần 10: Đóng gói và deploy (optional)**

* Đóng gói JAR
* Deploy lên Heroku / Render / Railway (free)

