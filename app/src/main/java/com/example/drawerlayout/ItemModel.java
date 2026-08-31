package com.example.drawerlayout;

public class ItemModel {
    private int imagemResId;
    private String nome;
    private String descricao;

    public ItemModel(int imagemResId, String nome, String descricao) {
        this.imagemResId = imagemResId;
        this.nome = nome;
        this.descricao = descricao;
    }
    public int getImagemResId() { return imagemResId; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
}
