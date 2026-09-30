package com.example.drawerlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private List<Instrumento> lista;

    public GridAdapter(Context context, List<Instrumento> lista) {
        this.context = context;

        if (lista == null) {
            this.lista = new ArrayList<>();
        } else {
            this.lista = new ArrayList<>(lista);
        }
    }

    public void atualizarDados(List<Instrumento> itens) {
        if (itens == null) {
            this.lista = new ArrayList<>();
        } else {
            this.lista = new ArrayList<>(itens);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Instrumento getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lista.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_grid, parent, false);
        }

        Instrumento item = lista.get(position);

        ImageView imagem = convertView.findViewById(R.id.imageGrid);
        TextView nome = convertView.findViewById(R.id.textGrid);

        MidiaUtils.carregarImagem(imagem, item.imagemUri);

        nome.setText(item.nome);

        return convertView;
    }
}