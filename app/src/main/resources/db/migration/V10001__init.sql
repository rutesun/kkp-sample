CREATE TABLE `user`
(
    `id`         bigint(20)                          NOT NULL AUTO_INCREMENT,
    `name`       varchar(50) COLLATE utf8_unicode_ci NOT NULL,
    `created_at` datetime                            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성시간',
    `updated_At` datetime                            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정시간',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

CREATE TABLE `chat_room`
(
    `id`         bigint(20) NOT NULL AUTO_INCREMENT,
    `title`      varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
    `created_at` datetime   NOT NULL                  DEFAULT CURRENT_TIMESTAMP COMMENT '생성시간',
    `updated_At` datetime   NOT NULL                  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정시간',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

CREATE TABLE `user_chat_room`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT,
    `chat_room_id` bigint(20)          DEFAULT NULL,
    `user_id`      bigint(20)          DEFAULT NULL,
    `created_at`   datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성시간',
    `updated_At`   datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정시간',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uidx_user_id_chat_room_id` (`user_id`, `chat_room_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

CREATE TABLE `money_distribution`
(
    `id`           bigint(20)                          NOT NULL AUTO_INCREMENT,
    `token`        varchar(10) COLLATE utf8_unicode_ci NOT NULL,
    `total_amount` bigint(20)                          NOT NULL,
    `chat_room_id` bigint(20)                          NOT NULL,
    `status`       varchar(20) COLLATE utf8_unicode_ci NOT NULL,
    `creator_id`   bigint(20)                          NOT NULL,
    `closed_at`    timestamp                           NOT NULL,
    `created_at`   datetime                            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성시간',
    `updated_At`   datetime                            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정시간',
    PRIMARY KEY (`id`),
    UNIQUE KEY `money_distribution_token_uindex` (`token`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

CREATE TABLE `distribution_item`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT,
    `receiver_id`     bigint(20)          DEFAULT NULL,
    `amount`          bigint(20) NOT NULL,
    `used`            tinyint(1) NOT NULL,
    `distribution_id` bigint(20) NOT NULL,
    `created_at`      datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성시간',
    `updated_At`      datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정시간',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uidx_distribution_id_receiver_id` (`distribution_id`, `receiver_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;
