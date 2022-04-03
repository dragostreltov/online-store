insert into category values(101, 'Telefoane, Tablete');
insert into category values(102, 'Gaming, Jocuri');
insert into subcategory values(1011, 'Telefoane', 101);
insert into subcategory values(1012, 'Tablete', 101);
insert into product values(10111, 'Telefon SAMSUNG Galaxy A52s 5G, 128GB, 6GB RAM, Dual SIM, Black', 'SAMSUNG Galaxy A52s', 1550.0, 1011);
insert into product values(10112, 'Telefon SAMSUNG Galaxy M52 5G, 128GB, GB RAM, Dual SIM, Black', 'SAMSUNG Galaxy M52', 1194.0, 1011);
insert into product values(10113, 'Telefon SAMSUNG Galaxy A22 5G, 64GB, 4GB RAM, Dual SIM, Gray', 'SAMSUNG Galaxy A22', 839.0, 1011);
insert into product values(10121, 'Tableta SAMSUNG Galaxy Tab A7 Lite, 8.7", 32GB, 3GB RAM, Wi-Fi + 4G, Silver', 'SAMSUNG Galaxy Tab A7 Lite', 665.1, 1012);
insert into product values(10122, 'Tableta APPLE iPad 9 (2021), 10.2", 64GB, Wi-Fi, Space Grey', 'APPLE iPad 9 (2021)', 1890.0, 1012);
insert into role values(100, 'ROLE_ADMIN');
insert into role values(101, 'ROLE_USER');
insert into user values(10001, true, 'admin', 'admin', 100);
insert into shopping_list values(11, 'test list', 10001);
insert into shopping_list values(12, 'test list2', 10001);
insert into shopping_list values(13, 'test list3', 10001);
insert into shopping_product values(10111, 11);
insert into shopping_product values(10112, 11);
insert into shopping_product values(10111, 12);
insert into shopping_product values(10113, 12);
insert into shopping_product values(10113, 13);