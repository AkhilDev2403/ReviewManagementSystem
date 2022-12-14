package com.commercetools.ReviewManagementSystem.Repository;

import com.commercetools.ReviewManagementSystem.Entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    @Query(value = "SELECT * FROM rating_review", nativeQuery = true)
    List<ReviewEntity> findAllReviews();

    @Modifying
    @Transactional
    @Query(value = "delete from rating_review where product_id = ?1 and customer_id = ?2", nativeQuery = true)
    Integer deleteByProductId(String pId, String cuId);


    //to get average of a count.. rating is the column where we store all the ratings given by the user and we set avg rating into average_rating column.
    @Query(value = "SELECT AVG (rating) AS average_rating FROM rating_review where product_id = ?1", nativeQuery = true)
    Optional<Float> getRatingCount(String productId);
}
