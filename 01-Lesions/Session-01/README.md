# 📚 PHẦN 1: GIỚI THIỆU & CHUẨN BỊ MÔI TRƯỜNG


## I. 💡 RESTful API là gì?


RESTful API (Representational State Transfer API) là một kiểu kiến trúc thiết kế API dựa trên các nguyên tắc của REST, được sử dụng để xây dựng các giao diện lập trình ứng dụng cho phép các hệ thống khác nhau giao tiếp qua mạng, thường sử dụng giao thức HTTP. RESTful API được thiết kế để đơn giản, dễ mở rộng và tận dụng các tiêu chuẩn web hiện có.

### **Các đặc điểm chính của RESTful API**
1. **Kiến trúc không trạng thái (Stateless)**: Mỗi yêu cầu (request) từ client gửi đến server phải chứa toàn bộ thông tin cần thiết để xử lý. Server không lưu trữ trạng thái của client giữa các yêu cầu, giúp hệ thống dễ mở rộng và bảo trì.

2. **Sử dụng các phương thức HTTP chuẩn**:
   - **GET**: Lấy dữ liệu từ server (ví dụ: truy xuất thông tin người dùng).
   - **POST**: Tạo mới tài nguyên trên server (ví dụ: thêm người dùng mới).
   - **PUT**: Cập nhật tài nguyên hiện có (ví dụ: chỉnh sửa thông tin người dùng).
   - **DELETE**: Xóa tài nguyên (ví dụ: xóa một bài đăng).
   - **PATCH**: Cập nhật một phần tài nguyên.

3. **Tài nguyên (Resources)**: RESTful API xoay quanh các tài nguyên, được biểu diễn dưới dạng các đối tượng (ví dụ: người dùng, bài viết) và được xác định bằng các URI (Uniform Resource Identifier). Ví dụ: `https://api.example.com/users/123` truy xuất thông tin người dùng có ID 123.

4. **Định dạng dữ liệu**: RESTful API thường sử dụng JSON hoặc XML để trao đổi dữ liệu giữa client và server, với JSON là lựa chọn phổ biến nhất do tính đơn giản và dễ đọc.

5. **Phân tầng (Layered System)**: Hệ thống RESTful có thể được tổ chức thành nhiều tầng, trong đó client không cần biết liệu nó đang giao tiếp trực tiếp với server hay qua một trung gian (như proxy hoặc load balancer).

6. **Khả năng cache**: RESTful API hỗ trợ lưu trữ tạm thời (cache) các phản hồi để cải thiện hiệu suất, đặc biệt với các yêu cầu GET.

### **Nguyên tắc thiết kế REST**
RESTful API tuân thủ sáu nguyên tắc của REST, được định nghĩa bởi Roy Fielding:
1. **Client-Server**: Tách biệt giữa client (giao diện người dùng) và server (xử lý dữ liệu).
2. **Stateless**: Mỗi yêu cầu là độc lập và không phụ thuộc vào trạng thái trước đó.
3. **Cacheable**: Phản hồi có thể được lưu trữ để tái sử dụng.
4. **Layered System**: Hệ thống có thể phân tầng để tăng tính linh hoạt.
5. **Code on Demand (tùy chọn)**: Server có thể gửi mã thực thi (như JavaScript) tới client, dù hiếm khi sử dụng.
6. **Uniform Interface**: Giao diện thống nhất với các phương thức chuẩn, tài nguyên, và cách biểu diễn.


RESTful API  Là **kiến trúc thiết kế API** phổ biến, sử dụng các phương thức HTTP như `GET`, `POST`, `PUT`, `DELETE`.

| Phương thức | Chức năng   | Ví dụ                 |
| ----------- | ----------- | --------------------- |
| GET         | Lấy dữ liệu | `GET /api/users`      |
| POST        | Tạo mới     | `POST /api/users`     |
| PUT         | Cập nhật    | `PUT /api/users/1`    |
| DELETE      | Xoá         | `DELETE /api/users/1` |

### **Ví dụ về RESTful API**
Giả sử một ứng dụng quản lý người dùng có các endpoint sau:
- `GET /users`: Lấy danh sách tất cả người dùng.
- `GET /users/123`: Lấy thông tin chi tiết của người dùng có ID 123.
- `POST /users`: Tạo một người dùng mới với dữ liệu được gửi trong body.
- `PUT /users/123`: Cập nhật thông tin người dùng có ID 123.
- `DELETE /users/123`: Xóa người dùng có ID 123.

### **Lợi ích của RESTful API**
- **Dễ mở rộng**: Do không trạng thái và sử dụng chuẩn HTTP, RESTful API phù hợp với các hệ thống lớn.
- **Tính linh hoạt**: Hỗ trợ nhiều định dạng dữ liệu và dễ tích hợp với các ứng dụng khác.
- **Hiệu suất cao**: Tận dụng cache và các phương thức HTTP tối ưu.
- **Dễ sử dụng**: Giao diện thống nhất giúp các nhà phát triển dễ dàng hiểu và triển khai.

### **Hạn chế**
- **Thiếu tiêu chuẩn chính thức**: REST không phải là một giao thức mà chỉ là tập hợp nguyên tắc, dẫn đến cách triển khai có thể khác nhau.
- **Khó khăn với các giao dịch phức tạp**: Do không trạng thái, REST không phù hợp cho các hoạt động yêu cầu theo dõi trạng thái phức tạp.
- **Hiệu suất với dữ liệu lớn**: Có thể chậm nếu yêu cầu trả về khối lượng dữ liệu lớn.


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

  * [Oracle JDK](https://www.oracle.com/java/technologies/downloads/#java21)
* Kiểm tra:

```bash
java -version
```

---

### 2. Cài đặt IDE

* VSCode + Extension Pack for Java
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

