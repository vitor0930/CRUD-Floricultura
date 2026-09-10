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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.example.dao.ClienteDAOImpl;
import org.example.model.Cliente;

public class PainelCliente extends JPanel {

    private ClienteDAOImpl clientedao = new ClienteDAOImpl();
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private JLabel labelNome = new JLabel("Nome");
    private JLabel labelCpf = new JLabel("CPF (apenas números)");
    private JLabel labelEmail = new JLabel("Email");
    private JTextField campoNome = new JTextField(40);
    private JTextField campoCpf = new JTextField(11);
    private JTextField campoEmail = new JTextField(40);
    private JButton botaoSalvar = new JButton("Salvar");
    private JButton botaoLimpar = new JButton("Limpar");
    private JButton botaoExcluir = new JButton("Excluir");
    private JButton botaoAtualizar = new JButton("Atualizar");
    private JPanel painelCadastro = new JPanel();
    private JPanel painelBtnCadastro = new JPanel();
    private JPanel painelBtnTabela = new JPanel();

    public PainelCliente() {

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        painelCadastro.setLayout(new GridLayout(4, 2));

        botaoSalvar.addActionListener(e -> salvarCliente());
        botaoLimpar.addActionListener(e -> limparCampos());
        botaoExcluir.addActionListener(e -> excluirCliente());
        botaoAtualizar.addActionListener(e -> atualizarCliente());
        
        painelCadastro.add(labelNome);
        painelCadastro.add(campoNome);
        painelCadastro.add(labelEmail);
        painelCadastro.add(campoEmail);
        painelCadastro.add(labelCpf);
        painelCadastro.add(campoCpf);
        painelBtnCadastro.add(botaoSalvar);
        painelBtnCadastro.add(botaoLimpar);
        painelBtnTabela.add(botaoExcluir);
        painelBtnTabela.add(botaoAtualizar);
        painelCadastro.add(painelBtnCadastro);
        painelCadastro.add(painelBtnTabela);

        add(painelCadastro);

        String[] colunas = { "ID", "Nome", "Email", "CPF" };
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
                selecionarCliente();
            }
        });

        JScrollPane clientes = new JScrollPane(tabela);

        carregarTabela();

        add(clientes);
    }

    public void salvarCliente(){
        String nome = campoNome.getText();
        String cpf = campoCpf.getText();
        String email = campoEmail.getText();
        
        if(nome.isEmpty() || cpf.isEmpty() || email.isEmpty()){
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }
        
        if(cpf.length() != 11){
            JOptionPane.showMessageDialog(this, "CPF inválido!");
            return;
        }

        try{
            Long.parseLong(cpf);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this, "CPF inválido! Digite apenas números.");
            return;
        }

        if(clientedao.buscarPorCpf(cpf) != null){
            JOptionPane.showMessageDialog(this, "CPF já cadastrado!");
            return;
        }
        
        if(clientedao.buscarPorEmail(email) != null){
            JOptionPane.showMessageDialog(this, "Email já cadastrado!");
            return;
        }

        Cliente cliente = new Cliente(nome, email, cpf);
        clientedao.salvar(cliente);
        JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!");
        carregarTabela();
    }

    public void limparCampos(){
        campoNome.setText("");
        campoCpf.setText("");
        campoEmail.setText("");
    }

    public void carregarTabela(){
        modeloTabela.setRowCount(0);
        for(Cliente cliente : clientedao.listarTodos()){
            modeloTabela.addRow(new Object[]{
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail()
            });
        }
    }

    private void excluirCliente(){
        int linhaSelecionada = tabela.getSelectedRow();
        if(linhaSelecionada == -1){
            JOptionPane.showMessageDialog(this, "Selecione um cliente!");
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
            clientedao.deletar(clientedao.buscarPorId(id));
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
        }
    }

    private void atualizarCliente(){
        int linhaSelecionada = tabela.getSelectedRow();
        if(linhaSelecionada == -1){
            JOptionPane.showMessageDialog(this, "Selecione um cliente!");
            return;
        }
        int id = (int) tabela.getValueAt(linhaSelecionada, 0);
        Cliente cliente = clientedao.buscarPorId(id);
        cliente.setNome(campoNome.getText());
        cliente.setCpf(campoCpf.getText());
        cliente.setEmail(campoEmail.getText());
        clientedao.atualizar(cliente);
        carregarTabela();
        JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
    }

    private void selecionarCliente(){
        int linhaSelecionada = tabela.getSelectedRow();
        if(linhaSelecionada == -1){
            JOptionPane.showMessageDialog(this, "Selecione um cliente!");
            return;
        }
        int id = (int) tabela.getValueAt(linhaSelecionada, 0);
        Cliente cliente = clientedao.buscarPorId(id);
        campoNome.setText(cliente.getNome());
        campoCpf.setText(cliente.getCpf());
        campoEmail.setText(cliente.getEmail());
    }

}
