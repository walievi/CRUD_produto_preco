package com.walievi;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            int status = 99;
            while(status != 0){
                status = exibirMenu();

                switch(status) {
                    case 1:
                        inserirNovoProduto();
                        break;
                        
                    case 2:
                        System.out.println("Opção não implementada.");
                        
                }                
                
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

            boolean podeConverterParaInt = true;
            try {
                Integer.parseInt(opcao);
            } catch (NumberFormatException e) {
                podeConverterParaInt = false;
            }
            
            if (podeConverterParaInt) {
                verProduto(Integer.parseInt(opcao));
            }else{   
                switch(opcao) {
                    case "I":
                        return 1;
                    case "D":
                        return 2;                    
                    case "S":
                        return 0;
                }
            }
            
        }
    }

    public static Tela configuraTela() {
        Tela tela = new Tela();
        
        tela.newColumn("ID", 5);
        tela.newColumn("Produto", 15);
        tela.newColumn("Marca", 20);
        tela.newColumn("Modelo", 20);
        tela.newColumn("Código de Barra", 20);
        tela.newColumn("Inserido em", 20);
        tela.newColumn("Atualizado em", 20);
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

    public static void inserirNovoProduto() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Produto novoProduto = new Produto();
        
        System.out.println("Digite o nome do produto:");
        novoProduto.setProduto(scanner.nextLine());
        
        System.out.println("Digite a marca do produto:");
        novoProduto.setMarca(scanner.nextLine());
        
        System.out.println("Digite o modelo do produto:");
        novoProduto.setModelo(scanner.nextLine());
        
        System.out.println("Digite o código de barras do produto:");
        novoProduto.setCodigoBarra(scanner.nextLine());
        
        novoProduto.salvar();
        
        System.out.println("Produto inserido com sucesso!");
    }

    public static void verProduto(int id) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        
        boolean sair = false;
        while (!sair) {  
            Produto produto = new Produto(id);
            Tela tela = new Tela();
            tela.clearScreen();

            tela.newColumn("CAMPO", 20);
            tela.newColumn("VALOR", 20);
            
            tela.addValue("CAMPO", "ID");
            tela.addValue("VALOR", String.valueOf(produto.getId()));
            tela.addLine();
            
            tela.addValue("CAMPO", "Produto");
            tela.addValue("VALOR", produto.getProduto());
            tela.addLine();
            
            tela.addValue("CAMPO", "Marca");
            tela.addValue("VALOR", produto.getMarca());
            tela.addLine();
            
            tela.addValue("CAMPO", "Modelo");
            tela.addValue("VALOR", produto.getModelo());
            tela.addLine();
            
            tela.addValue("CAMPO", "Código de Barra");
            tela.addValue("VALOR", produto.getCodigoBarra());
            tela.addLine();
            
            String valorString = "---";
            Preco valor = Preco.getCurrentByProdutoId(produto.getId());
            if (valor != null){
                valorString = valor.getValorCobrar().toString();
            }
            
            tela.addValue("CAMPO", "Valor atual");
            tela.addValue("VALOR", valorString);
            tela.addLine();        
            
            tela.addValue("CAMPO", "Inserido em");
            tela.addValue("VALOR", produto.getFormattedInsertAt());
            tela.addLine();
            
            tela.addValue("CAMPO", "Atualizado em");
            tela.addValue("VALOR", produto.getFormattedUpdateAt());
            tela.addLine();
            
            tela.addValue("CAMPO", "Desativado em");
            tela.addValue("VALOR", produto.getDeletedAt() != null ? produto.getFormattedDeletedAt() : "---");
            tela.addLine();
            
            tela.addMenuOption("E - Editar Produto");
            tela.addMenuOption("D - Desativar Produto");
            tela.addMenuOption("R - Reativar Produto");
            tela.addMenuOption("I - Inserir novo Valor");
            tela.addMenuOption("V - Voltar");
            
            tela.print();
            String opcao = tela.getOpcao().toUpperCase();
            
            switch (opcao) {
                case "E":
                    editarProduto(produto);
                    break;
                case "D":
                    produto.desativar();
                    break;
                case "R":
                    produto.reativar();
                    break;                    
                case "I":
                    //inserirNovoPreco();
                    break;
                case "V":
                    sair = true;
                    break;
                
            }
        }
            
    }

    public static void desativarProduto(Produto produto) throws SQLException{
        produto.desativar();
    }

    public static void editarProduto(Produto produto) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Digite o nome do produto: [" + produto.getProduto() + "]");
        String novoNome = scanner.nextLine();
        if (!novoNome.isEmpty()) {
            produto.setProduto(novoNome);
        }

        System.out.println("Digite a marca do produto: [" + produto.getMarca() + "]");
        String novaMarca = scanner.nextLine();
        if (!novaMarca.isEmpty()) {
            produto.setMarca(novaMarca);
        }

        System.out.println("Digite o modelo do produto: [" + produto.getModelo() + "]");
        String novoModelo = scanner.nextLine();
        if (!novoModelo.isEmpty()) {
            produto.setModelo(novoModelo);
        }

        System.out.println("Digite o código de barras do produto: [" + produto.getCodigoBarra() + "]");
        String novoCodigoBarra = scanner.nextLine();
        if (!novoCodigoBarra.isEmpty()) {
            produto.setCodigoBarra(novoCodigoBarra);
        }

        produto.salvar();
        
        System.out.println("Produto atualizado com sucesso!");        
    }
 


}
