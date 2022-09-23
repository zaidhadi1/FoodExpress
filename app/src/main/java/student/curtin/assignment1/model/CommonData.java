package student.curtin.assignment1.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CommonData extends ViewModel
{
    // Possibly Consider Storing Login Details as MutableLiveData type

    public MutableLiveData<Restaurant> restaurantChoice;
    public MutableLiveData<User> user;
    public MutableLiveData<Cart> cart;

    public  CommonData(){
        this.restaurantChoice = new MutableLiveData<Restaurant>();
        this.user = new MutableLiveData<User>();
        this.cart = new MutableLiveData<Cart>();

        this.cart.setValue(new Cart());
        //this.restaurantChoice.setValue(null);
        //this.user.setValue(null);
    }

    public Restaurant getChoice() {
        return this.restaurantChoice.getValue();
    }
    public void setChoice(Restaurant restaurantChoice) { this.restaurantChoice.setValue(restaurantChoice); }
    
    public User getUser() {return this.user.getValue(); }
    public void setUser(User user) {
        this.user.setValue(user);
    }

    public Cart getCart() {return  this.cart.getValue(); }
    public void setCart(Cart cart) { this.cart.setValue(cart); }
}
