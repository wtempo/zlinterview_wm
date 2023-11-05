INSERT INTO customer(id, name) VALUES('1ef3f80a-7c29-11ee-b962-0242ac120002', 'CUSTOMER A');
INSERT INTO offering(id, name, skus) VALUES('7b8488bd-bbeb-4e23-9d7a-246b71e02911', 'OFFERING A', '["7b8488bd-bbeb-4e23-9d7a-246b71e02911"]');
INSERT INTO customer_offering (id, customer_id, offering_id, active_subscription_limit) VALUES('44ee6ae9-0e1f-4393-ae22-adf37bc859de', '1ef3f80a-7c29-11ee-b962-0242ac120002', '7b8488bd-bbeb-4e23-9d7a-246b71e02911', 3);
INSERT INTO users (id, customer_id, first_name, last_name) VALUES('aaad212c-b879-47ae-b1de-a8ed130d4573', '1ef3f80a-7c29-11ee-b962-0242ac120002', 'USERA', 'USERA');
INSERT INTO users (id, customer_id, first_name, last_name) VALUES('bbb2ce04-9ca9-4288-8880-ce56cfd1749f', '1ef3f80a-7c29-11ee-b962-0242ac120002', 'USERB', 'USERB');
INSERT INTO users (id, customer_id, first_name, last_name) VALUES('cccff9dc-1649-4487-b9c6-033b7a30765d', '1ef3f80a-7c29-11ee-b962-0242ac120002', 'USERC', 'USERC');
INSERT INTO users (id, customer_id, first_name, last_name) VALUES('dddaaadc-1649-4487-b9c6-033b7a30765d', '1ef3f80a-7c29-11ee-b962-0242ac120002', 'USERC', 'USERC');
