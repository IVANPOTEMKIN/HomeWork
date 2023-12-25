CREATE TABLE faculty_example(
id BIGSERIAL PRIMARY KEY,
name TEXT UNIQUE NOT NULL,
color TEXT UNIQUE NOT NULL
);

ALTER TABLE faculty_example
ADD CONSTRAINT name_color_unique UNIQUE (name, color);

CREATE TABLE student_example(
id BIGSERIAL PRIMARY KEY,
name TEXT UNIQUE NOT NULL,
age INTEGER CHECK (age >= 16) DEFAULT '20',
faculty_id BIGSERIAL REFERENCES faculty_example (id)
);