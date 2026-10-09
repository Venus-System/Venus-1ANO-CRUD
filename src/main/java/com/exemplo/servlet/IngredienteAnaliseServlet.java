package com.exemplo.servlet;

import com.exemplo.dao.IngredienteAnaliseDAO;
import com.exemplo.model.IngredienteAnalise;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.print.DocFlavor;
import java.io.IOException;
import java.sql.SQLException;

import static java.lang.Integer.*;

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
            int idIngrediente = parseInt(idIngredienteTexto);
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
}
