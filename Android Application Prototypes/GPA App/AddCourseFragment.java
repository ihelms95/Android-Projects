package com.example.inclass09;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.ArrayList;


public class AddCourseFragment extends Fragment {
    AppDatabase db;
    ArrayList<Grade> grades;
    AddCourseFragmentListener listener;




    public AddCourseFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_add_course, container, false);
        db = Room.databaseBuilder(getContext(), AppDatabase.class, "grade.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        getActivity().setTitle("Add Course");
        EditText editTextCNumber = view.findViewById(R.id.editTextCNumber);
        EditText editTextCName = view.findViewById(R.id.editTextCName);
        EditText editTextCHours = view.findViewById(R.id.editTextCHours);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroupLetter);


        view.findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextCNumber.getText().toString().isEmpty()) {
                    new AlertDialog.Builder(getContext())
                            .setMessage("Course Number field is empty.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                    return;
                } else if (editTextCName.getText().toString().isEmpty()) {
                    new AlertDialog.Builder(getContext())
                            .setMessage("Course Name field is empty.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                    return;
                } else if (editTextCHours.getText().toString().isEmpty()) {
                    new AlertDialog.Builder(getContext())
                            .setMessage("Credit Hour Field is Empty")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                    return;
                } else if (radioGroup.getCheckedRadioButtonId() == -1) {
                    new AlertDialog.Builder(getContext())
                            .setMessage("Grade not selected.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                    return;
                }
                String type = "";
                if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonA) {
                    type = "A";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonB) {
                    type = "B";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonC) {
                    type = "C";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonD) {
                    type = "D";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonF) {
                    type = "F";
                }


                db.gradesDAO().insertAll(new Grade(editTextCNumber.getText().toString(), editTextCName.getText().toString(), Integer.parseInt(editTextCHours.getText().toString()), type.charAt(0)));
                listener.toGradesFrag();
            }

        });
        view.findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.toGradesFrag();
            }
        });




       return view;
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof AddCourseFragmentListener) {
            listener = (AddCourseFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement GradesListener");
        }
    }

    interface AddCourseFragmentListener {
        void toGradesFrag();
    }
}