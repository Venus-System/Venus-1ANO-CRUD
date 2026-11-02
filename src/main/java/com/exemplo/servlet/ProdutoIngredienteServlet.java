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

// Servlet que liga os produtos aos ingredientes.
@WebServlet("/produtoIngredientes")
public class ProdutoIngredienteServlet extends HttpServlet {

    private final ProdutoIngredienteDAO produtoIngredienteDAO = new ProdutoIngredienteDAO();
    private final IngredientesDAO ingredientesDAO = new IngredientesDAO();

    //cadastrar
    // Cadastra um ingrediente no produto e volta para a lista desse produto.
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
                // Converte o texto recebido em número. Se não for número, cai no NumberFormatException.
                int idIngrediente = Integer.parseInt(idIngredienteTexto);
                int idProduto = Integer.parseInt(idProdutoTexto);

                ProdutoIngrediente produtoIngrediente = new ProdutoIngrediente(idIngrediente, idProduto);

                if (produtoIngredienteDAO.cadastrar(produtoIngrediente)) {
                    response.sendRedirect(request.getContextPath() + "/produtoIngredientes?idProduto=" + idProduto);
                    return;
                }
                erro = "Não foi possível cadastrar o ingrediente do produto.";

            } catch (NumberFormatException nfe) {
                erro = "Dados inválidos. Selecione o produto e o ingrediente e tente novamente.";
            } catch (SQLException sqle) {
                String estado = sqle.getSQLState();
                // 23505 é o código do banco para registro repetido.
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
            request.setAttribute("ingredientes", ingredientesDAO.listar());
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar ingredientes", sqle);
        }
        request.setAttribute("erro", erro);
        request.getRequestDispatcher("/WEB-INF/views/cadastro_produto_ingrediente.jsp").forward(request, response);
    }


        //listar
        // Abre o cadastro ou mostra a lista (de todos ou só os ingredientes de um produto).
        @Override
        protected void doGet (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

            String idProdutoTexto = request.getParameter("idProduto"); //aqui filtrará por produto.
            String acao = request.getParameter("acao");
            try {
                if ("novo".equals(acao)) {
                    request.setAttribute("ingredientes", ingredientesDAO.listar());
                    request.getRequestDispatcher("/WEB-INF/views/cadastro_produto_ingrediente.jsp").forward(request, response);
                    return;
                }

                if (idProdutoTexto != null && !idProdutoTexto.isBlank()) {
                    int idProduto = Integer.parseInt(idProdutoTexto);
                    ArrayList<ProdutoIngrediente> listaProduto = produtoIngredienteDAO.listarPorIdProduto(idProduto);

                    request.setAttribute("produtosIngredientes", listaProduto);
                    request.getRequestDispatcher("/WEB-INF/views/lista_produto_ingrediente.jsp").forward(request, response);
                    return;
                }

                ArrayList<ProdutoIngrediente> listaProduto = produtoIngredienteDAO.listar();
                request.setAttribute("produtosIngredientes", listaProduto);
                request.getRequestDispatcher("/WEB-INF/views/lista_produto_ingrediente.jsp").forward(request, response);
            } catch (NumberFormatException nfe) {
                response.sendRedirect(request.getContextPath() + "/produtoIngredientes");
            } catch (SQLException sqle) {
                throw new ServletException("Erro ao buscar ingredientes dos produtos.", sqle);
            }
        }

        //deletar
        @Override

        // Deleta um vínculo pelo id, ou todos de um ingrediente, ou todos de um produto.
        protected void doDelete (HttpServletRequest request, HttpServletResponse response)
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
                    linhas = produtoIngredienteDAO.deletarPorId(id);
                } else if (idIngredienteTexto != null && !idIngredienteTexto.isBlank()) {
                    int idIngrediente = Integer.parseInt(idIngredienteTexto);
                    linhas = produtoIngredienteDAO.deletarPorIdIngrediente(idIngrediente);
                } else if (idProdutoTexto != null && !idProdutoTexto.isBlank()) {
                    int idProduto = Integer.parseInt(idProdutoTexto);
                    linhas = produtoIngredienteDAO.deletarPorIdProduto(idProduto);
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
