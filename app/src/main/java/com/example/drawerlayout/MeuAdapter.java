package com.example.drawerlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MeuAdapter extends BaseAdapter {

    private Context context;
    private List<ItemModel> lista;

    public MeuAdapter(Context context, List<ItemModel> lista) {
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

        // cria a linha usando o layout personalizado
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_lista, parent, false);
        }

        // pega o item correspondente à posição atual
        ItemModel item = lista.get(position);

        ImageView img = convertView.findViewById(R.id.imgItem);
        TextView nome = convertView.findViewById(R.id.txtNome);
        TextView desc = convertView.findViewById(R.id.txtDescricao);

        // coloca os dados do item na linha
        img.setImageResource(item.getImagemResId());
        nome.setText(item.getNome());
        desc.setText(item.getDescricao());

        return convertView;
    }
}