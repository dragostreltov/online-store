insert into role values(100, 'ROLE_ADMIN');
insert into role values(101, 'ROLE_USER');
insert into user values(10001, true, 'admin', 'admin', 100);

insert into coupon values(101, 'TOTAL15', 0.85);
insert into coupon values(102, 'GIFT100', 100);
insert into coupon values(103, 'RO22', 0.78);

insert into category values(101, 'Telefoane, Tablete');
insert into category values(102, 'Gaming, Jocuri');
insert into category values(103, 'Laptop, Desktop');

insert into subcategory values(1011, 'Telefoane', 101);
insert into subcategory values(1012, 'Tablete', 101);
insert into subcategory values(1021, 'Console jocuri', 102);
insert into subcategory values(1022, 'Jocuri', 102);
insert into subcategory values(1031, 'Laptopuri', 103);
insert into subcategory values(1032, 'Sisteme PC', 103);

insert into product values(10111, 'Telefon SAMSUNG Galaxy A52s 5G, 128GB, 6GB RAM, Dual SIM, Black', 'SAMSUNG Galaxy A52s', 1550.0, 1011);
insert into product values(10112, 'Telefon SAMSUNG Galaxy M52 5G, 128GB, GB RAM, Dual SIM, Black', 'SAMSUNG Galaxy M52', 1194.0, 1011);
insert into product values(10113, 'Telefon SAMSUNG Galaxy A22 5G, 64GB, 4GB RAM, Dual SIM, Gray', 'SAMSUNG Galaxy A22', 839.0, 1011);
insert into product values(10114, 'Telefon APPLE iPhone 12 5G, 64GB, Black', 'APPLE iPhone 12', 3160.0, 1011);
insert into product values(10115, 'Telefon SAMSUNG Galaxy A03s, 32GB, 3GB RAM, Dual SIM, Black', 'SAMSUNG Galaxy A03s', 689.0, 1011);
insert into product values(10116, 'Telefon APPLE iPhone 13 5G, 128GB, Midnight', 'APPLE iPhone 13', 3160.0, 1011);
insert into product values(10117, 'Telefon XIAOMI Redmi 9AT, 32GB, 2GB RAM, Dual SIM, Granite Gray', 'XIAOMI Redmi 9AT', 429.0, 1011);
insert into product values(10118, 'Telefon XIAOMI 11T 5G, 256GB, 8GB RAM, Dual SIM, Meteorite Gray', 'XIAOMI 11T', 1800.0, 1011);
insert into product values(10119, 'Telefon SAMSUNG Galaxy M52 5G, 128GB, GB RAM, Dual SIM, Black', 'SAMSUNG Galaxy M52', 1390.0, 1011);
insert into product values(10120, 'Telefon MOTOROLA Moto E20, 32GB, 2GB RAM, Dual SIM, Graphite Grey', 'MOTOROLA Moto E20', 460.0, 1011);
insert into product values(10121, 'Telefon ALLVIEW A20 Max, 3G, 16GB, 1GB RAM, Dual SIM, Blue Gradient', 'ALLVIEW A20 Max', 350.0, 1011);
insert into product values(10122, 'Telefon REALME 8i, 128GB, 4GB RAM, Dual Sim, Space Purple', 'REALME 8i', 800.0, 1011);
insert into product values(10123, 'Telefon MOTOROLA Moto G31, 64GB, 4GB RAM, Dual SIM, Mineral Grey', 'MOTOROLA Moto G31', 800.0, 1011);
insert into product values(10124, 'Telefon OPPO A53, 128GB, 4GB RAM, Dual SIM, Fancy Blue', 'OPPO A53', 780.0, 1011);

insert into product values(20121, 'Tableta SAMSUNG Galaxy Tab A7 Lite, 8.7", 32GB, 3GB RAM, Wi-Fi + 4G, Silver', 'SAMSUNG Galaxy Tab A7 Lite', 665.1, 1012);
insert into product values(20122, 'Tableta APPLE iPad 9 (2021), 10.2", 64GB, Wi-Fi, Space Grey', 'APPLE iPad 9 (2021)', 1890.0, 1012);
insert into product values(20123, 'Tableta LENOVO Tab M10 TB-X505F, 10.1", 32GB, 2GB RAM, Wi-Fi, negru', 'LENOVO Tab M10 TB-X505F', 500.0, 1012);
insert into product values(20124, 'Tableta SAMSUNG Galaxy Tab A8, 10.5", 32GB, 3GB RAM, Wi-Fi, Dark Gray', 'SAMSUNG Galaxy Tab A8', 864.0, 1012);
insert into product values(20125, 'Tableta LENOVO Tab M8 TB-8505X, 8", 32GB, 2GB RAM, Wi-Fi + 4G, Iron Grey', 'LENOVO Tab M8 TB-8505X', 570.0, 1012);
insert into product values(20126, 'Tableta HUAWEI MatePad 11, 128GB, 6GB RAM, Wi-Fi, Matte Gray', 'HUAWEI MatePad 11', 1800.0, 1012);
insert into product values(20127, 'Tableta HUAWEI MatePad T 10, 9.7", 32GB, 2GB RAM, Wi-Fi, Deepsea Blue', 'HUAWEI MatePad T 10', 350.0, 1012);
insert into product values(20128, 'Tableta ALLVIEW AX503, 7", 8GB, 1GB RAM, Wi-Fi + 3G, Black', 'ALLVIEW AX503', 260.0, 1012);
insert into product values(20129, 'Tableta ALLVIEW Viva 1003G, 10.1", 16GB, 2GB RAM, Wi-Fi + 3G, Black', 'ALLVIEW Viva 1003G', 430.0, 1012);
insert into product values(20130, 'Tableta LENOVO Yoga Smart Tab, 10.1", 64GB, 4GB RAM, WiFi, Black', 'LENOVO Yoga Smart Tab', 1200.0, 1012);

insert into product values(10210, 'Consola Microsoft Xbox Series S 500GB + Fortnite & Rocket League Bundle, alb', 'Xbox Series S', 1500.0, 1021);
insert into product values(10211, 'Consola NINTENDO Switch (Joy-Con Neon Red/Blue) HAD', 'NINTENDO Switch', 1500.0, 1021);
insert into product values(10212, 'Consola Microsoft Xbox Series S 512GB, alb', 'Xbox Series S', 1500.0, 1021);
insert into product values(10213, 'Consola portabila Nintendo Switch Lite blue', 'Nintendo Switch Lite', 1100.0, 1021);
insert into product values(10214, 'Consola portabila Nintendo Switch Lite, turquoise', 'Nintendo Switch Lite', 1100.0, 1021);

insert into product values(20220, 'Returnal PS5', 'Returnal PS5', 100.0, 1022);
insert into product values(20221, 'Call of Duty: Vanguard PS5', 'Call of Duty: Vanguard PS5', 170.0, 1022);
insert into product values(20222, 'Deathloop PS5', 'Deathloop PS5', 135.0, 1022);
insert into product values(20223, 'Elden Ring PS5', 'Elden Ring PS5', 270.0, 1022);
insert into product values(20224, 'Battlefield 2042 PS5', 'Battlefield 2042 PS5', 100.0, 1022);

insert into product values(10310, 'Laptop ASUS X515EA-BQ850, Intel Core i3-1115G4 pana la 4.1GHz, 15.6" Full HD, 8GB, SSD 256GB, Intel UHD Graphics, Free Dos, Peacock Blue', 'ASUS X515EA-BQ850', 1800.0, 1031);
insert into product values(10311, 'Laptop gaming LENOVO IdeaPad Gaming 3 15ACH6, AMD Ryzen 5 5600H pana la 4.2GHz, 15.6" Full HD, 16GB, SSD 512GB, NVIDIA GeForce GTX 1650, Free DOS, negru', 'LENOVO IdeaPad Gaming 3 15ACH6', 3750.0, 1031);
insert into product values(10312, 'Laptop LENOVO IdeaPad 3 15IGL05, Intel Celeron N4020 pana la 2.8GHz, 15.6" Full HD, 4GB, SSD 256GB, Intel UHD Graphics 600, Free Dos, Abyss Blue', 'LENOVO IdeaPad 3 15IGL05', 1100.0, 1031);
insert into product values(10313, 'Laptop HUAWEI MateBook D15, Intel Core i3-10110U pana la 4.1GHz, 15.6" Full HD, 8GB, SSD 256GB, Intel UHD Graphics, Windows 10 Home, argintiu', 'HUAWEI MateBook D15', 2100.0, 1031);
insert into product values(10314, 'Laptop APPLE MacBook Air 13 mgn63ze/a, Apple M1, 13.3" Retina Display, 8GB, SSD 256GB, Grafica integrata, macOS Big Sur, Space Gray - Tastatura layout INT', 'APPLE MacBook Air 13 mgn63ze/a', 1800.0, 1031);
insert into product values(10315, 'Laptop HP 15s-fq4012nq, Intel Core i5-1155G7 pana la 4.5GHz, 15.6" Full HD, 8GB, SSD 512GB, Intel Iris Xe Graphics, Windows 11 Home S, auriu pal', 'HP 15s-fq4012nq', 2800.0, 1031);
insert into product values(10316, 'Laptop LENOVO V15 ADA, AMD Ryzen 5 3500U pana la 3.7GHz, 15.6" Full HD, 8GB, SSD 256GB, AMD Radeon Vega 8 Graphics, Windows 10 Pro, gri', 'LENOVO V15 ADA', 2400.0, 1031);

insert into product values(20310, 'Sistem Desktop PC MYRIA Style V55WIN Powered by Asus, AMD Ryzen 5-3400G pana la 4.2GHz, 8GB, SSD 240GB, AMD Radeon RX Vega 11 Graphics, Windows 10 Home', 'MYRIA Style V55WIN', 1800.0, 1032);
insert into product values(20311, 'Sistem Desktop Gaming MYRIA Digital 34 Powered by ASUS, Intel Core I5-10400F pana la 4.3GHz, 16GB, SSD 240GB + HDD 1TB, NVIDIA GeForce GTX 1650 4GB, Ubuntu', 'MYRIA Digital 34', 3500.0, 1032);
insert into product values(20312, 'Sistem Desktop ACER Veriton Essential ES2740G, Intel Core i3-10100 pana la 4.3GHz, 8GB, SSD 256GB + HDD 1TB, Intel UHD Graphics 630, Windows 10 Home', 'ACER Veriton Essential ES2740G', 2000.0, 1032);
insert into product values(20313, 'Sistem Desktop PC ACER Nitro 50, Intel Core i5-11400F pana la 4.4GHz, 16GB, SSD 512GB, NVIDIA GeForce RTX 3060 12GB, Free Dos', 'ACER Nitro 50', 5100.0, 1032);
insert into product values(20314, 'Sistem Desktop Gaming MYRIA TUF V1 Powered by ASUS, Intel Core I5-11600K pana la 4.9GHz, 32GB, SSD 1TB, NVIDIA GeForce RTX 3070 Ti 8GB, FreeDos', 'MYRIA TUF V1', 100.0, 1032);
insert into product values(20315, 'Sistem Desktop PC DELL Precision 3450, Intel Core i9-11900 pana la 5.2GHz, 32GB, 2TB + SSD 1TB, NVIDIA Quadro P1000 4GB, Windows 11 Pro', 'DELL Precision 3450', 100.0, 1032);

insert into shopping_list values(11, 'test list', 10001);
insert into shopping_list values(12, 'test list2', 10001);
insert into shopping_list values(13, 'test list3', 10001);
insert into shopping_product values(10111, 11);
insert into shopping_product values(10112, 11);
insert into shopping_product values(10111, 12);
insert into shopping_product values(10113, 12);
insert into shopping_product values(10113, 13);
