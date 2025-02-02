package com.example.lab789.repository;

import com.example.lab789.domain.User;
import com.example.lab789.domain.validators.Validator;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserDBRepository implements Repository<Long, User> {
    private final String url;
    private final String user;
    private final String password;

    private final Validator<User> validator;

    public UserDBRepository(String url, String user, String password, Validator<User> validator) {
        this.url = "jdbc:postgresql://localhost:5432/socialnetwork1";
        this.user = "postgres";
        this.password = "Anamaria20030307";
        this.validator = validator;
    }


    @Override
    public Optional<User> findOne(Long longID) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/socialnetwork1", "postgres", "Anamaria20030307");
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id=?");) {
            statement.setLong(1, longID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                User u = new User(firstName, lastName, email);
                u.setId(longID);
                return Optional.of(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/socialnetwork1", "postgres", "Anamaria20030307");
             PreparedStatement statement = connection.prepareStatement("select * from users");
             ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                User user = new User(firstName, lastName, email);
                user.setId(id);
                users.add(user);
            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> save(User entity) {
        validator.validate(entity);
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/socialnetwork1", "postgres", "Anamaria20030307");
            PreparedStatement statement  = connection.prepareStatement("INSERT INTO users(first_name,last_name,email) VALUES (?,?,?)");)
        {
            statement.setString(1,entity.getFirstName());
            statement.setString(2,entity.getLastName());
            statement.setString(3,entity.getEmail());
            int affectedRows = statement.executeUpdate();
            return affectedRows!=0? Optional.empty():Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> delete(Long longID) {
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/socialnetwork1", "postgres", "Anamaria20030307");
            PreparedStatement statement  = connection.prepareStatement("DELETE FROM users WHERE ID = ?");)
        {
            Optional<User> cv = findOne(longID);
            statement.setLong(1,longID);
            int affectedRows = statement.executeUpdate();
            return affectedRows==0? Optional.empty():cv;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> update(User entity) {
        validator.validate(entity);
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/socialnetwork1", "postgres", "Anamaria20030307");
            PreparedStatement statement  = connection.prepareStatement("UPDATE users SET first_name = ?, last_name = ?, email = ? WHERE id = ?");)
        {
            statement.setString(1,entity.getFirstName());
            statement.setString(2,entity.getLastName());
            statement.setString(3,entity.getEmail());
            statement.setLong(4,entity.getId());
            int affectedRows = statement.executeUpdate();
            return affectedRows!=0? Optional.empty():Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}