insert into pat_users ( username, password, enabled ) values ('admin', 'admin', 1);

insert into pat_groups ( name ) values ('Administrators');
insert into pat_groups ( name ) values ('Users');

insert into pat_connections ( id, name, driverClassName, password, type, url, username, connectonstartup ) values ('1111-1111-1111-1111', 'administrator_connection', 'driver_name', 'password', 'aced00057372002f6f72672e70656e7461686f2e7061742e7365727665722e646174612e706f6a6f2e436f6e6e656374696f6e5479706500000000000000010200014c00046e616d657400124c6a6176612f6c616e672f537472696e673b78707400084d6f6e647269616e', 'url', 'username', 'false');

insert into pat_groups_users ( group_id, user_id ) values ('Administrators','admin');
insert into pat_groups_users ( group_id, user_id ) values ('Users','admin');

insert into pat_users_connections (user_id,connection_id) values ('admin','1111-1111-1111-1111');