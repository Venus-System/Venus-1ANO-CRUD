package com.exemplo.servlet;

import com.exemplo.dao.IngredienteAnaliseDAO;
import com.exemplo.dao.IngredientesDAO;
import com.exemplo.model.IngredienteAnalise;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/ingredienteAnalises")
public class IngredienteAnaliseServlet extends HttpServlet {

    private final IngredienteAnaliseDAO ingredienteAnaliseDAO = new IngredienteAnaliseDAO();
    private final IngredientesDAO ingredientesDAO = new IngredientesDAO();   // novo campo (+ import)

    // carrega os ingredientes para o <select> e abre o cadastro (único ponto de forward)
    private void abrirCadastro(HttpServletRequest request, HttpServletResponse response, String erro)
            throws ServletException, IOException {
        try {
            if (erro != null) {
                request.setAttribute("erro", erro);
            }
            request.setAttribute("ingredientes", ingredientesDAO.read());
            request.getRequestDispatcher("/WEB-INF/views/cadastro_ingrediente_analise.jsp")
                    .forward(request, response);
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao carregar ingredientes.", sqle);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idIngredienteTexto = request.getParameter("idIngrediente");
        String idAnaliseTexto = request.getParameter("idAnalise");

        try {
            if (idIngredienteTexto == null || idIngredienteTexto.isBlank() ||
                    idAnaliseTexto == null || idAnaliseTexto.isBlank()) {
                abrirCadastro(request, response, "Selecione o ingrediente.");
                return;
            }

            int idIngrediente = Integer.parseInt(idIngredienteTexto);
            int idAnalise = Integer.parseInt(idAnaliseTexto);

            if (idIngrediente <= 0 || idAnalise <= 0) {
                abrirCadastro(request, response, "Dados inválidos. Selecione o ingrediente e tente novamente.");
                return;
            }

            IngredienteAnalise ia = new IngredienteAnalise(idIngrediente, idAnalise);

            if (ingredienteAnaliseDAO.cadastrarIngredienteAnalise(ia)) {
                response.sendRedirect(request.getContextPath() + "/ingredienteAnalises?idAnalise=" + idAnalise);
            } else {
                abrirCadastro(request, response, "Não foi possível vincular o ingrediente à análise.");
            }
        } catch (NumberFormatException nfe) {
            abrirCadastro(request, response, "Dados inválidos. Selecione o ingrediente e tente novamente.");
        } catch (SQLException sqle) {
            String estado = sqle.getSQLState();
            if ("23505".equals(estado)) {
                abrirCadastro(request, response, "Esse ingrediente já está vinculado a esta análise.");
            } else if ("23503".equals(estado)) {
                abrirCadastro(request, response, "Ingrediente ou análise não existe.");
            } else if ("23502".equals(estado)) {
                abrirCadastro(request, response, "Preencha todos os campos obrigatórios.");
            } else {
                throw new ServletException("Erro ao cadastrar ingrediente da análise.", sqle);
            }
        }
    }

    //read
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idAnaliseTexto = request.getParameter("idAnalise");
        String idIngredienteTexto = request.getParameter("idIngrediente");

        try {
            if ("novo".equals(request.getParameter("acao"))) {
                abrirCadastro(request, response, null);
                return;
            }

            if (idAnaliseTexto != null && !idAnaliseTexto.isBlank()) {
                int idAnalise = Integer.parseInt(idAnaliseTexto);
                ArrayList<IngredienteAnalise> listaAnalises = ingredienteAnaliseDAO.readByIdAnalise(idAnalise);

                request.setAttribute("ingredientesAnalises", listaAnalises);
                request.getRequestDispatcher("/WEB-INF/views/lista_ingrediente_analise.jsp").forward(request, response);                return;
            }
            if (idIngredienteTexto != null && !idIngredienteTexto.isBlank()) {
                int idIngrediente = Integer.parseInt(idIngredienteTexto);
                ArrayList<IngredienteAnalise> listaIngredientes = ingredienteAnaliseDAO.readByIdIngrediente(idIngrediente);

                request.setAttribute("ingredientesAnalises", listaIngredientes);
                request.getRequestDispatcher("/WEB-INF/views/lista_ingrediente_analise.jsp").forward(request, response);                return;
            }

            if ("novo".equals(request.getParameter("acao"))) {
                abrirCadastro(request, response, null);
                return;
            }

            ArrayList<IngredienteAnalise> listaIngredienteAnalises = ingredienteAnaliseDAO.read();
            request.setAttribute("ingredientesAnalises", listaIngredienteAnalises);
            request.getRequestDispatcher("/WEB-INF/views/lista_ingrediente_analise.jsp").forward(request, response);        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/ingredienteAnalises");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar ingredientes da análise.", sqle);
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
        String idAnaliseTexto = request.getParameter("idAnalise");

        try {
            int linhas;

            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                linhas = ingredienteAnaliseDAO.deleteById(id);
            } else if (idIngredienteTexto != null && !idIngredienteTexto.isBlank()) {
                int idIngrediente = Integer.parseInt(idIngredienteTexto);
                linhas = ingredienteAnaliseDAO.deleteByIdIngrediente(idIngrediente);
            } else if (idAnaliseTexto != null && !idAnaliseTexto.isBlank()) {
                int idAnalise = Integer.parseInt(idAnaliseTexto);
                linhas = ingredienteAnaliseDAO.deleteByIdAnalise(idAnalise);
            } else {
                response.setStatus(400);
                response.getWriter().write("Id não informado.");
                return;
            }

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Registro não encontrado.");
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(400);
            response.getWriter().write("Id inválido.");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao excluir o ingrediente da análise.", sqle);
        }

    }
}

