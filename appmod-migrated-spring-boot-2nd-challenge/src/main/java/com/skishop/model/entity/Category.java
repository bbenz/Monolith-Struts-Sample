package com.skishop.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {
    public Category() {}
    @Id
    @Column(length = 36)
    private String id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(name = "parent_id", length = 36)
    private String parentId;

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

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

}