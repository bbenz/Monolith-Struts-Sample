package com.skiadvisor.service;

import com.skiadvisor.model.Product;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Service for accessing the ski shop database
 */
public class DatabaseService {
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private String dbDriver;

    public DatabaseService() throws IOException {
        loadDatabaseConfig();
    }

    private void loadDatabaseConfig() throws IOException {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new IOException("Unable to find database.properties");
            }
            props.load(input);
            
            dbUrl = props.getProperty("db.url");
            dbUsername = props.getProperty("db.username");
            dbPassword = props.getProperty("db.password");
            dbDriver = props.getProperty("db.driver");
            
            // Load JDBC driver
            try {
                Class.forName(dbDriver);
            } catch (ClassNotFoundException e) {
                throw new IOException("Database driver not found: " + dbDriver, e);
            }
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }

    /**
     * Get products by category name
     */
    public List<Product> getProductsByCategory(String categoryName) throws SQLException {
        List<Product> products = new ArrayList<>();
        
        String sql = "SELECT p.id, p.name, p.description, p.category_id, " +
                    "c.name as category_name, p.status, " +
                    "pr.regular_price, i.quantity " +
                    "FROM products p " +
                    "INNER JOIN categories c ON p.category_id = c.id " +
                    "LEFT JOIN prices pr ON p.id = pr.product_id " +
                    "LEFT JOIN inventory i ON p.id = i.product_id " +
                    "WHERE p.status = 'ACTIVE' AND " +
                    "(LOWER(c.name) = LOWER(?) " +
                    " OR c.parent_id IN (SELECT id FROM categories WHERE LOWER(name) = LOWER(?))) " +
                    "ORDER BY pr.regular_price";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categoryName);
            stmt.setString(2, categoryName);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("category_id"),
                        rs.getString("category_name"),
                        rs.getString("status"),
                        rs.getBigDecimal("regular_price"),
                        rs.getInt("quantity")
                    );
                    products.add(product);
                }
            }
        }
        
        return products;
    }

    /**
     * Get all available categories
     */
    public List<String> getAllCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        
        String sql = "SELECT DISTINCT name FROM categories ORDER BY name";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        }
        
        return categories;
    }

    /**
     * Search products by keyword
     */
    public List<Product> searchProducts(String keyword) throws SQLException {
        List<Product> products = new ArrayList<>();
        
        String sql = "SELECT p.id, p.name, p.description, p.category_id, " +
                    "c.name as category_name, p.status, " +
                    "pr.regular_price, i.quantity " +
                    "FROM products p " +
                    "INNER JOIN categories c ON p.category_id = c.id " +
                    "LEFT JOIN prices pr ON p.id = pr.product_id " +
                    "LEFT JOIN inventory i ON p.id = i.product_id " +
                    "WHERE p.status = 'ACTIVE' AND " +
                    "(LOWER(p.name) LIKE LOWER(?) OR LOWER(p.description) LIKE LOWER(?)) " +
                    "ORDER BY pr.regular_price";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("category_id"),
                        rs.getString("category_name"),
                        rs.getString("status"),
                        rs.getBigDecimal("regular_price"),
                        rs.getInt("quantity")
                    );
                    products.add(product);
                }
            }
        }
        
        return products;
    }

    /**
     * Get product statistics
     */
    public String getProductStatistics() throws SQLException {
        StringBuilder stats = new StringBuilder();
        
        String sql = "SELECT c.name as category, COUNT(p.id) as product_count, " +
                    "AVG(pr.regular_price) as avg_price " +
                    "FROM categories c " +
                    "LEFT JOIN products p ON c.id = p.category_id AND p.status = 'ACTIVE' " +
                    "LEFT JOIN prices pr ON p.id = pr.product_id " +
                    "GROUP BY c.name " +
                    "ORDER BY c.name";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            stats.append("\n=== PRODUCT INVENTORY STATISTICS ===\n");
            while (rs.next()) {
                stats.append(String.format("%-15s: %3d products, Avg Price: $%.2f\n",
                    rs.getString("category"),
                    rs.getInt("product_count"),
                    rs.getBigDecimal("avg_price") != null ? 
                        rs.getBigDecimal("avg_price").doubleValue() : 0.0
                ));
            }
        }
        
        return stats.toString();
    }
}
