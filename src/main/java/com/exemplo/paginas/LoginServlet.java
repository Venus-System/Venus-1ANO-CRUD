package com.exemplo.paginas;

import com.exemplo.dao.UsuarioDAO;
import com.exemplo.model.Usuario;
import com.exemplo.util.EmailUtil;
import com.exemplo.util.SenhaUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;

@WebServlet({"/login", "/verificar-codigo"})
public class LoginServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private static final long DEZ_MINUTOS = 10 * 60 * 1000;
    private static final SecureRandom SORTEIO = new SecureRandom();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessao = request.getSession(false);
        if (sessao != null && sessao.getAttribute("usuarioLogado") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        if ("/verificar-codigo".equals(request.getServletPath())) {
            if (sessao == null || sessao.getAttribute("usuarioPendente") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            if ("1".equals(request.getParameter("reenviar"))) {
                try {
                    enviarCodigo(request, (Usuario) sessao.getAttribute("usuarioPendente"));
                    response.sendRedirect(request.getContextPath() + "/verificar-codigo");
                } catch (MessagingException me) {
                    System.out.println("Erro ao enviar e-mail: " + me.getMessage());
                    request.setAttribute("erro", "Não foi possível enviar o código agora. Tente novamente mais tarde.");
                    request.getRequestDispatcher("/WEB-INF/views/verificar_codigo.jsp").forward(request, response);
                }
                return;
            }
            request.getRequestDispatcher("/WEB-INF/views/verificar_codigo.jsp").forward(request, response);
            return;
        }

        if ("1".equals(request.getParameter("criada"))) {
            request.setAttribute("sucesso", "Conta criada! Agora é só entrar com o seu e-mail e a sua senha.");
        }
        if ("1".equals(request.getParameter("saiu"))) {
            request.setAttribute("sucesso", "Você saiu do painel.");
        }
        if ("1".equals(request.getParameter("expirado"))) {
            request.setAttribute("erro", "O código expirou. Entre de novo para receber outro.");
        }
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if ("/verificar-codigo".equals(request.getServletPath())) {
            conferirCodigo(request, response);
            return;
        }

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

            if (!senhaTemHash(usuario.getSenha())) {
                usuarioDAO.trocarSenha(usuario.getIdUsuario(), SenhaUtil.gerarHash(senha));
            }

            request.getSession(true);
            request.changeSessionId();
            usuario.setSenha(null);

            if (!EmailUtil.configurado()) {
                System.out.println("EMAIL_REMETENTE ou EMAIL_SENHA_APP faltando no .env: login sem código de verificação");
                request.getSession().setAttribute("usuarioLogado", usuario);
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }

            enviarCodigo(request, usuario);
            response.sendRedirect(request.getContextPath() + "/verificar-codigo");

        } catch (SQLException sqle) {
            throw new ServletException("Erro ao fazer login.", sqle);
        } catch (MessagingException me) {
            System.out.println("Erro ao enviar e-mail: " + me.getMessage());
            request.getSession().invalidate();
            voltarComErro(request, response, email, "Não foi possível enviar o código agora. Tente novamente mais tarde.");
        }
    }

    private void enviarCodigo(HttpServletRequest request, Usuario usuario) throws IOException, MessagingException {
        String codigo = String.format("%06d", SORTEIO.nextInt(1000000));

        HttpSession sessao = request.getSession();
        sessao.setAttribute("usuarioPendente", usuario);
        sessao.setAttribute("codigoLogin", codigo);
        sessao.setAttribute("codigoValidade", System.currentTimeMillis() + DEZ_MINUTOS);
        sessao.setAttribute("codigoTentativas", 0);

        String imagem = getServletContext().getRealPath("/assets/img/email-senha.png");
        EmailUtil.enviarCodigo(usuario.getEmail(), usuario.getNomeCompleto(), codigo,
                "Alguém acabou de entrar na sua conta Venus com a senha certa. Para terminar de entrar, digite o código abaixo na tela do Venus.", 10, imagem);
    }

    private void conferirCodigo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessao = request.getSession(false);
        if (sessao == null || sessao.getAttribute("usuarioPendente") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (System.currentTimeMillis() > (Long) sessao.getAttribute("codigoValidade")) {
            sessao.invalidate();
            response.sendRedirect(request.getContextPath() + "/login?expirado=1");
            return;
        }

        String codigo = request.getParameter("codigo");
        if (codigo == null || !codigo.trim().equals(sessao.getAttribute("codigoLogin"))) {
            int tentativas = (Integer) sessao.getAttribute("codigoTentativas") + 1;
            if (tentativas >= 5) {
                sessao.invalidate();
                response.sendRedirect(request.getContextPath() + "/login?expirado=1");
                return;
            }
            sessao.setAttribute("codigoTentativas", tentativas);
            request.setAttribute("erro", "Código incorreto. Confira o e-mail e tente de novo.");
            request.getRequestDispatcher("/WEB-INF/views/verificar_codigo.jsp").forward(request, response);
            return;
        }

        Usuario usuario = (Usuario) sessao.getAttribute("usuarioPendente");
        sessao.removeAttribute("usuarioPendente");
        sessao.removeAttribute("codigoLogin");
        sessao.removeAttribute("codigoValidade");
        sessao.removeAttribute("codigoTentativas");

        request.changeSessionId();
        sessao.setAttribute("usuarioLogado", usuario);
        response.sendRedirect(request.getContextPath() + "/dashboard");
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
