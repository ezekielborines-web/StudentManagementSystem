package com.studentms;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Database connection helper.
 *
 * ⚠️  Change DB_URL, DB_USER, and DB_PASS to match your PostgreSQL setup.
 *     Or set environment variables:
 *       DB_URL  = jdbc:postgresql://localhost:5432/studentdb
 *       DB_USER = postgres
 *       DB_PASS = your_password
 */
public class DBConnection {

    private static final String DB_URL  = System.getenv("DB_URL")  != null
            ? System.getenv("DB_URL")
            : "jdbc:postgresql://localhost:5432/studentdb";

    private static final String DB_USER = System.getenv("DB_USER") != null
            ? System.getenv("DB_USER")
            : "postgres";

    private static final String DB_PASS = System.getenv("DB_PASS") != null
            ? System.getenv("DB_PASS")
            : "your_password";   // <-- change this

    public static Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (Exception e) {
            System.err.println("[DBConnection] Failed to connect: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
