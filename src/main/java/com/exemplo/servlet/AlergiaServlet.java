package com.exemplo.servlet;


import com.exemplo.dao.AlergiaDAO;
import com.exemplo.model.Alergia;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/alergias")
public class AlergiaServlet extends HttpServlet {

    private final AlergiaDAO alergiaDAO = new AlergiaDAO();

    //create
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");

        if (nome == null || nome.isBlank()) {
            request.setAttribute("erro", "Informe o nome da alergia.");
            request.getRequestDispatcher("/cadastro_alergia.jsp").forward(request, response);
            return;
        }

        try {
            Alergia alergia = new Alergia(nome.trim());

            if (alergiaDAO.inserirAlergia(alergia)) {
                response.sendRedirect(request.getContextPath() + "/alergias");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar a alergia.");
                request.getRequestDispatcher("/cadastro_alergia.jsp").forward(request, response);
            }
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                request.setAttribute("erro", "Esta alergia já está cadastrada.");
                request.getRequestDispatcher("/cadastro_alergia.jsp").forward(request, response);
                //O insert falha e o Java lança um SQLException.
                //23 significa violação de restrição.
            } else {
                throw new ServletException("Erro ao cadastrar alergia", sqle);
            }
        }
    }
}
