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
import java.util.ArrayList;

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

    //create
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{

        String idTexto = request.getParameter("id");

        try {
            if (idTexto != null && !idTexto.isBlank()){
                int id = Integer.parseInt(idTexto);
                Alergia alergia = alergiaDAO.readById(id);

                if (alergia == null){
                    response.sendRedirect(request.getContextPath()+"/alergias");]
                    return;
                }

                request.setAttribute("alergia", alergia);
                request.getRequestDispatcher("/editar_alergia.jsp").forward(request, response);
                return;
            }

            ArrayList<Alergia> listaAlergias = alergiaDAO.read();
            request.setAttribute("alergias", listaAlergias);
            request.getRequestDispatcher("/lista_alergias.jsp").forward(request, response);
        }catch (NumberFormatException nfe){
            response.sendRedirect(request.getContextPath()+"/alergias");
        }catch (SQLException sqle){
            throw new ServletException("Erro ao buscar alergias.", sqle);
        }
    }
}
