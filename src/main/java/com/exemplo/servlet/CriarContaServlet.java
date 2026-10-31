package com.exemplo.servlet;

import com.exemplo.dao.UsuarioDAO;
import com.exemplo.model.Usuario;
import com.exemplo.util.SenhaUtil;
import com.exemplo.util.Validador;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet("/criar-conta")
public class CriarContaServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/criar_conta.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String telefone = Validador.somenteNumeros(request.getParameter("telefone"));
        String dtNascimentoTexto = request.getParameter("dt_nascimento");
        String senha = request.getParameter("senha");

        if (nome == null || nome.isBlank() || email == null || email.isBlank()
                || dtNascimentoTexto == null || dtNascimentoTexto.isBlank() || senha == null || senha.isBlank()) {
            voltarComErro(request, response, "Preencha todos os campos.");
            return;
        }

        email = email.trim().toLowerCase();

        if (nome.trim().length() > 100) {
            voltarComErro(request, response, "O nome pode ter no máximo 100 caracteres.");
            return;
        }
        if (!Validador.emailValido(email) || email.length() > 100) {
            voltarComErro(request, response, "E-mail inválido.");
            return;
        }
        if (!Validador.telefoneValido(telefone)) {
            voltarComErro(request, response, "Telefone inválido. Use DDD e número (10 a 13 dígitos).");
            return;
        }
        if (!Validador.senhaValida(senha)) {
            voltarComErro(request, response, "A senha deve ter pelo menos 6 caracteres.");
            return;
        }

        LocalDate dtNascimento;
        try {
            dtNascimento = LocalDate.parse(dtNascimentoTexto);
        } catch (DateTimeParseException e) {
            voltarComErro(request, response, "Data de nascimento inválida.");
            return;
        }
        if (!Validador.nascimentoDepoisDe1900(dtNascimento)) {
            voltarComErro(request, response, "Data de nascimento inválida. Use uma data depois de 01/01/1900.");
            return;
        }
        if (!Validador.idadeMinimaValida(dtNascimento, 18)) {
            voltarComErro(request, response, "É preciso ter 18 anos ou mais para criar uma conta.");
            return;
        }

        Usuario usuario = new Usuario(nome.trim(), null, email, SenhaUtil.gerarHash(senha),
                telefone, dtNascimento, LocalDate.now());

        try {
            if (usuarioDAO.existeEmail(email)) {
                voltarComErro(request, response, "Este e-mail já está cadastrado. Tente entrar.");
                return;
            }
            usuarioDAO.cadastrarUsuario(usuario);
            response.sendRedirect(request.getContextPath() + "/login?criada=1");
        } catch (SQLException sqle) {
            String estado = sqle.getSQLState();
            if ("23505".equals(estado)) {
                voltarComErro(request, response, "Este e-mail já está cadastrado. Tente entrar.");
            } else if (estado != null && estado.startsWith("23")) {
                voltarComErro(request, response, "O banco não aceitou algum dado. Confira telefone e data de nascimento.");
            } else {
                throw new ServletException("Erro ao criar conta.", sqle);
            }
        }
    }

    private void voltarComErro(HttpServletRequest request, HttpServletResponse response, String erro)
            throws ServletException, IOException {
        request.setAttribute("erro", erro);
        request.getRequestDispatcher("/WEB-INF/views/criar_conta.jsp").forward(request, response);
    }
}
