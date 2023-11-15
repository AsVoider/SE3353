create table if not exists book
(
    id           int auto_increment
        primary key,
    title        varchar(100)                not null,
    isbn         varchar(20)                 not null,
    author       varchar(200)                null,
    type         varchar(50)                 null,
    price        decimal(10, 2) default 0.00 not null,
    publish_time date                        null,
    description  varchar(2000)               null,
    is_exist     int            default 0    not null,
    brief        varchar(500)                null,
    src          varchar(200)                null
)
    engine = InnoDB
    charset = utf8mb3;

create table if not exists user
(
    id         int auto_increment
        primary key,
    name       varchar(50)          not null,
    email      varchar(50)          not null,
    is_admin   tinyint(1) default 0 not null,
    is_enabled tinyint(1) default 1 not null
)
    engine = InnoDB
    charset = utf8mb3;

create table if not exists cart
(
    id      int auto_increment
        primary key,
    user_id int not null,
    constraint cart_ibfk_1
        foreign key (user_id) references user (id)
)
    engine = InnoDB
    charset = utf8mb3;

create index user_id
    on cart (user_id);

create table if not exists cart_item
(
    id      int auto_increment
        primary key,
    book_id int not null,
    cart_id int not null,
    num     int not null,
    constraint cart_item_ibfk_1
        foreign key (book_id) references book (id),
    constraint cart_item_ibfk_2
        foreign key (cart_id) references cart (id)
)
    engine = InnoDB;

create index book_id
    on cart_item (book_id);

create index cart_id
    on cart_item (cart_id);

create table if not exists order_table
(
    id        int auto_increment
        primary key,
    user_id   int                                      not null,
    buy_time  timestamp      default CURRENT_TIMESTAMP not null,
    send_to   varchar(200)                             not null,
    phone_num varchar(20)                              not null,
    address   varchar(200)                             not null,
    note      varchar(500)                             null,
    price     decimal(10, 2) default 0.00              not null,
    constraint order_table_ibfk_1
        foreign key (user_id) references user (id)
)
    engine = InnoDB
    charset = utf8mb3;

create table if not exists order_item
(
    id       int auto_increment
        primary key,
    book_id  int                         not null,
    order_id int                         not null,
    num      int                         not null,
    price    decimal(10, 2) default 0.00 not null,
    constraint order_item_ibfk_1
        foreign key (book_id) references book (id),
    constraint order_item_ibfk_2
        foreign key (order_id) references order_table (id)
)
    engine = InnoDB
    charset = utf8mb3;

create index book_id
    on order_item (book_id);

create index order_id
    on order_item (order_id);

create index user_id
    on order_table (user_id);

create table if not exists user_auth
(
    user_id  int         not null
        primary key,
    username varchar(31) not null,
    password varchar(31) not null,
    constraint user_auth_ibfk_1
        foreign key (user_id) references user (id)
)
    engine = InnoDB
    charset = utf8mb3;

