package student.curtin.assignment1.fragment.recycler;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import student.curtin.assignment1.R;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import student.curtin.assignment1.model.Cart;
import student.curtin.assignment1.model.CommonData;
import student.curtin.assignment1.model.Food;

/** NOT FINAL **/
public class CheckoutFragment extends Fragment {

    private CommonData viewModel;
    private Button checkoutButton;
    public CheckoutFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Instantiates the viewModel for use later.
        viewModel = new ViewModelProvider(getActivity()).get(CommonData.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        RecyclerView rv = view.findViewById(R.id.checkout_recView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        CheckoutAdapter adapter = new CheckoutAdapter();
        rv.setAdapter(adapter);

        checkoutButton = view.findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Prompt for login if User not specified

            }
        });

        return view;
    }

    private class CheckoutAdapter extends RecyclerView.Adapter<CheckoutViewHolder>{

        private List<Food> foodList;

        public CheckoutAdapter() {
            foodList = viewModel.getCart().getFoodList();
        }

        @Override
        public CheckoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.fragment_checkout_item,parent,false);
            CheckoutViewHolder checkoutViewHolder = new CheckoutViewHolder(view, parent);

            checkoutViewHolder.plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int current = Integer.parseInt((String) checkoutViewHolder.quantity.getText());
                    current++;
                    checkoutViewHolder.quantity.setText(Integer.toString(current));
                }});

            checkoutViewHolder.minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int current = Integer.parseInt((String) checkoutViewHolder.quantity.getText());
                    if(current > 0) {current--;}
                    checkoutViewHolder.quantity.setText(Integer.toString(current));
                }});

            checkoutViewHolder.confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get Food and Cart object
                    int vhPos = checkoutViewHolder.getAdapterPosition();
                    Food f = foodList.get(vhPos);
                    Cart cart = viewModel.getCart();

                    // Set new quantity of food item
                    f.setQuantity(Integer.parseInt((String)checkoutViewHolder.quantity.getText()));

                    // if quantity not zero then update,
                    if(f.getQuantity() > 0) {
                        cart.updateCartItem(f);
                    }
                    else //  otherwise remove
                    {
                        cart.removeFromCart(f);
                    }
                    notifyItemChanged(vhPos);
                    viewModel.setCart(cart);

                }});

            return checkoutViewHolder;
        }

        @Override
        public void onBindViewHolder(CheckoutViewHolder checkoutViewHolder, int position) {
            checkoutViewHolder.bind(foodList.get(position));
        }

        @Override
        public int getItemCount() {return foodList.size();}
    }

    private class CheckoutViewHolder extends RecyclerView.ViewHolder{

        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;
        TextView quantity;
        Button confirmButton;
        Button minusButton;
        Button plusButton;


        //Constructor
        public CheckoutViewHolder(@NonNull View itemView, ViewGroup parent)
        {
            super(itemView);
            foodImage = itemView.findViewById(R.id.foodImage);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            quantity = itemView.findViewById(R.id.quantity);
            confirmButton = itemView.findViewById(R.id.confirmButton);
            minusButton = itemView.findViewById(R.id.minusButton);
            plusButton = itemView.findViewById(R.id.plusButton);
        }

        public void bind(Food food)
        {
            foodName.setText(food.getFoodName());
            foodImage.setImageResource((int)food.getImage());
            foodPrice.setText(Double.toString(food.getPrice()));
            quantity.setText(Integer.toString(food.getQuantity()));
        }
    }
}


