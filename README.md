# CRUD-Floricultura
Projeto de DS com objetivo de criar um sistema para um comércio

# Proposta
Desenvolver um sistema com Java e MySQL de uma floricultura para facilitar o registro e o cadastro de produtos, vendas, e clientes

# Escopo do Sistema
## Requisitos Funcionais
- Cadastrar clientes
- Registrar vendas
- Registrar produtos
- Controle de estoque

## Requisitos Não Funcionais
- E-mails devem ser únicos e válidos
- O sistema deve usar uma timestamp ao realizar uma venda
- Deve-se ter apenas um registro por cliente
- O preço do produto não pode ser 0.00
- Ao registrar um produto ele não pode ser nulo

# Script do banco
CREATE TABLE clientes 
( 
 id INT PRIMARY KEY AUTO_INCREMENT,  
 nome VARCHAR(100) NOT NULL,  
 email VARCHAR(100) NOT NULL,  
 cpf CHAR(11) NOT NULL,  
 UNIQUE (email,cpf)
); 

CREATE TABLE produtos 
( 
 id INT PRIMARY KEY AUTO_INCREMENT,  
 nome VARCHAR(100) NOT NULL,  
 preco DECIMAL(10,2) NOT NULL,  
 categoria_id INT NOT NULL,  
); 

CREATE TABLE categorias 
( 
 id INT PRIMARY KEY AUTO_INCREMENT,  
 nome VARCHAR(100) NOT NULL,  
); 

CREATE TABLE vendas 
( 
 id INT PRIMARY KEY AUTO_INCREMENT,  
 data_venda DATETIME NOT NULL,  
 cliente_id INT NOT NULL,  
 produto_id INT NOT NULL,  
); 

ALTER TABLE produtos ADD FOREIGN KEY(categoria_id) REFERENCES categorias (categoria_id)
ALTER TABLE vendas ADD FOREIGN KEY(cliente_id) REFERENCES clientes (cliente_id)
ALTER TABLE vendas ADD FOREIGN KEY(produto_id) REFERENCES produtos (produto_id)
