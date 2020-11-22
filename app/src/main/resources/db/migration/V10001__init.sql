CREATE TABLE `user`
(
    `id`           bigint(20)                          NOT NULL AUTO_INCREMENT,
    `name`         varchar(50) COLLATE utf8_unicode_ci NOT NULL,
    `chat_room_id` bigint(20)                               DEFAULT NULL,
    `created_at`   timestamp                           NULL DEFAULT NULL,
    `updated_at`   timestamp                           NULL DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

CREATE TABLE `chat_room`
(
    `id`         bigint(20) NOT NULL AUTO_INCREMENT,
    `title`      varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
    `creator_id` bigint(20) NOT NULL,
    `created_at` timestamp  NULL                      DEFAULT NULL,
    `updated_at` timestamp  NULL                      DEFAULT NULL,
    PRIMARY KEY (`id`)
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
    `created_at`   timestamp                           NULL DEFAULT NULL,
    `updated_at`   timestamp                           NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `money_distribution_token_uindex` (`token`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

CREATE TABLE `distribution_item`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id`         bigint(20)      DEFAULT NULL,
    `amount`          bigint(20) NOT NULL,
    `used`            tinyint(1) NOT NULL,
    `distribution_id` bigint(20) NOT NULL,
    `created_at`      timestamp  NULL DEFAULT NULL,
    `updated_at`      timestamp  NULL DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;
