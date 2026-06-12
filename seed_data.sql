-- Tắt kiểm tra khóa ngoại tạm thời để tránh xung đột thứ tự xóa bảng
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE bookings;
TRUNCATE TABLE court_images;
TRUNCATE TABLE courts;
TRUNCATE TABLE time_slots;
TRUNCATE TABLE badminton_clusters;
TRUNCATE TABLE token_blacklist;
TRUNCATE TABLE otp_codes;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- 1. Khởi tạo danh sách Người dùng (Users)
-- Mật khẩu tương ứng của các tài khoản:
-- admin -> admin123
-- manager1 -> manager123
-- manager2 -> manager123
-- customer1 -> customer123
-- customer2 -> customer123
INSERT INTO users (id, username, email, password, role, full_name, phone_number, enabled, account_non_locked, created_at)
VALUES
(1, 'admin', 'admin@example.com', '$2a$12$YhICZKiP2IrBHdYAZLJQvewcUXV.3cAU7.sInQ6ScAaplPiy3.kia', 'ADMIN', 'Quản trị viên Hệ thống', '0123456789', 1, 1, NOW()),
(2, 'manager1', 'manager1@example.com', '$2a$12$BGmbxBQ.QN6SXW8wSxKpAemDgsSCGl3rUcyajw.BkPhhrYlKtc5Qy', 'MANAGER', 'Nguyễn Văn Quản Lý', '0987654321', 1, 1, NOW()),
(3, 'manager2', 'manager2@example.com', '$2a$12$BGmbxBQ.QN6SXW8wSxKpAemDgsSCGl3rUcyajw.BkPhhrYlKtc5Qy', 'MANAGER', 'Trần Thị Quản Lý', '0988888888', 1, 1, NOW()),
(4, 'customer1', 'customer1@example.com', '$2a$12$RH2uD2zW7C3HzghhU3R/lOcHUPoUS4RjMkgWeCOScjcC/WMJsLzWO', 'CUSTOMER', 'Lê Văn Khách Hàng', '0977777777', 1, 1, NOW()),
(5, 'customer2', 'customer2@example.com', '$2a$12$RH2uD2zW7C3HzghhU3R/lOcHUPoUS4RjMkgWeCOScjcC/WMJsLzWO', 'CUSTOMER', 'Phạm Minh Khách Hàng', '0966666666', 1, 1, NOW());

-- 2. Khởi tạo danh sách Cụm sân cầu lông (Badminton Clusters)
INSERT INTO badminton_clusters (id, name, description, address, phone_number, image_url, is_active, manager_id, created_at, updated_at)
VALUES
(1, 'Cụm Sân Cầu Lông Đống Đa', 'Cụm sân đạt tiêu chuẩn quốc tế tại quận Đống Đa, Hà Nội', 'Số 10 Đống Đa, Hà Nội', '0241112222', 'https://res.cloudinary.com/demo/image/upload/v1631234567/dongda_cluster.jpg', 1, 2, NOW(), NOW()),
(2, 'Cụm Sân Cầu Lông Cầu Giấy', 'Cụm sân khang trang đầy đủ tiện ích tại quận Cầu Giấy', 'Số 20 Cầu Giấy, Hà Nội', '0243334444', 'https://res.cloudinary.com/demo/image/upload/v1631234568/caugiay_cluster.jpg', 1, 3, NOW(), NOW());

-- 3. Khởi tạo các Sân Cầu lông (Courts - liên kết với Cụm sân)
INSERT INTO courts (id, name, description, base_price_per_hour, image_url, status, cluster_id, created_at)
VALUES
(1, 'Sân Số 1 Đống Đa', 'Sân thảm cao su giảm chấn chất lượng cao', 80000.00, 'https://res.cloudinary.com/demo/image/upload/v1631234569/court1.jpg', 'ACTIVE', 1, NOW()),
(2, 'Sân Số 2 Đống Đa (VIP)', 'Sân Vip có máy điều hòa và trang bị ghế ngồi nghỉ ngơi', 120000.00, 'https://res.cloudinary.com/demo/image/upload/v1631234570/court2.jpg', 'ACTIVE', 1, NOW()),
(3, 'Sân Số 1 Cầu Giấy', 'Sân thảm thường, không gian thoáng đãng ngoài trời có mái che', 75000.00, 'https://res.cloudinary.com/demo/image/upload/v1631234571/court3.jpg', 'ACTIVE', 2, NOW()),
(4, 'Sân Số 2 Cầu Giấy', 'Sân thảm gỗ tự nhiên chống trơn trượt', 90000.00, 'https://res.cloudinary.com/demo/image/upload/v1631234572/court4.jpg', 'ACTIVE', 2, NOW());

-- 4. Khởi tạo hình ảnh phụ cho sân (Court Images)
INSERT INTO court_images (id, image_url, public_id, court_id)
VALUES
(1, 'https://res.cloudinary.com/demo/image/upload/v1631234569/court1.jpg', 'court1_pub', 1),
(2, 'https://res.cloudinary.com/demo/image/upload/v1631234570/court2.jpg', 'court2_pub', 2),
(3, 'https://res.cloudinary.com/demo/image/upload/v1631234571/court3.jpg', 'court3_pub', 3),
(4, 'https://res.cloudinary.com/demo/image/upload/v1631234572/court4.jpg', 'court4_pub', 4);

-- 5. Khởi tạo Khung giờ chơi (Time Slots)
INSERT INTO time_slots (id, start_time, end_time, price, status)
VALUES
(1, '05:00:00', '07:00:00', 60000.00, 'ACTIVE'),  -- Sáng sớm (giá ưu đãi)
(2, '07:00:00', '09:00:00', 80000.00, 'ACTIVE'),  -- Giờ sáng
(3, '09:00:00', '11:00:00', 80000.00, 'ACTIVE'),
(4, '13:00:00', '15:00:00', 80000.00, 'ACTIVE'),  -- Giờ chiều
(5, '17:00:00', '19:00:00', 120000.00, 'ACTIVE'), -- Giờ vàng buổi tối (giá cao)
(6, '19:00:00', '21:00:00', 120000.00, 'ACTIVE'); -- Giờ vàng buổi tối (giá cao)

-- 6. Khởi tạo danh sách Đặt sân chơi thử nghiệm (Bookings)
INSERT INTO bookings (id, user_id, court_id, time_slot_id, booking_date, status, payment_status, total_price, note, created_at, updated_at)
VALUES
(1, 4, 1, 5, '2026-06-15', 'CONFIRMED', 'PAID', 120000.00, 'Khách hàng 1 đã thanh toán chuyển khoản qua App', NOW(), NOW()),
(2, 4, 2, 6, '2026-06-15', 'PENDING', 'UNPAID', 120000.00, 'Khách hàng 1 đăng ký lịch chờ, sẽ thanh toán sau', NOW(), NOW()),
(3, 5, 3, 2, '2026-06-16', 'CONFIRMED', 'PAID', 80000.00, 'Khách quen đặt trước thanh toán trước', NOW(), NOW()),
(4, 5, 4, 1, '2026-06-16', 'REJECTED', 'UNPAID', 60000.00, 'Bị từ chối do trùng khung giờ bảo trì kỹ thuật', NOW(), NOW());
