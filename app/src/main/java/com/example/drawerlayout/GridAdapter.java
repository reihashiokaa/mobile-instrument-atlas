package com.example.drawerlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private List<ItemModel> lista;

    public GridAdapter(Context context, List<ItemModel> lista) {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // cria cada item usando o layout da grade
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_grid, parent, false);
        }

        // pega o item da posição atual
        ItemModel item = lista.get(position);

        ImageView imagem = convertView.findViewById(R.id.imageGrid);
        TextView nome = convertView.findViewById(R.id.textGrid);

        // mostra a imagem e o nome na grade
        imagem.setImageResource(item.getImagemResId());
        nome.setText(item.getNome());

        return convertView;
    }
}