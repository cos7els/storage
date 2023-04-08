package org.cos7els.storage.repository;

import org.cos7els.storage.model.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> getPlansByIsActiveTrueOrderById();

    @Transactional
    int deletePlanById(Long id);

    Optional<Plan> getPlanById(Long id);
}