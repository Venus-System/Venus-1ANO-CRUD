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


    // Cadastro
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
        if (!Validador.nomeUsuarioValido(nome)) {
            request.setAttribute("erro", "Informe um nome válido de 2 a 100 caracteres.");
            request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario.jsp").forward(request, response);
            return;
        }
        // Normaliza o e-mail antes de validar e consultar a existência no banco.
        if (!Validador.emailValido(email) || email.length() > 100) {
            request.setAttribute("erro", "E-mail inválido.");
            request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario.jsp").forward(request, response);
            return;
        }

        telefone = Validador.somenteNumeros(telefone);

        if (!Validador.telefoneValido(telefone)) {
            request.setAttribute("erro", "Telefone inválido. Use DDD e número (10 a 13 dígitos).");
            request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario.jsp").forward(request, response);
            return;
        }

        if (!Validador.generoValido(genero)) {
            request.setAttribute("erro", "Gênero inválido.");
            request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario.jsp").forward(request, response);
            return;
        }
        if (genero != null && genero.isBlank()) {
            genero = null;
        }
        if (!Validador.senhaValida(senha)) {
            request.setAttribute("erro", "A senha deve ter pelo menos 6 caracteres.");
            request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario.jsp").forward(request, response);
            return;
        }

        try {
            LocalDate dtNascimento = LocalDate.parse(dtNascimentoTexto);
            LocalDate dtCadastro = LocalDate.now();

            if (!Validador.nascimentoDepoisDe1900(dtNascimento)) {
                request.setAttribute("erro", "Data de nascimento inválida. Use uma data depois de 01/01/1900.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario.jsp").forward(request, response);
                return;
            }
            if (!Validador.idadeMinimaValida(dtNascimento, 18)) {
                request.setAttribute("erro", "É necessário ter pelo menos 18 anos.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario.jsp").forward(request, response);
                return;
            }

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
            //no console do Tomcat aparece a mensagem de erro. Quem usa o site não vê isso.
            getServletContext().log("Erro SQL ao cadastrar usuário", sqle);

            if ("23505".equals(sqle.getSQLState())) {
                request.setAttribute("erro", "Este e-mail já está cadastrado.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_usuario.jsp").forward(request, response);
            } else if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                request.setAttribute("erro", "O banco não aceitou algum dado. Confira telefone, gênero e data de nascimento.");
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
                if (!Validador.idValido(idTexto)) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Id inválido.");
                    return;
                }
                int id = Integer.parseInt(idTexto);
                Usuario usuario = usuarioDAO.readById(id);
                // A consulta retorna null quando o usuário não existe.

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


    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8"); //define a codificação que entra, para os acentos.
        response.setCharacterEncoding("UTF-8"); // Define a codificação da resposta.
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
            if (id <= 0) {
                response.setStatus(400);
                response.getWriter().write("Id inválido.");
                return;
            }
            String nome = json.get("nome").getAsString();
            String genero = json.get("genero").getAsString();
            String email = json.get("email").getAsString().trim().toLowerCase();
            String telefone = Validador.somenteNumeros(json.get("telefone").getAsString());
            String dtNascimentoTexto = json.get("dt_nascimento").getAsString();


            if (nome.isBlank() || email.isBlank() || dtNascimentoTexto.isBlank()) {
                response.setStatus(400);
                response.getWriter().write("Preencha todos os campos obrigatórios.");
                return;
            }

            if (!Validador.nomeUsuarioValido(nome)) {
                response.setStatus(400);
                response.getWriter().write("Informe um nome válido de 2 a 100 caracteres.");
                return;
            }

            // Valida e-mail e telefone também no servidor.
            if (!Validador.emailValido(email) || email.length() > 100) {
                response.setStatus(400);
                response.getWriter().write("E-mail inválido.");
                return;
            }
            if (!Validador.telefoneValido(telefone)) {
                response.setStatus(400);
                response.getWriter().write("Telefone inválido. Use DDD e número (10 a 13 dígitos).");
                return;
            }
            if (!Validador.generoValido(genero)) {
                response.setStatus(400);
                response.getWriter().write("Gênero inválido.");
                return;
            }
            if (genero.isBlank()) {
                genero = null;
            }

            LocalDate dtNascimento = LocalDate.parse(dtNascimentoTexto);

            if (!Validador.nascimentoDepoisDe1900(dtNascimento)) {
                response.setStatus(400);
                response.getWriter().write("Data de nascimento inválida. Use uma data depois de 01/01/1900.");
                return;
            }
            if (!Validador.idadeMinimaValida(dtNascimento, 18)) {
                response.setStatus(400);
                response.getWriter().write("É necessário ter pelo menos 18 anos.");
                return;
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
            getServletContext().log("Erro SQL ao atualizar usuário", sqle);
            //mostra a causa real no Tomcat caso apareça algum erro inesperado.
            if ("23505".equals(sqle.getSQLState())) {
                response.setStatus(409);
                response.getWriter().write("Este e-mail já pertence a outro usuário.");
            } else if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                response.setStatus(400);
                response.getWriter().write("O banco não aceitou algum dado. Confira telefone, gênero e data de nascimento.");
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
            if (!Validador.idValido(idTexto)) {
                response.setStatus(400);
                response.getWriter().write("Id inválido.");
                return;
            }
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
