package com.example.lab789.repository;

import com.example.lab789.domain.FriendRequest;
import com.example.lab789.domain.Friendship;
import com.example.lab789.domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendshipDBRepository implements Repository<Long, Friendship> {

    private final String url;
    private final String user;
    private final String password;

    private final Validator<Friendship> validator;


    public FriendshipDBRepository(String url, String user, String password, Validator<Friendship> validator) {
        this.url = "jdbc:postgresql://localhost:5432/socialnetwork1";
        this.user = "postgres";
        this.password = "Anamaria20030307";
        this.validator = validator;
    }


    @Override
    public Optional<Friendship> findOne(Long longID) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/socialnetwork1", "postgres", "Anamaria20030307");
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendships WHERE id=?")) {
            statement.setLong(1, longID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long user1id = resultSet.getLong("user1id");
                Long user2id = resultSet.getLong("user2id");
                LocalDateTime friendsfrom = resultSet.getTimestamp("friendsfrom").toLocalDateTime();
                FriendRequest friend_req_status = FriendRequest.valueOf(resultSet.getString("friend_request_status"));
                Friendship f = new Friendship(user1id, user2id, friendsfrom);
                f.setId(longID);
                return Optional.of(f);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/socialnetwork1", "postgres", "Anamaria20030307");
             PreparedStatement statement = connection.prepareStatement("select * from friendships");
             ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long user1id = resultSet.getLong("user1id");
                Long user2id = resultSet.getLong("user2id");
                LocalDateTime friendsfrom = resultSet.getTimestamp("friendsfrom").toLocalDateTime();
                FriendRequest friendship_status = FriendRequest.valueOf(resultSet.getString("friend_request_status"));

                Friendship f = new Friendship(user1id, user2id, friendsfrom, friendship_status);
                f.setId(id);
                friendships.add(f);
            }
            return friendships;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        validator.validate(entity);
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/socialnetwork1", "postgres", "Anamaria20030307");
            PreparedStatement statement  = connection.prepareStatement("INSERT INTO friendships(user1id,user2id,friendsfrom, friend_request_status) VALUES (?,?,?,?)"))
        {
            statement.setLong(1,entity.getUser1Id());
            statement.setLong(2,entity.getUser2Id());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getFriendsFrom()));
            statement.setString(4, entity.getFriendRequestStatus().toString());
            int affectedRows = statement.executeUpdate();
            return affectedRows!=0? Optional.empty():Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> delete(Long longID) {
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/socialnetwork1", "postgres", "Anamaria20030307");
            PreparedStatement statement  = connection.prepareStatement("DELETE FROM friendships WHERE ID = ?");)
        {
            Optional<Friendship> cv = findOne(longID);
            statement.setLong(1,longID);
            int affectedRows = statement.executeUpdate();
            return affectedRows==0? Optional.empty():cv;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/socialnetwork1", "postgres", "Anamaria20030307");
            PreparedStatement statement  = connection.prepareStatement("UPDATE friendships SET user1id = ?, user2id = ?, friendsfrom = ?, friend_request_status = ? WHERE id = ?"))
        {
            statement.setLong(1,entity.getUser1Id());
            statement.setLong(2,entity.getUser2Id());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getFriendsFrom()));
            statement.setString(4, entity.getFriendRequestStatus().toString());
            statement.setLong(5,entity.getId());
            int affectedRows = statement.executeUpdate();
            return affectedRows!=0? Optional.empty():Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}