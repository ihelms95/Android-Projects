package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// Forum App
// MainActivity
// Issac Helms
public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener,
        RegisterFragment.RegisterListener, ForumsFragment.ForumsListener, NewForumFragment.NewForumFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void toRegister() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new RegisterFragment())
                .commit();
    }

    @Override
    public void cancelRegistration() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void toForums(DataServices.AuthResponse response) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, ForumsFragment.newInstance(response))
                .commit();
    }

    @Override
    public void doLogout() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void toForum(DataServices.Forum forum, DataServices.AuthResponse response) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, ForumFragment.newInstance(forum, response))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void toCreateForum(String token) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, NewForumFragment.newInstance(token))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void popAndUpdateList() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void cancelNewForum() {
        getSupportFragmentManager().popBackStack();
    }
}