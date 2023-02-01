package com.commercetools.ReviewManagementSystem.Repository;

import com.commercetools.ReviewManagementSystem.Entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestReviewRepository extends JpaRepository<TestEntity, Long> {
}
