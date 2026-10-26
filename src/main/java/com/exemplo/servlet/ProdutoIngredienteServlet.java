package com.exemplo.servlet;


import com.exemplo.dao.IngredientesDAO;
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
    private final IngredientesDAO ingredientesDAO = new IngredientesDAO();

    // Cadastro
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String erro = null;

        String idIngredienteTexto = request.getParameter("idIngrediente");
        String idProdutoTexto = request.getParameter("idProduto");

        if (idIngredienteTexto == null || idIngredienteTexto.isBlank() ||
                idProdutoTexto == null || idProdutoTexto.isBlank()) {

            erro = "Selecione o ingrediente.";

        } else {

            try {
                int idIngrediente = Integer.parseInt(idIngredienteTexto);
                int idProduto = Integer.parseInt(idProdutoTexto);

                ProdutoIngrediente produtoIngrediente = new ProdutoIngrediente(idIngrediente, idProduto);

                if (produtoIngredienteDAO.cadastrarProdutoIngrediente(produtoIngrediente)) {
                    response.sendRedirect(request.getContextPath() + "/produtoIngredientes?idProduto=" + idProduto);
                    return;
                }
                erro = "Não foi possível cadastrar o ingrediente do produto.";

            } catch (NumberFormatException nfe) {
                erro = "Dados inválidos. Selecione o produto e o ingrediente e tente novamente.";
            } catch (SQLException sqle) {
                String estado = sqle.getSQLState();
                if ("23505".equals(estado)) {
                    erro = "Este produto já possui esse ingrediente.";
                } else if ("23503".equals(estado)) {
                    erro = "Produto ou ingrediente não existe.";
                } else {
                    throw new ServletException("Erro ao cadastrar ingrediente do produto.", sqle);
                }
            }
        }

        try {
            request.setAttribute("ingredientes", ingredientesDAO.read());
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar ingredientes", sqle);
        }
        request.setAttribute("erro", erro);
        request.getRequestDispatcher("/WEB-INF/views/cadastro_produto_ingrediente.jsp").forward(request, response);
    }


    // Consulta
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idProdutoTexto = request.getParameter("idProduto"); //aqui filtrará por produto.
        String acao = request.getParameter("acao");
        try {
            if ("novo".equals(acao)) {
                request.setAttribute("ingredientes", ingredientesDAO.read());
                request.getRequestDispatcher("/WEB-INF/views/cadastro_produto_ingrediente.jsp").forward(request, response);
                return;
            }

            if (idProdutoTexto != null && !idProdutoTexto.isBlank()) {
                int idProduto = Integer.parseInt(idProdutoTexto);
                ArrayList<ProdutoIngrediente> listaProduto = produtoIngredienteDAO.readByIdProduto(idProduto);

                request.setAttribute("produtosIngredientes", listaProduto);
                request.getRequestDispatcher("/WEB-INF/views/lista_produto_ingrediente.jsp").forward(request, response);
                return;
            }

            ArrayList<ProdutoIngrediente> listaProduto = produtoIngredienteDAO.read();
            request.setAttribute("produtosIngredientes", listaProduto);
            request.getRequestDispatcher("/WEB-INF/views/lista_produto_ingrediente.jsp").forward(request, response);
        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/produtoIngredientes");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar ingredientes dos produtos.", sqle);
        }
    }

    // Exclusão
    // Prioriza o ID do vínculo; sem ele, exclui todos os vínculos do ingrediente ou do produto.
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
