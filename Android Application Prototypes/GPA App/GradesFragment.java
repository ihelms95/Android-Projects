package com.example.inclass09;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

// InClass09
// GradesFragment
// Issac Helms
public class GradesFragment extends Fragment {

    RecyclerView recyclerView;
    GradesListener listener;
    GradesAdapter adapter;
    LinearLayoutManager layoutManager;
    ArrayList<Grade> grades;
    AppDatabase db;
    TextView textViewGradesGPA;
    TextView textViewGradesHours;

    public GradesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Grades");
        View view = inflater.inflate(R.layout.fragment_grades, container, false);

        db = Room.databaseBuilder(getContext(), AppDatabase.class, "grade.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        /*db.gradesDAO().insertAll(new Grade("ITIS 4180", "Mobile Application Development", 3, 'A'),
                new Grade("ITIS 4221", "Security and Penetration Testing", 2, 'B'),
                new Grade("ITSC 4155", "Software Development Projects", 1, 'C'),
                new Grade("LBST 1105", "Visual Arts", 4, 'D'),
                new Grade("MUPF 1114", "Basketball Band", 0, 'F'));*/

        grades = (ArrayList<Grade>) db.gradesDAO().getAll();

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new GradesAdapter(GradesFragment.this::refreshList);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.buttonAddNewGrade).setOnClickListener(v -> listener.toAddCourse());

        textViewGradesGPA = view.findViewById(R.id.textViewGradesGPA);
        textViewGradesHours = view.findViewById(R.id.textViewGradesHours);

        calcGPA();

        return view;
    }

    public void calcGPA() {
        int points = 0;
        int creditHours = 0;
        for (Grade g : grades) {
            points += g.creditHours * letterToGrade(g.letterGrade);
            creditHours += g.creditHours;
        }
        if (!(creditHours == 0)) {
            double gpa = (double) ((double) points / (double) creditHours);
            textViewGradesGPA.setText(String.format("GPA: %s", new DecimalFormat("#.##").format(gpa)));
        } else {
            textViewGradesGPA.setText(String.format("GPA: %s", 0));
        }
        textViewGradesHours.setText(String.format("Hours: %s.0", creditHours));
    }

    public int letterToGrade(char letter) {
        switch (letter) {
            case 'A':
                return 4;
            case 'B':
                return 3;
            case 'C':
                return 2;
            case 'D':
                return 1;
            case 'F':
            default:
                return 0;
        }
    }

    public void refreshList() {
        grades = (ArrayList<Grade>) db.gradesDAO().getAll();
        calcGPA();
        adapter.notifyDataSetChanged();
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof GradesListener) {
            listener = (GradesListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement GradesListener");
        }
    }

    interface GradesListener {
        void toAddCourse();
    }

    class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.GradesViewHolder> {

        GradesAdapterListener adapterListener;

        public GradesAdapter(GradesAdapterListener adapterListener) {
            this.adapterListener = adapterListener;
        }

        @NonNull
        @Override
        public GradesAdapter.GradesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grades_list_item, parent, false);
            return new GradesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GradesAdapter.GradesViewHolder holder, int position) {
            Grade grade = grades.get(position);
            holder.textViewListCourseNumber.setText(grade.courseNumber);
            holder.textViewListCourseName.setText(grade.courseName);
            holder.textViewListCreditHours.setText(String.format("%s Credit Hours", grade.creditHours));
            holder.textViewListLetterGrade.setText(String.valueOf(grade.letterGrade));
            holder.imageViewTrash.setOnClickListener(v -> {
                db.gradesDAO().delete(grade);
                adapterListener.refreshGrades();
            });
        }

        @Override
        public int getItemCount() { return grades.size(); }

        class GradesViewHolder extends RecyclerView.ViewHolder {
            TextView textViewListCourseNumber;
            TextView textViewListCourseName;
            TextView textViewListCreditHours;
            TextView textViewListLetterGrade;
            ImageView imageViewTrash;

            public GradesViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewListCourseNumber = itemView.findViewById(R.id.textViewListCourseNumber);
                textViewListCourseName = itemView.findViewById(R.id.textViewListCourseName);
                textViewListCreditHours = itemView.findViewById(R.id.textViewListCreditHours);
                textViewListLetterGrade = itemView.findViewById(R.id.textViewListLetterGrade);
                imageViewTrash = itemView.findViewById(R.id.imageViewTrash);
            }
        }
    }

    interface GradesAdapterListener {
        void refreshGrades();
    }
}