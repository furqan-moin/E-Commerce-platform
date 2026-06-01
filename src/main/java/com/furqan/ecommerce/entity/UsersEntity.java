package com.furqan.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UsersEntity {
    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdAt;
}
