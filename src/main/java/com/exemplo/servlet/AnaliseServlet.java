package com.exemplo.servlet;

import com.exemplo.dao.AnaliseDAO;
import com.exemplo.model.Analise;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/analises")
public class AnaliseServlet extends HttpServlet {
    private final AnaliseDAO analiseDAO = new AnaliseDAO();

    //create
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String resumoResultado = request.getParameter("resumoResultado");
        String pontuacaoTexto = request.getParameter("pontuacao");
        String idUsuarioTexto = request.getParameter("idUsuario");

        if (resumoResultado == null || resumoResultado.isBlank() ||
                pontuacaoTexto == null || pontuacaoTexto.isBlank() ||
                idUsuarioTexto == null || idUsuarioTexto.isBlank()) {

            request.setAttribute("erro", "Preencha todos os campos obrigatórios.");
            request.getRequestDispatcher("/cadastro_analise.jsp").forward(request, response);
            return;
        }

        try {
            int pontuacao = Integer.parseInt(pontuacaoTexto);
            int idUsuario = Integer.parseInt(idUsuarioTexto);

            Analise analise = new Analise(resumoResultado.trim(), pontuacao, idUsuario);

            if (analiseDAO.cadastrarAnalise(analise)) {
                response.sendRedirect(request.getContextPath() + "/analises");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar a análise.");
                request.getRequestDispatcher("/cadastro_analise.jsp").forward(request, response);
            }
        } catch (NumberFormatException nfe) {
            request.setAttribute("erro", "Dados inválidos. Verifique a pontuação e o usuário e tente novamente.");
            request.getRequestDispatcher("/cadastro_analise.jsp").forward(request, response);
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                request.setAttribute("erro", "Usuário informado não existe ou os valores não são permitidos.");
                request.getRequestDispatcher("/cadastro_analise.jsp").forward(request, response);
            } else {

                throw new ServletException("Erro ao cadastrar a análise.", sqle);
            }
        }

    }
}
