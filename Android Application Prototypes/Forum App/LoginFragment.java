package com.example.midterm;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

// Forum App
// LoginFragment
// Issac Helms
public class LoginFragment extends Fragment {

    String email;
    String password;

    private LoginListener loginListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Login");
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        EditText editTextEmail = view.findViewById(R.id.emailLoginEditText);
        EditText editTextPassword = view.findViewById(R.id.passwordLoginEditText);

        view.findViewById(R.id.loginButton).setOnClickListener(v -> {
            email = editTextEmail.getText().toString();
            password = editTextPassword.getText().toString();

            if (email.isEmpty()) {
                Toast.makeText(getActivity(), "Email cannot be empty!", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(getActivity(), "Password cannot be empty!", Toast.LENGTH_SHORT).show();
            } else {
                new LoginWorkAsync().execute(email, password);
            }
        });

        view.findViewById(R.id.createNewAccountButton).setOnClickListener(v -> loginListener.toRegister());

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof LoginListener) {
            loginListener = (LoginListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement LoginListener");
        }
    }

    public interface LoginListener {
        void toForums(DataServices.AuthResponse response);
        void toRegister();
    }

    class LoginWorkAsync extends AsyncTask<String, Integer, DataServices.AuthResponse> {
        DataServices.RequestException failedException;

        @Override
        protected void onPostExecute(DataServices.AuthResponse response) {
            super.onPostExecute(response);
            if (failedException != null) {
                Toast.makeText(getActivity(), failedException.getMessage(), Toast.LENGTH_SHORT).show();
            } else if (response == null) {
                Toast.makeText(getActivity(), "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
            } else {
                loginListener.toForums(response);
            }
        }

        @Override
        protected DataServices.AuthResponse doInBackground(String... strings) {
            try {
                return DataServices.login(strings[0], strings[1]);
            } catch (DataServices.RequestException e) {
                failedException = e;
            }
            return null;
        }
    }
}