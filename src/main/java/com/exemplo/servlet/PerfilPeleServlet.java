package com.exemplo.servlet;


import com.exemplo.dao.PerfilPeleDAO;
import com.exemplo.model.PerfilPele;
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

@WebServlet("/perfisPele")
public class PerfilPeleServlet extends HttpServlet {

    private final PerfilPeleDAO perfilPeleDAO = new PerfilPeleDAO();

    //create
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String sensibilidadeTexto = request.getParameter("sensibilidade");
        String tipoPele = request.getParameter("tipo_pele");
        String nivelOleosidadeTexto = request.getParameter("nivel_oleosidade");
        String idUsuarioTexto = request.getParameter("id_usuario");

        if (sensibilidadeTexto == null || sensibilidadeTexto.isBlank() ||
                tipoPele == null || tipoPele.isBlank() ||
                nivelOleosidadeTexto == null || nivelOleosidadeTexto.isBlank() ||
                idUsuarioTexto == null || idUsuarioTexto.isBlank()) {

            request.setAttribute("erro", "Preencha todos os campos obrigatórios.");
            request.getRequestDispatcher("/cadastro_perfil_pele.jsp").forward(request, response);
            return;
        }
        try {
            int sensibilidade = Integer.parseInt(sensibilidadeTexto.trim());
            int nivelOleosidade = Integer.parseInt(nivelOleosidadeTexto.trim());
            int idUsuario = Integer.parseInt(idUsuarioTexto.trim());

            if (perfilPeleDAO.existePerfilParaUsuario(idUsuario)) {
                request.setAttribute("erro", "Este usuário já possui um perfil de pele cadastrado.");
                request.getRequestDispatcher("/cadastro_perfil_pele.jsp").forward(request, response);
                return;
            }

            PerfilPele perfilPele = new PerfilPele(sensibilidade, tipoPele.trim(), nivelOleosidade, idUsuario);
            if (perfilPeleDAO.cadastrarPerfilPele(perfilPele)) {
                response.sendRedirect(request.getContextPath() + "/perfisPele");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar o perfil de pele.");
                request.getRequestDispatcher("/cadastro_perfil_pele.jsp").forward(request, response);
            }
        } catch (NumberFormatException nfe) {
            request.setAttribute("erro", "Dados inválidos. Verifique sensibilidade, oleosidade e usuário e tente novamente.");
            request.getRequestDispatcher("/cadastro_perfil_pele.jsp").forward(request, response);
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                //violação de restrição --------> 23. Igual aos outros.
                request.setAttribute("erro", "Usuário não existe ou algum dado não foi aceito.");
                request.getRequestDispatcher("/cadastro_perfil_pele.jsp").forward(request, response);
            } else {
                throw new ServletException("Erro ao cadastrar perfil de pele.", sqle);
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
                PerfilPele perfilPele = perfilPeleDAO.readById(id);

                if (perfilPele == null) {
                    response.sendRedirect(request.getContextPath() + "/perfisPele");
                    return;
                }

                request.setAttribute("perfilPele", perfilPele);
                request.getRequestDispatcher("/editar_perfil_pele.jsp").forward(request, response);
                return;
            }

            if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                int idUsuario = Integer.parseInt(idUsuarioTexto);
                PerfilPele perfilPeleUsuario = perfilPeleDAO.readByIdUsuario(idUsuario);

                ArrayList<PerfilPele> listaUsuarios = new ArrayList<>();
                if (perfilPeleUsuario != null) {
                    listaUsuarios.add(perfilPeleUsuario);
                }

                request.setAttribute("perfisPele", listaUsuarios);
                request.getRequestDispatcher("/lista_perfis_pele.jsp").forward(request, response);
                return;
            }

            //aqui listará tudo.
            ArrayList<PerfilPele> listaPerfilPele = perfilPeleDAO.read();
            request.setAttribute("perfisPele", listaPerfilPele);
            request.getRequestDispatcher("/lista_perfis_pele.jsp").forward(request, response);
        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/perfisPele");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar perfis de pele.", sqle);
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

            if (!json.has("idPerfilPele") || !json.has("sensibilidade") ||
                    !json.has("tipoPele") || !json.has("nivelOleosidade")) {

                response.setStatus(400);
                response.getWriter().write("Dados incompletos.");
                return;
            }

            int id = Integer.parseInt(json.get("idPerfilPele").getAsString());
            int sensibilidade = Integer.parseInt(json.get("sensibilidade").getAsString());
            String tipoPele = json.get("tipoPele").getAsString();
            int nivelOleosidade = Integer.parseInt(json.get("nivelOleosidade").getAsString());

            if (tipoPele.isBlank()) {
                response.setStatus(400);
                response.getWriter().write("Informe o tipo de pele.");
                return;
            }

            PerfilPele perfilPele = new PerfilPele(id, sensibilidade, tipoPele.trim(), nivelOleosidade);

            int linhas = perfilPeleDAO.alterarValores(perfilPele);

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Perfil de pele não encontrado.");
            }
        } catch (JsonParseException | IllegalStateException
                 | UnsupportedOperationException | NumberFormatException e) {
            response.setStatus(400);
            response.getWriter().write("Dados inválidos.");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao atualizar o perfil de pele.", sqle);
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
                linhas = perfilPeleDAO.deleteById(id);
            } else if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                int idUsuario = Integer.parseInt(idUsuarioTexto);
                linhas = perfilPeleDAO.deleteByIdUsuario(idUsuario);
            } else {
                response.setStatus(400);
                response.getWriter().write("Id não informado.");
                return;
            }

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Perfil de pele não encontrado.");
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(400);
            response.getWriter().write("Id inválido.");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao excluir o perfil de pele.", sqle);
        }
    }
}
