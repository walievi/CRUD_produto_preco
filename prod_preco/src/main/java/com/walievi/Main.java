package com.walievi;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            List<Produto> produtos = Produto.getAll();

            // Criar um objeto da classe Tela
            Tela tela = new Tela();

            // Definir colunas
            tela.newColumn("ID", 5);
            tela.newColumn("Nome", 45);
            tela.newColumn("Idade", 5);

            // Adicionar valores para as linhas e então adicionar a linha à tabela
            tela.addValue("ID", "1");
            tela.addValue("Nome", "Alice");
            
            tela.addLine();

            tela.addValue("ID", "2");
            tela.addValue("Nome", "Bob");
            tela.addValue("Idade", "40");
            tela.addLine();

            tela.addValue("ID", "3");
            tela.addValue("Nome", "Carol");
            
            tela.addLine();

            // Adicionar opções ao menu
            tela.addMenuOption("Inserir novo registro");
            tela.addMenuOption("Editar registro existente");
            tela.addMenuOption("Excluir registro");
            tela.addMenuOption("Sair");

            // Imprimir a tabela e o menu
            tela.print();


  
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
