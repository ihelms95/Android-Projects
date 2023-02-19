package com.example.inclass09;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// InClass09
// MainActivity
// Issac Helms
public class MainActivity extends AppCompatActivity implements GradesFragment.GradesListener, AddCourseFragment.AddCourseFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new GradesFragment())
                .commit();
    }

    @Override
    public void toAddCourse() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new AddCourseFragment())
                .commit();
    }

    @Override
    public void toGradesFrag() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new GradesFragment())
                .commit();
    }
}