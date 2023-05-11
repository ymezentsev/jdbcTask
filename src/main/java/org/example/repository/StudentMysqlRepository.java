package org.example.repository;

import org.example.domain.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StudentMysqlRepository implements StudentRepository {
    private static final String SELECT_ALL_STUDENTS = "select * from students";
    private static final String SELECT_STUDENT_BY_ID = "select * from students where id=?";
    private static final String INSERT_STUDENT = "insert into students (name, age, group_id) VALUES (?, ?, ?)";


    private Connection getConnection() throws SQLException {
        ResourceBundle resource = ResourceBundle.getBundle("database");
        String dbName = resource.getString("db.name");
        String dbUrl = resource.getString("db.url");
        String dbUser = resource.getString("db.user");
        String dbPassword = resource.getString("db.password");

        return DriverManager.getConnection(dbUrl + dbName, dbUser, dbPassword);
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_STUDENTS)) {

            while (resultSet.next()) {
                students.add(buildStudent(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return students;
    }

    private Student buildStudent(ResultSet resultSet) throws SQLException {
        Student student = Student.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .age(resultSet.getInt("age"))
                .groupId(resultSet.getInt("group_id"))
                .build();
        return student;
    }

    @Override
    public Student findStudentById(int id) {
        Student student = null;
        ResultSet resultSet = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_STUDENT_BY_ID)) {

            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                student = buildStudent(resultSet);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeResultSet(resultSet);
        }
        return student;
    }

    private static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void saveStudent(Student student) {
        ResultSet resultSet = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STUDENT)) {

            preparedStatement.setString(1, student.getName());
            preparedStatement.setInt(2, student.getAge());
            preparedStatement.setInt(3, student.getGroupId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeResultSet(resultSet);
        }
    }
}
