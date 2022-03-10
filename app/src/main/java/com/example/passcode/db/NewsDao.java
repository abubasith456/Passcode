package com.example.passcode.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.passcode.model.PinModel;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM pinmodel")
    List<PinModel> getPin();

    @Insert
    void insertPin(PinModel... pin);

    @Update
    void updatePin(PinModel... pin);

    @Delete
    void delete(PinModel pin);
}
