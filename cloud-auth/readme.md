##项目介绍
本项目重点包括以下两点内容:
* OAuth2 + Spring Security框架

## 其他说明
* token默认时间
> access_token有效时间为12小时，默认refresh_token有效时间为30天，这两默认值在OAuth2的DefaultTokenServices中定义  

##Spring官方提供的OAuth2表结构
地址: `https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/resources/schema.sql`
```
CREATE TABLE `clientdetails` (
  `appId` varchar(128) NOT NULL,
  `resourceIds` varchar(256) DEFAULT NULL,
  `appSecret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `grantTypes` varchar(256) DEFAULT NULL,
  `redirectUrl` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additionalInformation` varchar(4096) DEFAULT NULL,
  `autoApproveScopes` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`appId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `oauth_access_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication_id` varchar(128) NOT NULL,
  `user_name` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) DEFAULT NULL,
  `authentication` blob,
  `refresh_token` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `oauth_approvals` (
  `userId` varchar(256) DEFAULT NULL,
  `clientId` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `expiresAt` timestamp NULL DEFAULT NULL,
  `lastModifiedAt` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `oauth_client_details` (
  `client_id` varchar(128) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `oauth_client_details` VALUES ('client', NULL, '$2a$10$QlVCrfYGe5E.2ToP3t/LGOZPyX1WCcN6IMSg3vekQTGyqx9Zse7J.', 'app', 'authorization_code', 'http://localhost/auth/redirect', NULL, NULL, NULL, NULL, NULL);

CREATE TABLE `oauth_client_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication_id` varchar(128) NOT NULL,
  `user_name` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `oauth_code` (
  `code` varchar(256) DEFAULT NULL,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `oauth_refresh_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

##一个基本的RBAC模型
```
CREATE TABLE `tb_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父权限',
  `name` varchar(64) NOT NULL COMMENT '权限名称',
  `enname` varchar(64) NOT NULL COMMENT '权限英文名称',
  `url` varchar(255) NOT NULL COMMENT '授权路径',
  `description` varchar(200) DEFAULT NULL COMMENT '备注',
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='权限表';
INSERT INTO `tb_permission` VALUES (1, NULL, '系统管理', 'System', '/', NULL, '2020-06-02 11:43:51', '2020-06-02 11:43:54');
INSERT INTO `tb_permission` VALUES (2, 1, '用户管理', 'SystemUser', '/user/', NULL, '2020-06-02 11:54:18', '2020-06-02 11:54:22');
INSERT INTO `tb_permission` VALUES (3, 2, '查看用户', 'SystemUserView', '/user/view', NULL, '2020-06-02 11:55:21', '2020-06-02 11:55:25');
INSERT INTO `tb_permission` VALUES (4, 2, '编辑用户', 'SystemUserUpdate', '/user/update', NULL, '2020-06-02 11:56:36', '2020-06-02 11:56:38');

CREATE TABLE `tb_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父角色',
  `name` varchar(64) NOT NULL COMMENT '角色名称',
  `enname` varchar(64) NOT NULL COMMENT '角色英文名称',
  `description` varchar(200) DEFAULT NULL COMMENT '备注',
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='角色表';
INSERT INTO `tb_role` VALUES (1, NULL, '超级管理员', 'admin', NULL, '2020-06-02 12:01:47', '2020-06-02 12:01:49');

CREATE TABLE `tb_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL COMMENT '角色 ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限 ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='角色权限表';
INSERT INTO `tb_role_permission` VALUES (1, 1, 1);
INSERT INTO `tb_role_permission` VALUES (2, 1, 2);
INSERT INTO `tb_role_permission` VALUES (3, 1, 3);
INSERT INTO `tb_role_permission` VALUES (4, 1, 4);

CREATE TABLE `tb_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码，加密存储',
  `phone` varchar(20) DEFAULT NULL COMMENT '注册手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '注册邮箱',
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`) USING BTREE,
  UNIQUE KEY `phone` (`phone`) USING BTREE,
  UNIQUE KEY `email` (`email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户表';
INSERT INTO `tb_user` VALUES (1, 'thymi', '$2a$10$.90vZUC9rmKzc7tMw/ufdOFggMop65bLCFzBzFYP5hnlfzunXsszG', '18675887158', '7270303@qq.com', '2020-06-02 11:40:22', '2020-06-02 11:40:24');

CREATE TABLE `tb_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户 ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色 ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户角色表';
INSERT INTO `tb_user_role` VALUES (1, 1, 1);
```

## jwt非对称加密方法
* 在命令行生成密钥和公钥  
>1 `keytool -genkeypair -alias gceasy -keyalg RSA -keypass gceasy -keystore gceasy.jks -storepass gceasy`  
>2 `keytool -importkeystore -srckeystore gceasy.jks -destkeystore gceasy.jks -deststoretype pkcs12`  
>3 生成完成后将jks文件拷贝到resources文件夹下    
* 在命令行生成公钥   
>1 `keytool -list -rfc --keystore gceasy.jks | openssl x509 -inform pem -pubkey`  
>2 在resources下新建文件pubkey.txt, 复制"Begin --- End"之间的内容到文件(含)  
* 在认证服务器的jwt转换器设置加密  
>未完待续...
