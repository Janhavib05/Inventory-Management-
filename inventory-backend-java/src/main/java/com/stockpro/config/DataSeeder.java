package com.stockpro.config;

import com.stockpro.model.Product;
import com.stockpro.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository repo;

    @Override
    public void run(String... args) {
        if (repo.count() > 0) return;

        repo.saveAll(List.of(
            Product.builder().name("Wireless Mouse").desc("Ergonomic 2.4GHz wireless mouse")
                    .price(new BigDecimal("29.99")).qty(150).build(),
            Product.builder().name("Mechanical Keyboard").desc("TKL RGB mechanical keyboard")
                    .price(new BigDecimal("89.99")).qty(75).build(),
            Product.builder().name("USB-C Hub").desc("7-in-1 USB-C multiport adapter")
                    .price(new BigDecimal("49.99")).qty(8).build(),       // low stock
            Product.builder().name("27\" Monitor").desc("4K IPS display, 144Hz")
                    .price(new BigDecimal("399.99")).qty(30).build(),
            Product.builder().name("Laptop Stand").desc("Adjustable aluminium laptop riser")
                    .price(new BigDecimal("34.99")).qty(5).build()         // low stock
        ));

        log.info("✅  Seeded {} sample products", repo.count());
    }
}
