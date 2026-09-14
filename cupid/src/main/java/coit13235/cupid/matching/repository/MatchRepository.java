/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ASUS
 */
package com.cupid.matching.repository;

import com.cupid.matching.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    boolean existsByUserOneIdAndUserTwoId(
            Long userOneId,
            Long userTwoId
    );

    List<Match> findByUserOneIdOrUserTwoId(
            Long userOneId,
            Long userTwoId
    );
}
