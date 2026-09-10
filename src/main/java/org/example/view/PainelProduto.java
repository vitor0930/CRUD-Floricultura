package org.example.view;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.example.dao.CategoriaDAOImpl;
import org.example.dao.ProdutoDAOImpl;
import org.example.model.Produto;
import org.example.model.Categoria;

public class PainelProduto extends JPanel {
    
    private ProdutoDAOImpl produtoDao = new ProdutoDAOImpl();
    private CategoriaDAOImpl categoriaDao = new CategoriaDAOImpl();
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private JLabel labelNome = new JLabel("Nome");
    private JLabel labelPreco = new JLabel("Preço");
    private JLabel labelCategoria = new JLabel("Categoria");
    private JTextField campoNome = new JTextField(40);
    private JTextField campoPreco = new JTextField(10);
    private JComboBox<String> comboCategoria = new JComboBox<>();
    private JLabel labelQuantidade = new JLabel("Quantidade");
    private JTextField campoQuantidade = new JTextField(5);
    private JButton botaoSalvar = new JButton("Salvar");
    private JButton botaoLimpar = new JButton("Limpar");
    private JButton botaoExcluir = new JButton("Excluir");
    private JButton botaoAtualizar = new JButton("Atualizar");
    private JPanel painelCadastro = new JPanel();
    private JPanel painelBtnCadastro = new JPanel();
    private JPanel painelBtnTabela = new JPanel();

    public PainelProduto() {

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        painelCadastro.setLayout(new GridLayout(5, 2));

        botaoSalvar.addActionListener(e -> salvarProduto());
        botaoLimpar.addActionListener(e -> limparCampos());
        botaoExcluir.addActionListener(e -> excluirProduto());
        botaoAtualizar.addActionListener(e -> atualizarProduto());
        
        preencherComboBox();
        painelCadastro.add(labelNome);
        painelCadastro.add(campoNome);
        painelCadastro.add(labelPreco);
        painelCadastro.add(campoPreco);
        painelCadastro.add(labelCategoria);
        painelCadastro.add(comboCategoria);
        painelCadastro.add(labelQuantidade);
        painelCadastro.add(campoQuantidade);
        painelBtnCadastro.add(botaoSalvar);
        painelBtnCadastro.add(botaoLimpar);
        painelBtnTabela.add(botaoExcluir);
        painelBtnTabela.add(botaoAtualizar);
        painelCadastro.add(painelBtnCadastro);
        painelCadastro.add(painelBtnTabela);

        add(painelCadastro);

        String[] colunas = { "ID", "Nome", "Preço", "Categoria", "Quantidade" };
        modeloTabela = new DefaultTableModel(colunas, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selecionarProduto();
            }
        });

        JScrollPane produtos = new JScrollPane(tabela);

        carregarTabela();

        add(produtos);
    }

    public void salvarProduto(){
        String nome = campoNome.getText();
        String preco = campoPreco.getText();
        String quantidade = campoQuantidade.getText();
        String categoriaNome = (String) comboCategoria.getSelectedItem();
        
        if(nome.isEmpty() || preco.isEmpty() || quantidade.isEmpty() || categoriaNome == null){
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        try{
            double precoValue = Double.parseDouble(preco);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Preço inválido!");
            return;
        }

        try {
            if (Integer.parseInt(quantidade) <= 0) {
                JOptionPane.showMessageDialog(this, "Quantidade inválida!");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida!");
            return;
        }

        Categoria categoria = categoriaDao.buscarPorNome(categoriaNome);
        if(categoria == null){
            JOptionPane.showMessageDialog(this, "Categoria inválida!");
            return;
        }

        Produto produto = new Produto(nome, Double.parseDouble(preco), categoria, Integer.parseInt(campoQuantidade.getText()));
        produtoDao.salvar(produto);
        JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!");
        carregarTabela();
    }

    public void limparCampos(){
        campoNome.setText("");
        campoPreco.setText("");
        comboCategoria.setSelectedIndex(0);
        campoQuantidade.setText("");
    }

    public void carregarTabela(){
        modeloTabela.setRowCount(0);
        for(Produto produto : produtoDao.listarTodos()){
            modeloTabela.addRow(new Object[]{
                produto.getId(),
                produto.getNome(),
                produto.getPreco(),
                produto.getCategoria().getNome(),
                produto.getQuantidade()
            });
        }
    }

    private void excluirProduto(){
        int linhaSelecionada = tabela.getSelectedRow();
        if(linhaSelecionada == -1){
            JOptionPane.showMessageDialog(this, "Selecione um produto!");
            return;
        }
        int id = (int) tabela.getValueAt(linhaSelecionada, 0);

        int confirmacao = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente excluir este produto?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if(confirmacao == JOptionPane.YES_OPTION){
            produtoDao.deletar(produtoDao.buscarPorId(id));
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
        }
    }

    private void atualizarProduto(){
        int linhaSelecionada = tabela.getSelectedRow();
        if(linhaSelecionada == -1){
            JOptionPane.showMessageDialog(this, "Selecione um produto!");
            return;
        }
        int id = (int) tabela.getValueAt(linhaSelecionada, 0);
        Produto produto = produtoDao.buscarPorId(id);
        produto.setNome(campoNome.getText());
        produto.setPreco(Double.parseDouble(campoPreco.getText()));
        produto.setQuantidade(Integer.parseInt(campoQuantidade.getText()));
        Categoria categoria = categoriaDao.buscarPorNome((String) comboCategoria.getSelectedItem());
        produto.setCategoria(categoria);
        produtoDao.atualizar(produto);
        carregarTabela();
        JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
    }

    private void selecionarProduto(){
        int linhaSelecionada = tabela.getSelectedRow();
        if(linhaSelecionada == -1){
            JOptionPane.showMessageDialog(this, "Selecione um produto!");
            return;
        }
        int id = (int) tabela.getValueAt(linhaSelecionada, 0);
        Produto produto = produtoDao.buscarPorId(id);
        campoNome.setText(produto.getNome());
        campoPreco.setText(String.valueOf(produto.getPreco()));
        campoQuantidade.setText(String.valueOf(produto.getQuantidade()));
        comboCategoria.setSelectedItem(produto.getCategoria().getNome());
    }

    public void preencherComboBox(){
        comboCategoria.removeAllItems();
        for(Categoria categoria : categoriaDao.listarTodos()){
            comboCategoria.addItem(categoria.getNome());
        }
    }
}
