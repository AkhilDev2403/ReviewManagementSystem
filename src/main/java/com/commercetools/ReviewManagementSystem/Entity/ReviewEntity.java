package com.commercetools.ReviewManagementSystem.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RatingReview")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String comment;

    @Column
    private float rating;

    @Column
    private String customerId;

    @Column
    private String productId;


}
