# 🚀 **Phần 5: Nâng cao REST API với Spring Data JPA**

## 🎯 Mục tiêu:

* Thực hiện các truy vấn SELECT nâng cao.
* Tìm kiếm theo tham số (query params).
* Áp dụng phân trang (pagination) và sắp xếp (sorting).
* Mapping giữa Entity và DTO.

---

## I. 📚 Spring Data JPA Query – Tổng quan

Spring Data JPA hỗ trợ:

* Truy vấn theo tên hàm (`findBy...`)
* Truy vấn bằng `@Query` (JPQL hoặc native SQL)
* Truy vấn có phân trang và sắp xếp

---

## II. 🔍 Các truy vấn SELECT thường gặp

📄 Ví dụ: `User` Entity

```java
@Entity
public class User {
    @Id @GeneratedValue
    private Long id;

    private String name;
    private String email;
    private int age;
}
```

📄 `UserRepository.java`

```java
public interface UserRepository extends JpaRepository<User, Long> {
    // 1. Tìm theo tên
    List<User> findByName(String name);

    // 2. Tìm chứa ký tự (LIKE)
    List<User> findByNameContaining(String keyword);

    // 3. Tìm theo tuổi lớn hơn
    List<User> findByAgeGreaterThan(int age);

    // 4. Tìm theo nhiều điều kiện
    List<User> findByNameAndAge(String name, int age);

    // 5. Tùy chỉnh truy vấn với @Query
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findByEmail(String email);
}
```

---

## III. 🧭 Tìm kiếm bằng Query Parameters

📄 `UserController.java`

```java
@GetMapping("/search")
public ResponseEntity<?> searchUsers(
    @RequestParam(required = false) String name,
    @RequestParam(required = false) Integer age
) {
    List<User> users;

    if (name != null && age != null) {
        users = userRepository.findByNameAndAge(name, age);
    } else if (name != null) {
        users = userRepository.findByNameContaining(name);
    } else if (age != null) {
        users = userRepository.findByAgeGreaterThan(age);
    } else {
        users = userRepository.findAll();
    }

    return ResponseEntity.ok(users);
}
```

👉 Truy cập:

```
GET /api/users/search?name=Ali
GET /api/users/search?age=20
GET /api/users/search?name=Ali&age=25
```

---

## IV. 📦 Pagination & Sorting

### 1. Dùng `Pageable` trong Repository

📄 `UserRepository.java`

```java
Page<User> findAll(Pageable pageable);
Page<User> findByNameContaining(String name, Pageable pageable);
```

---

### 2. Controller phân trang & sắp xếp

📄 `UserController.java`

```java
@GetMapping
public ResponseEntity<?> getUsersPage(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "5") int size,
    @RequestParam(defaultValue = "id,asc") String[] sort
) {
    Sort.Direction dir = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort[0]));

    Page<User> pageResult = userRepository.findAll(pageable);
    return ResponseEntity.ok(pageResult);
}
```

👉 Truy cập:

```
GET /api/users?page=0&size=10&sort=name,desc
```

📦 Kết quả trả về `Page<User>` chứa:

* `.getContent()` – danh sách user
* `.getTotalPages()`, `.getTotalElements()`, `.isLast()` – meta

---

## V. 🔄 Mapping giữa Entity và DTO

### ❓ Vì sao cần DTO?

* Giới hạn thông tin trả ra (ẩn password, v.v.)
* Gọn nhẹ cho client
* Format tuỳ ý (ví dụ: `fullName`, định dạng ngày…)

---

### 1. Tự viết mapping

📄 `UserDTO.java`

```java
@Data
@AllArgsConstructor
public class UserDTO {
    private String name;
    private String email;
}
```

📄 Trong `UserService.java`

```java
public UserDTO mapToDTO(User user) {
    return new UserDTO(user.getName(), user.getEmail());
}
```

---

### 2. Dùng **MapStruct** (tự động sinh code mapping)

#### a. Thêm dependency

```xml
<dependency>
  <groupId>org.mapstruct</groupId>
  <artifactId>mapstruct</artifactId>
  <version>1.5.5.Final</version>
</dependency>
<annotationProcessorPaths>
  <path>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct-processor</artifactId>
    <version>1.5.5.Final</version>
  </path>
</annotationProcessorPaths>
```

#### b. Tạo interface Mapper

📄 `UserMapper.java`

```java
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO dto);
}
```

➡️ MapStruct tự sinh ra class thực thi.

#### c. Dùng trong service

```java
@Autowired
private UserMapper userMapper;

UserDTO dto = userMapper.toDTO(user);
```

---

## VI. ✅ Tổng kết

| Tính năng          | Mục đích                               |
| ------------------ | -------------------------------------- |
| `findBy...`        | Truy vấn dữ liệu dựa trên tên hàm      |
| `@Query`           | Truy vấn tuỳ chỉnh bằng JPQL/SQL       |
| `@RequestParam`    | Tìm kiếm bằng tham số trên URL         |
| `Pageable`, `Sort` | Phân trang và sắp xếp dữ liệu          |
| DTO                | Trả dữ liệu theo định dạng client cần  |
| MapStruct          | Tự động sinh mã mapping Entity <-> DTO |

---

## 📝 Gợi ý thực hành

1. Tạo API `GET /api/users/search` hỗ trợ tìm theo `name`, `age`, `email`, có phân trang.
2. Dùng DTO để chỉ trả về `id`, `name`, `email`, ẩn đi các trường nhạy cảm.
3. Dùng `@Query` để tạo truy vấn phức tạp: tìm user theo tháng sinh, hoặc theo tên & tuổi.

