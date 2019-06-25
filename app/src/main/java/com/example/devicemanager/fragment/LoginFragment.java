package com.example.devicemanager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.devicemanager.R;
import com.example.devicemanager.activity.MainActivity;
import com.example.devicemanager.manager.Contextor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private EditText etEmail, etPassword;
    private FirebaseAuth mAuth;
    private String strEmail, strPassword;
    private Button btnSubmit;

    public static LoginFragment newInstance(){
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initInstances(view);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        etEmail.getText().clear();
        etPassword.getText().clear();
    }

    private void initInstances(View view) {
        etEmail = view.findViewById(R.id.etLoginEmail);
        etPassword = view.findViewById(R.id.etLoginPassword);
        btnSubmit = view.findViewById(R.id.btnLoginSubmit);

        btnSubmit.setOnClickListener(onClickBtnSubmit);
    }

    private void userLogin() {
        strEmail = etEmail.getText().toString().trim();
        strPassword = etPassword.getText().toString().trim();

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Contextor.getInstance().getContext(),
                            "Login Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                else {
                    Toast.makeText(Contextor.getInstance().getContext(),
                            "Failed to Login", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private View.OnClickListener onClickBtnSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            userLogin();
        }
    };

}
