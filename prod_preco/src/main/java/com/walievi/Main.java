package com.walievi;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            boolean sair = false;
            while (!sair) {
                String opcao = menuProdutos();

                try {
                    verProduto(Integer.parseInt(opcao));
                } catch (NumberFormatException e) {
                    switch(opcao) {
                        case "I":
                            inserirNovoProduto();
                            break;
                        case "D":
                            verDesativados();
                            break;
                        case "S":
                            sair = true;
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String menuProdutos() throws SQLException{
        Tela tela = new Tela();
        
        tela.newColumn("ID", 5);
        tela.newColumn("Produto", 15);
        tela.newColumn("Marca", 20);
        tela.newColumn("Modelo", 20);
        tela.newColumn("Código de Barra", 20);
        tela.newColumn("Preço", 10);

        for (Produto produto : Produto.getAll()) {
            tela.addValue("ID", String.valueOf(produto.getId()));
            tela.addValue("Produto", produto.getProduto());
            tela.addValue("Marca", produto.getMarca());
            tela.addValue("Modelo", produto.getModelo());
            tela.addValue("Código de Barra", produto.getCodigoBarra());
            String valorString = "---";
            System.out.println(produto.preco().getValorCobrar());
            if (produto.preco() != null && produto.preco().getValorCobrar() != null){
                valorString = produto.preco().getValorCobrar().toString();
            }
            tela.addValue("Preço", valorString);
            tela.addLine();
        }        

        tela.addMenuOption("Digite o ID do produto para ver detalhes ou editar");
        tela.addMenuOption("I - Inserir novo produto");
        tela.addMenuOption("D - Ver produtos desativados");
        tela.addMenuOption("S - Sair");

        tela.print();

        return Tela.getOpcao();
    }

    public static void verDesativados() throws SQLException{
        Tela tela = new Tela();
        
        tela.newColumn("ID", 5);
        tela.newColumn("Produto", 15);
        tela.newColumn("Marca", 20);
        tela.newColumn("Modelo", 20);
        tela.newColumn("Código de Barra", 20);
        tela.newColumn("Preço", 10);
        tela.newColumn("Desativado em", 20);

        for (Produto produto : Produto.getDeleteds()) {
            tela.addValue("ID", String.valueOf(produto.getId()));
            tela.addValue("Produto", produto.getProduto());
            tela.addValue("Marca", produto.getMarca());
            tela.addValue("Modelo", produto.getModelo());
            tela.addValue("Código de Barra", produto.getCodigoBarra());
            String valorString = "---";
            System.out.println(produto.preco().getValorCobrar());
            if (produto.preco() != null && produto.preco().getValorCobrar() != null){
                valorString = produto.preco().getValorCobrar().toString();
            }
            tela.addValue("Preço", valorString);
            tela.addValue("Desativado em", produto.getFormattedDeletedAt());
            tela.addLine();
        }        

        tela.addMenuOption("Digite o ID do produto para ver detalhes ou editar");
        tela.addMenuOption("V - Voltar");

        tela.print();

        String opcao = Tela.getOpcao();
        try {
            verProduto(Integer.parseInt(opcao));
        } catch (NumberFormatException e) {
            if(opcao.equals("V")){
                return;
            }
        }        
    }    

    public static void inserirNovoProduto() throws SQLException {
        Produto novoProduto = new Produto();
        
        System.out.println("Digite o nome do produto:");
        novoProduto.setProduto(Tela.getOpcao());
        
        System.out.println("Digite a marca do produto:");
        novoProduto.setMarca(Tela.getOpcao());
        
        System.out.println("Digite o modelo do produto:");
        novoProduto.setModelo(Tela.getOpcao());
        
        System.out.println("Digite o código de barras do produto:");
        novoProduto.setCodigoBarra(Tela.getOpcao());
        
        novoProduto.salvar();
    }

    public static void verProduto(int id) throws SQLException {
        
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
            tela.addMenuOption("H - Ver histórico de preços");
            tela.addMenuOption("I - Inserir novo Valor");
            tela.addMenuOption("V - Voltar");
            
            tela.print();
            String opcao = Tela.getOpcao();
            
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
                case "H":
                    historicoPrecos(produto.precos());
                    break;
                case "I":
                    inserirNovoPreco(produto);
                    break;
                case "V":
                    sair = true;
                    break;
                
            }
        }
            
    }
    
    public static void inserirNovoPreco(Produto produto) throws SQLException{
        System.out.println("Digite o novo preço:");
        if(produto.preco() != null && produto.preco().getValorCobrar() != null){
            System.out.println("[" + produto.preco().getValorCobrar() + "]");
        }

        String novoPrecoString = Tela.getOpcao();
        if (!novoPrecoString.isEmpty()) {
            BigDecimal novoPreco = new BigDecimal(novoPrecoString);
            produto.preco().setValorCobrar(novoPreco);
        }

        System.out.println("Digite o novo desconto máximo:");
        if(produto.preco() != null && produto.preco().getMaxDesconto() != null){
            System.out.println("[" + produto.preco().getMaxDesconto() + "]");
        }

        String novoDescontoString = Tela.getOpcao();
        if (!novoDescontoString.isEmpty()) {
            BigDecimal novoDesconto = new BigDecimal(novoDescontoString);
            produto.preco().setMaxDesconto(novoDesconto);
        }

        System.out.println("Digite o novo valor pago:");
        if(produto.preco() != null && produto.preco().getValorPago() != null){
            System.out.println("[" + produto.preco().getValorPago() + "]");
        }

        String novoValorPagoString = Tela.getOpcao();
        if (!novoValorPagoString.isEmpty()) {
            BigDecimal novoValorPago = new BigDecimal(novoValorPagoString);
            produto.preco().setValorPago(novoValorPago);
        }

        produto.preco().salvar();
        
    }

    public static void editarProduto(Produto produto) throws SQLException {
        
        System.out.println("Digite o nome do produto: [" + produto.getProduto() + "]");
        String novoNome = Tela.getOpcao();
        if (!novoNome.isEmpty()) {
            produto.setProduto(novoNome);
        }

        System.out.println("Digite a marca do produto: [" + produto.getMarca() + "]");
        String novaMarca = Tela.getOpcao();
        if (!novaMarca.isEmpty()) {
            produto.setMarca(novaMarca);
        }

        System.out.println("Digite o modelo do produto: [" + produto.getModelo() + "]");
        String novoModelo = Tela.getOpcao();
        if (!novoModelo.isEmpty()) {
            produto.setModelo(novoModelo);
        }

        System.out.println("Digite o código de barras do produto: [" + produto.getCodigoBarra() + "]");
        String novoCodigoBarra = Tela.getOpcao();
        if (!novoCodigoBarra.isEmpty()) {
            produto.setCodigoBarra(novoCodigoBarra);
        }

        produto.salvar();
        
        System.out.println("Produto atualizado com sucesso!");        
    }
 


    public static void historicoPrecos(List<Preco> precos) throws SQLException {    
        Tela tela = new Tela();
        tela.newColumn("Data", 20);
        tela.newColumn("Preço Venda", 15);
        tela.newColumn("Desconto Máx", 15);
        tela.newColumn("Preço Pago", 15);
        tela.newColumn("Desativado em", 20);
    
        for (Preco preco : precos) {
            tela.addValue("Data", preco.getFormattedInsertAt());
            tela.addValue("Preço Venda", preco.getValorCobrar().toString());
            tela.addValue("Desconto Máx", preco.getMaxDesconto().toString());
            tela.addValue("Preço Pago", preco.getMaxDesconto().toString());
            tela.addValue("Desativado em", preco.getDeletedAt() != null ? preco.getFormattedDeletedAt() : "---");
            tela.addLine();
        }
        tela.addMenuOption("V - Voltar");    
        tela.print();

        Tela.getOpcao();
    }
    


}
