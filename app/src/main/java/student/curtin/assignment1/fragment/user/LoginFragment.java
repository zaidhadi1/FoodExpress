package student.curtin.assignment1.fragment.user;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import student.curtin.assignment1.MainActivity;
import student.curtin.assignment1.R;
import student.curtin.assignment1.fragment.recycler.CheckoutFragment;
import student.curtin.assignment1.fragment.recycler.HomeFragment;
import student.curtin.assignment1.fragment.recycler.OrderHistoryFragment;
import student.curtin.assignment1.model.CommonData;
import student.curtin.assignment1.model.DBHandler;
import student.curtin.assignment1.model.User;

public class LoginFragment extends Fragment {

    private CommonData viewModel;
    Button login;
    Button registerButton;
    TextView emailBox;
    TextView passwordBox;
    TextView infoBox;

    public LoginFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(CommonData.class);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        login = view.findViewById(R.id.login);
        emailBox = view.findViewById(R.id.email);
        passwordBox = view.findViewById(R.id.password);
        infoBox = view.findViewById(R.id.login_info);
        registerButton = view.findViewById(R.id.registerButton);

        login.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                DBHandler dbHandler = DBHandler.getInstance(getContext());
                String email = emailBox.getText().toString();
                String password = passwordBox.getText().toString();

                if(dbHandler.checkUserExists(email)) {

                    User user = new User(email,password);
                    if(dbHandler.validateUser(user)){
                        viewModel.setUser(user);

                        // Go back to screen based on last selected navigation item
                        switch(MainActivity.getSelectedNavItem())
                        {
                            case R.id.nav_home:
                                MainActivity.changeFrag(new HomeFragment());
                                break;

                            case R.id.nav_checkout:
                                MainActivity.changeFrag(new CheckoutFragment());
                                break;

                            case R.id.nav_orders:
                                MainActivity.changeFrag(new OrderHistoryFragment());
                        }
                    }
                    else
                    {
                        infoBox.setTextColor(R.color.red);
                        infoBox.setText("ERROR : Invalid login details.");
                    }
                }
                else
                {
                    infoBox.setTextColor(R.color.red);
                    infoBox.setText("ERROR : User doesn't exist.");
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                MainActivity.changeFrag(new RegisterFragment());
            }
        });

        return view;
    }
}