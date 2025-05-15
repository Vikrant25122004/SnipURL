package com.SnipURL.SnipURL.Repository;

import com.SnipURL.SnipURL.Entity.My_order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface My_order_Repository extends MongoRepository<My_order,Long> {
    My_order findBymyOrderid(long myOrderid);

}
