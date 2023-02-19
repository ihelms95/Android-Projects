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
// RegisterFragment
// Issac Helms
public class RegisterFragment extends Fragment {

    String email;
    String name;
    String password;

    private RegisterListener registerListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Create New Account");
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        EditText nameEditText = view.findViewById(R.id.registerNameEditText);
        EditText emailEditText = view.findViewById(R.id.registerEmailEditText);
        EditText passwordEditText = view.findViewById(R.id.registerPasswordEditText);

        view.findViewById(R.id.submitRegistrationButton).setOnClickListener(v -> {
            name = nameEditText.getText().toString();
            email = emailEditText.getText().toString();
            password = passwordEditText.getText().toString();

            if (name.isEmpty()) {
                Toast.makeText(getActivity(), "Name is empty", Toast.LENGTH_SHORT).show();
            } else if (email.isEmpty()) {
                Toast.makeText(getActivity(), "Email is empty", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(getActivity(), "Password is empty", Toast.LENGTH_SHORT).show();
            } else {
                new RegisterWorkAsync().execute(name, email, password);
            }
        });

        view.findViewById(R.id.cancelRegistrationButton).setOnClickListener(v -> registerListener.cancelRegistration());

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof RegisterFragment.RegisterListener) {
            registerListener = (RegisterFragment.RegisterListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement RegisterListener");
        }
    }

    public interface RegisterListener {
        void toForums(DataServices.AuthResponse response);
        void cancelRegistration();
    }

    class RegisterWorkAsync extends AsyncTask<String, Integer, DataServices.AuthResponse> {
        DataServices.RequestException failedException;

        @Override
        protected void onPostExecute(DataServices.AuthResponse response) {
            super.onPostExecute(response);
            if (failedException != null) {
                Toast.makeText(getActivity(), failedException.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                registerListener.toForums(response);
            }
        }

        @Override
        protected DataServices.AuthResponse doInBackground(String... strings) {
            try {
                return DataServices.register(strings[0], strings[1], strings[2]);
            } catch (DataServices.RequestException e) {
                failedException = e;
            }
            return null;
        }
    }
}