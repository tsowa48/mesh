/**
 * Author:  tsowa
 * Created: 14.01.2020
 */
create database mesh;
create user mesh with login password 'mesh';
grant all on database mesh to mesh;
grant all privileges on all tables in schema public to mesh;
create table users(id serial not null primary key, login text not null, password text not null, fio text not null, token text);
create table client(id serial not null primary key, firstName text not null, lastName text not null, patronymic text, birth text not null, isMale boolean not null, address text not null, solvency real);
create table document(id serial not null primary key, type int not null, serial text not null, number text not null, issued text not null, issuedBy text, departmentCode int, cid int not null);
create table orders(id serial not null primary key, uid int not null, cid int not null, lid int not null, desired_amount real not null, desired_term int not null, approved_amount real not null, approved_term int not null, date int not null);
create table loan(id serial not null primary key, name text not null, min_amount real not null, max_amount real not null, min_term int not null, max_term int not null, percent real not null, calc_type int not null);--TODO: review columns

insert into users(login, password, fio) values
  ('admin', 'admin', 'Администратор'),
  ('operator', 'operator', 'Оператор'),
  ('collector', 'collector', 'Коллектор'),
  ('accountant', 'accountant', 'Бухгалтер');

insert into client(firstName, lastName, patronymic, birth, isMale, address) values
  ('Иванов', 'Иван', 'Сергеевич', '4.12.1960', true, 'Russia'),
  ('Petr', 'Polk', 'Chabka', '12.04.2002', false, 'Russia, Kazan');

insert into document(type,serial,number,issued,cid) values
  (0, '4212', '456123', 8, 1),
  (1, '42', '454654564654', 16, 1),
  (0, '4004', '132465', 6454, 2);
--select array_to_string(array(select chr((48 + round(random() * 69))::integer) from generate_series(1,15)), '')
