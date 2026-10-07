package com.example.drawerlayout;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;

public class FamiliaComInstrumentos {

    @Embedded public Familia familia;

    @Relation(parentColumn = "id", entityColumn = "familia_id")
    public List<Instrumento> instrumentos;
}
