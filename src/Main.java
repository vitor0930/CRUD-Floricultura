import dao.ClienteDAOImpl;
import model.Cliente;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        ClienteDAOImpl cl = new ClienteDAOImpl();

        //cl.salvar(new Cliente("Vitor Lopes", "vitorlopes0930@gmail.com", "12345678910"));
        /*List<Cliente> c = cl.listarTodos();
        c.forEach(cliente -> {
            System.out.println(cliente.getNome());
            System.out.println(cliente.getEmail());
            System.out.println(cliente.getCpf());
            System.out.println();
        });
        */
        //System.out.println(cl.buscarPorId(1));
        //cl.deletar(cl.buscarPorId(1));
        Cliente cliente = cl.buscarPorId(2);
        cliente.setNome("Vitor Lopes de Souza");
        cliente.setEmail("vitorlopes3009@bol.com.br");
        cliente.setCpf("10987654321");
        cl.atualizar(cliente);
    }

}