# CRUD_produto_preco
Trabalho de aula da cadeira de técnicas de programação.

## Descrição
Simples programa em JAVA em console, para gerenciar produtos e histórico de preços.
  
  

## Criar essa estrutura de BD

### Criação da tabela produtos

```SQL
CREATE  TABLE  produtos (
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	produto VARCHAR(255) NOT NULL,
	marca VARCHAR(255) NOT NULL,
	modelo VARCHAR(255) NOT NULL,
	codigo_barra CHAR(13),
	insert_at DATETIME  NOT NULL,
	update_at DATETIME  NOT NULL,
	deleted_at DATETIME
);
```
  

### Criação da tabela preços
```SQL
CREATE  TABLE  precos (
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	produto_id INT UNSIGNED NOT NULL,
	valor_cobrar DECIMAL(10,2) UNSIGNED NOT NULL,
	max_desconto DECIMAL(5,2) UNSIGNED NOT NULL  DEFAULT  0,
	valor_pago DECIMAL(10,2) UNSIGNED NOT NULL,
	insert_at DATETIME  NOT NULL,
	update_at DATETIME  NOT NULL,
	deleted_at DATETIME,
	FOREIGN KEY (produto_id) REFERENCES produtos(id)
);
```