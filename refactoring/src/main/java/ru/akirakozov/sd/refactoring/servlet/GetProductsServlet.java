package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.ProductsDatabaseManager;
import ru.akirakozov.sd.refactoring.html.ProductsHtmlBuilder;
import ru.akirakozov.sd.refactoring.products.Product;
import ru.akirakozov.sd.refactoring.products.ProductsManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends BaseProductsServlet {

    public GetProductsServlet(ProductsHtmlBuilder htmlBuilder) {
        super(htmlBuilder);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try (ProductsManager productManager = new ProductsDatabaseManager("jdbc:sqlite:test.db")) {
            htmlBuilder.buildProductsListHtml(productManager.getProducts(), response.getWriter());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        setOkHtmlResponse(response);
    }
}
