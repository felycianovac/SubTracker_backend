package com.subtracker.api.Subscription;

import com.subtracker.api.User.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByUsers(Users users);
}
