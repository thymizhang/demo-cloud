--oauth_client_details表，Spring Security OAuth2使用，用来存储客户端信息
create table oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256)
);
--oauth_code表，Spring Security OAuth2使用，用来存储授权码
create table oauth_code (
  code VARCHAR(256), authentication BLOB
);