package com.exemplo.servlet;

import com.exemplo.dao.PerfilCabeloDAO;
import com.exemplo.model.PerfilCabelo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/perfisCabelo")
public class PerfilCabeloSevlet extends HttpServlet {

    private final PerfilCabeloDAO perfilCabeloDAO = new PerfilCabeloDAO();

    //create
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String curvaturaTexto = request.getParameter("curvatura");
        String oleosidadeTexto = request.getParameter("oleosidade");
        String espessuraTexto = request.getParameter("espessura");
        String idUsuarioTexto = request.getParameter("idUsuario");

        if (curvaturaTexto == null || curvaturaTexto.isBlank() ||
                oleosidadeTexto == null || oleosidadeTexto.isBlank() ||
                espessuraTexto == null || espessuraTexto.isBlank() ||
                idUsuarioTexto == null || idUsuarioTexto.isBlank()) {

            request.setAttribute("erro", "Preencha todos os campos obrigatórios.");
            request.getRequestDispatcher("/casdatro_perfil_cabelo.jsp").forward(request, response);
            return;
        }

        try {
            int curvatura = Integer.parseInt(curvaturaTexto);
            int oleosidade = Integer.parseInt(oleosidadeTexto);
            int espessura = Integer.parseInt(espessuraTexto);
            int idUsuario = Integer.parseInt(idUsuarioTexto);

            if (perfilCabeloDAO.existePerfilParaUsuario(idUsuario)) {
                request.setAttribute("erro", "Este usuário já possui um perfil de cabelo cadastrado.");
                request.getRequestDispatcher("/cadastro_perfil_cabelo.jsp").forward(request, response);
                return;
            }

            PerfilCabelo perfilCabelo = new PerfilCabelo(curvatura, oleosidade, espessura, idUsuario);

            if (perfilCabeloDAO.cadastrarPerfilCabelo(perfilCabelo)) {
                response.sendRedirect(request.getContextPath() + "/perfisCabelo");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar o perfil de cabelo.");
                request.getRequestDispatcher("/cadastro_perfil_cabelo.jsp").forward(request, response);
            }
        } catch (NumberFormatException nfe) {
            request.setAttribute("erro", "Dados inválidos. Verifique os valores informados e tente novamente.");
            request.getRequestDispatcher("/cadastro_perfil_cabelo.jsp").forward(request, response);
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                request.setAttribute("erro", "Usuário informado não existe ou já possui perfil de cabelo.");
                request.getRequestDispatcher("/cadastro_perfil_cabelo.jsp").forward(request, response);
            } else {
                throw new ServletException("Erro ao cadastrar perfil de cabelo.", sqle);
            }
        }
    }
}
