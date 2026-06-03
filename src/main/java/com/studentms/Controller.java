package com.studentms;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;

public class Controller {

    // --- FXML Fields ---
    @FXML private TextField             txtName;
    @FXML private TextField             txtCourse;
    @FXML private ChoiceBox<YearLevel>  cbYear;

    @FXML private TableView<Student>              table;
    @FXML private TableColumn<Student, Integer>   colId;
    @FXML private TableColumn<Student, String>    colName;
    @FXML private TableColumn<Student, String>    colCourse;
    @FXML private TableColumn<Student, String>    colYear;

    @FXML private Label lblStatus;

    // --- State ---
    private final ObservableList<Student> list = FXCollections.observableArrayList();
    private Connection conn;
    private int selectedId = -1;

    // ---------------------------------------------------------------
    //  Initialization
    // ---------------------------------------------------------------
    @FXML
    public void initialize() {
        conn = DBConnection.connect();

        if (conn == null) {
            showAlert(AlertType.ERROR, "Database Error",
                    "Could not connect to PostgreSQL.\nCheck DBConnection.java settings.");
            return;
        }

        // Populate ChoiceBox with enum values
        cbYear.getItems().setAll(YearLevel.values());

        // Bind table columns to model properties
        colId.setCellValueFactory(data     -> data.getValue().idProperty().asObject());
        colName.setCellValueFactory(data   -> data.getValue().nameProperty());
        colCourse.setCellValueFactory(data -> data.getValue().courseProperty());
        colYear.setCellValueFactory(data   -> data.getValue().yearLevelProperty());

        loadData();

        // Row click → populate fields
        table.setOnMouseClicked(e -> {
            Student s = table.getSelectionModel().getSelectedItem();
            if (s != null) {
                selectedId = s.getId();
                txtName.setText(s.getName());
                txtCourse.setText(s.getCourse());
                cbYear.setValue(YearLevel.fromString(s.getYearLevel()));
            }
        });
    }

    // ---------------------------------------------------------------
    //  CRUD Operations
    // ---------------------------------------------------------------

    @FXML
    private void addStudent() {
        if (!validateInputs()) return;

        try {
            String sql = "INSERT INTO students(name, course, year_level) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtName.getText().trim());
            pst.setString(2, txtCourse.getText().trim());
            pst.setString(3, cbYear.getValue().toString());
            pst.executeUpdate();

            setStatus("✅ Student added successfully!");
            loadData();
            clearFields();
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Add Error", e.getMessage());
        }
    }

    @FXML
    private void updateStudent() {
        if (selectedId == -1) {
            showAlert(AlertType.WARNING, "No Selection", "Please select a student from the table first.");
            return;
        }
        if (!validateInputs()) return;

        try {
            String sql = "UPDATE students SET name=?, course=?, year_level=? WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtName.getText().trim());
            pst.setString(2, txtCourse.getText().trim());
            pst.setString(3, cbYear.getValue().toString());
            pst.setInt(4, selectedId);
            pst.executeUpdate();

            setStatus("✏️ Student updated successfully!");
            loadData();
            clearFields();
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Update Error", e.getMessage());
        }
    }

    @FXML
    private void deleteStudent() {
        if (selectedId == -1) {
            showAlert(AlertType.WARNING, "No Selection", "Please select a student from the table first.");
            return;
        }

        // Confirm before deleting
        Alert confirm = new Alert(AlertType.CONFIRMATION,
                "Are you sure you want to delete this student?",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirm Delete");
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    String sql = "DELETE FROM students WHERE id=?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setInt(1, selectedId);
                    pst.executeUpdate();

                    setStatus("🗑️ Student deleted.");
                    loadData();
                    clearFields();
                } catch (SQLException e) {
                    showAlert(AlertType.ERROR, "Delete Error", e.getMessage());
                }
            }
        });
    }

    @FXML
    private void clearFields() {
        txtName.clear();
        txtCourse.clear();
        cbYear.setValue(null);
        selectedId = -1;
        table.getSelectionModel().clearSelection();
        setStatus("Fields cleared.");
    }

    // ---------------------------------------------------------------
    //  Helpers
    // ---------------------------------------------------------------

    private void loadData() {
        list.clear();
        try {
            String sql = "SELECT * FROM students ORDER BY id";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course"),
                        rs.getString("year_level")
                ));
            }
            table.setItems(list);
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Load Error", e.getMessage());
        }
    }

    private boolean validateInputs() {
        if (txtName.getText().trim().isEmpty()) {
            showAlert(AlertType.WARNING, "Validation Error", "Name cannot be empty.");
            return false;
        }
        if (txtCourse.getText().trim().isEmpty()) {
            showAlert(AlertType.WARNING, "Validation Error", "Course cannot be empty.");
            return false;
        }
        if (cbYear.getValue() == null) {
            showAlert(AlertType.WARNING, "Validation Error", "Please select a Year Level.");
            return false;
        }
        return true;
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setStatus(String message) {
        if (lblStatus != null) lblStatus.setText(message);
    }
}
