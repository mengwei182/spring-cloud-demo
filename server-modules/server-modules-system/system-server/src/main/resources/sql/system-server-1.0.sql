DROP TABLE IF EXISTS `spring_cloud_demo`.`user`;
CREATE TABLE `spring_cloud_demo`.`user`
(
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(255) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `icon` VARCHAR(255) COMMENT '头像',
    `phone` VARCHAR(11) COMMENT '手机号',
    `email` VARCHAR(255) COMMENT '邮箱',
    `name` VARCHAR(32) COMMENT '姓名',
    `description` VARCHAR(255) COMMENT '描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
    `login_time` TIMESTAMP COMMENT '登录时间',
    `verify_status` INT DEFAULT 0 COMMENT '登录验证状态',
    `public_key` VARCHAR(1024) COMMENT '公钥',
    `private_key` VARCHAR(1024) COMMENT '私钥',
    `token_expire_time` TEXT COMMENT 'token过期时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
    `creator` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '创建者id',
    `updater` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '更新者id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) COMMENT '用户信息表';
INSERT INTO `spring_cloud_demo`.`user`(`id`, `username`, `password`, `email`, `name`)
VALUES (1, 'admin', '$2a$10$P75kwF3AjIXvnsPsCxOxFe/sA/Puhvc8tzJnmKIDt8UhFRMBeZQbK', 'admin@test.com', '超级管理员');

DROP TABLE IF EXISTS `spring_cloud_demo`.`role`;
CREATE TABLE `spring_cloud_demo`.`role`
(
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL COMMENT '角色名称',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
    `description` VARCHAR(255) COMMENT '描述',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
    `creator` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '创建者id',
    `updater` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '更新者id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) COMMENT '资源信息表';
INSERT INTO `spring_cloud_demo`.`role`(`id`, `name`, `description`)
VALUES (1, '超级管理员', '超级管理员');

DROP TABLE IF EXISTS `spring_cloud_demo`.`user_role_relation`;
CREATE TABLE `spring_cloud_demo`.`user_role_relation`
(
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `user_id` VARCHAR(255) NOT NULL COMMENT '用户ID',
    `role_id` VARCHAR(255) NOT NULL COMMENT '角色ID',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
    `creator` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '创建者id',
    `updater` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '更新者id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) COMMENT '用户角色信息表';
INSERT INTO `spring_cloud_demo`.`user_role_relation`(`id`, `user_id`, `role_id`)
VALUES (1, 1, 1);

DROP TABLE IF EXISTS `spring_cloud_demo`.`menu`;
CREATE TABLE `spring_cloud_demo`.`menu`
(
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `parent_id` VARCHAR(255) COMMENT '父级id',
    `type` INT NOT NULL DEFAULT 0 COMMENT '类型',
    `id_chain` VARCHAR(768) COMMENT 'id链',
    `level` INT NOT NULL DEFAULT 0 COMMENT '级别',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `name` VARCHAR(255) NOT NULL COMMENT '名称',
    `path` VARCHAR(255) COMMENT '路由地址',
    `component` VARCHAR(255) COMMENT '组件',
    `icon` VARCHAR(255) COMMENT '图标',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
    `hided` TINYINT COMMENT '0显示，1隐藏',
    `description` VARCHAR(255) COMMENT '描述',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
    `creator` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '创建者id',
    `updater` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '更新者id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX (`parent_id`),
    INDEX (`id_chain`)
) COMMENT '菜单信息表';
INSERT INTO `spring_cloud_demo`.`menu`(`id`, `parent_id`, `id_chain`, `name`, `path`, `component`)
VALUES (1, 0, 0, '系统管理', '/system/**', 'systemManager'),
       (2, 1, 0, '菜单管理', '/menu/**', 'systemManager/menuManager'),
       (3, 1, 0, '用户管理', '/user/**', 'systemManager/userManager'),
       (4, 1, 0, '角色管理', '/role/**', 'systemManager/roleManager'),
       (5, 1, 0, '部门管理', '/department/**', 'systemManager/departmentManager');

DROP TABLE IF EXISTS `spring_cloud_demo`.`role_menu_relation`;
CREATE TABLE `spring_cloud_demo`.`role_menu_relation`
(
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `role_id` VARCHAR(255) NOT NULL COMMENT '角色id',
    `menu_id` VARCHAR(255) NOT NULL COMMENT '菜单id',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
    `creator` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '创建者id',
    `updater` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '更新者id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) COMMENT '角色菜单信息表';
INSERT INTO `spring_cloud_demo`.`role_menu_relation`(`id`, `role_id`, `menu_id`)
VALUES (1, 1, 1),
       (2, 1, 2),
       (3, 1, 3),
       (4, 1, 4),
       (5, 1, 5),
       (6, 1, 6);

DROP TABLE IF EXISTS `spring_cloud_demo`.`department`;
CREATE TABLE `spring_cloud_demo`.`department`
(
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `parent_id` VARCHAR(255) COMMENT '父级id',
    `id_chain` VARCHAR(768) COMMENT 'id链',
    `level` INT NOT NULL DEFAULT 0 COMMENT '级别',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `name` VARCHAR(255) NOT NULL COMMENT '名称',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
    `description` VARCHAR(255) COMMENT '描述',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
    `creator` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '创建者id',
    `updater` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '更新者id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX (`parent_id`),
    INDEX (`id_chain`)
) COMMENT '部门信息表';

DROP TABLE IF EXISTS `spring_cloud_demo`.`user_department_relation`;
CREATE TABLE `spring_cloud_demo`.`user_department_relation`
(
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `user_id` VARCHAR(255) NOT NULL COMMENT '用户ID',
    `department_id` VARCHAR(255) NOT NULL COMMENT '部门ID',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
    `creator` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '创建者id',
    `updater` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '更新者id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) COMMENT '用户部门信息表';

DROP TABLE IF EXISTS `spring_cloud_demo`.`dictionary`;
CREATE TABLE `spring_cloud_demo`.`dictionary`
(
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL COMMENT '名称',
    `code` VARCHAR(255) NOT NULL COMMENT '编码',
    `parent_id` VARCHAR(255) COMMENT '父级id',
    `id_chain` VARCHAR(768) COMMENT 'id链',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
    `creator` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '创建者id',
    `updater` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '更新者id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX (`code`),
    INDEX (`parent_id`),
    INDEX (`id_chain`)
) COMMENT '字典信息表';