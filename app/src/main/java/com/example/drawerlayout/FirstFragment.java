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

    private SharedViewModel sharedViewModel;

    private ArrayAdapter<Familia> adapter;

    private boolean atualizandoSpinner = false;

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

        sharedViewModel =
                new ViewModelProvider(requireActivity())
                        .get(SharedViewModel.class);

        spinner = binding.familia;

        adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinner.setAdapter(adapter);

        sharedViewModel.getFamilias().observe(
                getViewLifecycleOwner(),
                familias -> {

                    atualizandoSpinner = true;

                    adapter.clear();

                    if (familias != null) {
                        adapter.addAll(familias);
                    }

                    adapter.notifyDataSetChanged();

                    Long familiaSelecionadaId =
                            sharedViewModel
                                    .getFamiliaSelecionadaId()
                                    .getValue();

                    if (familiaSelecionadaId != null) {

                        for (int i = 0; i < adapter.getCount(); i++) {

                            Familia familia =
                                    adapter.getItem(i);

                            if (familia != null
                                    && familia.id == familiaSelecionadaId) {

                                spinner.setSelection(i, false);
                                break;
                            }
                        }
                    }

                    atualizandoSpinner = false;
                }
        );

        sharedViewModel.getFamiliaSelecionadaId().observe(
                getViewLifecycleOwner(),
                familiaId -> {

                    if (familiaId == null) {
                        return;
                    }

                    for (int i = 0; i < adapter.getCount(); i++) {

                        Familia familia =
                                adapter.getItem(i);

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

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {

                        if (atualizandoSpinner) {
                            return;
                        }

                        Familia familia =
                                adapter.getItem(position);

                        if (familia == null) {
                            return;
                        }

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
                    }
                }
        );
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        binding = null;
    }
}