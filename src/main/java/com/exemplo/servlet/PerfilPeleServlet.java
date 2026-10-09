package com.exemplo.servlet;


import com.exemplo.dao.PerfilPeleDAO;
import com.exemplo.model.PerfilPele;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.InaccessibleObjectException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/perfisPele")
public class PerfilPeleServlet extends HttpServlet {

    PerfilPeleDAO perfilPeleDAO = new PerfilPeleDAO();

    //create
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, InaccessibleObjectException {

        request.setCharacterEncoding("UTF-8");

        String sensibilidadeTexto = request.getParameter("sensibilidade");
        String tipoPele = request.getParameter("tipo_pele");
        String nivelOleosidadeTexto = request.getParameter("nivel_oleosidade");
        String idUsuarioTexto = request.getParameter("id_usuario");

        if (sensibilidadeTexto == null || sensibilidadeTexto.isBlank() ||
                tipoPele == null || tipoPele.isBlank() ||
                nivelOleosidadeTexto == null || nivelOleosidadeTexto.isBlank() ||
                idUsuarioTexto == null || idUsuarioTexto.isBlank()) {

            request.setAttribute("erro", "Preencha todos os campos obrigatórios.");
            request.getRequestDispatcher("/cadastro_perfil_pele.jsp").forward(request, response);
            return;
        }
        try {
            int sensibilidade = Integer.parseInt(sensibilidadeTexto.trim());
            int nivelOleosidade = Integer.parseInt(nivelOleosidadeTexto.trim());
            int idUsuario = Integer.parseInt(idUsuarioTexto.trim());

            PerfilPele perfilPele = new PerfilPele(sensibilidade, tipoPele.trim(), nivelOleosidade, idUsuario);
            if (perfilPeleDAO.cadastrarPerfilPele(perfilPele)) {
                response.sendRedirect(request.getContextPath() + "/perfispele");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar o perfil de pele.");
                request.getRequestDispatcher("/cadastro_perfil_pele.jsp").forward(request, response);
            }
        } catch (NumberFormatException nfe) {
            request.setAttribute("erro", "Dados inválidos. Verifique sensibilidade, oleosidade e usuário e tente novamente.");
            request.getRequestDispatcher("/cadastro_perfil_pele.jsp").forward(request, response);
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                //violação de restrição --------> 23. Igual aos outros.
                request.setAttribute("erro", "Usuário não existe ou algum dado não foi aceito.");
                request.getRequestDispatcher("/cadastro_perfil_pele.jsp").forward(request, response);
            } else {
                throw new ServletException("Erro ao cadastrar perfil de pele.", sqle);
            }
        }

    }

    //read
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idTexto = request.getParameter("id");
        String idUsuarioTexto = request.getParameter("idUsuario");

        try {
            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                PerfilPele perfilPele = perfilPeleDAO.readById(id);

                if (perfilPele == null) {
                    response.sendRedirect(request.getContextPath() + "/perfisPele");
                    return;
                }

                request.setAttribute("perfisPele", perfilPele);
                request.getRequestDispatcher("/editar_perfil_pele.jsp").forward(request, response);
                return;
            }

            if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                int idUsuario = Integer.parseInt(idUsuarioTexto);
                PerfilPele perfilPeleUsuario = perfilPeleDAO.readByIdUsuario(idUsuario);

                ArrayList<PerfilPele> listaUsuarios = new ArrayList<>();
                if (listaUsuarios != null) {
                    listaUsuarios.add(perfilPeleUsuario);
                }

                request.setAttribute("perfisPele", listaUsuarios);
                request.getRequestDispatcher("/lista_perfis_pele.jsp").forward(request, response);
                return;
            }

            //aqui listará tudo.
            ArrayList<PerfilPele> listaPerfilPele = perfilPeleDAO.read();
            request.setAttribute("perfisPeLe", listaPerfilPele);
            request.getRequestDispatcher("/lista_perfis_pele.jsp").forward(request, response);
        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/perfisPele");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar perfis de pele.", sqle);
        }
    }
}
