package com.exemplo.paginas;

import com.exemplo.dao.UsuarioDAO;
import com.exemplo.model.Usuario;
import com.exemplo.util.EmailUtil;
import com.exemplo.util.SenhaUtil;
import com.exemplo.util.Validador;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet({"/sair", "/esqueceu-a-senha", "/nova-senha", "/senha-alterada"})
public class AcessoServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private static final Map<String, String> CODIGOS = new ConcurrentHashMap<>();
    private static final Map<String, Long> VALIDADE = new ConcurrentHashMap<>();
    private static final Map<String, Integer> TENTATIVAS = new ConcurrentHashMap<>();
    private static final long QUINZE_MINUTOS = 15 * 60 * 1000;
    private static final SecureRandom SORTEIO = new SecureRandom();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        switch (request.getServletPath()) {
            case "/sair":
                HttpSession sessao = request.getSession(false);
                if (sessao != null) {
                    sessao.invalidate();
                }
                response.sendRedirect(request.getContextPath() + "/login?saiu=1");
                break;
            case "/esqueceu-a-senha":
                request.getRequestDispatcher("/WEB-INF/views/esqueceu_a_senha.jsp").forward(request, response);
                break;
            case "/nova-senha":
                HttpSession sessaoSenha = request.getSession(false);
                if (sessaoSenha == null || sessaoSenha.getAttribute("emailRedefinir") == null) {
                    response.sendRedirect(request.getContextPath() + "/esqueceu-a-senha");
                    return;
                }
                request.getRequestDispatcher("/WEB-INF/views/nova_senha.jsp").forward(request, response);
                break;
            default:
                request.getRequestDispatcher("/WEB-INF/views/senha_alterada.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if ("/esqueceu-a-senha".equals(request.getServletPath())) {
            enviarCodigo(request, response);
        } else if ("/nova-senha".equals(request.getServletPath())) {
            salvarNovaSenha(request, response);
        } else {
            response.sendError(405);
        }
    }

    private void enviarCodigo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        email = email == null ? "" : email.trim().toLowerCase();
        request.setAttribute("email", email);

        if (!Validador.emailValido(email)) {
            request.setAttribute("erro", "Digite um e-mail válido.");
            request.getRequestDispatcher("/WEB-INF/views/esqueceu_a_senha.jsp").forward(request, response);
            return;
        }

        if (!EmailUtil.configurado()) {
            System.out.println("EMAIL_REMETENTE ou EMAIL_SENHA_APP faltando no .env");
            request.setAttribute("erro", "Não foi possível enviar o e-mail agora. Tente novamente mais tarde.");
            request.getRequestDispatcher("/WEB-INF/views/esqueceu_a_senha.jsp").forward(request, response);
            return;
        }

        try {
            Usuario usuario = usuarioDAO.readByEmail(email);

            if (usuario == null) {
                request.setAttribute("erro", "Não existe conta com esse e-mail. Confira se digitou certo ou crie uma conta.");
                request.getRequestDispatcher("/WEB-INF/views/esqueceu_a_senha.jsp").forward(request, response);
                return;
            }

            String codigo = String.format("%06d", SORTEIO.nextInt(1000000));
            CODIGOS.put(email, codigo);
            VALIDADE.put(email, System.currentTimeMillis() + QUINZE_MINUTOS);
            TENTATIVAS.put(email, 0);

            String imagem = getServletContext().getRealPath("/assets/img/email-senha.png");
            EmailUtil.enviarCodigo(usuario.getEmail(), usuario.getNomeCompleto(), codigo,
                    "Recebemos um pedido para redefinir a senha da sua conta Venus. Use o código abaixo na tela do Venus para criar uma senha nova.", 15, imagem);
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar o usuário.", sqle);
        } catch (MessagingException me) {
            System.out.println("Erro ao enviar e-mail: " + me.getMessage());
            request.setAttribute("erro", "Não foi possível enviar o e-mail agora. Tente novamente mais tarde.");
            request.getRequestDispatcher("/WEB-INF/views/esqueceu_a_senha.jsp").forward(request, response);
            return;
        }

        request.getSession().setAttribute("emailRedefinir", email);
        response.sendRedirect(request.getContextPath() + "/nova-senha");
    }

    private void salvarNovaSenha(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessao = request.getSession(false);
        String email = sessao == null ? null : (String) sessao.getAttribute("emailRedefinir");
        if (email == null) {
            response.sendRedirect(request.getContextPath() + "/esqueceu-a-senha");
            return;
        }

        String codigo = request.getParameter("codigo");
        String senha = request.getParameter("senha");
        String confirmacao = request.getParameter("confirmacao");

        if (!CODIGOS.containsKey(email) || System.currentTimeMillis() > VALIDADE.get(email)) {
            apagarCodigo(email);
            sessao.removeAttribute("emailRedefinir");
            response.sendRedirect(request.getContextPath() + "/esqueceu-a-senha?expirado=1");
            return;
        }

        String erro = null;
        if (codigo == null || !codigo.trim().equals(CODIGOS.get(email))) {
            int tentativas = TENTATIVAS.get(email) + 1;
            TENTATIVAS.put(email, tentativas);
            if (tentativas >= 5) {
                apagarCodigo(email);
                sessao.removeAttribute("emailRedefinir");
                response.sendRedirect(request.getContextPath() + "/esqueceu-a-senha?expirado=1");
                return;
            }
            erro = "Código incorreto. Confira o e-mail e tente de novo.";
        } else if (!Validador.senhaValida(senha)) {
            erro = "A senha precisa ter pelo menos 6 caracteres.";
        } else if (!senha.equals(confirmacao)) {
            erro = "As senhas devem ser iguais.";
        }

        if (erro != null) {
            request.setAttribute("erro", erro);
            request.getRequestDispatcher("/WEB-INF/views/nova_senha.jsp").forward(request, response);
            return;
        }

        try {
            Usuario usuario = usuarioDAO.readByEmail(email);
            usuarioDAO.trocarSenha(usuario.getIdUsuario(), SenhaUtil.gerarHash(senha));
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao trocar a senha.", sqle);
        }

        apagarCodigo(email);
        sessao.removeAttribute("emailRedefinir");

        response.sendRedirect(request.getContextPath() + "/senha-alterada");
    }

    private void apagarCodigo(String email) {
        CODIGOS.remove(email);
        VALIDADE.remove(email);
        TENTATIVAS.remove(email);
    }
}
