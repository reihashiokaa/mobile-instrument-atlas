package com.example.drawerlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.drawerlayout.databinding.FragmentSecondBinding;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private Spinner spinner;
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        switch (spinner.getSelectedItemPosition()) {
            case 0:
                List<ItemModel> cordas = new ArrayList<>();

                cordas.add(new ItemModel(
                        R.drawable.bmw_m135i_3,
                        "Imagem 1",
                        "Descricao 1"
                ));

                cordas.add(new ItemModel(
                        R.drawable.bmw_m135i_3,
                        "Imagem 2",
                        "Descricao 2"
                ));

                cordas.add(new ItemModel(
                        R.drawable.bmw_m135i_3,
                        "Imagem 3",
                        "Descricao 3"
                ));

                MeuAdapter adapter = new MeuAdapter(requireContext(), cordas);

                binding.listViewImagens.setAdapter(adapter);
                break;
            case 1:
                List<ItemModel> sopro = new ArrayList<>();

                sopro.add(new ItemModel(
                        R.drawable.ferrari_488_2,
                        "Imagem 1",
                        "Descricao 1"
                ));

                sopro.add(new ItemModel(
                        R.drawable.ferrari_488_2,
                        "Imagem 2",
                        "Descricao 2"
                ));

                sopro.add(new ItemModel(
                        R.drawable.ferrari_488_2,
                        "Imagem 3",
                        "Descricao 3"
                ));

                MeuAdapter adapter1 = new MeuAdapter(requireContext(), sopro);

                binding.listViewImagens.setAdapter(adapter1);
                break;
            case 2:
                List<ItemModel> percussao = new ArrayList<>();

                percussao.add(new ItemModel(
                        R.drawable.ford_shelby_6,
                        "Imagem 1",
                        "Descricao 1"
                ));

                percussao.add(new ItemModel(
                        R.drawable.ford_shelby_6,
                        "Imagem 2",
                        "Descricao 2"
                ));

                percussao.add(new ItemModel(
                        R.drawable.ford_shelby_6,
                        "Imagem 3",
                        "Descricao 3"
                ));

                MeuAdapter adapter2 = new MeuAdapter(requireContext(), percussao);

                binding.listViewImagens.setAdapter(adapter2);                break;


        }

        /*List<ItemModel> dados = new ArrayList<>();

        dados.add(new ItemModel(
                R.drawable.bmw_m135i_3,
                "Imagem 1",
                "Descricao 1"
        ));

        dados.add(new ItemModel(
                R.drawable.ferrari_488_2,
                "Imagem 2",
                "Descricao 2"
        ));

        dados.add(new ItemModel(
                R.drawable.ford_shelby_6,
                "Imagem 3",
                "Descricao 3"
        ));

        MeuAdapter adapter = new MeuAdapter(requireContext(), dados);

        binding.listViewImagens.setAdapter(adapter);*/
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}