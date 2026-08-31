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

    // viewmodel compartilhado com os outros fragments
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // pega o mesmo ViewModel usado pelos outros fragments
        sharedViewModel = new ViewModelProvider(requireActivity())
                .get(SharedViewModel.class);

        spinner = binding.familia;

        // carrega as famílias definidas no strings.xml
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        requireContext(),
                        R.array.familia,
                        android.R.layout.simple_spinner_item
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinner.setAdapter(adapter);

        // avisa o ViewModel sempre que o usuário escolher outra família
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> adapterView,
                            View view,
                            int position,
                            long id
                    ) {

                        // pega o nome da família escolhida
                        String familia =
                                adapterView.getItemAtPosition(position).toString();

                        // atualiza o valor compartilhado entre os fragments
                        sharedViewModel.setFamiliaSelecionada(familia);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // não precisa fazer nada
                    }
                }
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // libera a referência da view quando o fragment for destruído
        binding = null;
    }
}