package com.iic.lab.RealEstate.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.TypeAlias;

@Entity
@Table(name = "user_favourite_real_estate_listing")
public class UserFavouriteRealEstateListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private RealEstateListing realEstateListing;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RealEstateListing getRealEstateListing() {
        return realEstateListing;
    }

    public void setRealEstateListing(RealEstateListing realEstateListing) {
        this.realEstateListing = realEstateListing;
    }

    public UserFavouriteRealEstateListing(Long id, User user, RealEstateListing realEstateListing) {
        this.id = id;
        this.user = user;
        this.realEstateListing = realEstateListing;
    }

    public UserFavouriteRealEstateListing() {
    }

    public UserFavouriteRealEstateListing(User user, RealEstateListing realEstateListing) {
        this.user = user;
        this.realEstateListing = realEstateListing;
    }
}
