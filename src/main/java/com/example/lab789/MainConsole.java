package com.example.lab789;

import com.example.lab789.console.ConsoleUI;
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

public class MainConsole {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/socialnetwork1";
        String username =  "postgres";
        String password = "Anamaria20030307";

        Repository<Long, User> userRepo = new UserDBRepository(url, username, password, new UserValidator());
        Repository<Long, Friendship> friendshipRepo = new FriendshipDBRepository(url, username, password, new FriendshipValidator());
        Repository<Long, Message> messageRepo = new MessageDBRepository(url, username, password, userRepo);
        Service service = new Service(userRepo, friendshipRepo, messageRepo);
        ConsoleUI console = new ConsoleUI(service);

        console.run();
    }
}