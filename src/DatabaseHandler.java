import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatabaseHandler {
    private static DatabaseHandler handler = null;
    private static Connection connection = null;

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/WazeApp?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "ilovecompsci"; // Change if needed

    // Private constructor to enforce Singleton pattern
    private DatabaseHandler() {
        connectToDB();
    }

    // Singleton instance retrieval
    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    // Establishes a persistent database connection
    private static void connectToDB() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL Driver
                connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                System.out.println("Connected to database!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Get persistent connection
    public static Connection getConnection() {
        if (connection == null || isConnectionClosed()) {
            connectToDB();
        }
        return connection;
    }

    // Check if connection is closed
    private static boolean isConnectionClosed() {
        try {
            return connection == null || connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    // Updates last login time for a user
    public static void updateLastLogin(String username) {
        String query = "UPDATE WazeAccounts SET last_login = NOW() WHERE Username = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
            System.out.println("Last login updated for: " + username);
        } catch (SQLException e) {
            System.err.println("Error updating last login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //DASHBOARD DISPLAY
    public static ObservableList<AdminUser> displayUsers() {
        ObservableList<AdminUser> userList = FXCollections.observableArrayList();
        String query = "SELECT Username, Email, last_login FROM WazeAccounts WHERE role != 'admin'";

        try (PreparedStatement pstmt = getConnection().prepareStatement(query);
             ResultSet result = pstmt.executeQuery()) {

            while (result.next()) {
                String username = result.getString("Username");
                String email = result.getString("Email");
                Timestamp timestamp = result.getTimestamp("last_login");
                String lastLogin = (timestamp != null) ? timestamp.toLocalDateTime().toString() : "Logged in a long time ago";

                userList.add(new AdminUser(username, email, lastLogin));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching users: " + e.getMessage());
            e.printStackTrace();
        }
        return userList;
    }
     // ACCOUNT MANAGER TABLE DISPLAY
    public static ObservableList<AdminUser> displayAccounts() {
        ObservableList<AdminUser> accountList = FXCollections.observableArrayList();
        String query = "SELECT account_id, email, username, passwords, birthdate, first_name, last_name FROM WazeAccounts WHERE role != 'admin'";

        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet result = pstmt.executeQuery()) {

            while (result.next()) {
                int id = result.getInt("account_id");
                String email = result.getString("email");  // FIXED CASE
                String username = result.getString("username");  // FIXED CASE
                String password = result.getString("passwords");  // FIXED CASE
                String birthdate = result.getString("birthdate");  // FIXED CASE
                String firstName = result.getString("first_name");  // FIXED CASE
                String lastName = result.getString("last_name");  // FIXED CASE

                accountList.add(new AdminUser(id, email, username, password, birthdate, firstName, lastName));
            }
    } catch (SQLException e) {
        System.err.println("Error fetching accounts: " + e.getMessage());
        e.printStackTrace();
    }
    return accountList;
}
    // Inserts a new user into the database
    public boolean insertUser(String username, String password, String email, String birthdate, String firstName, String lastName) {
        if (isConnectionClosed()) {
            System.out.println("No database connection. Cannot create user.");
            return false;
        }

        String checkQuery = "SELECT * FROM WazeAccounts WHERE LOWER(Username) = ?";
        String insertQuery = "INSERT INTO WazeAccounts (Username, Passwords, Email, Birthdate, First_Name, Last_Name) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
             PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {

            checkStmt.setString(1, username.toLowerCase());
            ResultSet resultSet = checkStmt.executeQuery();

            if (resultSet.next()) {
                System.out.println("User already exists: " + username);
                return false;
            }

            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            insertStmt.setString(3, email);
            insertStmt.setString(4, birthdate);
            insertStmt.setString(5, firstName);
            insertStmt.setString(6, lastName);
            insertStmt.executeUpdate();

            System.out.println("User created successfully: " + username);
            return true;

        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void deleteUser(int accID) { //DELETES FROM WAZE ACCOUNTS
        String query = "DELETE FROM wazeaccounts WHERE account_id = ?";
        try (Connection conn = DatabaseHandler.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, accID);
            stmt.executeUpdate();
            System.out.println("User deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
}
    // Validates user login
    public static boolean validateLogin(String username, String password) {
        Connection conn = getConnection(); // Ensure connection is valid
    
        if (conn == null) {
            System.err.println("No database connection. Cannot validate login.");
            return false;
        }
    
        String query = "SELECT * FROM WazeAccounts WHERE Username = ? AND Passwords = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet result = pstmt.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            System.err.println("Error validating login: " + e.getMessage());
        }
        return false;
    }
}    

