# 🎓 Student Record Management System

A JavaFX + JDBC + PostgreSQL CRUD application for managing student records.

## Tech Stack
- **Java 17**
- **JavaFX 21** (UI)
- **PostgreSQL** (Database)
- **Maven** (Build tool)

---

## 📁 Project Structure

```
StudentManagementSystem/
├── database/
│   └── setup.sql              ← Run this in PostgreSQL first
├── src/main/
│   ├── java/com/studentms/
│   │   ├── MainApp.java        ← Entry point
│   │   ├── Controller.java     ← FXML Controller (CRUD logic)
│   │   ├── DBConnection.java   ← PostgreSQL connection
│   │   ├── Student.java        ← Model (TableView binding)
│   │   └── YearLevel.java      ← Enum for year levels
│   └── resources/
│       ├── com/studentms/
│       │   └── main.fxml       ← UI Layout
│       └── css/
│           └── style.css       ← Styling
└── pom.xml                     ← Maven dependencies
```

---


## ✨ Features

| Feature | Description |
|---|---|
| ➕ Add | Insert a new student record |
| ✏️ Update | Edit selected student (click row first) |
| 🗑️ Delete | Remove selected student (with confirmation) |
| 🔄 Clear | Reset input fields |
| 📋 Table | Live view of all records |
| ✅ Validation | Prevents empty field submissions |

---

## 📸 Preview

The app opens a form with:
- Name, Course, Year Level inputs
- Add / Update / Delete / Clear buttons
- A table showing all student records

---

## 🔧 Requirements

- Java 17+
- Maven 3.8+
- PostgreSQL 12+
