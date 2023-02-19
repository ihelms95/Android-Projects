package com.example.midterm;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

// Forum App
// NewForumFragment
// Issac Helms
public class NewForumFragment extends Fragment {

    private static final String ARG_PARAM_TOKEN = "ARG_PARAM_TOKEN";

    NewForumFragmentListener listener;

    private String mToken;

    public NewForumFragment() {
        // Required empty public constructor
    }

    public static NewForumFragment newInstance(String token) {
        NewForumFragment fragment = new NewForumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mToken = getArguments().getString(ARG_PARAM_TOKEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("New Forum");
        View view = inflater.inflate(R.layout.fragment_new_forum, container, false);
        Log.d("TAG", "onCreateView: here1");

        EditText forumTitleEditText = view.findViewById(R.id.forumTitleEditText);
        EditText forumDescriptionEditText = view.findViewById(R.id.forumDescriptionEditText);

        view.findViewById(R.id.submitNewForumButton).setOnClickListener(v -> {
            String title = forumTitleEditText.getText().toString();
            String description = forumDescriptionEditText.getText().toString();

            Log.d("TAG", "onClick: here2");
            if (title.isEmpty()) {
                Toast.makeText(getActivity(), "Forum Title is empty", Toast.LENGTH_SHORT).show();
            } else if (description.isEmpty()) {
                Toast.makeText(getActivity(), "Forum Description is empty", Toast.LENGTH_SHORT).show();
            } else {
                new CreateForumAsync().execute(mToken, title, description);
            }
        });

        view.findViewById(R.id.cancelNewForumButton).setOnClickListener(v -> listener.cancelNewForum());

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof NewForumFragment.NewForumFragmentListener) {
            listener = (NewForumFragment.NewForumFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement NewForumFragmentListener");
        }
    }

    interface NewForumFragmentListener {
        void popAndUpdateList();
        void cancelNewForum();
    }

    class CreateForumAsync extends AsyncTask<String, Integer, Boolean> {
        DataServices.RequestException failedException;

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            if (failedException != null) {
                Toast.makeText(getActivity(), failedException.getMessage(), Toast.LENGTH_SHORT).show();
            } else if (!success) {
                Toast.makeText(getActivity(), "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
            } else {
                listener.popAndUpdateList();
            }
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                DataServices.createForum(strings[0], strings[1], strings[2]);
                return true;
            } catch (DataServices.RequestException e) {
                e.printStackTrace();
            }
            return false;
        }
    }


}