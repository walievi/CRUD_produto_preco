package com.walievi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.walievi.orm.DB;

import java.math.BigDecimal;

public class Preco {
    private int id;
    private int produtoId;
    private BigDecimal valorCobrar;
    private BigDecimal maxDesconto;
    private BigDecimal valorPago;
    private Timestamp insertAt;
    private Timestamp updateAt;
    private Timestamp deletedAt;
    private DB db;

    public Preco() throws SQLException {
        this.db = new DB();
    }

    public Preco(int id) throws SQLException {
        this();
        loadFromDB(id);
    }

    // Implementação do método salvar
    public void salvar() throws SQLException {
        PreparedStatement stmt;
        if (this.id > 0) {
            // Update
            stmt = db.getConnection().prepareStatement("UPDATE preco SET produto_id = ?, valor_cobrar = ?, max_desconto = ?, valor_pago = ?, update_at = NOW() WHERE id = ?");
            stmt.setInt(5, this.id); // Preenchendo o último valor que é exclusivo do UPDATE
        } else {
            // Insert
            stmt = db.getConnection().prepareStatement("INSERT INTO preco (produto_id, valor_cobrar, max_desconto, valor_pago, insert_at, update_at) VALUES (?, ?, ?, ?, NOW(), NOW())", Statement.RETURN_GENERATED_KEYS);
        }

        stmt.setInt(1, this.produtoId);
        stmt.setBigDecimal(2, this.valorCobrar);
        stmt.setBigDecimal(3, this.maxDesconto);
        stmt.setBigDecimal(4, this.valorPago);

        stmt.executeUpdate();

        // Obtém o ID gerado para inserções
        if (this.id == 0) {
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                this.id = generatedKeys.getInt(1);
            }
        }
    }

    // Implementação do método loadFromDB
    private void loadFromDB(int id) throws SQLException {
        PreparedStatement stmt = db.getConnection().prepareStatement("SELECT * FROM precos WHERE id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            this.id = rs.getInt("id");
            this.produtoId = rs.getInt("produto_id");
            this.valorCobrar = rs.getBigDecimal("valor_cobrar");
            this.maxDesconto = rs.getBigDecimal("max_desconto");
            this.valorPago = rs.getBigDecimal("valor_pago");
            this.insertAt = rs.getTimestamp("insert_at");
            this.updateAt = rs.getTimestamp("update_at");
            this.deletedAt = rs.getTimestamp("deleted_at");
        }
    }

    public Produto produto() throws SQLException {
        return new Produto(this.produtoId);
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public BigDecimal getValorCobrar() {
        return valorCobrar;
    }

    public void setValorCobrar(BigDecimal valorCobrar) {
        this.valorCobrar = valorCobrar;
    }

    public BigDecimal getMaxDesconto() {
        return maxDesconto;
    }

    public void setMaxDesconto(BigDecimal maxDesconto) {
        this.maxDesconto = maxDesconto;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public Timestamp getInsertAt() {
        return insertAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void desativar() throws SQLException {
        if (this.id > 0) {
            PreparedStatement stmt = db.getConnection().prepareStatement("UPDATE precos SET deleted_at = NOW() WHERE id = ?");
            stmt.setInt(1, this.id);
        } else {
            System.out.println("Preco não pode ser desativado porque ainda não foi salvo no banco de dados.");
        }
    }
    

    public static List<Preco> getAll() throws SQLException {
        DB db = new DB();
        List<Preco> precos = new ArrayList<>();
        PreparedStatement stmt = db.getConnection().prepareStatement("SELECT * FROM precos WHERE deleted_at IS NULL");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            Preco preco = new Preco(id);
            precos.add(preco);
        }

        return precos;
    }

    public static List<Preco> getAllByProdutoId(int produtoId) throws SQLException {
        DB db = new DB();
        List<Preco> precos = new ArrayList<>();
        PreparedStatement stmt = db.getConnection().prepareStatement("SELECT * FROM precos WHERE produto_id = ? AND deleted_at IS NULL");
        stmt.setInt(1, produtoId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            Preco preco = new Preco(id);
            precos.add(preco);
        }

        return precos;
    }

    public static Preco getCurrentByProdutoId(int produtoId) throws SQLException {
        DB db = new DB();
        PreparedStatement stmt = db.getConnection().prepareStatement("SELECT * FROM precos WHERE produto_id = ? AND deleted_at IS NULL ORDER BY id DESC LIMIT 1");
        stmt.setInt(1, produtoId);
        ResultSet rs = stmt.executeQuery();
    
        if (rs.next()) {
            int id = rs.getInt("id");
            return new Preco(id);
        }
    
        return null;
    }
}
