package com.example.drawerlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.drawerlayout.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private Spinner spinner;
    private FragmentFirstBinding binding;

    // ViewModel compartilhado com os outros fragments
    private SharedViewModel sharedViewModel;

    // Adapter do Spinner
    private ArrayAdapter<Familia> adapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(
                inflater,
                container,
                false
        );

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            Bundle savedInstanceState
    ) {

        super.onViewCreated(view, savedInstanceState);

        // Pega o mesmo ViewModel usado pelos outros fragments
        sharedViewModel =
                new ViewModelProvider(requireActivity())
                        .get(SharedViewModel.class);

        // Pega o Spinner pelo ViewBinding
        spinner = binding.familia;

        // Cria o adapter vazio.
        // Os dados serão carregados pelo Room.
        adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        // Coloca o adapter no Spinner
        spinner.setAdapter(adapter);

        // Observa as famílias vindas do Room
        sharedViewModel.getFamilias().observe(
                getViewLifecycleOwner(),
                familias -> {

                    adapter.clear();

                    if (familias != null) {
                        adapter.addAll(familias);
                    }

                    adapter.notifyDataSetChanged();
                }
        );

        // Observa a família selecionada pelo ViewModel
        sharedViewModel.getFamiliaSelecionadaId().observe(
                getViewLifecycleOwner(),
                familiaId -> {

                    if (familiaId == null) {
                        return;
                    }

                    // Procura a família pelo ID,
                    // e não pela posição ou pelo nome.
                    for (int i = 0; i < adapter.getCount(); i++) {

                        Familia familia = adapter.getItem(i);

                        if (familia != null
                                && familia.id == familiaId) {

                            if (spinner.getSelectedItemPosition() != i) {
                                spinner.setSelection(i);
                            }

                            break;
                        }
                    }
                }
        );

        // Trata a seleção de uma família
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {

                        Familia familia =
                                adapter.getItem(position);

                        if (familia == null) {
                            return;
                        }

                        // Só atualiza o ViewModel se o ID
                        // realmente for diferente do atual.
                        Long familiaSelecionadaId =
                                sharedViewModel
                                        .getFamiliaSelecionadaId()
                                        .getValue();

                        if (familiaSelecionadaId == null
                                || familiaSelecionadaId != familia.id) {

                            sharedViewModel.selecionarFamilia(
                                    familia.id
                            );
                        }
                    }

                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent
                    ) {
                        // Não precisa fazer nada.
                    }
                }
        );
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        // Libera a referência da View
        binding = null;
    }
}