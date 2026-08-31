package com.example.drawerlayout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    // guarda qual família foi escolhida no Spinner
    private final MutableLiveData<String> familiaSelecionada =
            new MutableLiveData<>("Corda");

    // permite que os fragments acompanhem mudanças na família
    public LiveData<String> getFamiliaSelecionada() {
        return familiaSelecionada;
    }

    // atualiza a família quando o usuário muda o Spinner
    public void setFamiliaSelecionada(String familia) {
        familiaSelecionada.setValue(familia);
    }
}