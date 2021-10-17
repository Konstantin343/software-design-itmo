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
    public QueryServlet(ProductsManager manager, ProductsHtmlBuilder htmlBuilder) {
        super(manager, htmlBuilder);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

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

        setOkHtmlResponse(response);
    }

}
