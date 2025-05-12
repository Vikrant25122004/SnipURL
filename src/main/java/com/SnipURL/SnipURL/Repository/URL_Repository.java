package com.SnipURL.SnipURL.Repository;

import com.SnipURL.SnipURL.Entity.URL;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface URL_Repository extends MongoRepository<URL, ObjectId> {
}
