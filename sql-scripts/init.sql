CREATE TABLE IF NOT EXISTS department (
    id SERIAL NOT NULL,
    name VARCHAR(255),
    description VARCHAR(255),
    organization_id INTEGER,
    created_at TIMESTAMP(6),
    created_by VARCHAR(255),
    updated_at TIMESTAMP(6),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP(6),
    deleted_by VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS designation (
    id SERIAL NOT NULL,
    name VARCHAR(255),
    description VARCHAR(255),
    organization_id INTEGER,
    created_at TIMESTAMP(6),
    created_by VARCHAR(255),
    updated_at TIMESTAMP(6),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP(6),
    deleted_by VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS feature (
    id SERIAL NOT NULL,
    name VARCHAR(255),
    display_name VARCHAR(255),
    created_at TIMESTAMP(6),
    created_by VARCHAR(255),
    updated_at TIMESTAMP(6),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP(6),
    deleted_by VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS feature_role (
    feature_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    PRIMARY KEY (feature_id, role_id)
);

CREATE TABLE IF NOT EXISTS holiday (
    id SERIAL NOT NULL,
    organization_id INTEGER,
    holiday_date TIMESTAMP(6),
    holiday_description VARCHAR(255),
    holiday_name VARCHAR(255),
    created_at TIMESTAMP(6),
    created_by VARCHAR(255),
    updated_at TIMESTAMP(6),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP(6),
    deleted_by VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS leaves (
    id SERIAL NOT NULL,
    approved_leaves FLOAT CHECK ((approved_leaves>=0) AND (approved_leaves<=50)),
    available_paid_leaves FLOAT CHECK ((available_paid_leaves>=0) AND (available_paid_leaves<=50)),
    available_unpaid_leaves FLOAT CHECK ((available_unpaid_leaves>=0) AND (available_unpaid_leaves<=50)),
    available_sick_leaves FLOAT CHECK ((available_sick_leaves>=0) AND (available_sick_leaves<=50)),
    available_vacation_leaves FLOAT CHECK ((available_vacation_leaves>=0) AND (available_vacation_leaves<=50)),
    booked_leaves FLOAT CHECK ((booked_leaves>=0) AND (booked_leaves<=50)),
    total_leaves FLOAT CHECK ((total_leaves>=0) AND (total_leaves<=50)),
    user_id INTEGER UNIQUE,
    created_at TIMESTAMP(6),
    created_by VARCHAR(255),
    updated_at TIMESTAMP(6),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP(6),
    deleted_by VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS leaves_system (
    id SERIAL NOT NULL,
    accrual FLOAT CHECK ((accrual>=0) AND (accrual<=5)),
    carry_over_limits FLOAT CHECK ((carry_over_limits>=0) AND (carry_over_limits<=50)),
    consecutive_leaves FLOAT CHECK ((consecutive_leaves>=0) AND (consecutive_leaves<=50)),
    designation INTEGER UNIQUE,
    organization INTEGER,
    allowed_leave_durations VARCHAR(255) ARRAY,
    allowed_leave_types VARCHAR(255) ARRAY,
    created_at TIMESTAMP(6),
    created_by VARCHAR(255),
    updated_at TIMESTAMP(6),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP(6),
    deleted_by VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS leaves_tracker (
    id SERIAL NOT NULL,
    approved_end_date DATE,
    approved_start_date DATE,
    end_date DATE,
    start_date DATE,
    is_approved BOOLEAN,
    is_cancelled BOOLEAN,
    no_of_days FLOAT,
    reporting_manager INTEGER,
    user_id INTEGER,
    comment VARCHAR(255),
    note VARCHAR(255),
    reason VARCHAR(255),
    leave_cancel_reason VARCHAR(255),
    type_of_leave VARCHAR(255),
    approved_leave_breakups JSONB,
    leave_breakups JSONB,
    created_at TIMESTAMP(6),
    created_by VARCHAR(255),
    updated_at TIMESTAMP(6),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP(6),
    deleted_by VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS organization (
    id SERIAL NOT NULL,
    name VARCHAR(255),
    address VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    country VARCHAR(255),
    created_at TIMESTAMP(6),
    created_by VARCHAR(255),
    updated_at TIMESTAMP(6),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP(6),
    deleted_by VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_organization_role (
    id SERIAL NOT NULL,
    user_id INTEGER,
    organization_id INTEGER,
    role_id INTEGER,
    created_at TIMESTAMP(6),
    created_by VARCHAR(255),
    updated_at TIMESTAMP(6),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP(6),
    deleted_by VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_profile (
    id SERIAL NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    gender VARCHAR(255),
    work_phone_number VARCHAR(255),
    personal_phone_number VARCHAR(255),
    aadhar_card VARCHAR(255),
    pan_card VARCHAR(255),
    language VARCHAR(255),
    monthly_ctc VARCHAR(255),
    date_of_birth DATE,
    created_at TIMESTAMP(6),
    created_by VARCHAR(255),
    updated_at TIMESTAMP(6),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP(6),
    deleted_by VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_role (
    id SERIAL NOT NULL,
    name VARCHAR(255),
    organization INTEGER,
    created_at TIMESTAMP(6),
    created_by VARCHAR(255),
    updated_at TIMESTAMP(6),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP(6),
    deleted_by VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    username VARCHAR(255),
    password VARCHAR(255),
    password_reset_token VARCHAR(255),
    active_organization INTEGER,
    department INTEGER,
    designation INTEGER,
    organization INTEGER,
    user_profile INTEGER UNIQUE,
    reporting_manager INTEGER,
    google_id VARCHAR(255),
    outlook_id VARCHAR(255),
    created_at TIMESTAMP(6),
    created_by VARCHAR(255),
    updated_at TIMESTAMP(6),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP(6),
    deleted_by VARCHAR(255),
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS department ADD CONSTRAINT FK_department_organization FOREIGN KEY (organization_id) REFERENCES organization;

ALTER TABLE IF EXISTS designation ADD CONSTRAINT FK_designation_organization FOREIGN KEY (organization_id) REFERENCES organization;

ALTER TABLE IF EXISTS feature_role ADD CONSTRAINT FK_feature_role_user_role FOREIGN KEY (role_id) REFERENCES user_role;

ALTER TABLE IF EXISTS feature_role ADD CONSTRAINT FK_feature_role_feature FOREIGN KEY (feature_id) REFERENCES feature;

ALTER TABLE IF EXISTS holiday ADD CONSTRAINT FK_holiday_organization FOREIGN KEY (organization_id) REFERENCES organization;

ALTER TABLE IF EXISTS leaves ADD CONSTRAINT FK_leaves_user FOREIGN KEY (user_id) REFERENCES users;

ALTER TABLE IF EXISTS leaves_system ADD CONSTRAINT FK_leavesSys_designation FOREIGN KEY (designation) REFERENCES designation;

ALTER TABLE IF EXISTS leaves_system ADD CONSTRAINT FK_leavesSys_organization FOREIGN KEY (organization) REFERENCES organization;

ALTER TABLE IF EXISTS user_organization_role ADD CONSTRAINT FK_user_org_role_org FOREIGN KEY (organization_id) REFERENCES organization;

ALTER TABLE IF EXISTS user_organization_role ADD CONSTRAINT FK_user_org_role_role FOREIGN KEY (role_id) REFERENCES user_role ON DELETE CASCADE;

ALTER TABLE IF EXISTS user_organization_role ADD CONSTRAINT FK_user_org_role_users FOREIGN KEY (user_id) REFERENCES users;

ALTER TABLE IF EXISTS user_role ADD CONSTRAINT FK_user_role_org FOREIGN KEY (organization) REFERENCES organization;

ALTER TABLE IF EXISTS users ADD CONSTRAINT FK_user_dept FOREIGN KEY (department) REFERENCES department;

ALTER TABLE IF EXISTS users ADD CONSTRAINT FK_user_desg FOREIGN KEY (designation) REFERENCES designation;

ALTER TABLE IF EXISTS users ADD CONSTRAINT FK_user_org FOREIGN KEY (organization) REFERENCES organization;

ALTER TABLE IF EXISTS users ADD CONSTRAINT FK_user_rm FOREIGN KEY (reporting_manager) REFERENCES users;

ALTER TABLE IF EXISTS users ADD CONSTRAINT FK_user_profile FOREIGN KEY (user_profile) REFERENCES user_profile;

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