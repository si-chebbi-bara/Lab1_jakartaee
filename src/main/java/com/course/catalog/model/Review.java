package com.course.catalog.model;

import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue
    private Long id;

    private int rating;
    private String comment;

    @ManyToOne
    private Product product;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}