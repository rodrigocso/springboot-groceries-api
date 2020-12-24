insert into brand (id, name)
values (101, 'Kirkland'),
       (102, 'Amazon'),
       (103, 'Google'),
       (104, 'Microsoft');

insert into product (id, name, brand_id)
values (101, 'Chicken Breast', 101),
       (102, 'Filtered Water', 101),
       (103, 'Kindle Fire', 102),
       (104, 'Surface Pro', 104);
