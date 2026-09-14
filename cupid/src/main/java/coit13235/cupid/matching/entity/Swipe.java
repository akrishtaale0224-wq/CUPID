/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ASUS
 */
package com.cupid.matching.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "swipes")
public class Swipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long swiperId;

    private Long targetUserId;

    @Enumerated(EnumType.STRING)
    private SwipeDecision decision;

    private LocalDateTime createdAt;

    public Swipe() {
    }

    public Swipe(Long swiperId, Long targetUserId,
                 SwipeDecision decision) {
        this.swiperId = swiperId;
        this.targetUserId = targetUserId;
        this.decision = decision;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSwiperId() {
        return swiperId;
    }

    public void setSwiperId(Long swiperId) {
        this.swiperId = swiperId;
    }

    public Long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public SwipeDecision getDecision() {
        return decision;
    }

    public void setDecision(SwipeDecision decision) {
        this.decision = decision;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
