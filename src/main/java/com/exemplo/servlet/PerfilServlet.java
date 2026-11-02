package com.exemplo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Servlet da tela de perfil do administrador logado.
@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

    // Abre a tela de perfil.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/perfil.jsp").forward(request, response);
    }
}
