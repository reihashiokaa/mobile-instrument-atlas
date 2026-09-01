package com.example.drawerlayout;

import android.content.Intent;
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
    public void onViewCreated(
            @NonNull View view,
            Bundle savedInstanceState
    ) {

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

        // famílias definidas no strings.xml
        String[] familias = getResources()
                .getStringArray(R.array.familia);

        // lista que recebe os instrumentos da família escolhida
        List<ItemModel> instrumentos = new ArrayList<>();

        // CORDA
        if (familia.equals(familias[0])) {

            instrumentos.add(new ItemModel(
                    R.drawable.violino,
                    getString(R.string.violino_nome),
                    getString(R.string.violino_descricao),
                    getString(R.string.violino_detalhes),
                    R.raw.violino
            ));

            instrumentos.add(new ItemModel(
                    R.drawable.violao,
                    getString(R.string.violao_nome),
                    getString(R.string.violao_descricao),
                    getString(R.string.violao_detalhes),
                    R.raw.violao
            ));

            instrumentos.add(new ItemModel(
                    R.drawable.harpa,
                    getString(R.string.harpa_nome),
                    getString(R.string.harpa_descricao),
                    getString(R.string.harpa_detalhes),
                    R.raw.harpa
            ));

        }

        // SOPRO
        else if (familia.equals(familias[1])) {

            instrumentos.add(new ItemModel(
                    R.drawable.flauta,
                    getString(R.string.flauta_nome),
                    getString(R.string.flauta_descricao),
                    getString(R.string.flauta_detalhes),
                    R.raw.flauta
            ));

            instrumentos.add(new ItemModel(
                    R.drawable.clarinete,
                    getString(R.string.clarinete_nome),
                    getString(R.string.clarinete_descricao),
                    getString(R.string.clarinete_detalhes),
                    R.raw.clarinete
            ));

            instrumentos.add(new ItemModel(
                    R.drawable.trompete,
                    getString(R.string.trompete_nome),
                    getString(R.string.trompete_descricao),
                    getString(R.string.trompete_detalhes),
                    R.raw.trompete
            ));

        }

        // PERCUSSÃO
        else if (familia.equals(familias[2])) {

            instrumentos.add(new ItemModel(
                    R.drawable.bateria,
                    getString(R.string.bateria_nome),
                    getString(R.string.bateria_descricao),
                    getString(R.string.bateria_detalhes),
                    R.raw.bateria
            ));

            instrumentos.add(new ItemModel(
                    R.drawable.tambor,
                    getString(R.string.tambor_nome),
                    getString(R.string.tambor_descricao),
                    getString(R.string.tambor_detalhes),
                    R.raw.tambor
            ));

            instrumentos.add(new ItemModel(
                    R.drawable.pandeiro,
                    getString(R.string.pandeiro_nome),
                    getString(R.string.pandeiro_descricao),
                    getString(R.string.pandeiro_detalhes),
                    R.raw.pandeiro
            ));
        }

        // envia a lista pronta para o ListView
        MeuAdapter adapter =
                new MeuAdapter(requireContext(), instrumentos);

        binding.listViewImagens.setAdapter(adapter);

        // abre a tela de detalhes ao clicar em um instrumento
        binding.listViewImagens.setOnItemClickListener(
                (parent, view, position, id) -> {

                    ItemModel instrumento =
                            instrumentos.get(position);

                    Intent intent =
                            new Intent(
                                    requireContext(),
                                    DetailActivity.class
                            );

                    intent.putExtra(
                            "nome",
                            instrumento.getNome()
                    );

                    intent.putExtra(
                            "descricao",
                            instrumento.getDescricao()
                    );

                    intent.putExtra(
                            "detalhes",
                            instrumento.getDetalhes()
                    );

                    intent.putExtra(
                            "imagem",
                            instrumento.getImagemResId()
                    );

                    intent.putExtra(
                            "som",
                            instrumento.getSomResId()
                    );

                    startActivity(intent);
                }
        );
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        // evita manter referência da view depois que o fragment for destruído
        binding = null;
    }
}