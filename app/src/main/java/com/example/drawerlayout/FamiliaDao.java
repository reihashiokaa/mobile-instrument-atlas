package com.example.drawerlayout;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import java.util.List;

@Dao
public interface FamiliaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void inserir(List<Familia> familias);

    @Query("SELECT * FROM familias ORDER BY ordem ASC")
    LiveData<List<Familia>> observarFamilias();

    @Transaction
    @Query("SELECT * FROM familias WHERE id = :familiaId LIMIT 1")
    LiveData<FamiliaComInstrumentos> observarFamiliaComInstrumentos(long familiaId);
}
