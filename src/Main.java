import dao.ClienteDAOImpl;
import dao.VendaDAOImpl;
import model.Cliente;
import model.Venda;

import java.sql.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        ClienteDAOImpl cl = new ClienteDAOImpl();
        VendaDAOImpl vendaDAO = new VendaDAOImpl();

        //cl.salvar(new Cliente("Vitor Lopes", "vitorlopes0930@gmail.com", "12345678910"));

    }
}