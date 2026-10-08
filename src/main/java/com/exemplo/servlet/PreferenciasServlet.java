package com.exemplo.servlet;

import com.exemplo.dao.PreferenciasDAO;
import com.exemplo.model.Preferencias;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/preferencias")
public class PreferenciasServlet extends HttpServlet {

    private final PreferenciasDAO preferenciasDAO = new PreferenciasDAO();

    //create
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String faixaPreco = request.getParameter("faixa_preco");
        String restricoesDieta = request.getParameter("restricoes_dieta");
        String categoriaPref = request.getParameter("categoria_pref");
        String marcasFav = request.getParameter("marcas_fav");
        String idUsuarioTexto = request.getParameter("id_usuario");

        //igual ao produto sobre ehVegano
        boolean prefereVegano = request.getParameter("prefere_vegano") != null;

        if (idUsuarioTexto == null || idUsuarioTexto.isBlank()) {
            request.setAttribute("erro", "Informe o usuário das preferências.");
            request.getRequestDispatcher("/cadastro_preferencias.jsp").forward(request, response);
            return;
        }

        try {
            int idUsuario = Integer.parseInt(idUsuarioTexto.trim());

            Preferencias preferencias = new Preferencias(faixaPreco, prefereVegano, restricoesDieta, categoriaPref, marcasFav, idUsuario);

            if (preferenciasDAO.cadastrarPreferencias(preferencias)) {
                response.sendRedirect(request.getContextPath() + "/preferencias");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar as preferências.");
                request.getRequestDispatcher("/cadastro_preferencias.jsp").forward(request, response);
            }
        } catch (NumberFormatException nfe) {
            request.setAttribute("erro", "Dados inválidos. Selecione o usuário e tente novamente.");
            request.getRequestDispatcher("/cadastro_preferencias.jsp").forward(request, response);
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                request.setAttribute("erro", "Usuário não existe ou já possui preferências cadastradas.");
                request.getRequestDispatcher("/cadastro_preferencias.jsp").forward(request, response);
            } else {
                throw new ServletException("Erro ao cadastrar preferências.", sqle);
            }
        }
    }

    //read
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{

        String idTexto= request.getParameter("id");
        String idUsuarioTexto = request.getParameter("idUsuario");
        String faixaPrecoBusca = request.getParameter("faixaPreco");
        String marcaBusca = request.getParameter("marca");

        try {
            if (idTexto != null && !idTexto.isBlank()){
                int id = Integer.parseInt(idTexto);
                Preferencias preferencias = preferenciasDAO.readById(id);

                if (preferencias == null){
                    response.sendRedirect(request.getContextPath()+"/preferencias");
                    return;
                }

                if (idUsuarioTexto != null && idUsuarioTexto.isBlank()){
                    int idUsuario = Integer.parseInt(idUsuarioTexto);
                    ArrayList<Preferencias> listaUsuarios = preferenciasDAO.readByIdUsuario(idUsuario);

                    request.setAttribute("listaPreferencias", listaUsuarios);
                    request.getRequestDispatcher("/lista_preferencias.jsp").forward(request, response);
                    return;
                }

                if (faixaPrecoBusca!= null && !faixaPrecoBusca.isBlank()){
                    ArrayList<Preferencias> listaPrecos = preferenciasDAO.readByPreco(faixaPrecoBusca);

                    request.setAttribute("listaPreferencias", listaPrecos);
                    request.getRequestDispatcher("/lista_preferencias.jsp").forward(request, response);
                    return;
                }

                ArrayList<Preferencias> listaPreferencias = preferenciasDAO.read();
                request.setAttribute("listaPreferencias", listaPreferencias);
                request.getRequestDispatcher("/lista_preferencias.jsp").forward(request, response);
            }
        }catch (NumberFormatException nfe){
            response.sendRedirect(request.getContextPath()+"/preferencias");
        }catch (SQLException sqle){
            throw new ServletException("Erro ao buscar preferências.", sqle);
        }
    }
}
