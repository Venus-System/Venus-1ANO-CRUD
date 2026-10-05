package com.exemplo.servlet;


import com.exemplo.dao.UsuarioAlergiaDAO;
import com.exemplo.model.UsuarioAlergia;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kotlin.OverloadResolutionByLambdaReturnType;
import org.postgresql.core.SqlCommand;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
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
        String idAlergiaTexto = request.getParameter(request.getParameter("id_alergia"));

        if (grauTexto != null || !grauTexto.isBlank() ||
                idUsuarioTexto != null || !idUsuarioTexto.isBlank() ||
                idAlergiaTexto != null || !idAlergiaTexto.isBlank()) {

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
                response.sendRedirect(request.getContextPath() + "/usuarioAlergia");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar a alergia do usuário.");
                request.getRequestDispatcher("/caastro_usuario_alergia.jsp").forward(request, response);
            }
        } catch (NumberFormatException nfe) {
            request.setAttribute("erro", "Data de registro inválida.");
            request.getRequestDispatcher("/cadastro_usuario_alergia.jsp").forward(request, response);
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                //o 'if' confere se o código existe, e pergunta se o código começa com  (que é a classe de erro "violação de restrição de integridade")
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

            }
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

            if (!json.has("idUsuarioAlergia") || !json.has("grau") || !json.has("dt_registro")) {
                response.setStatus(400);
                response.getWriter().write("Dados incompletos.");
                return;
            }

            int id = Integer.parseInt(json.get("idUsuarioAlergia").getAsString());
            int grau = Integer.parseInt(json.get("grau").getAsString());
            String dtRegistroTexto = json.get("dt_registro").getAsString();

            if (dtRegistroTexto.isBlank()) {
                response.setStatus(400);
                response.getWriter().write("Preencha todos os campos obrigatórios.");
                return;
            }

            LocalDate dtRegistro = LocalDate.parse(dtRegistroTexto);

            UsuarioAlergia usuarioAlergia = new UsuarioAlergia(grau, id);

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
        } catch (DateTimeParseException dtpe) {
            response.setStatus(400);
            response.getWriter().write("Data de registro inválida.");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao atualizar a alergia do usuário");
        }
    }

}
