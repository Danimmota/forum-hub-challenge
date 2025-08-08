create table courses (

    id bigint not null auto_increment,
    name varchar(100) not null,

    primary key(id)

);

create table users(

    id bigint auto_increment,
    name varchar(100) not null,
    email varchar(100) not null unique,
    login varchar(100) not null,
	password varchar(255) not null,
	role enum('ROLE_USER', 'ROLE_ADMIN') not null default 'ROLE_USER',

    primary key(id)
);

create table topics (

    id bigint auto_increment,
    title varchar(255) not null,
    message text not null,
    creation_date datetime not null,
	status enum('SOLUCIONADO','PENDENTE') default 'PENDENTE',
    course_id bigint  not null,
    author_id bigint  not null,

    primary key(id),

	constraint fk_topics_author_id foreign key (author_id) references users(id),
    constraint fk_topics_course_id foreign key (course_id) references courses(id)

);

create table answers(

    id bigint not null auto_increment,
    message text not null,
    creation_date datetime not null,
	author_id bigint not null,
	topic_id bigint not null,

    primary key(id),

	constraint fk_answers_topic_id foreign key (topic_id) references topics(id),
    constraint fk_answers_author_id foreign key (author_id) references users(id)
);