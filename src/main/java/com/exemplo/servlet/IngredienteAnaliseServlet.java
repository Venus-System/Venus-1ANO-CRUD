package com.exemplo.servlet;

import com.exemplo.dao.IngredienteAnaliseDAO;
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

    //create
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idIngredienteTexto = request.getParameter("idIngrediente");
        String idAnaliseTexto = request.getParameter("idAnalise");

        if (idIngredienteTexto == null || idIngredienteTexto.isBlank() ||
                idAnaliseTexto == null || idAnaliseTexto.isBlank()) {

            request.setAttribute("erro", "Preencha todos os campos obrigatórios.");
            request.getRequestDispatcher("/cadastro_ingrediente_analise.jsp").forward(request, response);
            return;
        }

        try {
            int idIngrediente = Integer.parseInt(idIngredienteTexto);
            int idAnalise = Integer.parseInt(idAnaliseTexto);

            IngredienteAnalise ingredienteAnalise = new IngredienteAnalise(idIngrediente, idAnalise);

            if (ingredienteAnaliseDAO.cadastrarIngredienteAnalise(ingredienteAnalise)) {
                response.sendRedirect(request.getContextPath() + "/ingredienteAnalises");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar o ingrediente da análise.");
                request.getRequestDispatcher("/cadastro_ingrediente_analise.jsp").forward(request, response);
            }
        } catch (NumberFormatException nfe) {
            request.setAttribute("erro", "Dados inválidos. Selecione o ingrediente e a análise e tente novamente.");
            request.getRequestDispatcher("/cadastro_ingrediente_analise.jsp").forward(request, response);
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                request.setAttribute("erro", "Ingrediente ou análise não existe, ou o registro já está cadastrado.");
                request.getRequestDispatcher("/cadastro_ingrediente_analise.jsp").forward(request, response);
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
            if (idAnaliseTexto != null && !idAnaliseTexto.isBlank()) {
                int idAnalise = Integer.parseInt(idAnaliseTexto);
                ArrayList<IngredienteAnalise> listaAnalises = ingredienteAnaliseDAO.readByIdAnalise(idAnalise);

                request.setAttribute("ingredientesAnalises", listaAnalises);
                request.getRequestDispatcher("/lista_ingrediente_analise.jsp").forward(request, response);
                return;
            }
            if (idIngredienteTexto != null && !idIngredienteTexto.isBlank()) {
                int idIngrediente = Integer.parseInt(idIngredienteTexto);
                ArrayList<IngredienteAnalise> listaIngredientes = ingredienteAnaliseDAO.readByIdIngrediente(idIngrediente);

                request.setAttribute("ingredientesAnalises", listaIngredientes);
                request.getRequestDispatcher("/lista_ingrediente_analise.jsp").forward(request, response);
                return;
            }

            ArrayList<IngredienteAnalise> listaIngredienteAnalises = ingredienteAnaliseDAO.read();
            request.setAttribute("ingredientesAnalises", listaIngredienteAnalises);
            request.getRequestDispatcher("/lista_ingrediente_analise.jsp").forward(request, response);
        } catch (NumberFormatException nfe) {
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

