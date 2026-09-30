package com.example.drawerlayout;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface InstrumentoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void inserir(List<Instrumento> instrumentos);

    @Query("SELECT * FROM instrumentos WHERE id = :id LIMIT 1")
    LiveData<Instrumento> observarPorId(long id);
}