package com.example.drawerlayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.drawerlayout.databinding.FragmentThirdBinding;
import java.util.ArrayList;

public class ThirdFragment extends Fragment {

    private FragmentThirdBinding binding;

    // ViewModel compartilhado pelos fragments
    private SharedViewModel sharedViewModel;

    // Adapter responsável pelo GridView
    private GridAdapter adapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentThirdBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Pega o mesmo ViewModel compartilhado pelos fragments
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Cria o adapter inicialmente vazio
        adapter = new GridAdapter(requireContext(), new ArrayList<>());

        // Coloca o adapter no GridView
        binding.gridView.setAdapter(adapter);

        // Observa as variações vindas do Room
        sharedViewModel
                .getVariacoes()
                .observe(
                        getViewLifecycleOwner(),
                        lista -> {

                            // Atualiza os dados do adapter
                            adapter.atualizarDados(lista);
                        });

        // Trata o clique em uma variação
        binding.gridView.setOnItemClickListener(
                (parent, view1, position, id) -> {

                    // Pega o instrumento clicado
                    Instrumento instrumento = adapter.getItem(position);

                    // Cria a Intent para a tela de detalhes
                    Intent intent = new Intent(requireContext(), DetalheActivity.class);

                    // Envia somente o ID do instrumento
                    intent.putExtra(ContratoApp.EXTRA_INSTRUMENTO_ID, instrumento.id);

                    startActivity(intent);
                });
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        // Evita manter referência da View
        binding = null;
    }
}
