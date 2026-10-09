package com.exemplo.servlet;


import com.exemplo.dao.IngredientesDAO;
import com.exemplo.model.Ingredientes;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
                request.setAttribute("erro", "Não foi possível cadastrar o ingrediente.");
                request.getRequestDispatcher("/cadastro_ingrediente.jsp").forward(request, response);
            }
        } catch (NumberFormatException nfe) {
            request.setAttribute("erro", "Nível de perigo inválido.");
            request.getRequestDispatcher("/cadastro_ingrediente.jsp").forward(request, response);
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

    //update
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        String corpo = request.getReader().lines().collect(Collectors.joining());

        try {
            JsonObject json = JsonParser.parseString(corpo).getAsJsonObject();

            if (!json.has("idIngrediente") || !json.has("nivelPerigo")
                    || !json.has("tipo")) {
                response.setStatus(400);
                response.getWriter().write("Dados incompletos.");
                return;
            }

            int id = Integer.parseInt(json.get("idIngrediente").getAsString());
            int nivelPerigo = Integer.parseInt(json.get("nivelPerigo").getAsString());
            String tipo = json.get("tipo").getAsString();

            if (tipo.isBlank()) {
                response.setStatus(400);
                response.getWriter().write("Preencha todos os campos obrigatórios.");
                return;
            }

            Ingredientes ingredientes = new Ingredientes(id, nivelPerigo, tipo.trim());

            int linhas = ingredientesDAO.alterarValores(ingredientes);

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Ingrediente não encontrado.");
            }
        } catch (JsonParseException | IllegalStateException | UnsupportedOperationException | NumberFormatException e) {
            //UnsupportedOperationException -> Quando o Json traz null em algum campo.
            response.setStatus(400);
            response.getWriter().write("Dados inválidos.");
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                response.setStatus(409);
                response.getWriter().write("Os valores informados conflitam com outro ingrediente ou não são permitidos.");
            } else {
                throw new ServletException("Erro ao atualizar o ingrediente.", sqle);

            }
        }
    }

    //delete
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        String idTexto = request.getParameter("id");

        if (idTexto == null || idTexto.isBlank()) {
            response.setStatus(400);
            response.getWriter().write("Id não informado.");
            return;
        }

        try {
            int id = Integer.parseInt(idTexto);
            int linhas = ingredientesDAO.deleteById(id);

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Ingrediente não encontrado.");
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(400);
            response.getWriter().write("Id inválido.");
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                response.setStatus(409);
                //Conflict. Outras tabelas (como os nomes do ingrediente) ainda referenciam este registro.
                response.getWriter().write("Não é possível excluir: o ingrediente está em uso.");
            } else {
                throw new ServletException("Erro ao excluir o ingrediente.", sqle);
            }
        }
    }
}

