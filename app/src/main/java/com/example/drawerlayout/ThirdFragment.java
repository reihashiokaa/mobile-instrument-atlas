package com.example.drawerlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
                    R.drawable.bmw_m135i_3,
                    "Variação de corda 1",
                    "Descrição temporária"
            ));

            itens.add(new ItemModel(
                    R.drawable.ferrari_488_2,
                    "Variação de corda 2",
                    "Descrição temporária"
            ));

            itens.add(new ItemModel(
                    R.drawable.ford_shelby_6,
                    "Variação de corda 3",
                    "Descrição temporária"
            ));

        } else if (familia.equals("Sopro")) {

            itens.add(new ItemModel(
                    R.drawable.bmw_m135i_3,
                    "Variação de sopro 1",
                    "Descrição temporária"
            ));

            itens.add(new ItemModel(
                    R.drawable.ferrari_488_2,
                    "Variação de sopro 2",
                    "Descrição temporária"
            ));

            itens.add(new ItemModel(
                    R.drawable.ford_shelby_6,
                    "Variação de sopro 3",
                    "Descrição temporária"
            ));

        } else if (familia.equals("Percussão")) {

            itens.add(new ItemModel(
                    R.drawable.bmw_m135i_3,
                    "Variação de percussão 1",
                    "Descrição temporária"
            ));

            itens.add(new ItemModel(
                    R.drawable.ferrari_488_2,
                    "Variação de percussão 2",
                    "Descrição temporária"
            ));

            itens.add(new ItemModel(
                    R.drawable.ford_shelby_6,
                    "Variação de percussão 3",
                    "Descrição temporária"
            ));
        }

        GridAdapter adapter =
                new GridAdapter(requireContext(), itens);

        binding.gridView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}