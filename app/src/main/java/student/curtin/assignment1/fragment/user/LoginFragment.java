package student.curtin.assignment1.fragment.user;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import student.curtin.assignment1.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    //    Button register;
//    TextView emailBox;
//    TextView passwordBox;
//    TextView passwordBox2;
//    TextView register_info;

    public LoginFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
//
//        register = view.findViewById(R.id.register);
//        emailBox = view.findViewById(R.id.new_email);
//        passwordBox = view.findViewById(R.id.new_password);
//        passwordBox2 = view.findViewById(R.id.confirm_password);
//        register_info = view.findViewById(R.id.register_info);
//
//        register.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View view) {
//                DBHandler dbHandler = DBHandler.getInstance(getContext());
//                String email = (String) emailBox.getText();
//                String password = (String) passwordBox.getText();
//                String confirmPassword = (String) passwordBox.getText();
//
//                if(!dbHandler.checkUserExists(email)) {
//
//                    if(password.equals(confirmPassword)) {
//                        dbHandler.addUser_DB(email, password);
//                        Fragment frag = new LaunchPageFragment();
//                        MainActivity.changeFrag(frag);
//                    }
//                    else
//                    {
//                        register_info.setTextColor(R.color.red);
//                        register_info.setText("ERROR : PASSWORDS DO NOT MATCH.");
//                    }
//                }
//                else
//                {
//                    register_info.setTextColor(R.color.red);
//                    register_info.setText("ERROR : USER ALREADY EXISTS");
//                }
//            }
//        });
        return view;
    }
}