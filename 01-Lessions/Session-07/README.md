# 📘 **Phần 7: Swagger & API Documentation**

## 🎯 Mục tiêu:

* Biết cách **tạo tài liệu REST API** tự động.
* Hiểu khái niệm Swagger/OpenAPI.
* Cài đặt và sử dụng Swagger UI để test API trực quan.

---

## I. 📖 Swagger & OpenAPI là gì?

### 1. **Swagger / OpenAPI**

Là **chuẩn tài liệu hoá REST API** giúp:

* Hiển thị các API (GET/POST/PUT/DELETE).
* Cho phép **thử nghiệm API trực tiếp trên giao diện UI**.
* Hữu ích cho frontend/backend/devops/tester hiểu cách API hoạt động.

> Tài liệu được tự động sinh từ annotations trong code.

---

## II. 🧩 Thư viện sử dụng: **springdoc-openapi**

### 🔄 Vì sao không dùng springfox?

* `springfox` cũ, không còn được duy trì tốt.
* `springdoc-openapi` hỗ trợ tốt hơn Spring Boot 3+, tích hợp nhanh chóng.

---

## III. ⚙️ Cài đặt Swagger với springdoc-openapi

### 1. Thêm dependency vào `pom.xml`

```xml
<!-- Swagger OpenAPI cho Spring Boot -->
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>2.5.0</version> <!-- hoặc phiên bản mới nhất -->
</dependency>
```

> ✅ Không cần thêm config phức tạp, springdoc tự động scan các controller để tạo tài liệu.

---

## IV. 🧪 Truy cập giao diện Swagger UI

Khi chạy ứng dụng, mở trình duyệt:

```
http://localhost:8080/swagger-ui.html
```

Hoặc:

```
http://localhost:8080/swagger-ui/index.html
```

➡️ Bạn sẽ thấy một giao diện đẹp như sau:

✅ Danh sách các endpoint
✅ Các method như `GET`, `POST`, `DELETE`
✅ Các tham số `@RequestParam`, `@PathVariable`, `@RequestBody`
✅ Cho phép điền input và **test API trực tiếp**

---

## V. 📝 Tuỳ chỉnh thông tin API

Bạn có thể mô tả tên dự án, version, người phát triển...

📄 Thêm class cấu hình:

```java
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("User Management API")
                .version("1.0.0")
                .description("Tài liệu REST API quản lý người dùng")
                .contact(new Contact().name("Tên bạn").email("email@example.com"))
            );
    }
}
```

---

## VI. 🏷️ Swagger Annotations thường dùng

| Annotation                    | Mục đích                    |
| ----------------------------- | --------------------------- |
| `@Operation(summary = "...")` | Mô tả cho từng API          |
| `@Parameter`                  | Tuỳ chỉnh thông tin tham số |
| `@Schema`                     | Gắn mô tả chi tiết vào DTO  |
| `@Tag(name = "...")`          | Nhóm các API theo chủ đề    |

📄 Ví dụ trong controller:

```java
@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "Thao tác với người dùng")
public class UserController {

    @GetMapping
    @Operation(summary = "Lấy danh sách người dùng")
    public List<UserDTO> getAllUsers() {
        ...
    }
}
```

---

## VII. ✅ Tổng kết

| Tính năng         | Ghi chú                              |
| ----------------- | ------------------------------------ |
| springdoc-openapi | Tự động sinh tài liệu API            |
| Swagger UI        | Giao diện test API trực quan         |
| Annotations       | Mô tả rõ ràng hơn cho từng endpoint  |
| Hữu ích cho       | Frontend dev, QA, tester, public API |

---

## 🎯 Gợi ý thực hành

1. Thêm `springdoc-openapi` và kiểm tra giao diện Swagger.
2. Mô tả một số API bằng `@Operation` và `@Tag`.
3. Tuỳ chỉnh thông tin tài liệu (title, version, contact…).
4. Tạo một POST API và test gửi dữ liệu trực tiếp qua Swagger UI.

