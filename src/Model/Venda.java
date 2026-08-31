package Model;

import java.util.Date;

public class Venda {
    int id;
    Date data;
    Cliente cliente;

    public Venda(Cliente cliente, Date data) {
        this.cliente = cliente;
        this.data = data;
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
