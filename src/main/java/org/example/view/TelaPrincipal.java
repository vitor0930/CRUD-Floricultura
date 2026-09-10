package org.example.view;

import javax.swing.*;

public class TelaPrincipal extends JFrame {
    public TelaPrincipal() {
        JTabbedPane abas = new JTabbedPane();
        abas.add("Clientes", new PainelCliente());
        abas.add("Produtos", new PainelProduto());
        abas.add("Vendas", new PainelVenda());
        abas.add("Categorias", new PainelCategoria());
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