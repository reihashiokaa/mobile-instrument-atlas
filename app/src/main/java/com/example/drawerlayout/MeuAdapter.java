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
    public int getCount() { return lista.size(); }
    @Override
    public Object getItem(int position) { return lista.get(position); }
    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
// Infla o XML do layout personalizado (item_lista) para criar aview da linha
                    convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_lista, parent, false);
        }
        ItemModel item = lista.get(position);
        ImageView img = convertView.findViewById(R.id.imgItem);
        TextView nome = convertView.findViewById(R.id.txtNome);
        TextView desc = convertView.findViewById(R.id.txtDescricao);
        img.setImageResource(item.getImagemResId());
        nome.setText(item.getNome());
        desc.setText(item.getDescricao());
        return convertView;
    }
}
