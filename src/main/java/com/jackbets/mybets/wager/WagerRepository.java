package com.jackbets.mybets.wager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WagerRepository extends JpaRepository<Wager, Long> {

}
