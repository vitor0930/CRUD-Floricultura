package org.example.model;

import java.math.BigDecimal;

public class ItemVenda {
    private int id;
    private Produto produto;
    private Venda venda;
    private BigDecimal precoUnitario;
    private int quantidade;

    public ItemVenda(int id, Produto produto, Venda venda, BigDecimal precoUnitario, int quantidade){
        id = this.id;
        produto = this.produto;
        venda = this.venda;
        precoUnitario = this.precoUnitario;
        quantidade = this.quantidade;
    }

    public ItemVenda(Produto produto, Venda venda, BigDecimal precoUnitario, int quantidade){
        produto = this.produto;
        venda = this.venda;
        precoUnitario = this.precoUnitario;
        quantidade = this.quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal preçoUnitario) {
        this.precoUnitario = preçoUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

}
