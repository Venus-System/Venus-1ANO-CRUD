package com.exemplo.servlet;

import com.exemplo.dao.UsuarioDAO;
import com.exemplo.model.Usuario;
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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.stream.Collectors;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {
    //transforma a classe Java comum em um Servlet

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    //O tomcat cria só uma instância do servlet e reutiliza.


    //create
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        //aqui ele pega cada valor do formulário.

        String nome = request.getParameter("nome");
        String genero = request.getParameter("genero");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String telefone = request.getParameter("telefone");
        String dtNascimentoTexto = request.getParameter("dt_nascimento");

        if (nome == null || nome.isBlank() ||
                email == null || email.isBlank() ||
                senha == null || senha.isBlank() ||
                dtNascimentoTexto == null || dtNascimentoTexto.isBlank()) {

            request.setAttribute("erro", "Preencha todos os campos obrigatórios.");
            request.getRequestDispatcher("/cadastro_usuario.jsp").forward(request, response);
            return;
        }

        try {
            LocalDate dtNascimento = LocalDate.parse(dtNascimentoTexto);
            LocalDate dtCadastro = LocalDate.now();

            if (usuarioDAO.existeEmail(email)) {
                request.setAttribute("erro", "Este e-mail já está cadastrado.");
                request.getRequestDispatcher("/cadastro_usuario.jsp").forward(request, response);
                return;
                // Sem o return o código continuaria e tentaria cadastrar, e depois fazer um segundo redirect.
            }

            //monta o objeto model com os dados.
            Usuario usuario = new Usuario(nome, genero, email, senha, telefone, dtNascimento, dtCadastro);

            if (usuarioDAO.cadastrarUsuario(usuario)) {
                response.sendRedirect(request.getContextPath() + "/usuarios");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar o usuário.");
                request.getRequestDispatcher("/cadastro_usuario.jsp").forward(request, response);
            }

        } catch (DateTimeParseException dtpe) {
            request.setAttribute("erro", "Data de nascimento inválida.");
            request.getRequestDispatcher("/cadastro_usuario.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao cadastrar usuário ", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Se a URL for /usuarios?id=5, vem "5". Se for só /usuarios, vem null.
        String idTexto = request.getParameter("id");

        try {
            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                Usuario usuario = usuarioDAO.readById(id);
                //readById se nenhum usuário tiver esse id

                if (usuario == null) {
                    response.sendRedirect(request.getContextPath() + "/usuarios");
                }

                request.setAttribute("usuario", usuario);
                request.getRequestDispatcher("/editar_usuario.jsp").forward(request, response);
                return;
            }

            ArrayList<Usuario> listaUsuarios = usuarioDAO.read();
            request.setAttribute("usuarios", listaUsuarios);
            request.getRequestDispatcher("/lista_usuarios.jsp").forward(request, response);

        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/usuarios");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar usuários", sqle);
        }
    }

    //Se não veio com id, vai listar todos os usuários


    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8"); //define a codificação que entra, para os acentos.
        response.setCharacterEncoding("UTF-8"); //define a decodificação que sai.
        response.setContentType("text/plain"); //avisa que a resposta é texto simples.

        //Lê o corpo (linha por linha) da requisição.
        //o getReader lê o corpo, lines separa em linhas, collectiors.joining junta tudo em um texto só.
        String corpo = request.getReader().lines().collect(Collectors.joining());

        try {
            JsonObject json = JsonParser.parseString(corpo).getAsJsonObject();
            //sendo transformado em um objeto java. (dá para extrair cada campo)

            if (!json.has("idUsuario") || !json.has("nome") || !json.has("genero")
                    || !json.has("email") || !json.has("telefone") || !json.has("dt_nascimento")) {

                response.setStatus(400);
                response.getWriter().write("Dados incompletos.");
                return;
            }

            int id = Integer.parseInt(json.get("idUsuario").getAsString());
            String nome = json.get("nome").getAsString();
            String genero = json.get("genero").getAsString();
            String email = json.get("email").getAsString();
            String telefone = json.get("telefone").getAsString();
            String dtNascimentoTexto = json.get("dt_nascimento").getAsString();

            if (nome.isBlank() || email.isBlank() || dtNascimentoTexto.isBlank()) {
                response.setStatus(400);
                response.getWriter().write("Preencha todos os campos obrigatórios.");
                return;
            }

            LocalDate dtNascimento = LocalDate.parse(dtNascimentoTexto);

            Usuario usuarioExistente = usuarioDAO.readByEmail(email);

            if (usuarioExistente != null && usuarioExistente.getIdUsuario() != id) {
                response.setStatus(409);
                response.getWriter().write("Este e-mail já pertence a outro usuário.");
                return;
            }

            Usuario usuario = new Usuario(id, nome, genero, email, null, telefone, dtNascimento, null);
            //como o update não mexe em senha e dtCadastro, esses campos ficam null.

            int linhas = usuarioDAO.update(usuario);

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404); //se nenhum usuário tiver o id
                response.getWriter().write("Usuário não encontrado.");
            }
        } catch (JsonParseException | IllegalStateException | NumberFormatException e) {
            response.setStatus(400);
            response.getWriter().write("Dados inválidos");
        } catch (DateTimeParseException dtpe) {
            response.setStatus(400);
            response.getWriter().write("Data de nascimento inválida");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao atualizar o usuário.", sqle);
        }
    }
}