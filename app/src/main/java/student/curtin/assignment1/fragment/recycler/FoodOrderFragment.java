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

import student.curtin.assignment1.MainActivity;
import student.curtin.assignment1.R;
import student.curtin.assignment1.model.CommonData;
import student.curtin.assignment1.model.DBHandler;
import student.curtin.assignment1.model.Food;
import student.curtin.assignment1.model.Order;

public class FoodOrderFragment extends Fragment {

    private CommonData viewModel;
    private TextView restName;
    private TextView dateTime;
    private TextView total;
    private Button backButton;

    public FoodOrderFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(CommonData.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foodorder, container, false);

        restName = view.findViewById(R.id.orderRstName);
        dateTime = view.findViewById(R.id.dateTime);
        total = view.findViewById(R.id.total);
        backButton = view.findViewById(R.id.back_button_orders);
        restName.setText(viewModel.getOrderSelection().getRestName());
        dateTime.setText(viewModel.getOrderSelection().getDateTime());
        total.setText(String.format("%.2f",viewModel.getOrderSelection().getTotalCost()));

        RecyclerView rv = view.findViewById(R.id.foodorder_recView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        FoodOrderAdapter adapter = new FoodOrderAdapter();
        rv.setAdapter(adapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.resetOrderSelection();
                MainActivity.changeFrag(new OrderHistoryFragment());
            }
        });

        return view;
    }

    private class FoodOrderAdapter extends RecyclerView.Adapter<FoodOrderViewHolder> {

        private final List<Food> foodList;

        public FoodOrderAdapter() {
            Order order = viewModel.getOrderSelection();
            this.foodList = DBHandler.getInstance(getContext())
                    .getFoodOrderList(order.getEmail(), order.getDateTime(), order.getRestName());
        }

        @Override
        public FoodOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.fragment_foodorder_item, parent, false);
            return new FoodOrderViewHolder(view, parent);
        }

        @Override
        public void onBindViewHolder(FoodOrderViewHolder view, int position) {
            view.bind(foodList.get(position));
        }

        @Override
        public int getItemCount() {
            return foodList.size();
        }
    }

    private class FoodOrderViewHolder extends RecyclerView.ViewHolder
    {

        TextView foodName;
        TextView foodPrice;
        TextView quantity;

        //Constructor
        public FoodOrderViewHolder(@NonNull View itemView, ViewGroup parent)
        {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodorder_item);
            foodPrice = itemView.findViewById(R.id.foodorder_item_price);
            quantity = itemView.findViewById(R.id.foodorder_Image);
        }

        public void bind(Food food)
        {
            foodName.setText(food.getFoodName());
            foodPrice.setText(String.format("%.2f",food.getPrice()));
            quantity.setText(Integer.toString(food.getQuantity()));
        }
    }
}

