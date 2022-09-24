package student.curtin.assignment1.fragment.recycler;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import student.curtin.assignment1.MainActivity;
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

import java.util.LinkedList;
import java.util.List;

import student.curtin.assignment1.fragment.user.LaunchPageFragment;
import student.curtin.assignment1.model.Cart;
import student.curtin.assignment1.model.CommonData;
import student.curtin.assignment1.model.DBHandler;
import student.curtin.assignment1.model.Food;
import student.curtin.assignment1.model.Order;
import student.curtin.assignment1.model.User;

/** NOT FINAL **/
public class CheckoutFragment extends Fragment {

    private CommonData viewModel;
    private Button checkoutButton;
    private TextView total;
    private TextView subtotal;
    private double totalCost;
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
        total = view.findViewById(R.id.final_total_result);
        subtotal = view.findViewById(R.id.subtotal_result);

        RecyclerView rv = view.findViewById(R.id.checkout_recView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        CheckoutAdapter adapter = new CheckoutAdapter();
        rv.setAdapter(adapter);

        checkoutButton = view.findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(viewModel.getRestSelection() != null || adapter.getFoodList().isEmpty())
                {
                    if (viewModel.getUser().getEmail().equals(""))
                    {
                        MainActivity.changeFrag(new LaunchPageFragment());
                    }
                    else
                    {
                        Order order = new Order(viewModel.getUser().getEmail(),
                                viewModel.getRestSelection().getRestName());
                        order.setFoodList(adapter.getFoodList());
                        order.addToDB(DBHandler.getInstance(getContext()));

                        viewModel.resetCart();
                        MainActivity.changeFrag(new HomeFragment());
                    }

                }
            }
        });

        return view;
    }

    private class CheckoutAdapter extends RecyclerView.Adapter<CheckoutViewHolder>{

        private LinkedList<Food> foodList;

        public CheckoutAdapter() {
            foodList = viewModel.getCart().getFoodList();
            totalCost = 0;

            calcTotalCost();
        }

        @Override
        public CheckoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.fragment_checkout_item,parent,false);
            CheckoutViewHolder checkoutViewHolder = new CheckoutViewHolder(view, parent);

            calcTotalCost();

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
                        viewModel.setCart(cart);
                        foodList = viewModel.getCart().getFoodList();
                        notifyItemChanged(vhPos);
                    }
                    else //  otherwise remove
                    {
                        cart.removeFromCart(f);
                        viewModel.setCart(cart);
                        foodList = viewModel.getCart().getFoodList();
                        notifyItemRemoved(vhPos);
                    }
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

        public LinkedList<Food> getFoodList() { return foodList; }

        public void calcTotalCost() {
            totalCost = 0;
            for(Food f: this.foodList)
            {
                totalCost += f.getPrice() * f.getQuantity();
            }

            total.setText(String.format("%.2f",totalCost));
            subtotal.setText(String.format("%.2f",totalCost));

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
            foodImage.setImageResource(f.getImage());
            foodPrice.setText(String.format("%.2f",f.getPrice()));
            quantity.setText(Integer.toString(f.getQuantity()));

        }
    }
}


