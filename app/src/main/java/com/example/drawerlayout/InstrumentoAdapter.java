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

public class InstrumentoAdapter extends BaseAdapter {

    private Context context;
    private List<Instrumento> instrumentos;

    public InstrumentoAdapter(Context context, List<Instrumento> instrumentos) {
        this.context = context;

        if (instrumentos == null) {
            this.instrumentos = new ArrayList<>();
        } else {
            this.instrumentos = new ArrayList<>(instrumentos);
        }
    }

    public void atualizarDados(List<Instrumento> novosInstrumentos) {
        if (novosInstrumentos == null) {
            this.instrumentos = new ArrayList<>();
        } else {
            this.instrumentos = new ArrayList<>(novosInstrumentos);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return instrumentos.size();
    }

    @Override
    public Instrumento getItem(int position) {
        return instrumentos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return instrumentos.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lista, parent, false);
        }

        Instrumento instrumento = instrumentos.get(position);

        ImageView imagemInstrumento = convertView.findViewById(R.id.imgItem);
        TextView nomeInstrumento = convertView.findViewById(R.id.txtNome);
        TextView descricaoInstrumento = convertView.findViewById(R.id.txtDescricao);

        MidiaUtils.carregarImagem(imagemInstrumento, instrumento.imagemUri);

        nomeInstrumento.setText(instrumento.nome);
        descricaoInstrumento.setText(instrumento.descricao);

        return convertView;
    }
}
