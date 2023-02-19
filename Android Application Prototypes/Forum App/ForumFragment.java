package com.example.midterm;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

// Forum App
// ForumFragment
// Issac Helms
public class ForumFragment extends Fragment {

    private static final String ARG_PARAM_FORUM = "ARG_PARAM_FORUM";
    private static final String ARG_PARAM_RESPONSE = "ARG_PARAM_RESPONSE";

    private DataServices.Forum mForum;
    private DataServices.AuthResponse mResponse;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ForumCommentRecyclerViewAdapter adapter;

    TextView textViewCommentCount;

    public ForumFragment() {
        // Required empty public constructor
    }

    public static ForumFragment newInstance(DataServices.Forum forum, DataServices.AuthResponse response) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_FORUM, forum);
        args.putSerializable(ARG_PARAM_RESPONSE, response);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mForum = (DataServices.Forum) getArguments().getSerializable(ARG_PARAM_FORUM);
            mResponse = (DataServices.AuthResponse) getArguments().getSerializable(ARG_PARAM_RESPONSE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Forum");
        View view = inflater.inflate(R.layout.fragment_forum, container, false);

        TextView textViewForumPostName = view.findViewById(R.id.textViewForumPostName);
        TextView textViewForumAuthorName = view.findViewById(R.id.textViewForumAuthorName);
        TextView textViewForumDescriptionText = view.findViewById(R.id.textViewForumDescriptionText);
        textViewCommentCount = view.findViewById(R.id.textViewCommentCount);
        EditText editTextComment = view.findViewById(R.id.editTextComment);

        textViewForumPostName.setText(mForum.getTitle());
        textViewForumAuthorName.setText(mForum.getCreatedBy().getName());
        textViewForumDescriptionText.setText(mForum.getDescription());

        recyclerView = view.findViewById(R.id.commentRecyclerView);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        new LoadCommentsAsync().execute(mResponse.token);

        view.findViewById(R.id.buttonPostComment).setOnClickListener(v -> {
            String comment = editTextComment.getText().toString();

            if (comment.isEmpty()) {
                Toast.makeText(getContext(), "Comment is empty", Toast.LENGTH_SHORT).show();
            } else {
                new PostCommentAsync().execute(mResponse.token, comment);
            }
            editTextComment.setText("");
        });

        return view;
    }

    public void onLoading() {
        new LoadCommentsAsync().execute(mResponse.token);
        adapter.notifyDataSetChanged();
    }

    public class LoadCommentsAsync extends AsyncTask<String, Integer, ArrayList<DataServices.Comment>> {
        DataServices.RequestException failedException;

        @Override
        protected void onPostExecute(ArrayList<DataServices.Comment> comments) {
            super.onPostExecute(comments);

            if (failedException != null) {
                Toast.makeText(getActivity(), failedException.getMessage(), Toast.LENGTH_SHORT).show();
            } else if (comments == null) {
                Toast.makeText(getActivity(), "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
            } else {
                adapter = new ForumCommentRecyclerViewAdapter(comments, mResponse, mForum, ForumFragment.this::onLoading);
                recyclerView.setAdapter(adapter);
                textViewCommentCount.setText(String.format(comments.size() == 1 ? "%s comment" : "%s comments", comments.size()));
            }
        }

        @Override
        protected ArrayList<DataServices.Comment> doInBackground(String... strings) {
            try {
                return DataServices.getForumComments(strings[0], mForum.getForumId());
            } catch (DataServices.RequestException e) {
                failedException = e;
                e.printStackTrace();
            }
            return null;
        }
    }

    public class PostCommentAsync extends AsyncTask<String, Integer, Boolean> {
        DataServices.RequestException failedException;

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            if (failedException != null) {
                Toast.makeText(getActivity(), failedException.getMessage(), Toast.LENGTH_SHORT).show();
            } else if (!success) {
                Toast.makeText(getActivity(), "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
            } else {
                onLoading();
            }
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                DataServices.createComment(strings[0], mForum.getForumId(), strings[1]);
                return true;
            } catch (DataServices.RequestException e) {
                failedException = e;
                e.printStackTrace();
            }
            return false;
        }
    }


}