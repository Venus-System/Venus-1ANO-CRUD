package com.exemplo.servlet;


import com.exemplo.dao.ProdutoDAO;
import com.exemplo.model.Produto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/produtos")
public class ProdutoServlet extends HttpServlet {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    //create
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");
        String marca = request.getParameter("marca");
        String categoria = request.getParameter("categoria");
        String descricao = request.getParameter("descricao");
        String pontuacaoTexto = request.getParameter("pontuacao");
        String listaIngredientes = request.getParameter("lista_ingredientes");

        boolean ehVegano = request.getParameter("eh_vegano") != null;
        boolean ehCrueltyFree = request.getParameter("eh_cruelty_free") != null;

        if (nome == null || nome.isBlank() ||
                marca == null || marca.isBlank() ||
                categoria == null || categoria.isBlank()) {

            request.setAttribute("erro", "Preencha nome, marca e categoria.");
            request.getRequestDispatcher("/cadastro_produto.jsp").forward(request, response);
            return;
        }

        try {
            int pontuacao;

            //A pontuação não veio, ou veio vazia?
            if (pontuacaoTexto == null || pontuacaoTexto.isBlank()) {
                pontuacao = 0;
            } else {
                pontuacao = Integer.parseInt(pontuacaoTexto.trim());
            }

            Produto produto = new Produto(nome, marca, categoria, descricao, ehVegano, ehCrueltyFree, pontuacao, listaIngredientes);

            if (produtoDAO.cadastrarProduto(produto)) {
                response.sendRedirect(request.getContextPath() + "/produtos");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar o produto.");
                request.getRequestDispatcher("/cadastro_produto.jsp").forward(request, response);
            }
        } catch (NumberFormatException nfe) {
            request.setAttribute("erro", "Dados inválidos. Verifique a pontuação e tente novamente.");
            request.getRequestDispatcher("/cadastro_produto.jsp").forward(request, response);

        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                request.setAttribute("erro", "Este produto já está cadastrado ou algum dado não foi aceito.");
                request.getRequestDispatcher("/cadastro_produto.jsp").forward(request, response);
            } else {
                throw new ServletException("Erro ao cadastrar produto.", sqle);
            }
        }

    }
}
