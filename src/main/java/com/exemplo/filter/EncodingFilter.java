package com.exemplo.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter("/*")
//aqui ele registra o filtro no Tomcat e diz que ele vale para todas as URLs. O /* que configura todos de um avez.
public class EncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Como o navegador envia os dados do formulário (acentos)
        request.setCharacterEncoding("UTF-8");

        // Como a resposta volta para o navegador
        response.setCharacterEncoding("UTF-8");

        // Passa a requisição adiante: sem essa linha o servlet nunca é chamado
        chain.doFilter(request, response);
    }
}