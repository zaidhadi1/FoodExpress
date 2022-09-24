package student.curtin.assignment1.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CommonData extends ViewModel
{
    public MutableLiveData<Restaurant> restSelection;
    public MutableLiveData<Order> orderSelection;
    public MutableLiveData<User> user;
    public MutableLiveData<Cart> cart;

    public  CommonData(){
        this.restSelection = new MutableLiveData<Restaurant>();
        this.orderSelection = new MutableLiveData<Order>();
        this.user = new MutableLiveData<User>();
        this.cart = new MutableLiveData<Cart>();

        this.cart.setValue(new Cart());
        this.user.setValue(new User());
        //this.restaurantChoice.setValue(null);
        //this.user.setValue(null);
    }

    public Restaurant getRestSelection() {
        return this.restSelection.getValue();
    }
    public void setRestSelection(Restaurant restaurantChoice) { this.restSelection.setValue(restaurantChoice); }
    public void resetRestSelection() {this.restSelection.setValue(null);}

    public Order getOrderSelection() { return this.orderSelection.getValue();}
    public void setOrderSelection(Order order) {this.orderSelection.setValue(order);}
    public void resetOrderSelection() {this.orderSelection.setValue(null);}

    public User getUser() {return this.user.getValue(); }
    public void setUser(User user) {
        this.user.setValue(user);
    }
//    public void resetUser(){this.user.setValue(null);}

    public Cart getCart() {return  this.cart.getValue(); }
    public void setCart(Cart cart) { this.cart.setValue(cart); }
//    public void resetCart(){this.cart = new MutableLiveData<Cart>();}
}
