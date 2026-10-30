package com.exemplo.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class LoginFiltro implements Filter {

    private static final Set<String> PAGINAS_LIVRES = Set.of(
            "/", "/index.html", "/login", "/criar-conta", "/sair",
            "/esqueceu-a-senha", "/nova-senha", "/senha-alterada", "/verificar-codigo", "/erro");

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String caminho = request.getRequestURI().substring(request.getContextPath().length());

        if (PAGINAS_LIVRES.contains(caminho) || caminho.startsWith("/assets/")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession sessao = request.getSession(false);
        if (sessao != null && sessao.getAttribute("usuarioLogado") != null) {
            chain.doFilter(request, response);
            return;
        }

        if (!"GET".equals(request.getMethod()) && !"POST".equals(request.getMethod())) {
            response.setStatus(401);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain");
            response.getWriter().write("Sua sessão terminou. Entre de novo pelo login.");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/login");
    }
}
