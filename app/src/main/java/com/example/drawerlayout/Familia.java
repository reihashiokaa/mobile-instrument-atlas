package com.example.drawerlayout;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "familias")
public class Familia {

    @PrimaryKey public long id;

    @NonNull public String nome = "";

    public int ordem;

    @NonNull
    @Override
    public String toString() {
        return nome;
    }
}
