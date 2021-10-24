package ru.akirakozov.sd.refactoring.html;

import ru.akirakozov.sd.refactoring.products.Product;

import java.io.PrintWriter;
import java.util.List;

/**
 * @author Konstantin343
 * 
 * Implementation of {@link ProductsHtmlBuilder} for building simple HTML responses
 */
public class SimpleProductsHtmlBuilder implements ProductsHtmlBuilder {
    @Override
    public void buildProductsListHtml(List<Product> products, PrintWriter writer) {
        buildProductsListHtml(null, products, writer);
    }

    @Override
    public void buildMinPriceHtml(Product product, PrintWriter writer) {
        buildProductsListHtml("<h1>Product with min price: </h1>", List.of(product), writer);
    }

    @Override
    public void buildMaxPriceHtml(Product product, PrintWriter writer) {
        buildProductsListHtml("<h1>Product with max price: </h1>", List.of(product), writer);
    }
    

    @Override
    public void buildSumPriceHtml(Long sum, PrintWriter writer) {
        buildOneLineHtml("Summary price: ", sum, writer);
    }

    @Override
    public void buildCountProductsHtml(Integer count, PrintWriter writer) {
        buildOneLineHtml("Number of products: ", count, writer);
    }

    @Override
    public void buildAddProductHtml(Product product, PrintWriter writer) {
        writer.println("OK");
    }

    @Override
    public void buildUnknownCommandHtml(String command, PrintWriter writer) {
        writer.println("Unknown command: "+ command);
    }

    private void buildOneLineHtml(String caption, Object object, PrintWriter writer) {
        buildWithDefaultHtml(caption, writer, () -> {
            if (object != null) {
                writer.println(object);
            }
        });
    }

    private void buildProductsListHtml(String caption, List<Product> products, PrintWriter writer) {
        buildWithDefaultHtml(caption, writer, () -> {
            for (Product product : products) {
                writer.println(product.getName() + "\t" + product.getPrice() + "</br>");
            }
        });
    }
    
    private void buildWithDefaultHtml(String caption, PrintWriter writer, Runnable action) {
        writer.println("<html><body>");
        if (caption != null) {
            writer.println(caption);
        }
        action.run();
        writer.println("</body></html>");
    }
}
