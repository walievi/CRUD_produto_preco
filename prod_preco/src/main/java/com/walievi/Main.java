package com.walievi;

import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            int status = 99;
            while(status != 0){
                status = exibirMenu();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int exibirMenu() throws SQLException {
        while (true) {
            Tela tela = configuraTela();

            List<Produto> produtos = Produto.getAll();
            preencheTelaComProdutos(tela, produtos);
            adicionaOpcoesMenu(tela);
            
            tela.print();
            String opcao = tela.getOpcao().toUpperCase();
            if("S".equals(opcao)){
                return 0;
            }
            
        }
    }

    public static Tela configuraTela() {
        Tela tela = new Tela();
        
        tela.newColumn("ID", 5);
        tela.newColumn("Produto", 15);
        tela.newColumn("Marca", 10);
        tela.newColumn("Modelo", 10);
        tela.newColumn("Código de Barra", 20);
        tela.newColumn("Inserido em", 20);
        tela.newColumn("Atualizado em", 20);
        tela.newColumn("Deletado em", 20);

        return tela;
    }

    public static void preencheTelaComProdutos(Tela tela, List<Produto> produtos) {
        for (Produto produto : produtos) {
            tela.addValue("ID", String.valueOf(produto.getId()));
            tela.addValue("Produto", produto.getProduto());
            tela.addValue("Marca", produto.getMarca());
            tela.addValue("Modelo", produto.getModelo());
            tela.addValue("Código de Barra", produto.getCodigoBarra());
            tela.addValue("Inserido em", produto.getFormattedInsertAt());  // Você pode formatar a data aqui
            tela.addValue("Atualizado em", produto.getFormattedUpdateAt()); // Você pode formatar a data aqui
            tela.addValue("Deletado em", produto.getDeletedAt() != null ? produto.getFormattedDeletedAt() : "---");
            tela.addLine();
        }
    }

    public static void adicionaOpcoesMenu(Tela tela) {
        tela.addMenuOption("Digite o ID do produto para ver detalhes ou editar");
        tela.addMenuOption("I - Inserir novo produto");
        tela.addMenuOption("D - Ver produtos desativados");
        tela.addMenuOption("S - Sair");
    }
}
