package com.exemplo.servlet;

import com.exemplo.dao.UsuarioDAO;
import com.exemplo.model.Usuario;
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
            request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario.jsp").forward(request, response);
            return;
        }

        email = email.trim().toLowerCase();
        //email em espaços extras.
        if (!Validador.emailValido(email)) {
            request.setAttribute("erro", "E-mail inválido.");
            request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario.jsp").forward(request, response);
            return;
        }

        if (telefone != null) {
            telefone = telefone.trim();
            //email em espaços extras.
        }

        if (!Validador.telefoneValido(telefone)) {
            request.setAttribute("erro", "Telefone inválido. Use DDD e número.");
            request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario.jsp").forward(request, response);
            return;
        }
        if (!Validador.senhaValida(senha)) {
            request.setAttribute("erro", "A senha deve ter pelo menos 6 caracteres.");
            request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario.jsp").forward(request, response);
            return;
        }

        try {
            LocalDate dtNascimento = LocalDate.parse(dtNascimentoTexto);
            LocalDate dtCadastro = LocalDate.now();

            if (dtNascimento.isAfter(LocalDate.now())) {
                request.setAttribute("erro", "A data de nascimento não pode ser no futuro.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario.jsp").forward(request, response);
                return;
                //barrar datas de nascimento no futuro.
            }

            if (usuarioDAO.existeEmail(email)) {
                request.setAttribute("erro", "Este e-mail já está cadastrado.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario.jsp").forward(request, response);
                return;
                // Sem o return o código continuaria e tentaria cadastrar, e depois fazer um segundo redirect.
            }

            //monta o objeto model com os dados.
            Usuario usuario = new Usuario(nome.trim(), genero, email, SenhaUtil.gerarHash(senha), telefone, dtNascimento, dtCadastro);

            if (usuarioDAO.cadastrarUsuario(usuario)) {
                response.sendRedirect(request.getContextPath() + "/usuarios");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar o usuário.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario.jsp").forward(request, response);
            }

        } catch (DateTimeParseException dtpe) {
            request.setAttribute("erro", "Data de nascimento inválida.");
            request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario.jsp").forward(request, response);
        } catch (SQLException sqle) {
            getServletContext().log("Erro SQL ao cadastrar usuário. SQLState=" + sqle.getSQLState(), sqle);

            String estado = sqle.getSQLState();
            String detalhe = sqle.getMessage() == null ? "" : sqle.getMessage().toLowerCase();

            if ("23505".equals(estado) && detalhe.contains("email")) {
                request.setAttribute("erro", "Este e-mail já está cadastrado.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario.jsp").forward(request, response);
            } else if (estado != null && estado.startsWith("23")) {
                request.setAttribute("erro", "Não foi possível cadastrar: os dados entram em conflito com as regras do banco.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario.jsp").forward(request, response);
            } else {
                throw new ServletException("Erro ao cadastrar usuário.", sqle);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if ("novo".equals(request.getParameter("acao"))) {
            request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario.jsp").forward(request, response);
            return;
        }

        // Se a URL for /usuarios?id=5, vem "5". Se for só /usuarios, vem null.
        String idTexto = request.getParameter("id");

        try {
            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                Usuario usuario = usuarioDAO.readById(id);
                //readById se nenhum usuário tiver esse id

                if (usuario == null) {
                    response.sendRedirect(request.getContextPath() + "/usuarios");
                    return;
                }

                request.setAttribute("usuario", usuario);
                request.getRequestDispatcher("/WEB-INF/views/editar_usuario.jsp").forward(request, response);
                return;
            }

            ArrayList<Usuario> listaUsuarios = usuarioDAO.read();
            request.setAttribute("usuarios", listaUsuarios);
            request.getRequestDispatcher("/WEB-INF/views/lista_usuarios.jsp").forward(request, response);

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

            //extrai os campos como texto
            int id = Integer.parseInt(json.get("idUsuario").getAsString());
            String nome = json.get("nome").getAsString();
            String genero = json.get("genero").getAsString();
            String email = json.get("email").getAsString().trim().toLowerCase();
            String telefone = json.get("telefone").getAsString().trim();
            String dtNascimentoTexto = json.get("dt_nascimento").getAsString();


            if (nome.isBlank() || email.isBlank() || dtNascimentoTexto.isBlank()) {
                response.setStatus(400);
                response.getWriter().write("Preencha todos os campos obrigatórios.");
                return;
            }

            //Aquil valida o e-mail e o telefone no servidor.
            if (!Validador.emailValido(email)) {
                response.setStatus(400);
                response.getWriter().write("E-mail inválido.");
                return;
            }
            if (!Validador.telefoneValido(telefone)) {
                response.setStatus(400);
                response.getWriter().write("Telefone inválido. Use DDD e número.");
                return;
            }

            LocalDate dtNascimento = LocalDate.parse(dtNascimentoTexto);

            if (dtNascimento.isAfter(LocalDate.now())) {
                response.setStatus(400);
                response.getWriter().write("A data de nascimento não pode ser no futuro.");
                return;
                //para a data de nascimento não ser alterada para o futuro
            }

            Usuario usuarioExistente = usuarioDAO.readByEmail(email);

            if (usuarioExistente != null && usuarioExistente.getIdUsuario() != id) {
                response.setStatus(409);
                response.getWriter().write("Este e-mail já pertence a outro usuário.");
                return;
            }

            Usuario usuario = new Usuario(id, nome.trim(), genero, email, null, telefone, dtNascimento, null);
            //como o update não mexe em senha e dtCadastro, esses campos ficam null.

            int linhas = usuarioDAO.update(usuario);

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404); //se nenhum usuário tiver o id
                response.getWriter().write("Usuário não encontrado.");
            }
        } catch (JsonParseException | IllegalStateException | UnsupportedOperationException | NumberFormatException e) {
            response.setStatus(400);
            response.getWriter().write("Dados inválidos");
        } catch (DateTimeParseException dtpe) {
            response.setStatus(400);
            response.getWriter().write("Data de nascimento inválida");
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                response.setStatus(409);
                response.getWriter().write("Este e-mail já pertence a outro usuário.");
            } else {
                throw new ServletException("Erro ao atualizar o usuário.", sqle);
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
            int linhas = usuarioDAO.deleteById(id);

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Usuário não encontrado.");
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(400);
            response.getWriter().write("Id inválido.");
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                response.setStatus(409);
                response.getWriter().write("Não é possível excluir: o usuário possui registros vinculados.");
            } else {
                throw new ServletException("Erro ao excluir o usuário.", sqle);
            }
        }


    }
}