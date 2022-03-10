package com.example.passcode.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PinModel {

    @PrimaryKey(autoGenerate = true)
    public int user_id;

    @ColumnInfo(name = "pin")
    public String pin;

    @ColumnInfo(name = "hint")
    public String hint;

}
