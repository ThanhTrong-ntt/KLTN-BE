create table role
(
    id           varchar(255) not null,
    created_by   varchar(255),
    created_date datetime,
    modified_by  varchar(255),
    updated_date datetime,
    admin_flag   bit,
    removal_flag bit          not null,
    role_name    integer,
    primary key (id)
) engine=InnoDB;
create table t_access_control
(
    id            varchar(255) not null,
    function_code varchar(255),
    removal_flag  bit,
    screen_code   varchar(255),
    primary key (id)
) engine=InnoDB;
create table t_cart
(
    id           varchar(255) not null,
    created_by   varchar(255),
    created_date datetime,
    modified_by  varchar(255),
    updated_date datetime,
    removal_flag bit          not null,
    primary key (id)
) engine=InnoDB;
create table t_cart_detail
(
    id           varchar(255) not null,
    created_by   varchar(255),
    created_date datetime,
    modified_by  varchar(255),
    updated_date datetime,
    quantity     integer,
    removal_flag bit,
    cart_id      varchar(255) not null,
    product_id   varchar(255) not null,
    user_id      varchar(255),
    primary key (id)
) engine=InnoDB;
create table t_category
(
    id            varchar(255) not null,
    created_by    varchar(255),
    created_date  datetime,
    modified_by   varchar(255),
    updated_date  datetime,
    category_name varchar(255),
    description   varchar(255),
    removal_flag  bit          not null,
    primary key (id)
) engine=InnoDB;
create table t_media_file
(
    id           varchar(255) not null,
    created_by   varchar(255),
    created_date datetime,
    modified_by  varchar(255),
    updated_date datetime,
    file_name    varchar(255) not null,
    file_type    varchar(255) not null,
    removal_flag bit          not null,
    url          varchar(255) not null,
    primary key (id)
) engine=InnoDB;
create table t_order
(
    id           varchar(255) not null,
    created_by   varchar(255),
    created_date datetime,
    modified_by  varchar(255),
    updated_date datetime,
    removal_flag bit          not null,
    user_id      varchar(255),
    primary key (id)
) engine=InnoDB;
create table t_order_detail
(
    id           varchar(255) not null,
    created_by   varchar(255),
    created_date datetime,
    modified_by  varchar(255),
    updated_date datetime,
    removal_flag bit          not null,
    cart_id      varchar(255) not null,
    order_id     varchar(255),
    primary key (id)
) engine=InnoDB;
create table t_product
(
    id           varchar(255) not null,
    created_by   varchar(255),
    created_date datetime,
    modified_by  varchar(255),
    updated_date datetime,
    description  varchar(255),
    price        bigint,
    product_name varchar(255),
    quantity     integer,
    removal_flag bit          not null,
    primary key (id)
) engine=InnoDB;
create table t_product_images
(
    product_id varchar(255) not null,
    images_id  varchar(255) not null
) engine=InnoDB;
create table t_product_product_category
(
    product_id          varchar(255) not null,
    product_category_id varchar(255) not null
) engine=InnoDB;
create table t_role_to_user
(
    user_id varchar(255) not null,
    role_id varchar(255) not null,
    primary key (user_id, role_id)
) engine=InnoDB;
create table t_user
(
    id           varchar(255) not null,
    created_by   varchar(255),
    created_date datetime,
    modified_by  varchar(255),
    updated_date datetime,
    active_flag  bit,
    email        varchar(255) not null,
    password     varchar(255) not null,
    removal_flag bit,
    user_name    varchar(255),
    profile_id   varchar(255),
    primary key (id)
) engine=InnoDB;
create table t_user_profile
(
    id           varchar(255) not null,
    created_by   varchar(255),
    created_date datetime,
    modified_by  varchar(255),
    updated_date datetime,
    first_name   varchar(255) not null,
    last_name    varchar(255) not null,
    removal_flag bit          not null,
    avatar_id    varchar(255),
    primary key (id)
) engine=InnoDB;
alter table t_product_images drop index UK_dw7hps4yi7vkr5q0ywek68dfv;
alter table t_product_images
    add constraint UK_dw7hps4yi7vkr5q0ywek68dfv unique (images_id);
alter table t_product_product_category drop index UK_mxs67gvnit6mc2rp7frtol9n;
alter table t_product_product_category
    add constraint UK_mxs67gvnit6mc2rp7frtol9n unique (product_category_id);
alter table t_cart_detail
    add constraint FK16c5ieqp31gs5jr3n7tg9j2xs foreign key (cart_id) references t_cart (id);
alter table t_cart_detail
    add constraint FKdogwrfofmjmdfulefhrfe5krb foreign key (product_id) references t_product (id);
alter table t_cart_detail
    add constraint FKjd9hfckop5au4dd7n5x38lqh6 foreign key (user_id) references t_user (id);
alter table t_order
    add constraint FKho2r4qgj3txpy8964fnla95ub foreign key (user_id) references t_user (id);
alter table t_order_detail
    add constraint FKd908s6mjmbqkgq3lnoajfrxrf foreign key (cart_id) references t_cart (id);
alter table t_order_detail
    add constraint FKrldcrqix0q1dx0mrmlm96exi7 foreign key (order_id) references t_order (id);
alter table t_product_images
    add constraint FKnj7f87i5qsm2q43yokm51mtwh foreign key (images_id) references t_media_file (id);
alter table t_product_images
    add constraint FKos4qfdy8f40cp5bch2jxceyw4 foreign key (product_id) references t_product (id);
alter table t_product_product_category
    add constraint FK3nuahyma5o09jojiro813nubh foreign key (product_category_id) references t_category (id);
alter table t_product_product_category
    add constraint FKkmyyj27sdx0sm3eytahopesju foreign key (product_id) references t_product (id);
alter table t_role_to_user
    add constraint FK2sov3dedjriy6ymva9dsgufi6 foreign key (role_id) references role (id);
alter table t_role_to_user
    add constraint FKl5ag3nae5x1w7vw7psq49qwk9 foreign key (user_id) references t_user (id);
alter table t_user
    add constraint FKce7etvrq1o5ynjtwv549vrmu3 foreign key (profile_id) references t_user_profile (id);
alter table t_user_profile
    add constraint FKhmtiak0xg1eojt8bpel5yhbvg foreign key (avatar_id) references t_media_file (id);
