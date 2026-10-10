package com.exemplo.servlet;

import com.exemplo.dao.PerfilCabeloDAO;
import com.exemplo.model.PerfilCabelo;
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

@WebServlet("/perfisCabelo")
public class PerfilCabeloServlet extends HttpServlet {

    private final PerfilCabeloDAO perfilCabeloDAO = new PerfilCabeloDAO();

    //create
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String curvaturaTexto = request.getParameter("curvatura");
        String oleosidadeTexto = request.getParameter("oleosidade");
        String espessuraTexto = request.getParameter("espessura");
        String idUsuarioTexto = request.getParameter("idUsuario");

        if (curvaturaTexto == null || curvaturaTexto.isBlank() ||
                oleosidadeTexto == null || oleosidadeTexto.isBlank() ||
                espessuraTexto == null || espessuraTexto.isBlank() ||
                idUsuarioTexto == null || idUsuarioTexto.isBlank()) {

            request.setAttribute("erro", "Preencha todos os campos obrigatórios.");
            request.getRequestDispatcher("/cadastro_perfil_cabelo.jsp").forward(request, response);
            return;
        }

        try {
            int curvatura = Integer.parseInt(curvaturaTexto);
            int oleosidade = Integer.parseInt(oleosidadeTexto);
            int espessura = Integer.parseInt(espessuraTexto);
            int idUsuario = Integer.parseInt(idUsuarioTexto);

            if (perfilCabeloDAO.existePerfilParaUsuario(idUsuario)) {
                request.setAttribute("erro", "Este usuário já possui um perfil de cabelo cadastrado.");
                request.getRequestDispatcher("/cadastro_perfil_cabelo.jsp").forward(request, response);
                return;
            }

            PerfilCabelo perfilCabelo = new PerfilCabelo(curvatura, oleosidade, espessura, idUsuario, 0);

            if (perfilCabeloDAO.cadastrarPerfilCabelo(perfilCabelo)) {
                response.sendRedirect(request.getContextPath() + "/perfisCabelo");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar o perfil de cabelo.");
                request.getRequestDispatcher("/cadastro_perfil_cabelo.jsp").forward(request, response);
            }
        } catch (NumberFormatException nfe) {
            request.setAttribute("erro", "Dados inválidos. Verifique os valores informados e tente novamente.");
            request.getRequestDispatcher("/cadastro_perfil_cabelo.jsp").forward(request, response);
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                request.setAttribute("erro", "Usuário informado não existe ou já possui perfil de cabelo.");
                request.getRequestDispatcher("/cadastro_perfil_cabelo.jsp").forward(request, response);
            } else {
                throw new ServletException("Erro ao cadastrar perfil de cabelo.", sqle);
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
                PerfilCabelo perfilCabelo = perfilCabeloDAO.readById(id);

                if (perfilCabelo == null) {
                    response.sendRedirect(request.getContextPath() + "/perfisCabelo");
                    return;
                }

                request.setAttribute("perfilCabelo", perfilCabelo);
                request.getRequestDispatcher("/editar_perfil_cabelo.jsp").forward(request, response);
                return;
            }

            if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                int idUsuario = Integer.parseInt(idUsuarioTexto);

                PerfilCabelo perfilCabelo = perfilCabeloDAO.readByIdUsuario(idUsuario);

                if (perfilCabelo == null) {
                    response.sendRedirect(request.getContextPath() + "/perfisCabelo");
                    return;
                }

                request.setAttribute("perfilCabelo", perfilCabelo);
                request.getRequestDispatcher("/editar_perfil_cabelo.jsp").forward(request, response);
                return;
            }

            ArrayList<PerfilCabelo> listaPerfisCabelo = perfilCabeloDAO.read();
            request.setAttribute("perfisCabelo", listaPerfisCabelo);
            request.getRequestDispatcher("/lista_perfil_cabelo.jsp").forward(request, response);
        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/perfisCabelo");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar perfis de cabelo.", sqle);
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

            if (!json.has("idPerfilCabelo") || !json.has("curvatura") ||
                    !json.has("oleosidade") || !json.has("espessura")) {

                response.setStatus(400);
                response.getWriter().write("Dados incompletos.");
                return;
                //encerra o mehtodo na hora, depois de definir o status e escrever a mensagem.

            }


            int id = Integer.parseInt(json.get("idPerfilCabelo").getAsString());
            int curvatura = Integer.parseInt(json.get("curvatura").getAsString());
            int oleosidade = Integer.parseInt(json.get("oleosidade").getAsString());
            int espessura = Integer.parseInt(json.get("espessura").getAsString());

            PerfilCabelo perfilCabelo = new PerfilCabelo(id, curvatura, oleosidade, espessura);

            int linhas = perfilCabeloDAO.alterarValores(perfilCabelo);

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Perfil de cabelo não encontrado.");
            }
        } catch (JsonParseException | IllegalStateException | UnsupportedOperationException | NumberFormatException e) {
            response.setStatus(400);
            response.getWriter().write("Dados inválidos.");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao atualizar o perfil de cabelo.", sqle);
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
                linhas = perfilCabeloDAO.deleteById(id);
            } else if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                int idUsuario = Integer.parseInt(idUsuarioTexto);
                linhas = perfilCabeloDAO.deleteByIdUsuario(idUsuario);
            } else {
                response.setStatus(400);
                response.getWriter().write("Id não informado.");
                return;
                //encerra o mehtodo na hora, depois de definir o status e escrever a mensagem.

            }

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Perfil de cabelo não encontrado.");
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(400);
            response.getWriter().write("Id inválido.");
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                response.setStatus(409);
                //Conflict. Ele avisa que a requisição está correta, mas conflita com o estado atual dos dados no banco.
                response.getWriter().write("Não é possível excluir: o perfil de cabelo está em uso.");
            } else {
                throw new ServletException("Erro ao excluir o perfil de cabelo.", sqle);
            }
        }
    }
}
