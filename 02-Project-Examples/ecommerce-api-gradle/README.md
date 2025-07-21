# E-commerce api with gradle Example

Cấu trúc thư mục dự án Spring Boot API

```
src
└── main
    ├── java
    │   └── com
    │       └── example
    │           └── ecommerce
    │               ├── controllers
    │               ├── dtos
    │               ├── entities
    │               ├── exceptions
    │               ├── repositories
    │               ├── responses
    │               ├── services
    │               └── EcommerceApplication.java
    └── resources
```


### Giải thích nhanh:

* `controllers`: chứa các REST API controller.
* `dtos`: chứa các lớp Data Transfer Object.
* `entities`: chứa các entity ánh xạ với database.
* `exceptions`: chứa các lớp xử lý lỗi tùy chỉnh.
* `repositories`: chứa các interface làm việc với JPA/Hibernate.
* `responses`: chứa các lớp định dạng phản hồi trả về client.
* `services`: chứa logic nghiệp vụ chính.
* `resources`: chứa các file cấu hình như `application.properties`, `static`, `templates`, v.v.

## Run project

```bash
./gradlew bootRun
```

```
39447ec4-ca33-453b-a09f-9fa8212e7e6d
```