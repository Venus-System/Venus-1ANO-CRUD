package com.exemplo.servlet;


import com.exemplo.dao.UsuarioAlergiaDAO;
import com.exemplo.model.UsuarioAlergia;
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

@WebServlet("/usuarioAlergias")
public class UsuarioAlergiaServlet extends HttpServlet {

    private final UsuarioAlergiaDAO usuarioAlergiaDAO = new UsuarioAlergiaDAO();

    //create

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String grauTexto = request.getParameter("grau");
        String idUsuarioTexto = request.getParameter("id_usuario");
        String idAlergiaTexto = request.getParameter("id_alergia");

        if (grauTexto == null || grauTexto.isBlank() ||
                idUsuarioTexto == null || idUsuarioTexto.isBlank() ||
                idAlergiaTexto == null || idAlergiaTexto.isBlank()) {

            request.setAttribute("erro", "Preencha todos os campos obrigatórios.");
            request.getRequestDispatcher("/cadastro_usuario_alergia.jsp").forward(request, response);
            return;
        }

        try {
            int grau = Integer.parseInt(grauTexto);
            int idUsuario = Integer.parseInt(idUsuarioTexto);
            int idAlergia = Integer.parseInt(idAlergiaTexto);

            UsuarioAlergia usuarioAlergia = new UsuarioAlergia(grau, idUsuario, idAlergia);

            if (usuarioAlergiaDAO.inserirUsuarioAlergia(usuarioAlergia)) {
                response.sendRedirect(request.getContextPath() + "/usuarioAlergias");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar a alergia do usuário.");
                request.getRequestDispatcher("/cadastro_usuario_alergia.jsp").forward(request, response);
            }
        } catch (NumberFormatException nfe) {
            request.setAttribute("erro", "Dados inválidos. Verifique o grau informado e tente novamente.");
            request.getRequestDispatcher("/cadastro_usuario_alergia.jsp").forward(request, response);
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                //o 'if' confere se o código existe, e pergunta se o código começa com "23" (que é a classe de erro "violação de restrição de integridade")
                request.setAttribute("erro", "Usuário ou alergia não existe, ou o registro já está cadastrado.");
                request.getRequestDispatcher("/cadastro_usuario_alergia.jsp").forward(request, response);
            } else {
                throw new ServletException("Erro ao cadastrar alergia do usuário", sqle);
            }
        }
    }

    //read
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idTexto = request.getParameter("id");
        String idUsuarioTexto = request.getParameter("idUsuario"); //aqui filtra por usuário

        try {
            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                UsuarioAlergia usuarioAlergia = usuarioAlergiaDAO.readById(id);

                if (usuarioAlergia == null) {
                    response.sendRedirect(request.getContextPath() + "/usuarioAlergias");
                    return;
                }

                request.setAttribute("usuarioAlergia", usuarioAlergia);
                request.getRequestDispatcher("/editar_usuario_alergia.jsp").forward(request, response);
                return;
            }

            if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                int idUsuario = Integer.parseInt(idUsuarioTexto);
                ArrayList<UsuarioAlergia> listaUsuarioAlergia = usuarioAlergiaDAO.readByIdUsuario(idUsuario);

                request.setAttribute("usuariosAlergias", listaUsuarioAlergia);
                request.getRequestDispatcher("/lista_usuario_alergias.jsp").forward(request, response);
                return;
            }

            ArrayList<UsuarioAlergia> listaUsuarioAlergia = usuarioAlergiaDAO.read();
            request.setAttribute("usuariosAlergias", listaUsuarioAlergia);
            request.getRequestDispatcher("/lista_usuario_alergias.jsp").forward(request, response);

        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/usuarioAlergias");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar alergias dos usuários", sqle);
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

            if (!json.has("idUsuarioAlergia") || !json.has("grau")) {
                response.setStatus(400);
                response.getWriter().write("Dados incompletos.");
                return;
            }

            int id = Integer.parseInt(json.get("idUsuarioAlergia").getAsString());
            int grau = Integer.parseInt(json.get("grau").getAsString());

            UsuarioAlergia usuarioAlergia = new UsuarioAlergia(id, grau);

            int linhas = usuarioAlergiaDAO.update(usuarioAlergia);

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Registro não encontrado.");
            }
        } catch (JsonParseException | IllegalStateException | UnsupportedOperationException | NumberFormatException e) {
            response.setStatus(400);
            response.getWriter().write("Dados inválidos.");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao atualizar a alergia do usuário", sqle);
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
        String idAlergiaTexto = request.getParameter("idAlergia");

        try {
            int linha;


            //aqui será observado qual parâmetro veio na URL, pois dependendo do parâmetro outro mehtodo será chamado.
            if (idTexto != null && !idTexto.isBlank()) {
                linha = usuarioAlergiaDAO.deleteById(Integer.parseInt(idTexto));

            } else if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                linha = usuarioAlergiaDAO.deleteByIdUsuario(Integer.parseInt(idUsuarioTexto));

            } else if (idAlergiaTexto != null && !idAlergiaTexto.isBlank()) {
                linha = usuarioAlergiaDAO.deleteByIdAlergia(Integer.parseInt(idAlergiaTexto));

                //se nenhum
            } else {
                response.setStatus(400);
                response.getWriter().write("Id não informado.");
                return;
            }


            if (linha > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Registro não encontrado.");
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(400);
            response.getWriter().write("Id inválido.");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao excluir a alergia do usuário.", sqle);
        }
    }

}
