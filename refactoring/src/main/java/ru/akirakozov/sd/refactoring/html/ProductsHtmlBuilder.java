package ru.akirakozov.sd.refactoring.html;

import ru.akirakozov.sd.refactoring.products.Product;

import java.io.PrintWriter;
import java.util.List;

/**
 * @author Konstantin343
 * 
 * Interface for building HTML responses in Products Web App
 */
public interface ProductsHtmlBuilder {
    /**
     * Builds HTML with list of products
     * @param products list of products to show
     * @param writer writer to print html in it
     */
    void buildProductsListHtml(List<Product> products, PrintWriter writer);

    /**
     * Builds HTML with product with min price
     * @param product product to show
     * @param writer writer to print html in it
     */
    void buildMinPriceHtml(Product product, PrintWriter writer);

    /**
     * Builds HTML with product with max price
     * @param product product to show
     * @param writer writer to print html in it
     */
    void buildMaxPriceHtml(Product product, PrintWriter writer);

    /**
     * Builds HTML with sum price of all products
     * @param sum sum price of all products
     * @param writer writer to print html in it
     */
    void buildSumPriceHtml(Long sum, PrintWriter writer);

    /**
     * Builds HTML with count of all products
     * @param count count of all products
     * @param writer writer to print html in it
     */
    void buildCountProductsHtml(Integer count, PrintWriter writer);

    /**
     * Builds HTML with response on successfully added product
     * @param product product that as added
     * @param writer writer to print html in it
     */
    void buildAddProductHtml(Product product, PrintWriter writer);

    /**
     * Build HTML with information about unknown user command 
     * @param command unknown command
     * @param writer writer to print html in it
     */
    void buildUnknownCommandHtml(String command, PrintWriter writer);
}
