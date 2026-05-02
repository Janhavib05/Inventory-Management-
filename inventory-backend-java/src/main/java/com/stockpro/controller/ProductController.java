package com.stockpro.controller;

import com.stockpro.dto.ApiResponse;
import com.stockpro.dto.ProductDTO;
import com.stockpro.model.Product;
import com.stockpro.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    // GET /api/products?search=
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO.Response>>> getAll(
            @RequestParam(required = false) String search) {
        List<ProductDTO.Response> products = service.getAll(search);
        return ResponseEntity.ok(ApiResponse.ok(products, products.size()));
    }

    // GET /api/products/summary
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<ProductDTO.Summary>> getSummary() {
        return ResponseEntity.ok(ApiResponse.ok(service.getSummary()));
    }

    // GET /api/products/export/csv
    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCSV() {
        List<Product> products = service.getAllForExport();
        StringBuilder csv = new StringBuilder("Name,Description,Price,Qty\n");
        products.forEach(p ->
            csv.append(String.format("\"%s\",\"%s\",%s,%d%n",
                    p.getName(), p.getDesc(), p.getPrice(), p.getQty()))
        );

        byte[] bytes = csv.toString().getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "inventory.csv");
        headers.setContentLength(bytes.length);

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    // GET /api/products/:id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO.Response>> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.getById(id)));
    }

    // POST /api/products
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO.Response>> create(
            @Valid @RequestBody ProductDTO.Request req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(service.create(req)));
    }

    // PUT /api/products/:id  (full update)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO.Response>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO.Request req) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, req)));
    }

    // PATCH /api/products/:id  (partial update)
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO.Response>> patch(
            @PathVariable Long id,
            @RequestBody ProductDTO.PatchRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(service.patch(id, req)));
    }

    // DELETE /api/products/:id
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Product deleted successfully"));
    }
}
