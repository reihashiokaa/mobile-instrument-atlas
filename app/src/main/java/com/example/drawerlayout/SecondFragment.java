package com.example.drawerlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.drawerlayout.databinding.FragmentSecondBinding;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    // mesmo ViewModel usado pelos outros fragments
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // pega o ViewModel compartilhado pela MainActivity
        sharedViewModel = new ViewModelProvider(requireActivity())
                .get(SharedViewModel.class);

        // acompanha a família escolhida no primeiro fragment
        sharedViewModel.getFamiliaSelecionada().observe(
                getViewLifecycleOwner(),
                familia -> {

                    // atualiza a lista sempre que a família mudar
                    carregarInstrumentos(familia);
                }
        );
    }

    private void carregarInstrumentos(String familia) {

        // lista que vai receber os instrumentos da família escolhida
        List<ItemModel> instrumentos = new ArrayList<>();

        if (familia.equals("Corda")) {

            instrumentos.add(new ItemModel(
                    R.drawable.bmw_m135i_3,
                    "Instrumento de corda 1",
                    "Descrição 1"
            ));

            instrumentos.add(new ItemModel(
                    R.drawable.bmw_m135i_3,
                    "Instrumento de corda 2",
                    "Descrição 2"
            ));

            instrumentos.add(new ItemModel(
                    R.drawable.bmw_m135i_3,
                    "Instrumento de corda 3",
                    "Descrição 3"
            ));

        } else if (familia.equals("Sopro")) {

            instrumentos.add(new ItemModel(
                    R.drawable.ferrari_488_2,
                    "Instrumento de sopro 1",
                    "Descrição 1"
            ));

            instrumentos.add(new ItemModel(
                    R.drawable.ferrari_488_2,
                    "Instrumento de sopro 2",
                    "Descrição 2"
            ));

            instrumentos.add(new ItemModel(
                    R.drawable.ferrari_488_2,
                    "Instrumento de sopro 3",
                    "Descrição 3"
            ));

        } else if (familia.equals("Percussão")) {

            instrumentos.add(new ItemModel(
                    R.drawable.ford_shelby_6,
                    "Instrumento de percussão 1",
                    "Descrição 1"
            ));

            instrumentos.add(new ItemModel(
                    R.drawable.ford_shelby_6,
                    "Instrumento de percussão 2",
                    "Descrição 2"
            ));

            instrumentos.add(new ItemModel(
                    R.drawable.ford_shelby_6,
                    "Instrumento de percussão 3",
                    "Descrição 3"
            ));
        }

        // envia a lista pronta para o ListView
        MeuAdapter adapter =
                new MeuAdapter(requireContext(), instrumentos);

        binding.listViewImagens.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // evita manter referência da view depois que o fragment for destruído
        binding = null;
    }
}