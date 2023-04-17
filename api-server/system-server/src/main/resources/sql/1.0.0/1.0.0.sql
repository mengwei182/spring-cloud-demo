DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`
(
    `id` VARCHAR(32) NOT NULL,
    `parent_id` VARCHAR(255) COMMENT '父级id',
    `id_chain` VARCHAR(768) COMMENT 'id链',
    `level` INT NOT NULL DEFAULT 0 COMMENT '级别',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `name` VARCHAR(255) NOT NULL COMMENT '名称',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
    `description` VARCHAR(255) COMMENT '描述',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
    `create_id` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '创建者id',
    `update_id` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '更新者id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX (`parent_id`),
    INDEX (`id_chain`)
) COMMENT '部门信息表';

DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`
(
    `id` VARCHAR(32) NOT NULL,
    `parent_id` VARCHAR(255) COMMENT '父级id',
    `id_chain` VARCHAR(768) COMMENT 'id链',
    `level` INT NOT NULL DEFAULT 0 COMMENT '级别',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `name` VARCHAR(255) NOT NULL COMMENT '名称',
    `route_address` VARCHAR(255) COMMENT '路由地址',
    `icon` VARCHAR(255) COMMENT '图标',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
    `hided` TINYINT COMMENT '0显示，1隐藏',
    `description` VARCHAR(255) COMMENT '描述',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
    `create_id` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '创建者id',
    `update_id` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '更新者id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX (`parent_id`),
    INDEX (`id_chain`)
) COMMENT '菜单信息表';
INSERT INTO menu(`id`, `parent_id`, `id_chain`, `name`, `route_address`)
VALUES ('1', '0', '0', '系统管理', '/systemManager'),
       ('2', '1', '0', '菜单管理', '/systemManager/menuManager'),
       ('3', '1', '0', '用户管理', '/systemManager/userManager'),
       ('4', '1', '0', '角色管理', '/systemManager/roleManager'),
       ('5', '1', '0', '资源管理', '/systemManager/resourceManager'),
       ('6', '1', '0', '部门管理', '/systemManager/departmentManager');

DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource`
(
    `id` VARCHAR(32) NOT NULL,
    `name` VARCHAR(255) COMMENT '资源名称',
    `url` VARCHAR(255) NOT NULL COMMENT '资源URL',
    `category_id` VARCHAR(255) COMMENT '资源分类id',
    `description` VARCHAR(255) COMMENT '描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
    `create_id` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '创建者id',
    `update_id` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '更新者id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) COMMENT '资源信息表';
INSERT INTO resource(`id`, `name`, `url`)
VALUES ('1', '全部资源', '/*/**');

DROP TABLE IF EXISTS `resource_category`;
CREATE TABLE `resource_category`
(
    `id` VARCHAR(32) NOT NULL,
    `name` VARCHAR(255) NOT NULL COMMENT '分类名称',
    `description` VARCHAR(255) COMMENT '描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
    `create_id` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '创建者id',
    `update_id` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '更新者id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) COMMENT '资源分类信息表';
INSERT INTO resource_category(`id`, `name`, `description`)
VALUES ('1', '超级管理员资源', '超级管理员资源');

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id` VARCHAR(32) NOT NULL,
    `name` VARCHAR(255) NOT NULL COMMENT '角色名称',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
    `description` VARCHAR(255) COMMENT '描述',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
    `create_id` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '创建者id',
    `update_id` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '更新者id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) COMMENT '资源信息表';
INSERT INTO role(`id`, `name`, `description`)
VALUES ('1', '超级管理员', '超级管理员');

DROP TABLE IF EXISTS `role_menu_relation`;
CREATE TABLE `role_menu_relation`
(
    `id` VARCHAR(32) NOT NULL,
    `role_id` VARCHAR(255) NOT NULL COMMENT '角色id',
    `menu_id` VARCHAR(255) NOT NULL COMMENT '菜单id',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
    `create_id` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '创建者id',
    `update_id` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '更新者id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) COMMENT '角色菜单信息表';
INSERT INTO role_menu_relation(`id`, `role_id`, `menu_id`)
VALUES ('1', '1', '1'),
       ('2', '1', '2'),
       ('3', '1', '3'),
       ('4', '1', '4'),
       ('5', '1', '5'),
       ('6', '1', '6');

DROP TABLE IF EXISTS `role_resource_relation`;
CREATE TABLE `role_resource_relation`
(
    `id` VARCHAR(32) NOT NULL,
    `role_id` VARCHAR(255) NOT NULL COMMENT '角色id',
    `resource_id` VARCHAR(255) NOT NULL COMMENT '资源id',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
    `create_id` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '创建者id',
    `update_id` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '更新者id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) COMMENT '角色资源信息表';
INSERT INTO role_resource_relation(`id`, `role_id`, `resource_id`)
VALUES ('1', '1', '1');

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id` VARCHAR(32) NOT NULL,
    `username` VARCHAR(255) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `icon` VARCHAR(255) COMMENT '头像',
    `phone` VARCHAR(11) COMMENT '手机号',
    `email` VARCHAR(255) COMMENT '邮箱',
    `name` VARCHAR(32) COMMENT '姓名',
    `department_id` VARCHAR(255) COMMENT '部门ID',
    `description` VARCHAR(255) COMMENT '描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
    `login_time` TIMESTAMP COMMENT '登录时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
    `create_id` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '创建者id',
    `update_id` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '更新者id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) COMMENT '用户信息表';
INSERT INTO `user`(`id`, `username`, `password`, `email`, `name`)
VALUES ('1', 'superadmin', '$2a$10$P75kwF3AjIXvnsPsCxOxFe/sA/Puhvc8tzJnmKIDt8UhFRMBeZQbK', 'superadmin@test.com', '超级管理员');

DROP TABLE IF EXISTS `user_role_relation`;
CREATE TABLE `user_role_relation`
(
    `id` VARCHAR(32) NOT NULL,
    `user_id` VARCHAR(255) NOT NULL COMMENT '用户ID',
    `role_id` VARCHAR(255) NOT NULL COMMENT '角色ID',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
    `create_id` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '创建者id',
    `update_id` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '更新者id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) COMMENT '用户角色信息表';
INSERT INTO user_role_relation(`id`, `user_id`, `role_id`)
VALUES ('1', '1', '1');

DROP TABLE IF EXISTS `dictionary`;
CREATE TABLE `dictionary`
(
    `id` VARCHAR(32) NOT NULL,
    `name` VARCHAR(255) NOT NULL COMMENT '名称',
    `code` VARCHAR(255) NOT NULL COMMENT '编码',
    `parent_id` VARCHAR(255) COMMENT '父级id',
    `id_chain` VARCHAR(768) COMMENT 'id链',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
    `create_id` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '创建者id',
    `update_id` VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '更新者id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX (`code`),
    INDEX (`parent_id`),
    INDEX (`id_chain`)
) COMMENT '字典信息表';