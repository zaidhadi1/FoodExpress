package student.curtin.assignment1.model;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

public class Food {

    private final String foodName;
    private final int image;
    private final double price;
    private int quantity;
    private final String restName;

    // Constructor
    public Food(String foodName, int image, double price, String restName) {
        this.foodName = foodName;
        this.image = image;
        this.price = price;
        this.quantity = 0;
        this.restName = restName;
    }

    // GETTERS
    public String getRestName() {
        return this.restName;
    }

    public int getImage() {
        return this.image;
    }

    public String getFoodName() {
        return this.foodName;
    }

    public double getPrice() {
        return this.price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {this.quantity = quantity;}
}