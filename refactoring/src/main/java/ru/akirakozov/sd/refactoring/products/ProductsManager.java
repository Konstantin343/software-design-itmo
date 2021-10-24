package ru.akirakozov.sd.refactoring.products;

import java.util.List;

/**
 * @author Konstantin343
 * 
 * Interface for managing products in some data store
 */
public interface ProductsManager extends AutoCloseable {
    /**
     * Return all products from data store
     * @return all products from data store
     */
    List<Product> getProducts();

    /**
     * Create empty product's data store if not exists
     */
    void createProducts();

    /**
     * Add product in data store
     * @param product to add
     */
    void addProduct(Product product);

    /**
     * Find product with minimal price
     * @return product with minimal price
     */
    Product minPriceProduct();

    /**
     * Find product with maximal price
     * @return product with maximal price
     */
    Product maxPriceProduct();

    /**
     * Calculate sum of prices of all products
     * @return sum of prices of all products
     */
    Long sumPrice();

    /**
     * Count all products
     * @return amount of products
     */
    Integer countProducts();
}
