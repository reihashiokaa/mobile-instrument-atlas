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

        // famílias definidas no strings.xml
        String[] familias = getResources()
                .getStringArray(R.array.familia);

        // lista que vai receber as variações da família escolhida
        List<ItemModel> itens = new ArrayList<>();

        if (familia.equals(familias[0])) {

            itens.add(new ItemModel(
                    R.drawable.violino_eletrico,
                    getString(R.string.violino_eletrico_nome),
                    getString(R.string.violino_eletrico_descricao)
            ));

            itens.add(new ItemModel(
                    R.drawable.violao_12_cordas,
                    getString(R.string.violao_12_cordas_nome),
                    getString(R.string.violao_12_cordas_descricao)
            ));

            itens.add(new ItemModel(
                    R.drawable.harpa_celta,
                    getString(R.string.harpa_celta_nome),
                    getString(R.string.harpa_celta_descricao)
            ));

        } else if (familia.equals(familias[1])) {

            itens.add(new ItemModel(
                    R.drawable.flauta_piccolo,
                    getString(R.string.flauta_piccolo_nome),
                    getString(R.string.flauta_piccolo_descricao)
            ));

            itens.add(new ItemModel(
                    R.drawable.clarinete_baixo,
                    getString(R.string.clarinete_baixo_nome),
                    getString(R.string.clarinete_baixo_descricao)
            ));

            itens.add(new ItemModel(
                    R.drawable.trompete_piccolo,
                    getString(R.string.trompete_piccolo_nome),
                    getString(R.string.trompete_piccolo_descricao)
            ));

        } else if (familia.equals(familias[2])) {

            itens.add(new ItemModel(
                    R.drawable.bateria_eletronica,
                    getString(R.string.bateria_eletronica_nome),
                    getString(R.string.bateria_eletronica_descricao)
            ));

            itens.add(new ItemModel(
                    R.drawable.tambor_africano,
                    getString(R.string.tambor_africano_nome),
                    getString(R.string.tambor_africano_descricao)
            ));

            itens.add(new ItemModel(
                    R.drawable.pandeiro_meia_lua,
                    getString(R.string.pandeiro_meia_lua_nome),
                    getString(R.string.pandeiro_meia_lua_descricao)
            ));
        }

        GridAdapter adapter =
                new GridAdapter(requireContext(), itens);

        binding.gridView.setAdapter(adapter);

        // abre a tela de detalhes ao clicar em um item da galeria
        binding.gridView.setOnItemClickListener((parent, view, position, id) -> {

            ItemModel item = itens.get(position);

            Intent intent =
                    new Intent(requireContext(), DetailActivity.class);

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