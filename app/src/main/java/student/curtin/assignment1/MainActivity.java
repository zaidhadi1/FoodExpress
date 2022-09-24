package student.curtin.assignment1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;



import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import student.curtin.assignment1.fragment.recycler.CheckoutFragment;
import student.curtin.assignment1.fragment.recycler.FoodFragment;
import student.curtin.assignment1.fragment.recycler.HomeFragment;
import student.curtin.assignment1.fragment.recycler.OrderHistoryFragment;
import student.curtin.assignment1.fragment.recycler.RestaurantFragment;
import student.curtin.assignment1.model.CommonData;
import student.curtin.assignment1.model.DBHandler;
import student.curtin.assignment1.model.Restaurant;

public class MainActivity extends AppCompatActivity {

    private CommonData viewModel;
    private DBHandler dbHandler;
    private static FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create dbhandler class
        dbHandler = DBHandler.getInstance(this);
        viewModel = new ViewModelProvider(this).get(CommonData.class);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navbar);

        fm = getSupportFragmentManager();
        HomeFragment homeFrag = (HomeFragment) fm.findFragmentById(R.id.main_body);
        if (homeFrag == null) {
            homeFrag = new HomeFragment();
            fm.beginTransaction().add(R.id.main_body, homeFrag).commit();
        }

        // Consider observing User in viewModel to implement log in/out button

        viewModel.restSelection.observe(this, new Observer<Restaurant>() {
            @Override
            public void onChanged(@Nullable Restaurant restaurant) {
                Fragment fragment;

                if(viewModel.getRestSelection() == null)
                {
                    fragment = new RestaurantFragment();
                }
                else
                {
                    fragment = new FoodFragment();
                }

                changeFrag(fragment);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    // Check which icon is selected
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        changeFrag(fragment);
                        break;
                    case R.id.nav_browse:
                        if(viewModel.getRestSelection() == null)
                        {
                            fragment = new RestaurantFragment();
                        }
                        else
                        {
                            fragment = new FoodFragment();
                        }
                        changeFrag(fragment);
                        break;
                    case R.id.nav_checkout:
                        fragment = new CheckoutFragment();
                        changeFrag(fragment);
                        break;
                    case R.id.nav_orders:
                        fragment = new OrderHistoryFragment();
                        changeFrag(fragment);
                        break;
                }
                return true;
            }
        });

    }

    public static void changeFrag(Fragment fragment)
    {
        fm.beginTransaction().replace(R.id.main_body, fragment).commit();
    }

}

/**
 RestaurantFragment restaurantFrag = (RestaurantFragment) fm.findFragmentById(R.id.main_body);

 if(restaurantFrag == null) {
 restaurantFrag = new RestaurantFragment();
 fm.beginTransaction().add(R.id.main_body, restaurantFrag).commit();
 }
 this is the original code. below is the altered
 */
/** // Instantiate home page Homefragment
 HomeFragment homeFrag = (HomeFragment) fm.findFragmentById(R.id.home_container);
 if(homeFrag == null) {
 homeFrag = new HomeFragment();
 fm.beginTransaction().add(R.id.home_container, homeFrag).commit();
 }

 // Instantiate browse BrowseFragment
 BrowseFragment browseFrag = (BrowseFragment) fm.findFragmentById(R.id.browse_container);
 if(browseFrag == null) {
 browseFrag = new BrowseFragment();
 fm.beginTransaction().add(R.id.browse_container, browseFrag).commit();
 } **/
/**
 // Instantiate bottom NAVBAR fragment
 NavBarFragment navFrag = (NavBarFragment) fm.findFragmentById(R.id.navbar);
 if(navFrag == null) {
 navFrag = new NavBarFragment();
 fm.beginTransaction().add(R.id.navbar, navFrag).commit();
 }

 viewModel.restaurantChoice.observe(this, new Observer<Restaurant>() {
@Override
public void onChanged(@Nullable Restaurant restaurant) {
FoodFragment foodFrag = new FoodFragment();
fm.beginTransaction().replace(R.id.main_body, foodFrag).commit(); //changed from main_body to broswe to browse
}
}); */

/**nav_home etc*/