package org.example.model;

import java.time.LocalDateTime;

public class Venda {
    int id;
    LocalDateTime data;
    Cliente cliente;
    boolean cancelada;

    public Venda(int id, LocalDateTime data, Cliente cliente, boolean cancelada) {
        this.id = id;
        this.data = data;
        this.cliente = cliente;
        this.cancelada = cancelada;
    }

    public Venda(Cliente cliente) {
        this.cliente = cliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public boolean isCancelada() {
        return cancelada;
    }

    public void setCancelada(boolean cancelada) {
        this.cancelada = cancelada;
    }

    @Override
    public String toString() {
        return "Venda{" +
                "cancelada=" + cancelada +
                ", id=" + id +
                ", data=" + data +
                ", cliente=" + cliente.getNome() +
                '}';
    }
}