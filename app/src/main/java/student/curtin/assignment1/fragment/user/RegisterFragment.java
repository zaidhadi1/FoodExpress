package student.curtin.assignment1.fragment.user;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import student.curtin.assignment1.MainActivity;
import student.curtin.assignment1.R;
import student.curtin.assignment1.model.DBHandler;

public class RegisterFragment extends Fragment {

    Button register;
    Button backButton;
    TextView emailBox;
    TextView passwordBox;
    TextView passwordBox2;
    TextView register_info;

    public RegisterFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        register = view.findViewById(R.id.register);
        emailBox = view.findViewById(R.id.new_email);
        passwordBox = view.findViewById(R.id.new_password);
        passwordBox2 = view.findViewById(R.id.confirm_password);
        register_info = view.findViewById(R.id.register_info);
        backButton = view.findViewById(R.id.back_button_createAccount);

        register.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                DBHandler dbHandler = DBHandler.getInstance(getContext());
                String email = emailBox.getText().toString();
                String password = passwordBox.getText().toString();
                String confirmPassword = passwordBox2.getText().toString();
                if(!(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()))
                {
                    if (!dbHandler.checkUserExists(email)) {

                        if (password.equals(confirmPassword)) {
                            dbHandler.addUser_DB(email, password);
                            Fragment frag = new LoginFragment();
                            MainActivity.changeFrag(frag);
                        } else {
                            register_info.setTextColor(R.color.red);
                            register_info.setText("ERROR : Passwords do not match.");
                        }
                    } else {
                        register_info.setTextColor(R.color.red);
                        register_info.setText("ERROR : User already exists.");
                    }
                }
                else
                {
                    register_info.setTextColor(R.color.red);
                    register_info.setText("ERROR : All fields must be filled.");
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.changeFrag(new LoginFragment());
            }
        });
        return view;
    }
}