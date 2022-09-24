package student.curtin.assignment1.fragment.recycler;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import student.curtin.assignment1.MainActivity;
import student.curtin.assignment1.R;
import student.curtin.assignment1.fragment.recycler.RestaurantFragment;
import student.curtin.assignment1.model.CommonData;
import student.curtin.assignment1.model.DBHandler;
import student.curtin.assignment1.model.Order;
import student.curtin.assignment1.model.Restaurant;

public class OrderHistoryFragment extends Fragment {

    private CommonData viewModel;

    public OrderHistoryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(CommonData.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orderhistory, container, false);
        RecyclerView rv = view.findViewById(R.id.orderHistory_recView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        OrderHistoryAdapter adapter = new OrderHistoryAdapter();
        rv.setAdapter(adapter);
        return view;
    }

    private class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryViewHolder>
    {
        private List<Order> orderList;

        public OrderHistoryAdapter() {
            this.orderList = DBHandler.getInstance(getContext()).getOrderHistoryList(viewModel.getUser().getEmail());
        }

        @Override
        public OrderHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.fragment_orderhistory_item, parent, false);
            OrderHistoryViewHolder vh = new OrderHistoryViewHolder(view, parent);
            return vh;
        }

        @Override
        public void onBindViewHolder(OrderHistoryViewHolder view, int position) {
            view.bind(orderList.get(position));
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }
    }

    private class OrderHistoryViewHolder extends RecyclerView.ViewHolder {

        TextView restName;
        ImageView restImage;
        TextView totalCost;
        TextView itemCount;
        TextView dateTime;

        //Constructor
        public OrderHistoryViewHolder(@NonNull View itemView, ViewGroup parent) {
            super(itemView);
            restName = itemView.findViewById(R.id.restName);
            restImage = itemView.findViewById(R.id.restImage);
            totalCost = itemView.findViewById(R.id.totalCost);
            itemCount = itemView.findViewById(R.id.itemCount);
            dateTime = itemView.findViewById(R.id.dateTime);
        }

        public void bind(Order order) {
            Restaurant restaurant = DBHandler.getInstance(getContext()).getRestaurantByName(order.getRestName());
            restName.setText(restaurant.getRestName());
            restImage.setImageResource(restaurant.getRestImage());
            totalCost.setText(String.format("%.2f", Double.toString(order.getTotalCost())));
            itemCount.setText(String.format("%s items",order.getItemCount()));
            dateTime.setText(order.getDateTime());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.setOrderSelection(order);
                    MainActivity.changeFrag(new FoodOrderFragment());
                }
            });
        }
    }
}

