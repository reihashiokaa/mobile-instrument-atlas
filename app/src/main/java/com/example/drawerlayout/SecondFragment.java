package com.example.drawerlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;

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
                    R.drawable.violino,
                    "Violino",
                    "Instrumento de cordas tocado com arco, conhecido por seu som expressivo e agudo."
            ));

            instrumentos.add(new ItemModel(
                    R.drawable.violao,
                    "Violão",
                    "Instrumento de cordas dedilhadas muito utilizado em diferentes estilos musicais."
            ));

            instrumentos.add(new ItemModel(
                    R.drawable.harpa,
                    "Harpa",
                    "Instrumento de cordas tocado com os dedos, conhecido por seu som suave e característico."
            ));

        } else if (familia.equals("Sopro")) {

            instrumentos.add(new ItemModel(
                    R.drawable.flauta,
                    "Flauta",
                    "Instrumento de sopro que produz som pela passagem de ar e possui timbre leve e agudo."
            ));

            instrumentos.add(new ItemModel(
                    R.drawable.clarinete,
                    "Clarinete",
                    "Instrumento de sopro de palheta simples, conhecido por sua grande variedade de tons."
            ));

            instrumentos.add(new ItemModel(
                    R.drawable.trompete,
                    "Trompete",
                    "Instrumento de metal com som forte e brilhante, muito utilizado em bandas e orquestras."
            ));

        } else if (familia.equals("Percussão")) {

            instrumentos.add(new ItemModel(
                    R.drawable.bateria,
                    "Bateria",
                    "Conjunto de instrumentos de percussão utilizado para criar ritmo e acompanhar músicas."
            ));

            instrumentos.add(new ItemModel(
                    R.drawable.tambor,
                    "Tambor",
                    "Instrumento de percussão que produz som pela vibração de uma membrana ao ser golpeada."
            ));

            instrumentos.add(new ItemModel(
                    R.drawable.pandeiro,
                    "Pandeiro",
                    "Instrumento de percussão de mão muito presente em diversos estilos da música brasileira."
            ));
        }

        // envia a lista pronta para o ListView
        MeuAdapter adapter =
                new MeuAdapter(requireContext(), instrumentos);

        binding.listViewImagens.setAdapter(adapter);

        // abre a tela de detalhes ao clicar em um instrumento
        binding.listViewImagens.setOnItemClickListener((parent, view, position, id) -> {

            ItemModel instrumento = instrumentos.get(position);

            Intent intent = new Intent(requireContext(), DetailActivity.class);

            intent.putExtra("nome", instrumento.getNome());
            intent.putExtra("descricao", instrumento.getDescricao());
            intent.putExtra("imagem", instrumento.getImagemResId());

            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // evita manter referência da view depois que o fragment for destruído
        binding = null;
    }
}