package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.html.ProductsHtmlBuilder;
import ru.akirakozov.sd.refactoring.products.ProductsManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseProductsServlet extends HttpServlet {
    protected final ProductsHtmlBuilder htmlBuilder;
    protected final ProductsManager productsManager;
    
    public BaseProductsServlet(ProductsManager productsManager, ProductsHtmlBuilder htmlBuilder) {
        this.productsManager = productsManager;
        this.htmlBuilder = htmlBuilder;
    }
    
    protected void setOkHtmlResponse(HttpServletResponse response) {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
