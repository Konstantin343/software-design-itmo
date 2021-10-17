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
public class QueryServlet extends BaseProductsServlet {
    public QueryServlet(ProductsHtmlBuilder htmlBuilder) {
        super(htmlBuilder);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String command = request.getParameter("command");

        try (ProductsManager productsManager = new ProductsDatabaseManager("jdbc:sqlite:test.db")) {
            if ("max".equals(command)) {
                htmlBuilder.buildMaxPriceHtml(productsManager.maxPrice(), response.getWriter());
            } else if ("min".equals(command)) {
                htmlBuilder.buildMinPriceHtml(productsManager.minPrice(), response.getWriter());
            } else if ("sum".equals(command)) {
                htmlBuilder.buildSumPriceHtml(productsManager.sumPrice(), response.getWriter());
            } else if ("count".equals(command)) {
                htmlBuilder.buildCountProductsHtml(productsManager.countProducts(), response.getWriter());
            } else {
                htmlBuilder.buildUnknownCommandHtml(command, response.getWriter());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        setOkHtmlResponse(response);
    }

}
