package com.exemplo.servlet;

import com.exemplo.dao.AnaliseDAO;
import com.exemplo.dao.PerfilCabeloDAO;
import com.exemplo.dao.PerfilPeleDAO;
import com.exemplo.dao.PreferenciasDAO;
import com.exemplo.dao.ProdutoUsuarioDAO;
import com.exemplo.dao.RecomendaDAO;
import com.exemplo.dao.UsuarioAlergiaDAO;
import com.exemplo.dao.UsuarioDAO;
import com.exemplo.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/usuarioDetalhe")
public class UsuarioDetalheServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final UsuarioAlergiaDAO usuarioAlergiaDAO = new UsuarioAlergiaDAO();
    private final ProdutoUsuarioDAO produtoUsuarioDAO = new ProdutoUsuarioDAO();
    private final PreferenciasDAO preferenciasDAO = new PreferenciasDAO();
    private final PerfilPeleDAO perfilPeleDAO = new PerfilPeleDAO();
    private final PerfilCabeloDAO perfilCabeloDAO = new PerfilCabeloDAO();
    private final AnaliseDAO analiseDAO = new AnaliseDAO();
    private final RecomendaDAO recomendaDAO = new RecomendaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idTexto = request.getParameter("id");
        if (idTexto == null || !idTexto.matches("\\d+")) {
            response.sendRedirect(request.getContextPath() + "/usuarios");
            return;
        }

        try {
            int id = Integer.parseInt(idTexto);
            Usuario usuario = usuarioDAO.readById(id);

            if (usuario == null) {
                response.sendRedirect(request.getContextPath() + "/usuarios");
                return;
            }

            request.setAttribute("usuario", usuario);
            request.setAttribute("alergias", usuarioAlergiaDAO.readByIdUsuario(id));
            request.setAttribute("produtos", produtoUsuarioDAO.readByIdUsuario(id));
            request.setAttribute("recomendas", recomendaDAO.readByIdUsuario(id));
            request.setAttribute("preferencias", preferenciasDAO.readByIdUsuario(id));
            request.setAttribute("perfilPele", perfilPeleDAO.readByIdUsuario(id));
            request.setAttribute("perfilCabelo", perfilCabeloDAO.readByIdUsuario(id));
            request.setAttribute("analises", analiseDAO.readByIdUsuario(id));

            request.getRequestDispatcher("/WEB-INF/views/usuario_detalhe.jsp").forward(request, response);

        } catch (SQLException sqle) {
            throw new ServletException("Erro ao carregar os dados do usuário.", sqle);
        }
    }
}
