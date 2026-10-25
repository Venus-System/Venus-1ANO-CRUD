package com.exemplo.servlet;

import com.exemplo.dao.AnaliseDAO;
import com.exemplo.dao.IngredientesDAO;
import com.exemplo.dao.ProdutoDAO;
import com.exemplo.dao.UsuarioDAO;
import com.exemplo.model.Analise;
import com.exemplo.model.Ingredientes;
import com.exemplo.model.Produto;
import com.exemplo.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final IngredientesDAO ingredientesDAO = new IngredientesDAO();
    private final AnaliseDAO analiseDAO = new AnaliseDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            ArrayList<Usuario> usuarios = usuarioDAO.read();
            ArrayList<Produto> produtos = produtoDAO.read();
            ArrayList<Ingredientes> ingredientes = ingredientesDAO.read();
            ArrayList<Analise> analises = analiseDAO.read();

            int seguros = 0;
            int atencao = 0;
            int altoRisco = 0;
            for (Ingredientes ingrediente : ingredientes) {
                if (ingrediente.getNivelPerigo() <= 2) {
                    seguros++;
                } else if (ingrediente.getNivelPerigo() == 3) {
                    atencao++;
                } else {
                    altoRisco++;
                }
            }

            ArrayList<Produto> ultimosProdutos = new ArrayList<>();
            for (int i = produtos.size() - 1; i >= 0 && ultimosProdutos.size() < 5; i--) {
                ultimosProdutos.add(produtos.get(i));
            }

            request.setAttribute("totalUsuarios", usuarios.size());
            request.setAttribute("totalProdutos", produtos.size());
            request.setAttribute("totalIngredientes", ingredientes.size());
            request.setAttribute("totalAnalises", analises.size());
            request.setAttribute("ingredientesSeguros", seguros);
            request.setAttribute("ingredientesAtencao", atencao);
            request.setAttribute("ingredientesAltoRisco", altoRisco);
            request.setAttribute("ultimosProdutos", ultimosProdutos);

            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);

        } catch (SQLException sqle) {
            throw new ServletException("Erro ao carregar o dashboard.", sqle);
        }
    }
}
