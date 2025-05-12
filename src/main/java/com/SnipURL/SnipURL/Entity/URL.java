package com.SnipURL.SnipURL.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "URL")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class URL {
    @Id
    private ObjectId Id;
    private String original;
    private  String shortURL;
    private LocalDateTime created_At;
    private LocalDateTime Expire_By;

}
