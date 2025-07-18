package com.smm.course_registration.Entity;


import com.smm.course_registration.Entity.audit.Auditable;
import jakarta.persistence.*;
import org.hibernate.annotations.ValueGenerationType;

import java.util.Set;

@Entity
@Table(name = "\"User\"")
public class User extends Auditable {

    private String username;
    private String password;
    private String role;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;



    public User(){}

    public User(String userName, String password){
        this.username = userName;
        this.password = password;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return username;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return password;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }

}
