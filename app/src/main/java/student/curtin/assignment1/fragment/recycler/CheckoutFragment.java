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


        TextView total = view.findViewById(R.id.final_total_result);
        TextView subtotal = view.findViewById(R.id.subtotal_result);

        total.setText(Double.toString(adapter.getTotalCost()));
        subtotal.setText(Double.toString(adapter.getTotalCost()));


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
        private double totalCost;

        public CheckoutAdapter() {
            foodList = viewModel.getCart().getFoodList();
            totalCost = 0;

            for(Food f: this.foodList)
            {
                totalCost += f.getPrice() * f.getQuantity();
            }
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
                        // broken
                        cart.updateCartItem(f);
                    }
                    else //  otherwise remove
                    {
                        cart.removeFromCart(f);
                    }

                    notifyItemRemoved(vhPos);
                    viewModel.setCart(cart);
                    foodList = viewModel.getCart().getFoodList();
                    calcTotalCost();

                }});

            return checkoutViewHolder;
        }

        @Override
        public void onBindViewHolder(CheckoutViewHolder checkoutViewHolder, int position) {
            checkoutViewHolder.bind(foodList.get(position));
        }

        @Override
        public int getItemCount() {return foodList.size();}

        public double getTotalCost() {return totalCost;}
        public void calcTotalCost() {
            totalCost = 0;
            for(Food f: this.foodList)
            {
                totalCost += f.getPrice() * f.getQuantity();
            }
        }
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

        public void bind(Food f)
        {
            foodName.setText(f.getFoodName());
            foodImage.setImageResource((int)f.getImage());
            foodPrice.setText(Double.toString(f.getPrice()));
            quantity.setText(Integer.toString(f.getQuantity()));

        }
    }
}


