package student.curtin.assignment1.fragment.recycler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import student.curtin.assignment1.model.Cart;
import student.curtin.assignment1.model.CommonData;
import student.curtin.assignment1.R;
import student.curtin.assignment1.model.DBHandler;
import student.curtin.assignment1.model.Food;

/** WORK IN PROGRESS **/
public class FoodFragment extends Fragment {

    private CommonData viewModel;
    public FoodFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Instantiates the viewModel for use later.
        viewModel = new ViewModelProvider(getActivity()).get(CommonData.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);
        RecyclerView rv = view.findViewById(R.id.food_recView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        FoodAdapter adapter = new FoodAdapter();
        rv.setAdapter(adapter);

        TextView restTitle = view.findViewById(R.id.restaurant_title);
        Button backButton = view.findViewById(R.id.back_button);

        restTitle.setText(viewModel.getChoice().getRestName());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.resetChoice();
            }
        });


        return view;
    }

    private class FoodAdapter extends RecyclerView.Adapter<FoodViewHolder>{

        private List<Food> foodList;
        private Cart cart;

        public FoodAdapter()
        {
            foodList = DBHandler.getInstance(getContext())
                .getRestFoodList(viewModel.getChoice().getRestName());

            cart = viewModel.getCart();
        }

        @Override
        public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.fragment_food_item,parent,false);
            FoodViewHolder foodViewHolder = new FoodViewHolder(view, parent);

            foodViewHolder.plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int current = Integer.parseInt((String) foodViewHolder.quantity.getText());
                    current++;
                    foodViewHolder.quantity.setText(Integer.toString(current));
                }});

            foodViewHolder.minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int current = Integer.parseInt((String) foodViewHolder.quantity.getText());
                    if(current > 0) {current--;}
                    foodViewHolder.quantity.setText(Integer.toString(current));
                }});

            foodViewHolder.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int vhPos = foodViewHolder.getAdapterPosition();
                    Food f = foodList.get(vhPos);
                    f.setQuantity(Integer.parseInt((String)foodViewHolder.quantity.getText()));
                    if(f.getQuantity() > 0) {
                        cart.addToCart(f);
                        viewModel.setCart(cart);
                        notifyItemChanged(vhPos);
                    }
                }});

            return foodViewHolder;
        }

        @Override
        public void onBindViewHolder(FoodViewHolder foodViewHolder, int position) {
            foodViewHolder.bind(foodList.get(position));
        }

        @Override
        public int getItemCount() {return foodList.size();}
    }

    private class FoodViewHolder extends RecyclerView.ViewHolder{

        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;
        TextView quantity;
        Button addButton;
        Button minusButton;
        Button plusButton;

        //Constructor
        public FoodViewHolder(@NonNull View itemView, ViewGroup parent)
        {
            super(itemView);
            foodImage = itemView.findViewById(R.id.foodImage);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            quantity = itemView.findViewById(R.id.quantity);
            addButton = itemView.findViewById(R.id.addButton);
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

