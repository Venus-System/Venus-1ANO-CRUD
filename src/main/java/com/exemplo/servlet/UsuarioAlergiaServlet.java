package com.exemplo.servlet;


import com.exemplo.dao.AlergiaDAO;
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
    private final AlergiaDAO alergiaDAO = new AlergiaDAO();
    //create

    //O SQLException do read() é tratado dentro do auxiliar.
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, String erro)
            throws ServletException, IOException {
        try {
            request.setAttribute("alergias", alergiaDAO.read());
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar alergias", sqle);
        }
        request.setAttribute("erro", erro);
        request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario_alergia.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String grauTexto = request.getParameter("grau");
        String idUsuarioTexto = request.getParameter("idUsuario");
        String idAlergiaTexto = request.getParameter("idAlergia");

        if (grauTexto == null || grauTexto.isBlank() ||
                idUsuarioTexto == null || idUsuarioTexto.isBlank() ||
                idAlergiaTexto == null || idAlergiaTexto.isBlank()) {

            mostrarFormulario(request, response, "Preencha todos os campos obrigatórios.");
            return;
        }
        //a validação dos campos vazios ficou antes do try, que trata o SQLException.

        try {
            int grau = Integer.parseInt(grauTexto);
            int idUsuario = Integer.parseInt(idUsuarioTexto);
            int idAlergia = Integer.parseInt(idAlergiaTexto);

            UsuarioAlergia usuarioAlergia = new UsuarioAlergia(grau, idUsuario, idAlergia);

            if (usuarioAlergiaDAO.inserirUsuarioAlergia(usuarioAlergia)) {
                //a interrogação serve para o endereço dos parâmetros. Com o getParameter o servlet lê toda a informação extra.
                //Depois de cadastrar, a pessoa volta para a lista do usuário em que estava.
                response.sendRedirect(request.getContextPath() + "/usuarioAlergias?idUsuario=" + idUsuario);
            } else {
                mostrarFormulario(request, response, "Não foi possível cadastrar a alergia do usuário.");
            }
        } catch (NumberFormatException nfe) {
            mostrarFormulario(request, response, "Dados inválidos. Verifique o grau informado e tente novamente.");
        } catch (SQLException sqle) {
            String estado = sqle.getSQLState();
            //aqui significa que o vínculo já existe.
            if ("23505".equals(estado)) {
                mostrarFormulario(request, response, "Este usuário já possui essa alergia.");

                //aqui significa que o usuário ou a alergia já existe.
            } else if ("23503".equals(estado)) {
                mostrarFormulario(request, response, "Usuário ou alergia não existe.");
            } else {
                //aqui o throw também encerra o mehtodo.
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
        String acao = request.getParameter("acao");

        try {
            if ("novo".equals(acao)) {
                request.setAttribute("alergias", alergiaDAO.read());
                request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario_alergia.jsp").forward(request, response);
                return;
            }

            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                UsuarioAlergia usuarioAlergia = usuarioAlergiaDAO.readById(id);

                if (usuarioAlergia == null) {
                    response.sendRedirect(request.getContextPath() + "/usuarioAlergias");
                    return;
                }

                request.setAttribute("usuarioAlergia", usuarioAlergia);
                request.getRequestDispatcher("/WEB-INF/views/editar_usuario_alergia.jsp").forward(request, response);
                return;
            }

            if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                int idUsuario = Integer.parseInt(idUsuarioTexto);
                ArrayList<UsuarioAlergia> listaUsuarioAlergia = usuarioAlergiaDAO.readByIdUsuario(idUsuario);

                request.setAttribute("usuariosAlergias", listaUsuarioAlergia);
                request.getRequestDispatcher("/WEB-INF/views/lista_usuario_alergias.jsp").forward(request, response);
                return;
            }

            ArrayList<UsuarioAlergia> listaUsuarioAlergia = usuarioAlergiaDAO.read();
            request.setAttribute("usuariosAlergias", listaUsuarioAlergia);
            request.getRequestDispatcher("/WEB-INF/views/lista_usuario_alergias.jsp").forward(request, response);

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
