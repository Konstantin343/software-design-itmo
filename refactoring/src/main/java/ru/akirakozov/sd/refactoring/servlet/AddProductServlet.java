package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.ProductsDatabaseManager;
import ru.akirakozov.sd.refactoring.html.ProductsHtmlBuilder;
import ru.akirakozov.sd.refactoring.products.Product;
import ru.akirakozov.sd.refactoring.products.ProductsManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class AddProductServlet extends BaseProductsServlet {

    public AddProductServlet(ProductsHtmlBuilder htmlBuilder) {
        super(htmlBuilder);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Product product = new Product(
                request.getParameter("name"),
                Long.parseLong(request.getParameter("price"))
        );

        try (ProductsManager productsManager = new ProductsDatabaseManager("jdbc:sqlite:test.db")) {
            productsManager.addProduct(product);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        htmlBuilder.buildAddProductHtml(product, response.getWriter());
        setOkHtmlResponse(response);
    }
}
