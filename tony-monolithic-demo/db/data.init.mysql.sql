INSERT INTO sys_employee (employee_id, account, real_name, employee_mobile, pwd, salt, account_state, creator_id, creator_name) VALUES ('ADMIN', 'admin', '超级管理员', '13984842424', '68846F643A26BD47B1ECE1BFA7BDAECF', 'TONY_SALT',1,'ADMIN','超级管理员');
INSERT INTO sys_role (role_id, role_name, role_code, build_in, creator_id, creator_name) VALUES ('ADMIN', '超级管理员', 'ADMIN', 1,  'ADMIN', '超级管理员');
INSERT INTO sys_employee_role(employee_id, role_id) VALUES ('ADMIN','ADMIN');

INSERT INTO sys_dict_type (dict_type_id, app_id, dict_type_code, dict_type_code_seq, dict_type_name, build_in, creator_id, creator_name) VALUES ('STATE',  '', 'STATE', 'STATE', '状态', 1,    'ADMIN', '超级管理员');
INSERT INTO sys_dict_type (dict_type_id, app_id, dict_type_code, dict_type_code_seq, dict_type_name, build_in, creator_id, creator_name) VALUES ('YES_OR_NO',  '', 'YES_OR_NO', 'YES_OR_NO', '是否', 1,   'ADMIN', '超级管理员');

INSERT INTO sys_dict (dict_id, dict_type_id, dict_name, dict_code, dict_value, dict_meta, build_in, creator_id, creator_name) VALUES ('STATE.DISABLED', 'STATE', '禁用', 'DISABLED', 'false', '{"color": "error", "valueType": "Boolean"}', 1,   'ADMIN', '超级管理员');
INSERT INTO sys_dict (dict_id, dict_type_id, dict_name, dict_code, dict_value, dict_meta, build_in, creator_id, creator_name) VALUES ('STATE.ENABLED', 'STATE', '启用', 'ENABLED', 'true', '{"color": "success", "valueType": "Boolean"}', 1,   'ADMIN', '超级管理员');
INSERT INTO sys_dict (dict_id, dict_type_id, dict_name, dict_code, dict_value, dict_meta, build_in, creator_id, creator_name) VALUES ('YES_OR_NO.NO', 'YES_OR_NO', '否', 'FALSE', 'false', '{"color": "error", "valueType": "Boolean"}', 1,  'ADMIN', '超级管理员');
INSERT INTO sys_dict (dict_id, dict_type_id, dict_name, dict_code, dict_value, dict_meta, build_in, creator_id, creator_name) VALUES ('YES_OR_NO.YES', 'YES_OR_NO', '是', 'TRUE', 'true', '{"color": "success", "valueType": "Boolean"}', 1,  'ADMIN', '超级管理员');
