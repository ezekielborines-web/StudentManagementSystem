-- ============================================================
--  Student Management System — PostgreSQL Setup Script
--  Run this in pgAdmin or psql BEFORE launching the app
-- ============================================================

-- 1. Create the database (run this as superuser / postgres)
CREATE DATABASE studentdb;

-- 2. Connect to studentdb, then run the rest:
\c studentdb

-- 3. Create the students table
CREATE TABLE IF NOT EXISTS students (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    course     VARCHAR(50)  NOT NULL,
    year_level VARCHAR(20)  NOT NULL
);

-- 4. (Optional) Seed some sample data
INSERT INTO students (name, course, year_level) VALUES
    ('Juan dela Cruz',   'BSCS',  '1st Year'),
    ('Maria Santos',     'BSIT',  '2nd Year'),
    ('Jose Reyes',       'BSECE', '3rd Year'),
    ('Ana Magbanua',     'BSCS',  '4th Year');
