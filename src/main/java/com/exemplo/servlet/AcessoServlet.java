package com.exemplo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet({"/sair", "/esqueceu-a-senha", "/nova-senha", "/senha-alterada"})
public class AcessoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        switch (request.getServletPath()) {
            case "/sair":
                HttpSession sessao = request.getSession(false);
                if (sessao != null) {
                    sessao.invalidate();
                }
                response.sendRedirect(request.getContextPath() + "/login?saiu=1");
                break;
            // Estas rotas exibem as telas de recuperação, sem alterar a senha no banco.
            case "/esqueceu-a-senha":
                request.getRequestDispatcher("/WEB-INF/views/esqueceu_a_senha.jsp").forward(request, response);
                break;
            case "/nova-senha":
                request.getRequestDispatcher("/WEB-INF/views/nova_senha.jsp").forward(request, response);
                break;
            default:
                request.getRequestDispatcher("/WEB-INF/views/senha_alterada.jsp").forward(request, response);
        }
    }
}
