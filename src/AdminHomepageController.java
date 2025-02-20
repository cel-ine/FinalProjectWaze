import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AdminHomepageController {
    //MAIN PANE
    @FXML private StackPane MainPane;
    @FXML private TableView<AdminUser> dashboardTable;
    @FXML private TableColumn<AdminUser, String> usernameCol;
    @FXML private TableColumn<AdminUser, String> emailCol;
    @FXML private TableColumn<AdminUser, String> lastLogInCol;
    @FXML private Button manageAccBTN, managePlannedDBTN, manageRouteBTN;
    @FXML private Label totalUsersLabel, totalPlannedDrivesLabel, totalRoutesLabel;
    
    //MANAGE ACC PANE
    @FXML private StackPane AccountManagerPane;
    @FXML private Button accmngraddUserBTN, back2dashboardBTN, AccMngrUpdateBTN, AccMngrDeleteBTN;
    @FXML private MenuItem SignOutBTN;
    @FXML private MenuButton menuBTN;
    @FXML private TableView<AdminUser> AccMngrTable;
    @FXML private TableColumn<AdminUser, String> accIDCol;
    @FXML private TableColumn<AdminUser, String> emailCol1;
    @FXML private TableColumn<AdminUser, String> usernameCol1;
    @FXML private TableColumn<AdminUser, String> passwordCol;
    @FXML private TableColumn<AdminUser, String> birthdateCol;
    @FXML private TableColumn<AdminUser, String> firstNameCol;
    @FXML private TableColumn<AdminUser, String> lastNameCol;
    @FXML private TextField searchTF;
    
    //MANAGE PLANNED DRIVES PANE
    @FXML private StackPane PLannedDirvesPane;


    //MANAGE ROUTES PANE
    @FXML private StackPane RoutesManagerPane;


    private ObservableList<AdminUser> userList = FXCollections.observableArrayList();
    private ObservableList<AdminUser> accountsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        showPane(MainPane); 

        //DASHBOARD TABLE
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        lastLogInCol.setCellValueFactory(new PropertyValueFactory<>("lastLogIn"));
        dashboardTable.setItems(userList);
        dashboardTable.refresh();
        loadDashboardData();
        //ACCOUNTS MANAGER TABLE (Fixed Issues)
        accIDCol.setCellValueFactory(new PropertyValueFactory<>("accID"));
        emailCol1.setCellValueFactory(new PropertyValueFactory<>("email"));
        usernameCol1.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        birthdateCol.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        AccMngrTable.setItems(accountsList);

        updateCounters();
}
    private void refreshDashboardTable() {
        ObservableList<AdminUser> userList = DatabaseHandler.displayAccounts(); // Fetch updated list
        dashboardTable.setItems(userList); // Set new data
    }
    // Load data into Account Manager Table
    private void loadAccountManagerData() {
        accountsList.clear();
        accountsList.addAll(DatabaseHandler.displayAccounts());
        AccMngrTable.setItems(accountsList);
        
    }
    // Load data into Dashboard Table
    private void loadDashboardData() {
        userList.clear();
        userList.addAll(DatabaseHandler.displayUsers());
        dashboardTable.setItems(userList);
    }
    private void updateCounters() { //COUTERS PARA SA DASHBOARD
    String query = "SELECT " +
                   "(SELECT COUNT(*) FROM WazeAccounts) AS total_users, " +
                   "(SELECT COUNT(*) FROM WazeRoutes) AS total_routes, " +
                   "(SELECT COUNT(*) FROM WazePlannedDrives) AS total_planned_drives";

    try (Connection conn = DatabaseHandler.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        if (rs.next()) {
            int totalUsers = rs.getInt("total_users");
            int totalRoutes = rs.getInt("total_routes");
            int totalPlannedDrives = rs.getInt("total_planned_drives");

            totalUsersLabel.setText("Total Users: " + totalUsers);
            totalRoutesLabel.setText("Total Routes: " + totalRoutes);
            totalPlannedDrivesLabel.setText("Total Planned Drives: " + totalPlannedDrives);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    @FXML
    public void handleSignOut(ActionEvent event) throws IOException {
        // Get the current stage correctly from the menu button
        Stage stage = (Stage) menuBTN.getScene().getWindow();  
        stage.close(); // Close current stage

        // Load the Sign-In page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();

        // Create a new stage for sign-in
        Stage signInStage = new Stage();
        signInStage.setScene(new Scene(root));
        signInStage.show();
    }

    @FXML
    private void manageAccButtonHandler(ActionEvent event) { 
        showPane(AccountManagerPane);
        loadAccountManagerData();
    }

        @FXML
        private void back2dashboardButtonHandler(ActionEvent event) { 
            showPane(MainPane);
        }

        @FXML 
        private void deleteButtonHandler(ActionEvent event) {
            AdminUser selectedUser = AccMngrTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                DatabaseHandler.deleteUser(selectedUser.getAccID());
                loadAccountManagerData();
                updateCounters();
                refreshDashboardTable();
            }
        }



    @FXML
    private void managePlannedDrivesButtonHandler(ActionEvent event) { 
        showPane(PLannedDirvesPane);
    }
    
    @FXML
    private void manageRoutesButtonHandler(ActionEvent event) { 
        showPane(RoutesManagerPane);
    }
    

    private void showPane(StackPane paneToShow) {
        MainPane.setOpacity(0);
        AccountManagerPane.setOpacity(0);
        PLannedDirvesPane.setOpacity(0);
        RoutesManagerPane.setOpacity(0);
    
        paneToShow.setOpacity(1);
        paneToShow.toFront(); 
    }
}
