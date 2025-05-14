package com.SnipURL.SnipURL.Repository;

import com.SnipURL.SnipURL.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface User_Repository extends MongoRepository<User, ObjectId> {
    User findByusername(String username);

    User findByUsername(String krish20005);
}
