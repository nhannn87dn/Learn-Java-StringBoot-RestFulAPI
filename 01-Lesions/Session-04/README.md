# 🧰 **Phần 4: Validation, Xử lý lỗi và Chuẩn hóa phản hồi trong REST API**

---

## I. 🔍 Vì sao cần xác thực (Validation), xử lý lỗi và chuẩn hóa phản hồi?

### 1. ❓ Tại sao cần validation?

**Validation** là quá trình **kiểm tra dữ liệu từ client gửi lên** có hợp lệ hay không, ví dụ:

| Trường | Điều kiện                           |
| ------ | ----------------------------------- |
| name   | Không được rỗng (required)          |
| email  | Phải đúng định dạng email           |
| age    | Phải là số, nằm trong khoảng nào đó |

Nếu không kiểm tra, ta có thể lưu dữ liệu sai vào database, gây lỗi hoặc hành vi không mong muốn.

---

### 2. ❓ Tại sao cần xử lý lỗi?

Ví dụ: client gửi email sai định dạng, hoặc gọi API lấy user với id không tồn tại.

👉 Nếu không xử lý rõ ràng, API sẽ trả về lỗi khó hiểu như:

```json
{
  "timestamp": "...",
  "status": 500,
  "error": "Internal Server Error",
  "trace": "...",
  "message": null,
  "path": "/api/users/999"
}
```

➡️ Khó debug, không rõ vì sao sai.

---

### 3. ❓ Tại sao nên chuẩn hóa phản hồi?

Client (Web, Mobile) cần biết:

* Thao tác có thành công không?
* Nếu thất bại, lý do là gì?
* Mã lỗi HTTP nào? (200, 201, 400, 404, 500...)

➡️ Dùng `ResponseEntity` để tùy biến thông điệp phản hồi.

---

## II. ✅ Áp dụng Validation với Jakarta Bean Validation

### 🧩 1. Thêm dependency (nếu chưa có)

Spring Boot 3 trở lên đã tích hợp `jakarta.validation`. Nếu bạn dùng Spring Boot 2, thêm:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

---

### 🧩 2. Thêm validation vào DTO

📄 `UserDTO.java`

```java
package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {

    @NotBlank(message = "Tên không được để trống")
    @Size(min = 2, max = 50, message = "Tên phải từ 2 đến 50 ký tự")
    private String name;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;
}
```

### 🧠 **Danh sách phổ biến nhất (hay dùng)**


| Annotation         | Ý nghĩa                                                          |
| ------------------ | ---------------------------------------------------------------- |
| `@NotNull`         | Không được null (có thể rỗng)                                    |
| `@NotEmpty`        | Không được rỗng (String, List, Collection – nhưng có thể là " ") |
| `@NotBlank`        | Không được null, không được rỗng và không toàn khoảng trắng      |
| `@Size(min, max)`  | Giới hạn độ dài chuỗi, số phần tử list                           |
| `@Min(value)`      | Số tối thiểu                                                     |
| `@Max(value)`      | Số tối đa                                                        |
| `@Email`           | Phải đúng định dạng email                                        |
| `@Pattern(regexp)` | Phải khớp với regex                                              |
| `@Positive`        | Số dương                                                         |
| `@PositiveOrZero`  | Số dương hoặc 0                                                  |
| `@Negative`        | Số âm                                                            |
| `@Past`            | Ngày trong quá khứ                                               |
| `@Future`          | Ngày trong tương lai                                             |



Bạn có thể xem danh sách đầy đủ các **constraint annotations** (ràng buộc kiểm tra dữ liệu) trong Java (Jakarta Bean Validation) tại những nơi sau:


- **Trang tài liệu chính thức (Jakarta Bean Validation)**

* [Jakarta Bean Validation 3.0 (Javadoc)](https://jakarta.ee/specifications/bean-validation/3.0/apidocs/jakarta/validation/constraints/package-summary.html)

  > Đây là danh sách đầy đủ các constraint có sẵn.

---

-  **Tài liệu phụ trợ (dễ hiểu hơn cho người mới)**

* [Hibernate Validator Reference Guide](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#validator-defineconstraints)

  > Hibernate Validator là bản triển khai phổ biến nhất của Jakarta Bean Validation. Tài liệu này có ví dụ chi tiết hơn.              |

---

### 🧩 3. Áp dụng trong Controller

📄 `UserController.java`

```java
@PostMapping
public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO dto) {
    User user = userService.createUser(dto);
    return ResponseEntity.status(201).body(user);
}
```

> ✅ Dùng `@Valid` để Spring tự động kiểm tra dữ liệu gửi lên.

---

## III. ❌ Xử lý lỗi (Exception Handling)

### 🔥 Tình huống lỗi thường gặp:

| Lỗi                     | Nguyên nhân                   |
| ----------------------- | ----------------------------- |
| Dữ liệu không hợp lệ    | Vi phạm validation (`@Valid`) |
| Không tìm thấy user     | Truy cập ID không tồn tại     |
| Lỗi bất ngờ từ hệ thống | Exception runtime             |

---

### 🧩 1. Tạo lớp xử lý lỗi tổng quát

📄 `GlobalExceptionHandler.java`

```java
package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý lỗi validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

    // Xử lý lỗi không tìm thấy tài nguyên
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
    }

    // Xử lý lỗi không mong muốn khác
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralError(Exception ex) {
        return ResponseEntity.status(500).body(Map.of("error", "Lỗi hệ thống"));
    }
}
```

📄 `ResourceNotFoundException.java`

```java
package com.example.demo.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

---

## IV. 📦 Trả về Response chuẩn với `ResponseEntity`

### So sánh:

| Cũ (trả trực tiếp) | Mới (dùng ResponseEntity)                      |
| ------------------ | ---------------------------------------------- |
| `return user;`     | `return ResponseEntity.ok(user);`              |
| `return null;`     | `return ResponseEntity.status(404).body(...);` |

### 📄 Ví dụ trong `UserController.java`

```java
@GetMapping("/{id}")
public ResponseEntity<?> getUser(@PathVariable Long id) {
    User user = userService.getUserById(id);
    if (user == null) {
        throw new ResourceNotFoundException("Không tìm thấy user với id: " + id);
    }
    return ResponseEntity.ok(user);
}
```

---

## V. ✅ Kết quả thực tế

### 🟢 Gửi đúng dữ liệu:

```json
POST /api/users
{
  "name": "Alice",
  "email": "alice@gmail.com"
}
```

👉 Phản hồi:

```json
{
  "id": 1,
  "name": "Alice",
  "email": "alice@gmail.com"
}
```

---

### 🔴 Gửi sai dữ liệu:

```json
{
  "name": "",
  "email": "sai-dinh-dang"
}
```

👉 Phản hồi:

```json
{
  "name": "Tên không được để trống",
  "email": "Email không đúng định dạng"
}
```

---

## VI. 🧠 Tổng kết

| Thành phần                                    | Vai trò                                    |
| --------------------------------------------- | ------------------------------------------ |
| `@Valid`                                      | Kích hoạt kiểm tra dữ liệu đầu vào         |
| Annotation Validation                         | Quy định điều kiện cho từng trường         |
| `@RestControllerAdvice` + `@ExceptionHandler` | Xử lý lỗi tập trung, trả thông báo rõ ràng |
| `ResponseEntity`                              | Tùy chỉnh mã HTTP, thông điệp phản hồi     |


