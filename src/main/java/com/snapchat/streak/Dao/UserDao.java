package com.snapchat.streak.Dao;

import com.snapchat.streak.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserDao extends MongoRepository<User, String> {

    @Query("{username : ?0}")
    List<User> findByUsername(String name);

}
