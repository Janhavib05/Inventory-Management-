package com.stockpro.repository;

import com.stockpro.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Search by name or description (case-insensitive)
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.desc) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByKeyword(@Param("keyword") String keyword);

    // Low stock items (qty < threshold)
    List<Product> findByQtyLessThan(int threshold);

    // Summary aggregates
    @Query("SELECT COUNT(p) FROM Product p")
    long countAll();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.qty < 10")
    long countLowStock();

    @Query("SELECT COALESCE(SUM(p.price * p.qty), 0) FROM Product p")
    BigDecimal totalInventoryValue();
}
