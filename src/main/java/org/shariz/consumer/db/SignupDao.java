package org.shariz.consumer.db;

import org.shariz.model.SignupRequest;
import org.shariz.utils.HelperMethods;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Handles all interactions  Postgresql service interactions. Each SQL statement is defined in a file inside resources/sql
 * The SQQL statements are read during instantiation and kept in memory for subsequent database interactions.
 */
public class SignupDao {
    private Connection pgConnection = null;

    private String emailCheckSql = null;
    private String insertSignupRequestSql = null;

    /**
     * Constructor.
     * creates the database connections and reads the SQL statements from the resources/sql folder.
     */
    public SignupDao() {
        final Properties properties = HelperMethods.getPostgresqlProperties();
        try {
            pgConnection = DriverManager.getConnection(properties.getProperty("jdbc.url"), properties);
        } catch (SQLException exception) {
            throw new RuntimeException(exception.getMessage());
        }
        try {
            emailCheckSql = HelperMethods.getSqlFromFile("sql/check_email_exists.sql");
            insertSignupRequestSql = HelperMethods.getSqlFromFile("sql/insert_signup_request.sql");

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException((e));
        }
    }

    /**
     * Checks is a given email is already in registered.
     *
     * @param email
     * @return true in case of email existing in the database, false otherwise.
     * @throws SQLException
     */
    public boolean isAlreadyRegistered(String email) throws SQLException {
        boolean emailExists = false;
        PreparedStatement statement = null;
        try {
            statement = pgConnection.prepareStatement(emailCheckSql);
            statement.setString(1, email);
            ResultSet resultSet = null;
            try {
                resultSet = statement.executeQuery();
                emailExists = resultSet.next();
            } finally {
                if (resultSet != null) resultSet.close();
            }
        } finally {
            if (statement != null) statement.close();
        }
        return emailExists;
    }

    /**
     * Save the signup request in  Postgresql  in the table shariz_challenge.signup_request
     *
     * @param signupRequest
     * @throws SQLException
     */
    public void persistSignupRequest(SignupRequest signupRequest) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement = pgConnection.prepareStatement(insertSignupRequestSql);
            statement.setString(1, signupRequest.getFirstName());
            statement.setString(2, signupRequest.getLastName());
            statement.setString(3, signupRequest.getEmail());
            statement.setString(4, signupRequest.getCompany());
            statement.executeUpdate();
        } finally {
            if (statement != null) statement.close();
        }
    }
}
