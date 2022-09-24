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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import student.curtin.assignment1.fragment.user.LoginFragment;
import student.curtin.assignment1.model.CommonData;
import student.curtin.assignment1.model.DBHandler;
import student.curtin.assignment1.model.Food;

public class HomeFragment extends Fragment {

    private CommonData viewModel;
    private Button LogInOutButton;
    public HomeFragment() {}

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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        LogInOutButton = view.findViewById(R.id.loginButton);
        if(viewModel.getUser().getEmail().equals(""))
        {
            LogInOutButton.setText("Log in");
            LogInOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.changeFrag(new LoginFragment());
                }
            });
        }
        else
        {
            LogInOutButton.setText("Log out");
            LogInOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.resetUser();
                    MainActivity.changeFrag(new HomeFragment());
                }
            });
        }

        RecyclerView rv = view.findViewById(R.id.home_recView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        HomeAdapter adapter = new HomeAdapter();
        rv.setAdapter(adapter);
        return view;
    }

    private class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder>{

        private List<Food> foodList;
        public HomeAdapter(){

            foodList = new LinkedList<Food>();
            Random rng = new Random(LocalDateTime.MAX.getDayOfMonth()); // Special Menu Depends on Day Of Month
            List<Food> allFoods = DBHandler.getInstance(getActivity()).getFoodList();
            int i = 0;
            // Grab 7 random items to display
            while (i < 7)
            {
                int num = rng.nextInt(allFoods.size()); // Get random number between 0 and allFoods.size

                if (!foodList.contains(allFoods.get(num))) {
                    foodList.add(allFoods.get(num));
                    i++;
                }
            }
        }

        @Override
        public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.fragment_home_item,parent,false);
            return new HomeViewHolder(view, parent);
        }

        @Override
        public void onBindViewHolder(HomeViewHolder view, int position) {
            view.bind(foodList.get(position));
        }

        @Override
        public int getItemCount() {return foodList.size();}
    }

    private class HomeViewHolder extends RecyclerView.ViewHolder{

        // TODO Implement fully
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;
        TextView restName;


        //Constructor
        public HomeViewHolder(@NonNull View itemView, ViewGroup parent)
        {
            super(itemView);
            foodImage = itemView.findViewById(R.id.foodImage);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            restName = itemView.findViewById(R.id.restName);

        }

        public void bind(Food food)
        {
            foodName.setText(food.getFoodName());
            foodImage.setImageResource((int)food.getImage());
            foodPrice.setText(String.format("%.2f",food.getPrice()));
            restName.setText(food.getRestName());
        }
    }
}
