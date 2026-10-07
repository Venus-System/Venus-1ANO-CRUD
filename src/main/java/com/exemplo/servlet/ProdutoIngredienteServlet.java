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
import java.util.ArrayList;

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

    //read
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idProdutoTexto = request.getParameter("idProduto"); //aqui filtrará por produto.

        try {
            if (idProdutoTexto != null || !idProdutoTexto.isBlank()) {
                int idProduto = Integer.parseInt(idProdutoTexto);
                ArrayList<ProdutoIngrediente> listaProduto = produtoIngredienteDAO.readByIdProduto(idProduto);

                request.setAttribute("produtosIngredientes", listaProduto);
                request.getRequestDispatcher("/lista_produto_ingrediente.jsp").forward(request, response);
                return;
            }

            ArrayList<ProdutoIngrediente> listaProduto = produtoIngredienteDAO.read();
            request.setAttribute("produtosIngredientes", listaProduto);
            request.getRequestDispatcher("/lista_produto_ingrediente").forward(request, response);
        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/produtoIngredientes");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar ingredientes dos produtos.", sqle);
        }
    }

    //delete
    @Override

    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        String idTexto = request.getParameter("id");
        String idIngredienteTexto = request.getParameter("idIngrediente");
        String idProdutoTexto = request.getParameter("idProduto");

        try {
            int linhas;

            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                linhas = produtoIngredienteDAO.deleteById(id);
            } else if (idIngredienteTexto != null && !idIngredienteTexto.isBlank()) {
                int idIngrediente = Integer.parseInt(idIngredienteTexto);
                linhas = produtoIngredienteDAO.deleteByIdIngrediente(idIngrediente);
            } else if (idProdutoTexto != null && !idProdutoTexto.isBlank()) {
                int idProduto = Integer.parseInt(idProdutoTexto);
                linhas = produtoIngredienteDAO.deleteByIdProduto(idProduto);
            } else {
                response.setStatus(400);
                response.getWriter().write("Id não informado.");
                return;
            }

            if (linhas > 0) {
                response.setStatus(200);
                //algo foi apagado, então a exclusão deu certo.
            } else {
                response.setStatus(404);
                response.getWriter().write("Registro não encontrado.");
                //o comando rodou sem erro, mas nenhum registro tinha aquele id, então não havia o que apagar.
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(400);
            response.getWriter().write("Id inválido.");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao excluir o ingrediente do produto.", sqle);
        }
    }
}
