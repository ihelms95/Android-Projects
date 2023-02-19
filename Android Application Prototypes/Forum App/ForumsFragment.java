package com.example.midterm;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

// Forum App
// ForumsFragment
// Issac Helms
public class ForumsFragment extends Fragment {

    private static final String ARG_PARAM_RESPONSE = "ARG_PARAM_RESPONSE";

    private DataServices.AuthResponse mResponse;

    ForumsListener listener;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ForumsRecyclerViewAdapter adapter;

    public ForumsFragment() {
        // Required empty public constructor
    }

    public static ForumsFragment newInstance(DataServices.AuthResponse mResponse) {
        ForumsFragment fragment = new ForumsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_RESPONSE, mResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mResponse = (DataServices.AuthResponse) getArguments().getSerializable(ARG_PARAM_RESPONSE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Forums");
        Log.d("TAG", "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_forums, container, false);

        view.findViewById(R.id.buttonLogout).setOnClickListener(v -> listener.doLogout());
        view.findViewById(R.id.buttonNewForum).setOnClickListener(v -> listener.toCreateForum(mResponse.token));

        recyclerView = view.findViewById(R.id.forumsRecyclerView);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        new LoadForumsAsync().execute(mResponse);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ForumsFragment.ForumsListener) {
            listener = (ForumsFragment.ForumsListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ForumsListener");
        }
    }

    public void onLoading() {
        new LoadForumsAsync().execute(mResponse);
        adapter.notifyDataSetChanged();
    }

    interface ForumsListener {
        void doLogout();
        void toForum(DataServices.Forum forum, DataServices.AuthResponse response);
        void toCreateForum(String token);
    }

    public class LoadForumsAsync extends AsyncTask<DataServices.AuthResponse, Integer, ArrayList<DataServices.Forum>> {
        DataServices.RequestException failedException;

        @Override
        protected void onPostExecute(ArrayList<DataServices.Forum> forums) {
            super.onPostExecute(forums);

            if (failedException != null) {
                Toast.makeText(getActivity(), failedException.getMessage(), Toast.LENGTH_SHORT).show();
            } else if (forums == null) {
                Toast.makeText(getActivity(), "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
            } else {
                adapter = new ForumsRecyclerViewAdapter(forums, listener, mResponse, ForumsFragment.this::onLoading);
                recyclerView.setAdapter(adapter);
            }
        }

        @Override
        protected ArrayList<DataServices.Forum> doInBackground(DataServices.AuthResponse... authResponses) {
            try {
                return DataServices.getAllForums(authResponses[0].token);
            } catch (DataServices.RequestException e) {
                failedException = e;
                e.printStackTrace();
            }
            return null;
        }
    }

 }