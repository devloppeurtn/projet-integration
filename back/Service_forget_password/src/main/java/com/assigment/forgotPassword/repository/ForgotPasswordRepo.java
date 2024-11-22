package com.assigment.forgotPassword.repository;

import com.assigment.forgotPassword.entity.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;



@Repository
public interface ForgotPasswordRepo extends MongoRepository<User, String> {
    User findByEmail(String email);
    User findByToken(String token);
}

