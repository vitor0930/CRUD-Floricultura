package org.example.view;

import javax.swing.*;

public class TelaPrincipal extends JFrame {

    private PainelCliente painelCliente;
    private PainelProduto painelProduto;
    private PainelVenda painelVenda;
    private PainelCategoria painelCategoria;

    public TelaPrincipal() {
        JTabbedPane abas = new JTabbedPane();
        painelCliente = new PainelCliente();
        painelProduto = new PainelProduto();
        painelVenda = new PainelVenda();
        painelCategoria = new PainelCategoria(painelProduto);

        abas.add("Clientes", painelCliente);
        abas.add("Produtos", painelProduto);
        abas.add("Vendas", painelVenda);
        abas.add("Categorias", painelCategoria);
        add(abas);

        setTitle("Sistema Floricultura");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new TelaPrincipal();
    }

}