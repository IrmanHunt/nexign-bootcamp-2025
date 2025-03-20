package com.romanlotohin.nexign_bootcamp_2025.repository;

import com.romanlotohin.nexign_bootcamp_2025.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
    Subscriber findByMsisdn(String msisdn);
}
