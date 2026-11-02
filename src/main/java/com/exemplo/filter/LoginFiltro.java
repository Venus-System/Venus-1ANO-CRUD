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

// Filtro que roda antes de toda página e só deixa entrar no painel quem fez login.
@WebFilter("/*")
public class LoginFiltro implements Filter {

    // Páginas que qualquer pessoa pode abrir sem login.
    private static final Set<String> PAGINAS_LIVRES = Set.of(
            "/", "/index.html", "/aplicativo.html", "/login", "/sair",
            "/esqueceu-a-senha", "/nova-senha", "/senha-alterada", "/erro");

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // Tira o nome da aplicação do endereço, para sobrar só a página pedida.
        String caminho = request.getRequestURI().substring(request.getContextPath().length());

        if (PAGINAS_LIVRES.contains(caminho) || caminho.startsWith("/assets/")) {
            chain.doFilter(request, response);
            return;
        }

        // false: só pega a sessão se ela já existir. Se tiver usuário logado, a página abre.
        HttpSession sessao = request.getSession(false);
        if (sessao != null && sessao.getAttribute("usuarioLogado") != null) {
            chain.doFilter(request, response);
            return;
        }

        // PUT e DELETE vêm do JavaScript, então ele recebe o status 401 em vez de ser redirecionado.
        if (!"GET".equals(request.getMethod()) && !"POST".equals(request.getMethod())) {
            response.setStatus(401);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain");
            response.getWriter().write("Sua sessão terminou. Entre de novo pelo login.");
            return;
        }

        // Sem login: manda para a tela de login.
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
