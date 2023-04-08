package org.cos7els.storage.repository;

import org.cos7els.storage.model.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    @Transactional
    int deleteSubscriptionById(Long id);
}
