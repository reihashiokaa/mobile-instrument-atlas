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

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    // ViewModel compartilhado pelos fragments
    private SharedViewModel sharedViewModel;

    // Adapter responsável pelo ListView
    private MeuAdapter adapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // pega o mesmo ViewModel usado pelos outros fragments
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // cria o adapter inicialmente vazio
        adapter = new MeuAdapter(requireContext(), new ArrayList<>());

        // coloca o adapter no ListView
        binding.listViewImagens.setAdapter(adapter);

        // observa os instrumentos vindos do Room
        sharedViewModel
                .getInstrumentos()
                .observe(
                        getViewLifecycleOwner(),
                        lista -> {

                            // atualiza os dados do adapter
                            adapter.atualizarDados(lista);
                        });

        // trata o clique em um instrumento
        binding.listViewImagens.setOnItemClickListener(
                (parent, view1, position, id) -> {

                    // pega o instrumento clicado pelo adapter
                    Instrumento instrumento = adapter.getItem(position);

                    // cria a Intent para a tela de detalhes
                    Intent intent = new Intent(requireContext(), DetailActivity.class);

                    // envia somente o ID do instrumento
                    intent.putExtra(ContratoApp.EXTRA_INSTRUMENTO_ID, instrumento.id);

                    startActivity(intent);
                });
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        // evita manter referência da View
        binding = null;
    }
}
