insert into users ( username ) values ('admin');

insert into groups ( name ) values ('Administrators');

insert into groups_users ( group_id, user_id ) values ('Administrators','admin');