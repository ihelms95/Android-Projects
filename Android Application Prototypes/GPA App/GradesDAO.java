package com.example.inclass09;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// InClass09
// GradesDAO
// Issac Helms
@Dao
public interface GradesDAO {

    @Query("SELECT * from grades")
    List<Grade> getAll();

    @Query("SELECT * from grades WHERE _id = :id limit 1")
    Grade findById(long id);

    @Update
    void update(Grade grade);

    @Insert
    void insertAll(Grade... grades);

    @Delete
    void delete(Grade grade);
}
