package com.SnipURL.SnipURL.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "User")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private ObjectId Id;
    @NonNull
    @Indexed(unique = true)
    private String username;
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @DBRef
    private ArrayList<URL> urls = new ArrayList<>();

}
