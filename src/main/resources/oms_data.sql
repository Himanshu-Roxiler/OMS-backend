INSERT INTO user_role ("name") VALUES ('admin');

INSERT INTO feature ("name","display_name") VALUES ('admin','admin');
INSERT INTO feature ("name","display_name") VALUES ('users','users');
INSERT INTO feature ("name","display_name") VALUES ('department','department');
INSERT INTO feature ("name","display_name") VALUES ('designation','designation');
INSERT INTO feature ("name","display_name") VALUES ('roles','roles');
INSERT INTO feature ("name","display_name") VALUES ('profile','profile');
INSERT INTO feature ("name","display_name") VALUES ('leave','leave');
INSERT INTO feature ("name","display_name") VALUES ('leave_policy','leave_policy');

INSERT INTO "feature_role" (feature_id, role_id) VALUES (1, 1);
INSERT INTO "feature_role" (feature_id, role_id) VALUES (2, 1);
INSERT INTO "feature_role" (feature_id, role_id) VALUES (3, 1);
INSERT INTO "feature_role" (feature_id, role_id) VALUES (4, 1);
INSERT INTO "feature_role" (feature_id, role_id) VALUES (5, 1);
INSERT INTO "feature_role" (feature_id, role_id) VALUES (6, 1);
INSERT INTO "feature_role" (feature_id, role_id) VALUES (7, 1);
INSERT INTO "feature_role" (feature_id, role_id) VALUES (8, 1);