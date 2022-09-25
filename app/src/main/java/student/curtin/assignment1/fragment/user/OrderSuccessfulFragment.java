package student.curtin.assignment1.fragment.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import student.curtin.assignment1.MainActivity;
import student.curtin.assignment1.R;
import student.curtin.assignment1.fragment.recycler.CheckoutFragment;
import student.curtin.assignment1.fragment.recycler.HomeFragment;
import student.curtin.assignment1.fragment.recycler.OrderHistoryFragment;
import student.curtin.assignment1.model.CommonData;
import student.curtin.assignment1.model.DBHandler;
import student.curtin.assignment1.model.User;

public class OrderSuccessfulFragment extends Fragment {

    private CommonData viewModel;
    private final int CHECKOUT_SUCCESSFUL_TIMEOUT = 4000;

    public OrderSuccessfulFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(CommonData.class);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_successful, container, false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.changeFrag(new HomeFragment());
            }
        }, CHECKOUT_SUCCESSFUL_TIMEOUT);
        return view;
    }
}
