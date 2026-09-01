package com.example.drawerlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.drawerlayout.databinding.FragmentThirdBinding;

import java.util.ArrayList;
import java.util.List;

public class ThirdFragment extends Fragment {

    private FragmentThirdBinding binding;
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentThirdBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // usa o mesmo ViewModel compartilhado pelos fragments
        sharedViewModel =
                new ViewModelProvider(requireActivity())
                        .get(SharedViewModel.class);

        // atualiza a grade quando a família do Spinner mudar
        sharedViewModel.getFamiliaSelecionada()
                .observe(getViewLifecycleOwner(), this::atualizarGrid);
    }

    private void atualizarGrid(String familia) {

        List<ItemModel> itens = new ArrayList<>();

        if (familia.equals("Corda")) {

            itens.add(new ItemModel(
                    R.drawable.violino_eletrico,
                    "Violino elétrico",
                    "Variação moderna do violino que utiliza captação eletrônica."
            ));

            itens.add(new ItemModel(
                    R.drawable.violao_12_cordas,
                    "Violão de 12 cordas",
                    "Violão com pares de cordas que produz um som mais cheio e brilhante."
            ));

            itens.add(new ItemModel(
                    R.drawable.harpa_celta,
                    "Harpa celta",
                    "Tipo de harpa tradicional de tamanho menor, associada à música celta."
            ));

        } else if (familia.equals("Sopro")) {

            itens.add(new ItemModel(
                    R.drawable.flauta_piccolo,
                    "Flauta piccolo",
                    "Pequena flauta de som bastante agudo, muito utilizada em orquestras."
            ));

            itens.add(new ItemModel(
                    R.drawable.clarinete_baixo,
                    "Clarinete baixo",
                    "Versão maior e mais grave do clarinete tradicional."
            ));

            itens.add(new ItemModel(
                    R.drawable.trompete_piccolo,
                    "Trompete piccolo",
                    "Versão menor do trompete, conhecida por alcançar notas mais agudas."
            ));

        } else if (familia.equals("Percussão")) {

            itens.add(new ItemModel(
                    R.drawable.bateria_eletronica,
                    "Bateria eletrônica",
                    "Conjunto eletrônico que reproduz sons de bateria por meio de pads."
            ));

            itens.add(new ItemModel(
                    R.drawable.tambor_africano,
                    "Tambor africano",
                    "Instrumento tradicional de percussão presente em diferentes culturas africanas."
            ));

            itens.add(new ItemModel(
                    R.drawable.pandeiro_meia_lua,
                    "Pandeiro meia-lua",
                    "Instrumento de percussão com formato semicircular e pequenas platinelas metálicas."
            ));
        }

        GridAdapter adapter =
                new GridAdapter(requireContext(), itens);

        binding.gridView.setAdapter(adapter);

        // abre a tela de detalhes ao clicar em um item da galeria
        binding.gridView.setOnItemClickListener((parent, view, position, id) -> {

            ItemModel item = itens.get(position);

            Intent intent = new Intent(requireContext(), DetailActivity.class);

            intent.putExtra("nome", item.getNome());
            intent.putExtra("descricao", item.getDescricao());
            intent.putExtra("imagem", item.getImagemResId());

            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}