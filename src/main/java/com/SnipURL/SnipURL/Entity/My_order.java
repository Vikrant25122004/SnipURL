package com.SnipURL.SnipURL.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class My_order {
    @Id
    private Long myOrderid;
    private String status;
    private String orderid;
    private String reciept;
    private String shorturl;
    private String paymentId;
    private int amount;
}
