package com.example.midterm;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

// Forum App
// ForumCommentRecyclerViewAdapter
// Issac Helms
public class ForumCommentRecyclerViewAdapter extends RecyclerView.Adapter<ForumCommentRecyclerViewAdapter.ForumCommentViewHolder> {

    ArrayList<DataServices.Comment> comments;
    DataServices.AuthResponse response;
    DataServices.Forum forum;
    ForumCommentRecyclerViewAdapterListener adapterListener;

    public ForumCommentRecyclerViewAdapter(ArrayList<DataServices.Comment> comments, DataServices.AuthResponse response, DataServices.Forum forum, ForumCommentRecyclerViewAdapterListener adapterListener) {
        this.comments = comments;
        this.response = response;
        this.forum = forum;
        this.adapterListener = adapterListener;
    }

    @NonNull
    @Override
    public ForumCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_list_row, parent, false);
        return new ForumCommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumCommentViewHolder holder, int position) {
        DataServices.Comment comment = comments.get(position);
        holder.comment = comment;
        holder.token = response.token;
        holder.forum = forum;
        holder.textViewCommentName.setText(comment.createdBy.getName());
        holder.textViewComment.setText(comment.text);
        holder.textViewCommentDate.setText(new SimpleDateFormat("MM/dd/yyyy h:mma", Locale.getDefault()).format(comment.getCreatedAt().getTime()));

        if (comment.getCreatedBy().uid != response.account.uid) {
            holder.imageViewCommentTrash.setEnabled(false);
            holder.imageViewCommentTrash.setVisibility(View.INVISIBLE);
        } else {
            holder.imageViewCommentTrash.setEnabled(true);
            holder.imageViewCommentTrash.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() { return comments.size(); }

    public class ForumCommentViewHolder extends RecyclerView.ViewHolder {

        DataServices.Comment comment;
        DataServices.Forum forum;
        TextView textViewCommentName;
        TextView textViewComment;
        TextView textViewCommentDate;
        ImageView imageViewCommentTrash;
        String token;

        public ForumCommentViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewCommentName = itemView.findViewById(R.id.textViewCommentName);
            textViewComment = itemView.findViewById(R.id.textViewComment);
            textViewCommentDate = itemView.findViewById(R.id.textViewCommentDate);
            imageViewCommentTrash = itemView.findViewById(R.id.imageViewCommentTrash);

            itemView.findViewById(R.id.imageViewCommentTrash).setOnClickListener(v -> {
                new DeleteCommentAsync().execute(token);
                adapterListener.refreshList();
            });
        }

        class DeleteCommentAsync extends AsyncTask<String, Integer, Boolean> {
            @Override
            protected Boolean doInBackground(String... strings) {
                try {
                    DataServices.deleteComment(strings[0], forum.getForumId(), comment.getCommentId());
                } catch (DataServices.RequestException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
    }

    interface ForumCommentRecyclerViewAdapterListener {
        void refreshList();
    }

}
