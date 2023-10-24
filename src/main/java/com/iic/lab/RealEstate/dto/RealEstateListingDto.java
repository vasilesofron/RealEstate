package com.iic.lab.RealEstate.dto;

import com.iic.lab.RealEstate.model.ListingType;
import com.iic.lab.RealEstate.model.User;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class RealEstateListingDto {

    @NotBlank(message = "Name should not be blank.")
    @NotNull
    @NotEmpty
    private String name;

    @NotBlank(message = "Address should not be blank.")
    @NotNull
    @NotEmpty
    private String address;

    @NotNull
    @NotEmpty
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private BigDecimal utilitySize;

    @NotNull
    private BigDecimal totalSize;

    @NotNull
    private Integer bedroomNumber;

    @NotNull(message = "Bathroom number should not be blank.")
    private Integer bathroomNumber;

    @NotNull
    private ListingType listingType;

    @NotNull
    private Integer constructionYear;

    private User creator;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getUtilitySize() {
        return utilitySize;
    }

    public void setUtilitySize(BigDecimal utilitySize) {
        this.utilitySize = utilitySize;
    }

    public BigDecimal getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(BigDecimal totalSize) {
        this.totalSize = totalSize;
    }

    public Integer getBedroomNumber() {
        return bedroomNumber;
    }

    public void setBedroomNumber(Integer bedroomNumber) {
        this.bedroomNumber = bedroomNumber;
    }

    public Integer getBathroomNumber() {
        return bathroomNumber;
    }

    public void setBathroomNumber(Integer bathroomNumber) {
        this.bathroomNumber = bathroomNumber;
    }

    public ListingType getListingType() {
        return listingType;
    }

    public void setListingType(ListingType listingType) {
        this.listingType = listingType;
    }

    public Integer getConstructionYear() {
        return constructionYear;
    }

    public void setConstructionYear(Integer constructionYear) {
        this.constructionYear = constructionYear;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
