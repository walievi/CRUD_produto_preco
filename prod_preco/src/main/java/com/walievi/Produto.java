package com.walievi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import com.walievi.orm.DB;

public class Produto {
    private int id;
    private String produto;
    private String marca;
    private String modelo;
    private String codigoBarra;
    private Timestamp insertAt;
    private Timestamp updateAt;
    private Timestamp deletedAt;
    private DB db;

    public Produto() throws SQLException {
        this.db = new DB();
    }

    public Produto(int id) throws SQLException {
        this();
        loadFromDB(id);
    }

    public List<Preco> precos() throws SQLException{
        return Preco.getAllByProdutoId(this.id);
    }

    public void salvar() throws SQLException {
        PreparedStatement stmt;
        if (this.id > 0) {
            // Update
            stmt = db.getConnection().prepareStatement("UPDATE produtos SET produto = ?, marca = ?, modelo = ?, codigo_barra = ?, update_at = NOW() WHERE id = ?");
            stmt.setInt(5, this.id);
        } else {
            // Insert
            stmt = db.getConnection().prepareStatement("INSERT INTO produtos (produto, marca, modelo, codigo_barra, insert_at, update_at) VALUES (?, ?, ?, ?, NOW(), NOW())", Statement.RETURN_GENERATED_KEYS);
        }

        stmt.setString(1, this.produto);
        stmt.setString(2, this.marca);
        stmt.setString(3, this.modelo);
        stmt.setString(4, this.codigoBarra);

        stmt.executeUpdate();

        // Obtém o ID gerado para inserções
        if (this.id == 0) {
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                this.id = generatedKeys.getInt(1);
            }
        }
    }
    
    private void loadFromDB(int id) throws SQLException {
        PreparedStatement stmt = db.getConnection().prepareStatement("SELECT * FROM produtos WHERE id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            this.id = rs.getInt("id");
            this.produto = rs.getString("produto");
            this.marca = rs.getString("marca");
            this.modelo = rs.getString("modelo");
            this.codigoBarra = rs.getString("codigo_barra");
            this.insertAt = rs.getTimestamp("insert_at");
            this.updateAt = rs.getTimestamp("update_at");
            this.deletedAt = rs.getTimestamp("deleted_at");
        }
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getProduto() {
        return produto;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public Timestamp getInsertAt() {
        return insertAt;
    }
    
    public String getFormattedInsertAt() {
        return Util.dataToBr(insertAt);
    }
    
    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public String getFormattedUpdateAt() {
        return Util.dataToBr(updateAt);
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public String getFormattedDeletedAt() {
        return Util.dataToBr(deletedAt);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public void desativar() throws SQLException {
        PreparedStatement stmt = db.getConnection().prepareStatement("UPDATE produtos SET deleted_at = NOW() WHERE id = ?");
        stmt.setInt(1, this.id);
    }

    public void reativar() throws SQLException {
        PreparedStatement stmt = db.getConnection().prepareStatement("UPDATE produtos SET deleted_at = NULL WHERE id = ?");
        stmt.setInt(1, this.id);
    }    
    
    public static List<Produto> getAll() throws SQLException {
        DB db = new DB();
        List<Produto> produtos = new ArrayList<>();
        PreparedStatement stmt = db.getConnection().prepareStatement("SELECT * FROM produtos WHERE deleted_at IS NULL");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            Produto produto = new Produto(id);
            produtos.add(produto);
        }

        return produtos;
    }

    public static List<Produto> getDeleted() throws SQLException {
        DB db = new DB();
        List<Produto> produtos = new ArrayList<>();
        PreparedStatement stmt = db.getConnection().prepareStatement("SELECT * FROM produtos WHERE deleted_at IS NOT NULL");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            Produto produto = new Produto(id);
            produtos.add(produto);
        }

        return produtos;
    }    
}
