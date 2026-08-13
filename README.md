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
CREATE TABLE Clientes 
( 
 Id INT PRIMARY KEY AUTO_INCREMENT,  
 Nome VARCHAR NOT NULL,  
 Email VARCHAR NOT NULL,  
 CPF CHAR NOT NULL,  
 UNIQUE (Email,CPF)
); 

CREATE TABLE Produtos 
( 
 Id INT PRIMARY KEY AUTO_INCREMENT,  
 Nome VARCHAR NOT NULL,  
 Preco FLOAT NOT NULL,  
 Categoria_id INT NOT NULL,  
); 

CREATE TABLE Categorias 
( 
 Id INT PRIMARY KEY AUTO_INCREMENT,  
 Nome VARCHAR NOT NULL,  
); 

CREATE TABLE Vendas 
( 
 Id INT PRIMARY KEY AUTO_INCREMENT,  
 Data DATETIME NOT NULL,  
 cliente_id INT NOT NULL,  
 produto_id INT NOT NULL,  
); 

ALTER TABLE Produtos ADD FOREIGN KEY(Categoria_id) REFERENCES Categorias (Categoria_id)
ALTER TABLE Vendas ADD FOREIGN KEY(cliente_id) REFERENCES Clientes (cliente_id)
ALTER TABLE Vendas ADD FOREIGN KEY(produto_id) REFERENCES Produtos (produto_id)
