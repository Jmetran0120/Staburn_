package com.gabriel.model;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String email;
    private String name;
    private String role; // "customer" or "admin"
}

