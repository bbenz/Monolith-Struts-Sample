package com.skishop.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    public Role() {}
    @Id
    @Column(length = 36)
    private String id;

    @Column(length = 50, nullable = false)
    private String name;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}