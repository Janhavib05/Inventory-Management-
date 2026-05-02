package com.stockpro.service;

import com.stockpro.dto.ProductDTO;
import com.stockpro.exception.ResourceNotFoundException;
import com.stockpro.model.Product;
import com.stockpro.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository repo;

    // ─── Read ─────────────────────────────────────────────────────────────────

    public List<ProductDTO.Response> getAll(String search) {
        List<Product> products = (search != null && !search.isBlank())
                ? repo.searchByKeyword(search.trim())
                : repo.findAll();
        return products.stream().map(this::toResponse).toList();
    }

    public ProductDTO.Response getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    public ProductDTO.Summary getSummary() {
        return new ProductDTO.Summary(
                repo.countAll(),
                repo.countLowStock(),
                repo.totalInventoryValue()
        );
    }

    public List<Product> getAllForExport() {
        return repo.findAll();
    }

    // ─── Write ────────────────────────────────────────────────────────────────

    @Transactional
    public ProductDTO.Response create(ProductDTO.Request req) {
        Product product = Product.builder()
                .name(req.getName())
                .desc(req.getDesc())
                .price(req.getPrice())
                .qty(req.getQty())
                .build();
        return toResponse(repo.save(product));
    }

    @Transactional
    public ProductDTO.Response update(Long id, ProductDTO.Request req) {
        Product product = findOrThrow(id);
        product.setName(req.getName());
        product.setDesc(req.getDesc());
        product.setPrice(req.getPrice());
        product.setQty(req.getQty());
        return toResponse(repo.save(product));
    }

    @Transactional
    public ProductDTO.Response patch(Long id, ProductDTO.PatchRequest req) {
        Product product = findOrThrow(id);
        if (req.getName()  != null) product.setName(req.getName());
        if (req.getDesc()  != null) product.setDesc(req.getDesc());
        if (req.getPrice() != null) product.setPrice(req.getPrice());
        if (req.getQty()   != null) product.setQty(req.getQty());
        return toResponse(repo.save(product));
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException(id);
        repo.deleteById(id);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private Product findOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    private ProductDTO.Response toResponse(Product p) {
        return ProductDTO.Response.builder()
                .id(p.getId())
                .name(p.getName())
                .desc(p.getDesc())
                .price(p.getPrice())
                .qty(p.getQty())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}
