package com.commercetools.reviewmanagementsystem.repository;

import com.commercetools.reviewmanagementsystem.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    @Modifying
    @Transactional
    @Query(value = "delete from rating_review where customer_id = ?1 and  product_id = ?2", nativeQuery = true)
    Integer deleteByProductId(String cuId, String pId);

    //to get average of a count.. rating is the column where we store all the ratings given by the user, and we set avg rating into average_rating column.
    @Query(value = "SELECT AVG (rating) AS average_rating FROM rating_review where product_id = ?1", nativeQuery = true)
    Optional<Float> getRatingCount(String productId);

    @Query(value = "select * from rating_review r where r.customer_id = ?1 and r.product_id = ?2", nativeQuery = true)
    Optional<ReviewEntity> findByCustomerIdAndProductId(String customerId, String productId);

    @Query(value = "select * from rating_review r where r.customer_id = ?1  and r.product_id = ?2", nativeQuery = true)
    ReviewEntity findByCustomerDetails(String customerId, String productId);

    @Query(value = "select * from rating_review r where r.product_id = ?1 and r.customer_id = ?2", nativeQuery = true)
    Optional<ReviewEntity> findByProductAndUser(String productId, String customerId);

}
