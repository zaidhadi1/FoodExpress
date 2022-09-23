package student.curtin.assignment1.model;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

public class Food {

    //private static List<Food> foodList = new LinkedList<Food>();
    private String foodName;
    private int image;
    private double price;
    private int quantity;
    private String restName;

    // Constructor
    public Food(String foodName, int image, double price, String restName) {
        this.foodName = foodName;
        this.image = image;
        this.price = price;
        this.quantity = 0;
        this.restName = restName;
        //foodList.add(this);
    }

 //  public  List<Food> getFoodList(){
//        if(foodList.size() == 0)
//        {
//            Food food1 = new Food("Water", android.R.color.black, 2.50 ,"ABC");
//            Food food2 = new Food("Sparkling Water", android.R.color.black, 25.00 ,"ABC");
//            Food food3 = new Food("Tea", android.R.color.black, 3.00 ,"ABC");
//            Food food4 = new Food("Espresso", android.R.color.black, 3.20 ,"ABC");
//            Food food5 = new Food("Latte", android.R.color.black, 4.50 ,"ABC");
//            Food food6 = new Food("Ice Latte", android.R.color.black, 7.50 ,"ABC");
//            Food food7 = new Food("Brownie", android.R.color.black, 4.00 ,"ABC");
//            Food food8 = new Food("Banana Bread", android.R.color.black, 4.50 ,"ABC");
//            Food food9 = new Food("Carrot Cake", android.R.color.black, 6.00 ,"ABC");
//            Food food10 = new Food("Chocolate Cake", android.R.color.black, 9.50 ,"ABC");
//            Food food11 = new Food("Double Choc Muffin", android.R.color.black, 6.50 ,"ABC");
//            Food food12 = new Food("Salmon Bagel", android.R.color.black, 16.50 ,"ABC");
//            Food food13 = new Food("Cheese Toastie", android.R.color.black, 12.50 ,"ABC");
//            Food food14 = new Food("Steak Sandwich", android.R.color.black, 22.50 ,"ABC");
//            Food food15 = new Food("Bacon-Egg Wrap", android.R.color.black, 20.00 ,"ABC");
//            Food food16 = new Food("4-Piece Chicken", android.R.color.black, 5.50 ,"KFC");
//            Food food17 = new Food("8-Piece Chicken", android.R.color.black, 12.50 ,"KFC");
//            Food food18 = new Food("4-Piece Spicy Chicken", android.R.color.black, 7.50 ,"KFC");
//            Food food19 = new Food("8-Piece Spicy Chicken", android.R.color.black, 14.50 ,"KFC");
//            Food food20 = new Food("Mash Potatoes", android.R.color.black, 5.00 ,"KFC");
//        }
//        return foodList;
//    }

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

    public void incQuantity() {
        this.quantity += 1;
    }

    public void decQuantity() {if(this.quantity > 0) {this.quantity -= 1;}}

    public  void  resetQuantity() {this.quantity = 0;}
}