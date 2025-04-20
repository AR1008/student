package com.university.attendance.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
public class Admin extends User {
    
    // Constructors
    public Admin() {
        super();
        this.setRole("ADMIN");
    }
}