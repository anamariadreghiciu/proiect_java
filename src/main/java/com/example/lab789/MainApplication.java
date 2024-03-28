package com.example.lab789;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.lab789.domain.Friendship;
import com.example.lab789.domain.Message;
import com.example.lab789.domain.User;
import com.example.lab789.domain.validators.FriendshipValidator;
import com.example.lab789.domain.validators.UserValidator;
import com.example.lab789.repository.FriendshipDBRepository;
import com.example.lab789.repository.MessageDBRepository;
import com.example.lab789.repository.Repository;
import com.example.lab789.repository.UserDBRepository;
import com.example.lab789.service.Service;

import java.io.IOException;

public class MainApplication extends Application {

    private static String url;
    private static String username;
    private static String password;
    private Service service;

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public static void main(String[] args) {

        url = "jdbc:postgresql://localhost:5432/socialnetwork1";
        username = "postgres";
        password = "Anamaria20030307";

        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Inițializare repository și service
            Repository<Long, User> userRepo = new UserDBRepository(url, username, password, new UserValidator());
            Repository<Long, Friendship> friendshipRepo = new FriendshipDBRepository(url, username, password, new FriendshipValidator());
            Repository<Long, Message> messageRepo = new MessageDBRepository(url, username, password, userRepo);
            service = new Service(userRepo, friendshipRepo, messageRepo);

            // Inițializare și afișare fereastră
            initView(primaryStage);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView(Stage primaryStage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("sample.fxml"));
            if (fxmlLoader == null) {
                System.out.println("FXML file not loaded.");
                return;
            }

            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setTitle("Hello!");
            primaryStage.setScene(scene);

            Controller appController = fxmlLoader.getController();

            // Ensure that the service is not null before passing it to the controller
            if (service == null) {
                throw new IllegalStateException("Service not properly initialized.");
            }

            appController.setService(this.service);
            appController.initApp(service.getAllUsers());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}