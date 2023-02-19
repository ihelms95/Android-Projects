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
// ForumsRecyclerViewAdapter
// Issac Helms
public class ForumsRecyclerViewAdapter extends RecyclerView.Adapter<ForumsRecyclerViewAdapter.ForumsViewHolder> {

    ArrayList<DataServices.Forum> forums;
    ForumsFragment.ForumsListener listener;
    DataServices.AuthResponse response;
    ForumsRecyclerViewAdapterListener adapterListener;

    public ForumsRecyclerViewAdapter(ArrayList<DataServices.Forum> forums, ForumsFragment.ForumsListener listener, DataServices.AuthResponse response, ForumsRecyclerViewAdapterListener adapterListener) {
        this.forums = forums;
        this.listener = listener;
        this.response = response;
        this.adapterListener = adapterListener;
    }

    @NonNull
    @Override
    public ForumsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forums_list_row, parent, false);
        return new ForumsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumsViewHolder holder, int position) {
        DataServices.Forum forum = forums.get(position);
        holder.forum = forum;
        holder.textViewListForumName.setText(forum.getTitle());
        holder.textViewListUserName.setText(forum.getCreatedBy().getName());

        if (forum.getDescription().length() > 200) {
            holder.textViewListForumDescription.setText(String.format("%s...", forum.getDescription().substring(0, 199)));
        } else {
            holder.textViewListForumDescription.setText(forum.getDescription());
        }

        holder.textViewListForumLikes.setText(String.format(forum.getLikedBy().size() == 1 ? "%s Like" : "%s Likes", forum.getLikedBy().size()));
        holder.textViewListForumDate.setText(new SimpleDateFormat("MM/dd/yyyy h:mma", Locale.getDefault()).format(forum.getCreatedAt().getTime()));

        if (forum.getCreatedBy().uid != response.account.uid) {
            holder.imageViewListTrashCan.setEnabled(false);
            holder.imageViewListTrashCan.setVisibility(View.INVISIBLE);
        } else {
            holder.imageViewListTrashCan.setEnabled(true);
            holder.imageViewListTrashCan.setVisibility(View.VISIBLE);
        }

        if (forum.getLikedBy().contains(response.account)) {
            holder.imageViewLikeImage.setImageResource(R.drawable.like_favorite);
            holder.likedPost = true;
        } else {
            holder.imageViewLikeImage.setImageResource(R.drawable.like_not_favorite);
            holder.likedPost = false;
        }

        holder.listener = listener;
        holder.response = response;
    }

    @Override
    public int getItemCount() {
        return forums.size();
    }

    public class ForumsViewHolder extends RecyclerView.ViewHolder {

        DataServices.Forum forum;
        TextView textViewListForumName;
        TextView textViewListUserName;
        TextView textViewListForumDescription;
        TextView textViewListForumLikes;
        TextView textViewListForumDate;
        ImageView imageViewLikeImage;
        ImageView imageViewListTrashCan;
        boolean likedPost;
        ForumsFragment.ForumsListener listener;
        DataServices.AuthResponse response;

        public ForumsViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewListForumName = itemView.findViewById(R.id.textViewListForumName);
            textViewListUserName = itemView.findViewById(R.id.textViewListUserName);
            textViewListForumDescription = itemView.findViewById(R.id.textViewListForumDescription);
            textViewListForumLikes = itemView.findViewById(R.id.textViewListForumLikes);
            textViewListForumDate = itemView.findViewById(R.id.textViewListForumDate);
            imageViewLikeImage = itemView.findViewById(R.id.imageViewListLike);
            imageViewListTrashCan = itemView.findViewById(R.id.imageViewListTrashCan);

            itemView.findViewById(R.id.imageViewListTrashCan).setOnClickListener(v -> {
                new DeleteForumAsync().execute(response);
                adapterListener.refreshList();
            });

            itemView.findViewById(R.id.imageViewListLike).setOnClickListener(v -> {
                if (likedPost) {
                    new UnlikeForumAsync().execute(response);
                } else {
                    new LikeForumAsync().execute(response);
                }
                adapterListener.refreshList();
            });

            itemView.setOnClickListener(v -> listener.toForum(forum, response));

        }

        class LikeForumAsync extends AsyncTask<DataServices.AuthResponse, Integer, Boolean> {
            @Override
            protected Boolean doInBackground(DataServices.AuthResponse... responses) {
                try {
                    DataServices.likeForum(responses[0].token, forum.getForumId());
                } catch (DataServices.RequestException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }

        class UnlikeForumAsync extends AsyncTask<DataServices.AuthResponse, Integer, Boolean> {
            @Override
            protected Boolean doInBackground(DataServices.AuthResponse... responses) {
                try {
                    DataServices.unLikeForum(responses[0].token, forum.getForumId());
                } catch (DataServices.RequestException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }

        class DeleteForumAsync extends AsyncTask<DataServices.AuthResponse, Integer, Boolean> {
            @Override
            protected Boolean doInBackground(DataServices.AuthResponse... responses) {
                try {
                    DataServices.deleteForum(responses[0].token, forum.getForumId());
                } catch (DataServices.RequestException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
    }

    interface ForumsRecyclerViewAdapterListener {
        void refreshList();
    }
}
