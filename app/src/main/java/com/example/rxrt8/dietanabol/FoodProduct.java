package com.example.rxrt8.dietanabol;


/**
 * Created by rxrt8 on 2017-09-13.
 */

public class FoodProduct {

    private int id;
    private String productName;
    private boolean gramsOrPieces;
    private boolean regularlyPurchased;
    private int quantity;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean isGramsOrPieces() {
        return gramsOrPieces;
    }

    public void setGramsOrPieces(boolean gramsOrPieces) {
        this.gramsOrPieces = gramsOrPieces;
    }

    public boolean isRegularlyPurchased() {
        return regularlyPurchased;
    }

    public void setRegularlyPurchased(boolean regularlyPurchased) {
        this.regularlyPurchased = regularlyPurchased;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}