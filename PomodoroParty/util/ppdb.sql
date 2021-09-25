drop database if exists PomodoroParty;
create database PomodoroParty;
use PomodoroParty;
create table Users (
	user_id int(11) primary key not null auto_increment,
    user_username varchar(50) not null,
    user_password varchar(50) not null,
    user_nickname varchar(50) not null,
    user_last_session_date date not null,
    user_num_streaks int(11) not null,
    user_total_sessions int(11) not null
);
create table CurrentlyOnline (
	nickname varchar(50) not null
);
create table Guests (
	guest_id int(11) primary key not null auto_increment,
    guest_username varchar(50) not null
);
