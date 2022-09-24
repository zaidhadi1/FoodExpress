package student.curtin.assignment1.fragment.user;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import student.curtin.assignment1.MainActivity;
import student.curtin.assignment1.R;
import student.curtin.assignment1.model.CommonData;

public class LaunchPageFragment extends Fragment {

    private CommonData viewModel;

    public LaunchPageFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(CommonData.class);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launch_page, container, false);
        Button login = view.findViewById(R.id.login);
        Button register = view.findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag = new LoginFragment();
                MainActivity.changeFrag(frag);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag = new RegisterFragment();
                MainActivity.changeFrag(frag);
            }
        });

        return view;
    }
}