package com.exemplo.servlet;

import com.exemplo.dao.AdminDAO;
import com.exemplo.model.Admin;
import com.exemplo.util.SenhaUtil;
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

@WebServlet("/admins")
public class AdminServlet extends HttpServlet {

    private final AdminDAO adminDAO = new AdminDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String confirmacao = request.getParameter("confirmacao");

        nome = nome == null ? "" : nome.trim();
        email = email == null ? "" : email.trim().toLowerCase();

        String erro = null;
        if (nome.length() < 2 || nome.length() > 100) {
            erro = "Informe o nome do admin (de 2 a 100 caracteres).";
        } else if (!Validador.emailValido(email)) {
            erro = "Digite um e-mail válido.";
        } else if (!Validador.senhaValida(senha)) {
            erro = "A senha precisa ter pelo menos 6 caracteres.";
        } else if (!senha.equals(confirmacao)) {
            erro = "As senhas devem ser iguais.";
        }

        if (erro != null) {
            request.setAttribute("erro", erro);
            request.getRequestDispatcher("/WEB-INF/views/cadastro_admin.jsp").forward(request, response);
            return;
        }

        try {
            Admin admin = new Admin(nome, email, SenhaUtil.gerarHash(senha));

            if (adminDAO.inserirAdmin(admin)) {
                response.sendRedirect(request.getContextPath() + "/admins");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar o admin.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_admin.jsp").forward(request, response);
            }
        } catch (SQLException sqle) {
            getServletContext().log("Erro SQL ao cadastrar admin", sqle);
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                request.setAttribute("erro", "Já existe um admin com esse e-mail.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_admin.jsp").forward(request, response);
            } else {
                throw new ServletException("Erro ao cadastrar admin", sqle);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idTexto = request.getParameter("id");
        String acao = request.getParameter("acao");

        if ("novo".equals(acao)) {
            request.getRequestDispatcher("/WEB-INF/views/cadastro_admin.jsp").forward(request, response);
            return;
        }

        try {
            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                Admin admin = adminDAO.readById(id);

                if (admin == null) {
                    response.sendRedirect(request.getContextPath() + "/admins");
                    return;
                }

                request.setAttribute("admin", admin);
                request.getRequestDispatcher("/WEB-INF/views/editar_admin.jsp").forward(request, response);
                return;
            }

            ArrayList<Admin> listaAdmins = adminDAO.read();
            request.setAttribute("admins", listaAdmins);
            request.getRequestDispatcher("/WEB-INF/views/lista_admins.jsp").forward(request, response);
        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/admins");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar admins.", sqle);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        String corpo = request.getReader().lines().collect(Collectors.joining());

        try {
            JsonObject json = JsonParser.parseString(corpo).getAsJsonObject();

            if (!json.has("idAdmin") || !json.has("nome") || !json.has("email")) {
                response.setStatus(400);
                response.getWriter().write("Dados incompletos.");
                return;
            }

            int id = Integer.parseInt(json.get("idAdmin").getAsString());
            String nome = json.get("nome").getAsString().trim();
            String email = json.get("email").getAsString().trim().toLowerCase();
            String senha = json.has("senha") ? json.get("senha").getAsString() : "";

            if (nome.length() < 2 || nome.length() > 100) {
                response.setStatus(400);
                response.getWriter().write("Informe o nome do admin (de 2 a 100 caracteres).");
                return;
            }
            if (!Validador.emailValido(email)) {
                response.setStatus(400);
                response.getWriter().write("Digite um e-mail válido.");
                return;
            }
            if (!senha.isEmpty() && !Validador.senhaValida(senha)) {
                response.setStatus(400);
                response.getWriter().write("A senha precisa ter pelo menos 6 caracteres.");
                return;
            }

            Admin admin = new Admin(id, nome, email, null, null);
            int linhas = adminDAO.update(admin);

            if (linhas > 0) {
                if (!senha.isEmpty()) {
                    adminDAO.trocarSenha(id, SenhaUtil.gerarHash(senha));
                }
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Admin não encontrado.");
            }
        } catch (JsonParseException | IllegalStateException | UnsupportedOperationException | NumberFormatException e) {
            response.setStatus(400);
            response.getWriter().write("Dados inválidos.");
        } catch (SQLException sqle) {
            getServletContext().log("Erro SQL ao atualizar admin", sqle);
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                response.setStatus(409);
                response.getWriter().write("Já existe um admin com esse e-mail.");
            } else {
                throw new ServletException("Erro ao atualizar o admin.", sqle);
            }
        }
    }

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
            int linhas = adminDAO.deleteById(id);

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Admin não encontrado.");
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(400);
            response.getWriter().write("Id inválido.");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao excluir o admin.", sqle);
        }
    }
}
