package com.skishop.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "point_accounts")
public class PointAccount {
    public PointAccount() {}
    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "user_id", length = 36, nullable = false)
    private String userId;

    @Column(nullable = false)
    private Integer balance;

    @Column(name = "lifetime_earned", nullable = false)
    private Integer lifetimeEarned;

    @Column(name = "lifetime_redeemed", nullable = false)
    private Integer lifetimeRedeemed;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getBalance() {
        return this.balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getLifetimeEarned() {
        return this.lifetimeEarned;
    }

    public void setLifetimeEarned(Integer lifetimeEarned) {
        this.lifetimeEarned = lifetimeEarned;
    }

    public Integer getLifetimeRedeemed() {
        return this.lifetimeRedeemed;
    }

    public void setLifetimeRedeemed(Integer lifetimeRedeemed) {
        this.lifetimeRedeemed = lifetimeRedeemed;
    }

}