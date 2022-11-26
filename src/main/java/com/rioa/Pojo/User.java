package com.rioa.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "userId")
    @GeneratedValue(generator = "user_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    private Long userId;

    @Column(name = "username")
    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "Username must be 4-20 characters long and contain only letters and numbers")
    private String username;

    @Column(name = "password")
    @NotBlank(message = "Password is required")
    @JsonIgnore
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9]).{8,}$", message = "Password must be at least 8 characters long and contain at least one lowercase letter,one number")
    private String password;

    @Column(name = "email")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Email must be valid")
    private String email;

    @Column(name = "avatar")
    @JsonIgnore
    @Lob
    private byte[] avatar;

    @Column(name = "nickname")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "Nickname must be 4-20 characters long and contain only letters and numbers")
    //default value is username
    private String nickname;

    @Column
    @JsonIgnore
    private LocalDateTime userCreateDate = LocalDateTime.now();

    @Column
    @JsonIgnore
    private String roles;

//    @Column
//    @JsonIgnore
//    private boolean isActive;


}
