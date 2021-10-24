package ru.akirakozov.sd.refactoring.html;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.akirakozov.sd.refactoring.products.Product;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class ProductsHtmlBuilderTests {
    private final ProductsHtmlBuilder productsHtmlBuilder = new SimpleProductsHtmlBuilder();

    @Test
    public void testHtml() {
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);

        productsHtmlBuilder.buildAddProductHtml(new Product("Phone", 120), writer);
        productsHtmlBuilder.buildCountProductsHtml(5, writer);
        productsHtmlBuilder.buildSumPriceHtml(1000L, writer);
        productsHtmlBuilder.buildMinPriceHtml(new Product("Phone", 120), writer);
        productsHtmlBuilder.buildMaxPriceHtml(new Product("Phone", 120), writer);
        productsHtmlBuilder.buildProductsListHtml(List.of(
                new Product("Phone", 120),
                new Product("TV", 280)), writer);
        productsHtmlBuilder.buildUnknownCommandHtml("unknown-command", writer);
        Assert.assertEquals(out.toString(), 
                "OK\n" +
                        "<html><body>\n" +
                        "Number of products: \n" +
                        "5\n" +
                        "</body></html>\n" +
                        "<html><body>\n" +
                        "Summary price: \n" +
                        "1000\n" +
                        "</body></html>\n" +
                        "<html><body>\n" +
                        "<h1>Product with min price: </h1>\n" +
                        "Phone\t120</br>\n" +
                        "</body></html>\n" +
                        "<html><body>\n" +
                        "<h1>Product with max price: </h1>\n" +
                        "Phone\t120</br>\n" +
                        "</body></html>\n" +
                        "<html><body>\n" +
                        "Phone\t120</br>\n" +
                        "TV\t280</br>\n" +
                        "</body></html>\n" +
                        "Unknown command: unknown-command\n");
    }
}
