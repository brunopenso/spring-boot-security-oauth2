# spring-boot-security-oauth2
This code is based on the tutorial given by DEVGLAN -> https://www.devglan.com/spring-security/spring-boot-security-oauth2-example

### Query to create some users and table on DB
```
CREATE TABLE userdb
(
    id bigint NOT NULL,
    username character varying(255) NOT NULL,
    pass character varying(255) NULL,
	phone character varying(25) NULL,
    created_at timestamp not null,
    last_login timestamp NULL,
    update_at timestamp NULL,
    CONSTRAINT userdb_pkey PRIMARY KEY (id)
)
```
```
INSERT INTO Userdb (id, username, pass, phone, created_at) VALUES (1, 'Alex123', '$2a$04$I9Q2sDc4QGGg5WNTLmsz0.fvGv3OjoZyj81PrSFyGOqMphqfS2qKu', '+5541999999999', '2018-07-30 10:00:00');
INSERT INTO Userdb (id, username, pass, phone, created_at) VALUES (2, 'Tom234', '$2a$04$I9Q2sDc4QGGg5WNTLmsz0.fvGv3OjoZyj81PrSFyGOqMphqfS2qKu', '+5541999999999', '2018-07-30 10:00:00');
INSERT INTO Userdb (id, username, pass, phone, created_at) VALUES (3, 'Adam', '$2a$04$I9Q2sDc4QGGg5WNTLmsz0.fvGv3OjoZyj81PrSFyGOqMphqfS2qKu', '+5541999999999', '2018-07-30 10:00:00');
```
## Objectives:
1. Convert to Spring boot 2.0 latest version - DONE
2. Avoid to use the same refresh token always - DONE
3. Token store goes to database - DONE
4. Add postman/soapui collection with all working - DONE
5. Change token generation to JWT
6. Revoke refresh tokens - DONE
7. Enable client_id and client_secret DB configuration - DONE
8. Move access_token from query string to http header input - ALMOST
9. Revoke tokens - DONE
10. Migrate database access to a new way
11. Remote token services for token validation - DONE
12. Understand what is the AUTHORITY, hasHole and security definitions
13. Secure public API at least with a client_id

### Token Store goes to database
Create tables
```
create table if not exists oauth_access_token (
  token_id VARCHAR(255),
  token BYTEA,
  authentication_id VARCHAR(255) PRIMARY KEY,
  user_name VARCHAR(255),
  client_id VARCHAR(255),
  authentication BYTEA,
  refresh_token VARCHAR(255)
);
create table if not exists oauth_refresh_token (
  token_id VARCHAR(255),
  token BYTEA,
  authentication BYTEA
);
```
### Enable client_id and client_secret DB configuration
Create table
```
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
```
Details of the table
```
client_id – to store the id of newly registered clients
client_secret – to store the password of clients -> ATTENTION: BEcause of spring boot 2 the secret should be BCrypted
access_token_validity – which indicates if client is still valid
authorities – to indicate what roles are permitted with particular client
scope – allowed actions, for example writing statuses on Facebook etc.
authorized_grant_types, which provides information how users can login to the particular client (in our example case it’s a form login with password)
```
Insert a client_detail
```
INSERT INTO oauth_client_details
    (client_id, client_secret, scope, authorized_grant_types,
    web_server_redirect_uri, authorities, access_token_validity,
    refresh_token_validity, additional_information, autoapprove)
VALUES
    ('devglan-client', '$2a$04$e/c1/RfsWuThaWFCrcCuJeoyvwCV0URN/6Pn9ZFlrtIWaU/vj/BfG', 'password,authorization_code,refresh_token,implicit', 'read,write,trust',null, null, 36000, 360000, null, 'true');
```

### Add postman/soapui collection with all working
Postman is a good tool to work with API, but to create more complex situation I prefer to use soap ui as a nice and productivity tool.

See file spring-boot-tests-soapui-project.xml that run on Soap Ui version 4 or newer

### Move access_token from query string to http header input
Spring framework accepts access_token on query string and header.
On http header you should use the Authorization: Bearer <Token>

I will look forward on how to accept only on http header

### Revoke tokens 
Added TokenController to handle revoke actions

### Remote token services for token validation
Configured Authorization Server to enable check_token endpoint to validate if a token is still valid

## Notes
- The annotation @EnableJpaAuditing is used to handle the created and update values of bean User, and works together with @EntityListeners(AuditingEntityListener.class)
