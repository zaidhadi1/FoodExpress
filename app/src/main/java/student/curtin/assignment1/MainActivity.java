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
import student.curtin.assignment1.fragment.user.LoginFragment;
import student.curtin.assignment1.model.CommonData;
import student.curtin.assignment1.model.DBHandler;
import student.curtin.assignment1.model.Restaurant;

public class MainActivity extends AppCompatActivity {

    private CommonData viewModel;
    private DBHandler dbHandler;
    private static BottomNavigationView bottomNavigationView;
    private static FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create dbhandler class
        dbHandler = DBHandler.getInstance(this);
        viewModel = new ViewModelProvider(this).get(CommonData.class);

        bottomNavigationView = findViewById(R.id.bottom_navbar);

        fm = getSupportFragmentManager();
        HomeFragment homeFrag = (HomeFragment) fm.findFragmentById(R.id.main_body);
        if (homeFrag == null) {
            homeFrag = new HomeFragment();
            fm.beginTransaction().add(R.id.main_body, homeFrag).commit();
        }

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
                        if(viewModel.getUser().getEmail().equals(""))
                        {
                            fragment = new LoginFragment();
                        }
                        else
                        {
                        fragment = new OrderHistoryFragment();
                        }
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

    public static int getSelectedNavItem()
    {
        return bottomNavigationView.getSelectedItemId();
    }

}
