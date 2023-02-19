package com.example.inclass09;

import androidx.room.Database;
import androidx.room.RoomDatabase;

// InClass09
// AppDatabase
// Issac Helms
@Database(version = 2, entities = {Grade.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract GradesDAO gradesDAO();
}
