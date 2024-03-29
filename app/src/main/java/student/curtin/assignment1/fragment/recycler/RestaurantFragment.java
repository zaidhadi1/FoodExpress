package student.curtin.assignment1.fragment.recycler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import student.curtin.assignment1.model.Cart;
import student.curtin.assignment1.model.CommonData;
import student.curtin.assignment1.R;
import student.curtin.assignment1.model.DBHandler;
import student.curtin.assignment1.model.Restaurant;

public class RestaurantFragment extends Fragment {

    private CommonData viewModel;
    public RestaurantFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(CommonData.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        RecyclerView rv = view.findViewById(R.id.rest_recView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RestAdapter adapter = new RestAdapter();
        rv.setAdapter(adapter);
        return view;
    }

    private class RestAdapter extends RecyclerView.Adapter<RestViewHolder> {

        private final List<Restaurant> restaurantList;

        public RestAdapter(){this.restaurantList = DBHandler.getInstance(getContext()).getRestaurantList();}

        @Override
        public RestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.fragment_restaurant_item,parent,false);
            RestViewHolder vh = new RestViewHolder(view, parent);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.setCart(new Cart());
                    viewModel.setRestSelection(restaurantList.get(vh.getAdapterPosition()));
                    MainActivity.changeFrag(new FoodFragment());
                }
            });

            return vh;
        }

        @Override
        public void onBindViewHolder(RestViewHolder view, int position) {
            view.bind(restaurantList.get(position));
        }

        @Override
        public int getItemCount() {
            return restaurantList.size();
        }
    }

    private class RestViewHolder extends RecyclerView.ViewHolder{

        TextView restText;
        ImageView restImage;
        //Constructor
        public RestViewHolder(@NonNull View itemView, ViewGroup parent) {
            super(itemView);
            restText = itemView.findViewById(R.id.restText);
            restImage = itemView.findViewById(R.id.restImage);
        }

        public void bind(Restaurant restaurant)
        {
            restText.setText(restaurant.getRestName());
            restImage.setImageResource(restaurant.getRestImage());
        }
    }
}
