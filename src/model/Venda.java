package model;

import java.sql.Date;

public class Venda {
    int id;
    Date data;
    Cliente cliente;
    boolean cancelada;

    public Venda(int id, Date data, Cliente cliente, boolean cancelada) {
        this.id = id;
        this.data = data;
        this.cliente = cliente;
        this.cancelada = cancelada;
    }

    public Venda(Cliente cliente, Date data) {
        this.cliente = cliente;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
