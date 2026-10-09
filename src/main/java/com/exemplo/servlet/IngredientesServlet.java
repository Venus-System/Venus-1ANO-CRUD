package com.exemplo.servlet;


import com.exemplo.dao.IngredientesDAO;
import com.exemplo.model.Ingredientes;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

@WebServlet("/ingredientes")
public class IngredientesServlet extends HttpServlet {

    private final IngredientesDAO ingredientesDAO = new IngredientesDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nivelPerigoTexto = request.getParameter("nivelPerigo");
        String tipo = request.getParameter("tipo");

        if (nivelPerigoTexto == null || nivelPerigoTexto.isBlank() ||
                tipo == null || tipo.isBlank()) {

            request.setAttribute("erro", "Preencha todos os campos obrigatórios.");
            request.getRequestDispatcher("/cadastro_ingrediente.jsp").forward(request, response);
            return;
        }

        try {
            int nivelPerigo = Integer.parseInt(nivelPerigoTexto);

            Ingredientes ingredientes = new Ingredientes(nivelPerigo, tipo.trim());

            if (ingredientesDAO.cadastrarIngredientes(ingredientes)) {
                response.sendRedirect(request.getContextPath() + "/ingredientes");
            } else {
                request.setAttribute("erro", "Não possível cadastrar o ingrediente.");
                request.getRequestDispatcher("/cadastro_ingrediente.jsp").forward(request, response);
            }
        } catch (NumberFormatException nfe) {
            request.setAttribute("erro", "Nível de perigo inválido.");
            request.getRequestDispatcher("/cadastro_ingredientes.jsp").forward(request, response);
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                request.setAttribute("erro", "O ingrediente já está cadastrado ou os valores informados não são permitidos.");
                request.getRequestDispatcher("/cadastro_ingrediente.jsp").forward(request, response);
            } else {
                throw new ServletException("Erro ao cadastrar ingrediente.", sqle);

            }
        }
    }

    //read
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idTexto = request.getParameter("id");

        try {
            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                Ingredientes ingredientes = ingredientesDAO.readById(id);

                if (ingredientes == null) {
                    response.sendRedirect(request.getContextPath() + "/ingredientes");
                    return;
                }

                request.setAttribute("ingrediente", ingredientes);
                request.getRequestDispatcher("/editar_ingrediente.jsp").forward(request, response);
                return;
            }

            ArrayList<Ingredientes> listaIngredientes = ingredientesDAO.read();
            request.setAttribute("ingredientes", listaIngredientes);
            request.getRequestDispatcher("/lista_ingredientes.jsp").forward(request, response);
        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/ingredientes");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar ingredientes.", sqle);
        }
    }
}
