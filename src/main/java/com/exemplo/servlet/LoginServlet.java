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

// Servlet do login: só deixa entrar quem tem nível admin.
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // Abre a tela de login. Se a pessoa já estiver logada, vai direto para o painel.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // false: só pega a sessão se ela já existir, sem criar uma nova.
        HttpSession sessao = request.getSession(false);
        if (sessao != null && sessao.getAttribute("usuarioLogado") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        if ("1".equals(request.getParameter("saiu"))) {
            request.setAttribute("sucesso", "Você saiu do painel.");
        }
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    // Confere o e-mail, a senha e se a conta é admin. Se a senha ainda estiver em texto, troca por hash. Depois guarda o usuário na sessão e abre o painel.
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
            Usuario usuario = usuarioDAO.listarPorEmail(email);

            if (usuario == null || !senhaConfere(senha, usuario.getSenha())) {
                voltarComErro(request, response, email, "E-mail ou senha incorretos.");
                return;
            }

            if (!"admin".equals(usuario.getNivel())) {
                voltarComErro(request, response, email, "Esta conta não tem acesso ao painel.");
                return;
            }

            if (!senhaTemHash(usuario.getSenha())) {
                usuarioDAO.trocarSenha(usuario.getIdUsuario(), SenhaUtil.gerarHash(senha));
            }

            request.getSession(true);
            request.changeSessionId();
            usuario.setSenha(null);

            request.getSession().setAttribute("usuarioLogado", usuario);
            response.sendRedirect(request.getContextPath() + "/dashboard");

        } catch (SQLException sqle) {
            throw new ServletException("Erro ao fazer login.", sqle);
        }
    }

    // Confere se a senha guardada no banco já está em hash (BCrypt).
    private boolean senhaTemHash(String senhaDoBanco) {
        return senhaDoBanco != null && senhaDoBanco.startsWith("$2");
    }

    // Compara a senha digitada com a do banco, esteja ela em hash ou em texto.
    private boolean senhaConfere(String senhaDigitada, String senhaDoBanco) {
        if (senhaTemHash(senhaDoBanco)) {
            return SenhaUtil.conferir(senhaDigitada, senhaDoBanco);
        }
        return senhaDoBanco != null && senhaDoBanco.equals(senhaDigitada);
    }

    // Volta para a tela de login mostrando a mensagem de erro.
    private void voltarComErro(HttpServletRequest request, HttpServletResponse response, String email, String erro)
            throws ServletException, IOException {
        request.setAttribute("erro", erro);
        request.setAttribute("email", email);
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
}
