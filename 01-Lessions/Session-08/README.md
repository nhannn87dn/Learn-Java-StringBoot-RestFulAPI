# ✅ **Phần 8: Testing trong Spring Boot**

## 🎯 Mục tiêu:

* Hiểu các loại test trong Spring Boot.
* Viết được **unit test** và **integration test**.
* Sử dụng `MockMvc` để test các REST API endpoint một cách chính xác.

---

## I. 🧪 Tại sao phải test?

Trong phát triển phần mềm, **testing là bắt buộc** để:

* Đảm bảo code hoạt động đúng.
* Tránh lỗi khi chỉnh sửa (regression).
* Giúp refactor yên tâm.
* Tự động kiểm tra trước khi deploy.

---

## II. 🧱 Các loại test trong Spring Boot

| Loại test            | Mục đích                    | Annotation chính              |
| -------------------- | --------------------------- | ----------------------------- |
| **Unit Test**        | Kiểm tra từng lớp riêng lẻ  | `@WebMvcTest`, `@DataJpaTest` |
| **Integration Test** | Kiểm tra toàn hệ thống thật | `@SpringBootTest`             |

---

## III. 🧪 Unit Test với `@WebMvcTest`

### 👉 Dùng khi muốn test riêng các controller (không cần chạy toàn bộ ứng dụng)

### 1. Dependency cần thiết

Spring Boot Starter Test đã bao gồm:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-test</artifactId>
  <scope>test</scope>
</dependency>
```

### 2. Ví dụ controller

```java
@RestController
@RequestMapping("/api/hello")
public class HelloController {
    @GetMapping
    public String hello() {
        return "Hello, Spring!";
    }
}
```

### 3. Viết test với `@WebMvcTest`

```java
@WebMvcTest(HelloController.class)
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHelloEndpoint() throws Exception {
        mockMvc.perform(get("/api/hello"))
               .andExpect(status().isOk())
               .andExpect(content().string("Hello, Spring!"));
    }
}
```

> ✅ Ưu điểm: nhanh, gọn, chỉ load các bean liên quan đến controller.

---

## IV. 🔄 Integration Test với `@SpringBootTest`

### 👉 Dùng để test **end-to-end**, chạy toàn bộ ứng dụng như thật:

* Controller → Service → Repository → Database (in-memory như H2)

### 1. Ví dụ

```java
@SpringBootTest
@AutoConfigureMockMvc
class UserApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").isNumber());
    }
}
```

> 🔄 Thường kết hợp với database test như H2, và có thể preload dữ liệu bằng SQL script nếu cần.

---

## V. 🧰 Giới thiệu MockMvc

**MockMvc** là công cụ cho phép:

* Mô phỏng HTTP request (`GET`, `POST`, …)
* Kiểm tra response, status, body
* Không cần chạy server thật

📌 Sử dụng được trong cả `@WebMvcTest` lẫn `@SpringBootTest`.

### Ví dụ với JSON:

```java
@Test
void testCreateUser() throws Exception {
    String jsonBody = """
        {
            "name": "John",
            "email": "john@example.com"
        }
    """;

    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("John"));
}
```

---

## VI. ✅ Tổng kết

| Chủ đề            | Ghi chú                              |
| ----------------- | ------------------------------------ |
| `@WebMvcTest`     | Test controller đơn lẻ               |
| `@SpringBootTest` | Test toàn ứng dụng                   |
| `MockMvc`         | Mô phỏng HTTP request dễ dàng        |
| JSONPath          | Kiểm tra dữ liệu trong JSON response |
| Test coverage     | Giúp đảm bảo chất lượng code         |

---

## 🎯 Gợi ý thực hành

1. Tạo controller `/api/hello` và viết unit test.
2. Tạo CRUD API `/api/users`, viết integration test cho `GET`, `POST`.
3. Thử trả lỗi 404 khi gọi API sai, kiểm tra trong test.
4. Thêm @SpringBootTest vào test class và dùng MockMvc để test luồng thực tế.
