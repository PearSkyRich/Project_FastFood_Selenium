# 🍔 Hệ Thống Quản Lý Cửa Hàng Đồ Ăn Nhanh (FastFood FAF)

![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)
![WebSocket](https://img.shields.io/badge/WebSocket-RealTime-brightgreen?style=for-the-badge)

## 📸 Giao diện ứng dụng

*(Ghi chú cho Dev: Thay thế đường dẫn `./images/...` bên dưới bằng ảnh chụp màn hình thực tế của bạn)*

### 1. Màn hình Thu ngân (POS) & Gộp đơn hàng
> Hiển thị sơ đồ bàn với mã màu thông minh. Trạng thái bàn tự động cập nhật khi khách hàng đặt món.
![Màn hình Thu Ngân](./images/man_hinh_thu_ngan.png) 

### 2. Màn hình Quản lý Bếp (Real-time)
> Nhận thông báo đơn hàng mới tức thì qua WebSocket mà không cần tải lại trang.
![Màn hình Bếp](./images/man_hinh_bep.png)

### 3. Giao diện Đặt món (Menu)
> Hiển thị danh sách món ăn, cho phép thêm món vào giỏ hàng và thanh toán tiện lợi.
![Màn hình Đặt Món](./images/man_hinh_menu.png)

---
FastFood FAF là một hệ thống phần mềm quản lý nhà hàng F&B hiện đại, được phát triển với trọng tâm là **Đồng bộ hóa thời gian thực (Real-time)** giữa bộ phận Thu ngân và Bếp, giúp tối ưu hóa quy trình phục vụ khách hàng và quản lý nguyên liệu chặt chẽ.

---

## ✨ Tính năng nổi bật
- **Bán hàng & Thu ngân (POS):** Giao diện trực quan, gộp đơn hàng tự động thông minh, quản lý sơ đồ bàn bằng mã màu trạng thái (Trống -> Chờ thanh toán -> Đang nấu -> Khách đang ăn).
- **Đồng bộ Bếp Real-time:** Sử dụng giao thức WebSocket, màn hình dưới bếp tự động nổ đơn và chớp màu ngay khi thu ngân thanh toán mà không cần tải lại trang.
- **Quản lý Kho thông minh:** Tự động trừ nguyên liệu theo định mức công thức (Recipe) và áp dụng thuật toán tính **Giá vốn trung bình (MAC)** khi nhập kho.
- **Phân quyền chặt chẽ:** Tự động điều hướng và giới hạn chức năng theo vai trò người dùng (Admin, Thu ngân, Bếp, Bàn khách).

---

## 🛠 Công nghệ sử dụng
### 1. Frontend
- **Framework:** ReactJS (Build tools: Vite)
- **UI Library:** Ant Design (AntD)
- **Giao tiếp HTTP:** Axios
- **Real-time:** SockJS & STOMP Client

### 2. Backend
- **Framework:** Java Spring Boot (Java 21)
- **ORM:** Spring Data JPA / Hibernate
- **Real-time:** Spring WebSocket
- **Database:** MySQL

---

## 🚀 Hướng dẫn cài đặt và chạy dự án (Localhost)

### Yêu cầu môi trường (Prerequisites)
- [Java JDK 21](https://www.oracle.com/java/technologies/downloads/#java21) trở lên.
- [Node.js v24.14.0](https://nodejs.org/) trở lên.
- MySQL Server.

### Bước 1: Khởi tạo Cơ sở dữ liệu (Database)
1. Mở MySQL Workbench hoặc trình quản lý CSDL của bạn.
2. Tạo một database mới với tên: `fastfood-db`.
3. Import file `fastfood-db.sql` (nằm ở thư mục gốc của dự án) vào database vừa tạo để nạp cấu trúc bảng và dữ liệu mẫu.

### Bước 2: Chạy Backend (Spring Boot)
1. Mở thư mục `backend` bằng IDE (IntelliJ IDEA / Eclipse / VS Code).
2. Kiểm tra file `application.properties` (hoặc `application.yml`) để đảm bảo thông tin kết nối MySQL (username/password) khớp với máy của bạn.
3. Chạy file khởi động `FastFoodApplication.java`.
4. Backend sẽ khởi chạy tại cổng: `http://localhost:8080`.

### Bước 3: Chạy Frontend (ReactJS)
1. Mở terminal, trỏ đường dẫn vào thư mục `frontend`.
2. Cài đặt các gói thư viện phụ thuộc:
   ```bash
   npm install
3.Khởi chạy ứng dụng: npm run dev
4.Frontend sẽ chạy tại cổng: http://localhost:5173. Mở đường dẫn này trên trình duyệt (Chrome/Edge) để sử dụng.

## Tài khoản demo
| Vai trò | Tài khoản (Username) | Mật khẩu (Password) | Chức năng chính |
|---|---|---|---|
| Quản trị viên | ADMIN | 123456 | Toàn quyền cấu hình, quản lý kho, xem báo cáo thống kê |
| Thu ngân | ThuNgan | 123456 | Lên đơn, chọn bàn, thanh toán, giải phóng bàn |
| Bếp | Bep | 123456 | Nhận đơn thời gian thực, báo hoàn thành món |
| Khách hàng | Ban01 | 123456 | Khách tự xem menu và đặt món tại bàn |

## đội ngũ phát triển 
1.Nguyễn Viết Chung

2.Nguyễn Hoàng Hải Đăng

3.Trần Khánh Duy

4.Phan Quang Hiếu
