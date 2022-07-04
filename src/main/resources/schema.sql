drop table if exists attached_photo;
drop table if exists point_history;
drop table if exists review;

create table attached_photo
(
    id        bigint not null auto_increment,
    photo_id  BINARY(16) comment '사진 id',
    review_id BINARY(16) comment '리뷰 id',
    position  integer comment '순서',
    primary key (id)
) comment '리뷰 사진';

create table point_history
(
    id              bigint     not null auto_increment,
    type            varchar(255) comment '구분',
    source_id       BINARY(16) not null comment '구분 id',
    add_point       integer    not null comment '기본 포인트',
    add_bonus_point integer    not null comment '보너스 포인트',
    created_at      datetime   not null,
    created_by      BINARY(16) not null,
    updated_at      datetime   not null,
    updated_by      BINARY(16) not null,
    primary key (id)
) comment '포인트 이력';

create table review
(
    review_id  BINARY(16) not null,
    content    varchar(255) comment '내용',
    place_id   BINARY(16) comment '장소 id',
    deleted    bit,
    created_at datetime   not null,
    created_by BINARY(16) not null,
    updated_at datetime   not null,
    updated_by BINARY(16) not null,
    primary key (review_id)
) comment '리뷰';
