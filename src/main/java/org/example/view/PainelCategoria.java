package org.example.view;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.example.dao.CategoriaDAOImpl;
import org.example.model.Categoria;

public class PainelCategoria extends JPanel {
    
    private CategoriaDAOImpl categoriaDao = new CategoriaDAOImpl();
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private JLabel labelNome = new JLabel("Nome");
    private JTextField campoNome = new JTextField(40);
    private JButton botaoSalvar = new JButton("Salvar");
    private JButton botaoLimpar = new JButton("Limpar");
    private JButton botaoExcluir = new JButton("Excluir");
    private JButton botaoAtualizar = new JButton("Atualizar");
    private JPanel painelCadastro = new JPanel();
    private JPanel painelBtnCadastro = new JPanel();
    private JPanel painelBtnTabela = new JPanel();

    private PainelProduto painelProduto;

    public PainelCategoria(PainelProduto painelProduto) {

        this.painelProduto = painelProduto;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        painelCadastro.setLayout(new GridLayout(2, 2));

        botaoSalvar.addActionListener(e -> salvarCategoria());
        botaoLimpar.addActionListener(e -> limparCampos());
        botaoExcluir.addActionListener(e -> excluirCategoria());
        botaoAtualizar.addActionListener(e -> atualizarCategoria());
        
        painelCadastro.add(labelNome);
        painelCadastro.add(campoNome);
        painelBtnCadastro.add(botaoSalvar);
        painelBtnCadastro.add(botaoLimpar);
        painelBtnTabela.add(botaoExcluir);
        painelBtnTabela.add(botaoAtualizar);
        painelCadastro.add(painelBtnCadastro);
        painelCadastro.add(painelBtnTabela);

        add(painelCadastro);

        String[] colunas = { "ID", "Nome" };
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
                selecionarCategoria();
            }
        });

        JScrollPane clientes = new JScrollPane(tabela);

        carregarTabela();

        add(clientes);
    }

    public void salvarCategoria(){
        String nome = campoNome.getText();
        
        if(nome.isEmpty()){
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        Categoria categoria = new Categoria(nome);
        categoriaDao.salvar(categoria);
        JOptionPane.showMessageDialog(this, "Categoria salva com sucesso!");

        limparCampos();
        carregarTabela();
        painelProduto.preencherComboBox();
    }

    public void limparCampos(){
        campoNome.setText("");
    }

    public void carregarTabela(){
        modeloTabela.setRowCount(0);
        for(Categoria categoria : categoriaDao.listarTodos()){
            modeloTabela.addRow(new Object[]{
                categoria.getId(),
                categoria.getNome()
            });
        }
    }

    private void excluirCategoria(){
        int linhaSelecionada = tabela.getSelectedRow();
        if(linhaSelecionada == -1){
            JOptionPane.showMessageDialog(this, "Selecione uma categoria!");
            return;
        }
        int id = (int) tabela.getValueAt(linhaSelecionada, 0);

        int confirmacao = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente excluir este cliente?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if(confirmacao == JOptionPane.YES_OPTION){
            categoriaDao.deletar(categoriaDao.buscarPorId(id));
            JOptionPane.showMessageDialog(this, "Categoria excluída com sucesso!");

            limparCampos();
            carregarTabela();
            painelProduto.preencherComboBox();
        }
    }

    private void atualizarCategoria(){
        int linhaSelecionada = tabela.getSelectedRow();
        if(linhaSelecionada == -1){
            JOptionPane.showMessageDialog(this, "Selecione uma categoria!");
            return;
        }
        int id = (int) tabela.getValueAt(linhaSelecionada, 0);
        Categoria categoria = categoriaDao.buscarPorId(id);
        categoria.setNome(campoNome.getText());
        categoriaDao.atualizar(categoria);

        JOptionPane.showMessageDialog(this, "Categoria atualizada com sucesso!");

        limparCampos();
        carregarTabela();
        painelProduto.preencherComboBox();
    }

    private void selecionarCategoria(){
        int linhaSelecionada = tabela.getSelectedRow();
        if(linhaSelecionada == -1){
            JOptionPane.showMessageDialog(this, "Selecione uma categoria!");
            return;
        }
        int id = (int) tabela.getValueAt(linhaSelecionada, 0);
        Categoria categoria = categoriaDao.buscarPorId(id);
        campoNome.setText(categoria.getNome());
    }
}
