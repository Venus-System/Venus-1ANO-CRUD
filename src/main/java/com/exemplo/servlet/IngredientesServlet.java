package com.exemplo.servlet;


import com.exemplo.dao.IngredientesDAO;
import com.exemplo.model.Ingredientes;
import com.exemplo.util.Validador;
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

// Servlet dos ingredientes: recebe as requisições da tela e chama o IngredientesDAO.
@WebServlet("/ingredientes")
public class IngredientesServlet extends HttpServlet {

    private final IngredientesDAO ingredientesDAO = new IngredientesDAO();

    // Cadastra um ingrediente com o tipo e o nível de perigo e volta para a lista.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nivelPerigoTexto = request.getParameter("nivelPerigo");
        String tipo = request.getParameter("tipo");

        if (nivelPerigoTexto == null || nivelPerigoTexto.isBlank() ||
                tipo == null || tipo.isBlank()) {

            request.setAttribute("erro", "Preencha todos os campos obrigatórios.");
            request.getRequestDispatcher("/WEB-INF/views/cadastro_ingrediente.jsp").forward(request, response);
            return;
        }

        try {
            // Converte o texto recebido em número. Se não for número, cai no NumberFormatException.
            int nivelPerigo = Integer.parseInt(nivelPerigoTexto);
            String tipoLimpo = tipo.trim();

            if (!Validador.tipoIngredienteValido(tipoLimpo)) {
                request.setAttribute("erro", "Tipo inválido. Use de 2 a 100 caracteres, começando por letra ou número.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_ingrediente.jsp").forward(request, response);
                return;
            }

            if (!Validador.nivelPerigoValido(nivelPerigo)) {
                request.setAttribute("erro", "O nível de perigo deve ser de 1 a 5.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_ingrediente.jsp").forward(request, response);
                return;
            }

            Ingredientes ingredientes = new Ingredientes(nivelPerigo, tipoLimpo);

            if (ingredientesDAO.cadastrar(ingredientes)) {
                response.sendRedirect(request.getContextPath() + "/ingredientes");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar o ingrediente.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_ingrediente.jsp").forward(request, response);
            }
        } catch (NumberFormatException nfe) {
            request.setAttribute("erro", "Nível de perigo inválido.");
            request.getRequestDispatcher("/WEB-INF/views/cadastro_ingrediente.jsp").forward(request, response);
        } catch (SQLException sqle) {
            String estado = sqle.getSQLState();
            // 23505 é o código do banco para registro repetido.
            if ("23505".equals(estado)) {
                request.setAttribute("erro", "Já existe um ingrediente com esse tipo.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_ingrediente.jsp").forward(request, response);
            } else if ("23514".equals(estado)) {
                request.setAttribute("erro", "Os valores informados não são permitidos.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_ingrediente.jsp").forward(request, response);
            } else if ("23502".equals(estado)) {
                request.setAttribute("erro", "Preencha todos os campos obrigatórios.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_ingrediente.jsp").forward(request, response);
            } else {
                throw new ServletException("Erro ao cadastrar ingrediente.", sqle);
            }
        }
    }

    //listar
    // Abre as telas: com acao=novo abre o cadastro, com id abre a alteração e sem nada mostra a lista.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idTexto = request.getParameter("id");
        String acao = request.getParameter("acao");

        if ("novo".equals(acao)) {
            request.getRequestDispatcher("/WEB-INF/views/cadastro_ingrediente.jsp").forward(request, response);
            return;
        }

        try {
            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                Ingredientes ingredientes = ingredientesDAO.listarPorId(id);

                if (ingredientes == null) {
                    response.sendRedirect(request.getContextPath() + "/ingredientes");
                    return;
                }

                request.setAttribute("ingrediente", ingredientes);
                request.getRequestDispatcher("/WEB-INF/views/editar_ingrediente.jsp").forward(request, response);
                return;
            }

            ArrayList<Ingredientes> listaIngredientes = ingredientesDAO.listar();
            request.setAttribute("ingredientes", listaIngredientes);
            request.getRequestDispatcher("/WEB-INF/views/lista_ingredientes.jsp").forward(request, response);
        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/ingredientes");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar ingredientes.", sqle);
        }
    }

    //alterar
    // Altera o ingrediente com os dados em JSON.
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        // Lê o corpo da requisição: no PUT os dados chegam em JSON, e não pelo getParameter.
        String corpo = request.getReader().lines().collect(Collectors.joining());

        try {
            // Transforma o texto JSON em um objeto, para ler cada campo pelo nome.
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
            String tipoLimpo = tipo.trim();

            if (tipoLimpo.isBlank()) {
                response.setStatus(400);
                response.getWriter().write("Preencha todos os campos obrigatórios.");
                return;
            }

            if (!Validador.tipoIngredienteValido(tipoLimpo)) {
                response.setStatus(400);
                response.getWriter().write("Tipo inválido. Use de 2 a 100 caracteres, começando por letra ou número.");
                return;
            }

            if (!Validador.nivelPerigoValido(nivelPerigo)) {
                response.setStatus(400);
                response.getWriter().write("O nível de perigo deve ser de 1 a 5.");
                return;
            }

            Ingredientes ingredientes = new Ingredientes(id, nivelPerigo, tipoLimpo);

            int linhas = ingredientesDAO.alterar(ingredientes);

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Ingrediente não encontrado.");
            }
        } catch (JsonParseException | IllegalStateException | UnsupportedOperationException | NumberFormatException e) {
            response.setStatus(400);
            response.getWriter().write("Dados inválidos.");
        } catch (SQLException sqle) {
            String estado = sqle.getSQLState();
            if ("23514".equals(estado) || "23502".equals(estado)) {
                response.setStatus(400);
                response.getWriter().write("Os valores informados não são permitidos.");
            } else {
                throw new ServletException("Erro ao atualizar o ingrediente.", sqle);
            }
        }
    }

    //deletar
    // Deleta o ingrediente pelo id.
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
            int linhas = ingredientesDAO.deletarPorId(id);

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
            if ("23503".equals(sqle.getSQLState())) {
                response.setStatus(409);
                response.getWriter().write("Não é possível excluir: o ingrediente está em uso.");
            } else {
                throw new ServletException("Erro ao excluir o ingrediente.", sqle);
            }
        }
    }
}

