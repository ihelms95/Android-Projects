package com.example.inclass09;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// InClass09
// Grade
// Issac Helms
@Entity(tableName = "grades")
public class Grade {

    @PrimaryKey(autoGenerate = true)
    public long _id;

    @ColumnInfo(name="course_number")
    public String courseNumber;

    @ColumnInfo(name = "course_Name")
    public String courseName;

    @ColumnInfo(name = "credit_hours")
    public int creditHours;

    @ColumnInfo(name = "letter_grade")
    public char letterGrade;

    public Grade() {}

    public Grade(long id, String courseNumber, String courseName, int creditHours, char letterGrade) {
        this._id = id;
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.letterGrade = letterGrade;
    }

    public Grade(String courseNumber, String courseName, int creditHours, char letterGrade) {
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.letterGrade = letterGrade;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "_id=" + _id +
                ", courseNumber='" + courseNumber + '\'' +
                ", courseName='" + courseName + '\'' +
                ", creditHours='" + creditHours + '\'' +
                ", letterGrade='" + letterGrade + '\'' +
                '}';
    }
}
