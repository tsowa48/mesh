/**
 * Author:  tsowa
 * Created: 14.01.2020
 */
create database mesh;
create user mesh with login password 'mesh';
grant all on database mesh to mesh;
grant all privileges on all tables in schema public to mesh;
create table users(id serial not null primary key, login text not null, password text not null, fio text not null, rid int not null, token text);
create table client(id serial not null primary key, firstName text not null, lastName text not null, patronymic text, birth text not null, isMale boolean not null, address text not null, solvency real);
create table document(id serial not null primary key, type int not null, serial text not null, number text not null, issued text not null, issuedBy text, departmentCode int, cid int not null);
create table orders(id serial not null primary key, uid int not null, cid int not null, lid int, desired_amount real not null, desired_term int not null, date int not null);
create table loan(id serial not null primary key, name text not null, min_amount real not null, max_amount real not null, min_term int not null, max_term int not null, min_percent real not null, max_percent real not null, min_solvency real not null, max_solvency real not null);
create table role(id serial not null primary key, name text not null, access text not null);

insert into role(name, access) values ('Администратор', 'admin'), ('Оператор', 'operator');

insert into users(login, password, fio, rid) values
  ('admin', 'admin', 'Администратор', 1),
  ('operator', 'operator', 'Оператор', 2),
  ('collector', 'collector', 'Коллектор', 3),
  ('accountant', 'accountant', 'Бухгалтер', 4);

insert into client(firstName, lastName, patronymic, birth, isMale, address) values
  ('Иванов', 'Иван', 'Сергеевич', '4.12.1960', true, 'Russia'),
  ('Petr', 'Polk', 'Chabka', '12.04.2002', false, 'Russia, Kazan');

insert into document(type,serial,number,issued,cid) values
  (0, '4212', '456123', 8, 1),
  (1, '42', '454654564654', 16, 1),
  (0, '4004', '132465', 6454, 2);
