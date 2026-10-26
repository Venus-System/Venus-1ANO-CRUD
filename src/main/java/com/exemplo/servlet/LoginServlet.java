package com.exemplo.servlet;

import com.exemplo.dao.UsuarioDAO;
import com.exemplo.model.Usuario;
import com.exemplo.util.SenhaUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessao = request.getSession(false);
        if (sessao != null && sessao.getAttribute("usuarioLogado") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        if ("1".equals(request.getParameter("criada"))) {
            request.setAttribute("sucesso", "Conta criada! Agora é só entrar com o seu e-mail e a sua senha.");
        }
        if ("1".equals(request.getParameter("saiu"))) {
            request.setAttribute("sucesso", "Você saiu do painel.");
        }
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        email = email == null ? "" : email.trim().toLowerCase();

        if (email.isBlank() || senha == null || senha.isBlank()) {
            voltarComErro(request, response, email, "Preencha o e-mail e a senha.");
            return;
        }

        try {
            Usuario usuario = usuarioDAO.readByEmail(email);

            if (usuario == null || !senhaConfere(senha, usuario.getSenha())) {
                voltarComErro(request, response, email, "E-mail ou senha incorretos.");
                return;
            }

            // Converte a senha antiga em texto para BCrypt após confirmar as credenciais.
            if (!senhaTemHash(usuario.getSenha())) {
                usuarioDAO.trocarSenha(usuario.getIdUsuario(), SenhaUtil.gerarHash(senha));
            }

            request.getSession(true);
            // Renova o identificador da sessão após a autenticação.
            request.changeSessionId();

            // Evita manter a senha carregada do banco no objeto guardado na sessão.
            usuario.setSenha(null);
            request.getSession().setAttribute("usuarioLogado", usuario);
            response.sendRedirect(request.getContextPath() + "/dashboard");

        } catch (SQLException sqle) {
            throw new ServletException("Erro ao fazer login.", sqle);
        }
    }

    private boolean senhaTemHash(String senhaDoBanco) {
        return senhaDoBanco != null && senhaDoBanco.startsWith("$2");
    }

    private boolean senhaConfere(String senhaDigitada, String senhaDoBanco) {
        if (senhaTemHash(senhaDoBanco)) {
            return SenhaUtil.conferir(senhaDigitada, senhaDoBanco);
        }
        return senhaDoBanco != null && senhaDoBanco.equals(senhaDigitada);
    }

    private void voltarComErro(HttpServletRequest request, HttpServletResponse response, String email, String erro)
            throws ServletException, IOException {
        request.setAttribute("erro", erro);
        request.setAttribute("email", email);
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
}
