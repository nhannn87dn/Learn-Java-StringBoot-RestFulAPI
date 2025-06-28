# 📦 **Phần 10: Đóng gói và Deploy Spring Boot App (Optional)**

## 🎯 Mục tiêu

* Biết cách đóng gói ứng dụng Spring Boot thành file `.jar`
* Biết cách deploy ứng dụng miễn phí lên các nền tảng cloud phổ biến như **Heroku**, **Render**, hoặc **Railway**

---

## I. 🧱 Đóng gói JAR Spring Boot

Spring Boot ứng dụng dạng web có thể **đóng gói thành 1 file `.jar` duy nhất**, chạy ở bất kỳ đâu có Java.

### ✅ Lệnh Maven để build:

```bash
./mvnw clean package
```

Sau khi chạy thành công, file JAR sẽ nằm trong thư mục:

```
target/myapp-0.0.1-SNAPSHOT.jar
```

### ✅ Chạy thử file JAR:

```bash
java -jar target/myapp-0.0.1-SNAPSHOT.jar
```

Mặc định ứng dụng sẽ chạy trên `http://localhost:8080`.

> 📌 Đảm bảo file `application.properties` không trỏ đến database nội bộ nếu deploy online (nên dùng `env` hoặc cấu hình phù hợp).

---

## II. ☁️ Deploy lên Render (dễ nhất)

### 🔹 Bước 1: Tạo tài khoản tại [https://render.com](https://render.com)

### 🔹 Bước 2: Tạo repository trên GitHub (push source code của bạn lên)

### 🔹 Bước 3: Trên Render

1. Chọn **"New Web Service"**
2. Chọn GitHub repo của bạn
3. Điền:

   * **Build Command**: `./mvnw clean package`
   * **Start Command**: `java -jar target/<tên-file>.jar`
   * **Environment**: chọn `Java`
   * **Port**: Render sẽ dùng biến môi trường `$PORT`, bạn cần dùng port này trong app.

### ✅ Cập nhật `application.properties`:

```properties
server.port=${PORT:8080}
```

Nếu bạn có database, dùng Render Postgres tích hợp.

---

## III. 🚀 Deploy lên Railway (hiện đại, dễ xài)

### 🔹 Bước 1: Vào [https://railway.app](https://railway.app), đăng ký

### 🔹 Bước 2: Chọn "New Project" → "Deploy from GitHub Repo"

### 🔹 Bước 3: Railway tự động build, bạn có thể cấu hình:

* `build command`: `./mvnw clean package`
* `start command`: `java -jar target/*.jar`

### ✅ Sửa `application.properties`:

```properties
server.port=${PORT:8080}
```

### ✅ Nếu cần Database:

Railway hỗ trợ MySQL, Postgres free → Bạn copy credentials và cấu hình `application.properties` như:

```properties
spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
```

---

## IV. ☁️ Deploy lên Heroku (có thể dùng, nhưng giới hạn)

> ⚠️ Từ 2023, Heroku không còn gói Free chính thức nhưng vẫn dùng được với tài khoản cũ.

### 🔹 Bước 1: Cài Heroku CLI

```bash
npm install -g heroku
```

### 🔹 Bước 2: Tạo Procfile

Tạo file `Procfile` trong root:

```
web: java -jar target/myapp-0.0.1-SNAPSHOT.jar
```

### 🔹 Bước 3: Deploy

```bash
heroku login
heroku create
git push heroku main
```

Nếu thành công → Heroku cung cấp link chạy app như `https://yourapp.herokuapp.com`.

---

## V. 📌 Mẹo và lưu ý khi deploy

| Vấn đề                            | Gợi ý                                            |
| --------------------------------- | ------------------------------------------------ |
| Port cố định (8080) không chạy    | Dùng `server.port=${PORT:8080}`                  |
| Database local không kết nối được | Dùng DB cloud (Render, Railway, PlanetScale...)  |
| Quản lý thông tin nhạy cảm        | Sử dụng biến môi trường (`env`) thay vì hardcode |

---

## ✅ Tổng kết

| Mục tiêu              | Hoàn thành                  |
| --------------------- | --------------------------- |
| Build JAR             | ✅ `mvn package`             |
| Deploy cloud          | ✅ Render / Railway / Heroku |
| Dùng biến môi trường  | ✅ Hạn chế hardcode          |
| Hỗ trợ database cloud | ✅ Postgres, MySQL           |

