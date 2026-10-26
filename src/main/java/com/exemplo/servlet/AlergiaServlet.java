package com.exemplo.servlet;


import com.exemplo.dao.AlergiaDAO;
import com.exemplo.model.Alergia;
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

@WebServlet("/alergias")
public class AlergiaServlet extends HttpServlet {

    private final AlergiaDAO alergiaDAO = new AlergiaDAO();

    // Cadastro
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");

        if (nome == null || nome.isBlank()) {
            request.setAttribute("erro", "Informe o nome da alergia.");
            request.getRequestDispatcher("/WEB-INF/views/cadastro_alergia.jsp").forward(request, response);
            return;
        }

        nome = nome.trim();

        if (!Validador.nomeAlergiaValido(nome)) {
            request.setAttribute("erro", "Nome inválido. Use de 2 a 100 caracteres, começando por letra ou número.");
            request.getRequestDispatcher("/WEB-INF/views/cadastro_alergia.jsp").forward(request, response);
            return;
        }

        try {
            Alergia alergia = new Alergia(nome.trim());

            if (alergiaDAO.inserirAlergia(alergia)) {
                response.sendRedirect(request.getContextPath() + "/alergias");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar a alergia.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_alergia.jsp").forward(request, response);
            }
        } catch (SQLException sqle) {
            getServletContext().log("Erro SQL ao cadastrar alergia", sqle);
            if ("23505".equals(sqle.getSQLState())) {
                request.setAttribute("erro", "Esta alergia já está cadastrada.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_alergia.jsp").forward(request, response);
            } else if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                request.setAttribute("erro", "O banco não aceitou o nome informado.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_alergia.jsp").forward(request, response);
            } else {
                throw new ServletException("Erro ao cadastrar alergia", sqle);
            }
        }
    }

    // Consulta
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idTexto = request.getParameter("id");
        String acao = request.getParameter("acao");

        if ("novo".equals(acao)) {
            request.getRequestDispatcher("/WEB-INF/views/cadastro_alergia.jsp").forward(request, response);
            return;
        }

        try {
            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                Alergia alergia = alergiaDAO.readById(id);

                if (alergia == null) {
                    response.sendRedirect(request.getContextPath() + "/alergias");
                    return;
                }

                request.setAttribute("alergia", alergia);
                request.getRequestDispatcher("/WEB-INF/views/editar_alergia.jsp").forward(request, response);
                return;
            }

            ArrayList<Alergia> listaAlergias = alergiaDAO.read();
            request.setAttribute("alergias", listaAlergias);
            request.getRequestDispatcher("/WEB-INF/views/lista_alergias.jsp").forward(request, response);
        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/alergias");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar alergias.", sqle);
        }
    }

    // Atualização
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        String corpo = request.getReader().lines().collect(Collectors.joining());

        try {
            JsonObject json = JsonParser.parseString(corpo).getAsJsonObject();

            if (!json.has("idAlergia") || !json.has("nome")) {
                response.setStatus(400);
                response.getWriter().write("Dados incompletos.");
                return;
            }

            String idTexto = json.get("idAlergia").getAsString();
            if (!Validador.idValido(idTexto)) {
                response.setStatus(400);
                response.getWriter().write("Id inválido.");
                return;
            }

            int id = Integer.parseInt(idTexto.trim());
            String nome = json.get("nome").getAsString();

            if (nome.isBlank()) {
                response.setStatus(400);
                response.getWriter().write("Informe o nome da alergia.");
                return;
            }

            nome = nome.trim();
            if (!Validador.nomeAlergiaValido(nome)) {
                response.setStatus(400);
                response.getWriter().write("Nome inválido. Use de 2 a 100 caracteres, começando por letra ou número.");
                return;
            }

            Alergia alergia = new Alergia(id, nome);
            int linhas = alergiaDAO.update(alergia);

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Alergia não encontrada.");
            }
        } catch (JsonParseException | IllegalStateException | UnsupportedOperationException | NumberFormatException e) {
            response.setStatus(400);
            response.getWriter().write("Dados inválidos.");
        } catch (SQLException sqle) {
            getServletContext().log("Erro SQL ao atualizar alergia", sqle);
            if ("23505".equals(sqle.getSQLState())) {
                response.setStatus(409);
                response.getWriter().write("Já existe uma alergia com esse nome.");
            } else if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                response.setStatus(400);
                response.getWriter().write("O banco não aceitou o nome informado.");
            } else {
                throw new ServletException("Erro ao atualizar a alergia.", sqle);
            }
        }
    }

    // Exclusão
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
            if (!Validador.idValido(idTexto)) {
                response.setStatus(400);
                response.getWriter().write("Id inválido.");
                return;
            }
            int id = Integer.parseInt(idTexto.trim());
            int linhas = alergiaDAO.deleteById(id);

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Alergia não encontrada.");
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(400);
            response.getWriter().write("Id inválido.");
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                response.setStatus(409);
                //Conflict. Ele avisa que a requisição está correta, mas conflita com o estado atual dos dados no banco.
                response.getWriter().write("Não é possível excluir: a alergia está em uso por algum usuário.");
            } else {
                throw new ServletException("Erro ao excluir a alergia.", sqle);
            }
        }
    }
}
