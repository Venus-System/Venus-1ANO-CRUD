package com.exemplo.servlet;


import com.exemplo.dao.ProdutoIngredienteDAO;
import com.exemplo.model.ProdutoIngrediente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/produtoIngredientes")
public class ProdutoIngredienteServlet extends HttpServlet {

    private final ProdutoIngredienteDAO produtoIngredienteDAO = new ProdutoIngredienteDAO();

    //create
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idIngredienteTexto = request.getParameter("id_igrediente");
        String idProdutoTexto = request.getParameter("id_produto");

        if (idIngredienteTexto == null || idIngredienteTexto.isBlank() ||
                idProdutoTexto == null || idProdutoTexto.isBlank()) {

            request.setAttribute("erro", "Preencha todos os campos obrigatórios.");
            request.getRequestDispatcher("/cadastro_produto_ingrediente.jsp").forward(request, response);
            return;
        }

        try {
            int idIngrediente = Integer.parseInt(idIngredienteTexto);
            int idProduto = Integer.parseInt(idProdutoTexto);

            ProdutoIngrediente produtoIngrediente = new ProdutoIngrediente(idIngrediente, idProduto);

            if (produtoIngredienteDAO.cadastrarProdutoIngrediente(produtoIngrediente)) {
                response.sendRedirect(request.getContextPath() + "/produtoIngredientes");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar o ingrediente do produto.");
                request.getRequestDispatcher("/cadastro_produto_ingrediente.jsp").forward(request, response);
            }
        } catch (NumberFormatException nfe) {
            request.setAttribute("erro ", "Dados inválidos. Selecione o produto e o ingrediente e tente novamente.");
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                request.setAttribute("erro", "Produto ou ingrediente não existe, ou o registro já está cadastrado.");
                request.getRequestDispatcher("/cadastro_produto_ingrediente.jsp").forward(request, response);
            } else {
                throw new ServletException("Erro ao cadastrar ingrediente do produto.", sqle);
            }
        }

    }
}
