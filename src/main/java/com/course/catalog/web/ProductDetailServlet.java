package com.course.catalog.web;

import com.course.catalog.model.ProductDTO;
import com.course.catalog.model.Review;
import com.course.catalog.service.ProductService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/catalog/product")
public class ProductDetailServlet extends HttpServlet {

    @Inject
    private ProductService productService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long id = Long.valueOf(req.getParameter("id"));
        ProductDTO product = productService.getProduct(id);
        List<Review> reviews = productService.getReviews(id);

        req.setAttribute("product", product);
        req.setAttribute("reviews", reviews);
        req.getRequestDispatcher("/WEB-INF/views/product-detail.jsp").forward(req, resp);
    }
}