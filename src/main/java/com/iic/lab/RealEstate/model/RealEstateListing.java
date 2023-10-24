package com.iic.lab.RealEstate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;



@Entity
@Table(name = "real_estate_listing")
public class RealEstateListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name should not be blank.")
    @NotNull
    @NotEmpty
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Address should not be blank.")
    @NotNull
    @NotEmpty
    @Column(name = "address")
    private String address;

    @NotBlank(message = "Description should not be blank.")
    @NotNull
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "price")
    private BigDecimal price;

    @NotNull
    @Column(name = "utility_size")
    private BigDecimal utilitySize;

    @NotNull
    @Column(name = "total_size")
    private BigDecimal totalSize;

    @NotNull
    @Column(name = "bedroom_number")
    private Integer bedroomNumber;

    @NotNull
    @Column(name = "bathroom_Number")
    private Integer bathroomNumber;

    @NotNull
    @Column(name = "listing_type")
    private ListingType listingType;

    @NotNull
    @Column(name = "construction_year")
    private Integer constructionYear;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    public RealEstateListing() {
    }

    public RealEstateListing(Long id, String name, String address, String description, BigDecimal price, BigDecimal utilitySize, BigDecimal totalSize, Integer bedroomNumber, Integer bathroomNumber, ListingType listingType, Integer constructionYear, User creator) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.price = price;
        this.utilitySize = utilitySize;
        this.totalSize = totalSize;
        this.bedroomNumber = bedroomNumber;
        this.bathroomNumber = bathroomNumber;
        this.listingType = listingType;
        this.constructionYear = constructionYear;
        this.creator = creator;
    }

    public RealEstateListing(String name, String address, String description, BigDecimal price, BigDecimal utilitySize, BigDecimal totalSize, Integer bedroomNumber, Integer bathroomNumber, ListingType listingType, Integer constructionYear) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.price = price;
        this.utilitySize = utilitySize;
        this.totalSize = totalSize;
        this.bedroomNumber = bedroomNumber;
        this.bathroomNumber = bathroomNumber;
        this.listingType = listingType;
        this.constructionYear = constructionYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
