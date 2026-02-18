package com.skishop.repository;

import com.skishop.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByNameContainingIgnoreCase(String keyword);
    List<Product> findByBrandContainingIgnoreCase(String keyword);
    List<Product> findByStatus(String status);

    @Query("SELECT p FROM Product p WHERE (:keyword IS NULL OR :keyword = '' OR UPPER(p.name) LIKE UPPER(CONCAT('%', :keyword, '%')) OR UPPER(p.brand) LIKE UPPER(CONCAT('%', :keyword, '%')) OR UPPER(p.description) LIKE UPPER(CONCAT('%', :keyword, '%')))")
    List<Product> search(@Param("keyword") String keyword);

    List<Product> findByCategoryIdInAndStatus(List<String> categoryIds, String status);
}
