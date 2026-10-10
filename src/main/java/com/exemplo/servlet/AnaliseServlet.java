package com.exemplo.servlet;

import com.exemplo.dao.AnaliseDAO;
import com.exemplo.model.Analise;
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

    //read
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idTexto = request.getParameter("id");
        String idUsuarioTexto = request.getParameter("idUsuario");

        try {
            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                Analise analise = analiseDAO.readById(id);

                if (analise == null) {
                    response.sendRedirect(request.getContextPath() + "/analises");
                    return;
                }

                request.setAttribute("analise", analise);
                request.getRequestDispatcher("/editar_analise.jsp").forward(request, response);
                return;
            }

            if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                int idUsuario = Integer.parseInt(idUsuarioTexto);
                ArrayList<Analise> listaUsuarios = analiseDAO.readByIdUsuario(idUsuario);

                request.setAttribute("analises", listaUsuarios);
                request.getRequestDispatcher("/lista_analises.jsp").forward(request, response);
                return;
            }

            ArrayList<Analise> listaAnalises = analiseDAO.read();
            request.setAttribute("analises", listaAnalises);
            request.getRequestDispatcher("/lista_analises.jsp").forward(request, response);
        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/analises");

        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar análises.", sqle);
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

            if (!json.has("idAnalise") || !json.has("resumoResultado") ||
                    !json.has("pontuacao")) {
                response.setStatus(400);
                response.getWriter().write("Dados incompletos.");
                return;
            }

            int id = Integer.parseInt(json.get("idAnalise").getAsString());
            String resumoResultado = json.get("resumoResultado").getAsString();
            int pontuacao = Integer.parseInt(json.get("pontuacao").getAsString());

            if (resumoResultado.isBlank()) {
                response.setStatus(400);
                response.getWriter().write("Preencha todos os campos obrigatórios.");
                return;
            }

            Analise analise = new Analise(id, resumoResultado.trim(), pontuacao);

            int linhas = analiseDAO.update(analise);

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Análise não encontrada.");
            }
        } catch (JsonParseException | IllegalStateException | UnsupportedOperationException | NumberFormatException e) {
            response.setStatus(400);
            response.getWriter().write("Dados inválidos.");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao atualizar a análise.", sqle);
        }
    }

    //delete
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        String idTexto = request.getParameter("id");
        String idUsuarioTexto = request.getParameter("idUsuario");

        try {
            int linhas;

            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                linhas = analiseDAO.deleteById(id);
            } else if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                int idUsuario = Integer.parseInt(idUsuarioTexto);
                linhas = analiseDAO.deleteByIdUsuario(idUsuario);
            } else {
                response.setStatus(400);
                response.getWriter().write("Id não informado.");
                return;
            }

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Análise não encontrada.");
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(400);
            response.getWriter().write("Id inválido.");
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                response.setStatus(409);
                response.getWriter().write("Não é possível excluir: a análise está em uso.");
            } else {
                throw new ServletException("Erro ao excluir a análise.", sqle);
            }
        }
    }
}
