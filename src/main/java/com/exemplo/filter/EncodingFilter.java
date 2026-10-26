package com.exemplo.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter("/*")
// Aplica a codificação UTF-8 às requisições e respostas de todas as URLs da aplicação.
public class EncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Define a codificação antes da leitura dos parâmetros para preservar os acentos.
        request.setCharacterEncoding("UTF-8");

        // Define a codificação do texto enviado ao navegador.
        response.setCharacterEncoding("UTF-8");

        // Continua a cadeia de filtros até o recurso solicitado.
        chain.doFilter(request, response);
    }
}