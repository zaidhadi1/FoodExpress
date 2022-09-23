package student.curtin.assignment1.model;

import java.util.LinkedList;

public class Cart {
    private LinkedList<Food> cart;

    public Cart()
    {
        this.cart = new LinkedList<Food>();
    }

    public void addToCart(Food inFood) {
        if (!this.cart.contains(inFood)) {
            this.cart.add(inFood);
        }
        else
        {
            updateCartItem(inFood);
        }
    }

    public void updateCartItem(Food inFood)
    {
        boolean found = false;
        int index = 0, i = 0;

        while(!found)
        {
            for (Food f : cart)
            {
                if (f.getFoodName().equals(inFood.getFoodName())) {
                    found = true;
                    index = i;
                }
                i++;
            }
        }

//        Food f = cart.remove(index);
//        f.setQuantity(inFood.getQuantity());
//        cart.add(f);
        cart.get(index).setQuantity(inFood.getQuantity());
    }

    public void removeFromCart(Food inFood)
    {
        int index = 0;
        for(int i = 0; i < cart.size(); i++)
        {
            Food f = cart.get(i);
            if(f.getFoodName().equals(inFood.getFoodName()))
            {
                cart.remove(f);
            }
        }
    }

    public LinkedList<Food> getFoodList() { return this.cart; }
}
