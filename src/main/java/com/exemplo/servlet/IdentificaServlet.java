package com.exemplo.servlet;

import com.exemplo.dao.AnaliseDAO;
import com.exemplo.dao.IdentificaDAO;
import com.exemplo.dao.IngredientesDAO;
import com.exemplo.model.Identifica;
import com.exemplo.util.Validador;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

@WebServlet("/identificas")
public class IdentificaServlet extends HttpServlet {

    private final IdentificaDAO identificaDAO = new IdentificaDAO();
    private final AnaliseDAO analiseDAO = new AnaliseDAO();
    private final IngredientesDAO ingredientesDAO = new IngredientesDAO();

    private void abrirCadastro(HttpServletRequest request, HttpServletResponse response, String erro)
            throws ServletException, IOException {
        try {
            if (erro != null) {
                request.setAttribute("erro", erro);
            }
            request.setAttribute("analises", analiseDAO.read());
            request.setAttribute("ingredientes", ingredientesDAO.read());
            request.getRequestDispatcher("/WEB-INF/views/cadastro_identifica.jsp").forward(request, response);
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao carregar análises e ingredientes.", sqle);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idAnaliseTexto = request.getParameter("idAnalise");
        String idIngredienteTexto = request.getParameter("idIngrediente");

        if (!Validador.idValido(idAnaliseTexto) || !Validador.idValido(idIngredienteTexto)) {
            abrirCadastro(request, response, "Selecione a análise e o ingrediente.");
            return;
        }

        int idAnalise = Integer.parseInt(idAnaliseTexto.trim());
        int idIngrediente = Integer.parseInt(idIngredienteTexto.trim());

        try {
            if (analiseDAO.readById(idAnalise) == null || ingredientesDAO.readById(idIngrediente) == null) {
                abrirCadastro(request, response, "A análise ou o ingrediente não existe.");
                return;
            }
            identificaDAO.cadastrarIdentifica(new Identifica(idIngrediente, idAnalise));
            response.sendRedirect(request.getContextPath() + "/identificas?idAnalise=" + idAnalise);
        } catch (SQLException sqle) {
            if ("23505".equals(sqle.getSQLState())) {
                abrirCadastro(request, response, "Esse ingrediente já foi identificado nesta análise.");
            } else {
                throw new ServletException("Erro ao cadastrar ingrediente identificado.", sqle);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idAnaliseTexto = request.getParameter("idAnalise");
        String idIngredienteTexto = request.getParameter("idIngrediente");

        try {
            if ("novo".equals(request.getParameter("acao"))) {
                abrirCadastro(request, response, null);
                return;
            }

            if (Validador.idValido(idAnaliseTexto) && Validador.idValido(idIngredienteTexto)) {
                Identifica identifica = identificaDAO.readByChave(
                        Integer.parseInt(idAnaliseTexto.trim()), Integer.parseInt(idIngredienteTexto.trim()));
                if (identifica == null) {
                    response.sendRedirect(request.getContextPath() + "/identificas");
                    return;
                }
                request.setAttribute("identifica", identifica);
                request.setAttribute("ingredientes", ingredientesDAO.read());
                request.getRequestDispatcher("/WEB-INF/views/editar_identifica.jsp").forward(request, response);
                return;
            }

            if (Validador.idValido(idAnaliseTexto)) {
                request.setAttribute("identificas", identificaDAO.readByIdAnalise(Integer.parseInt(idAnaliseTexto.trim())));
            } else {
                request.setAttribute("identificas", identificaDAO.read());
            }
            request.getRequestDispatcher("/WEB-INF/views/lista_identifica.jsp").forward(request, response);

        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar ingredientes identificados.", sqle);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        String corpo = request.getReader().lines().collect(Collectors.joining());

        try {
            JsonObject json = JsonParser.parseString(corpo).getAsJsonObject();

            if (!json.has("idAnalise") || !json.has("idIngredienteAntigo") || !json.has("idIngrediente")) {
                response.setStatus(400);
                response.getWriter().write("Dados incompletos.");
                return;
            }

            String idAnaliseTexto = json.get("idAnalise").getAsString();
            String idAntigoTexto = json.get("idIngredienteAntigo").getAsString();
            String idNovoTexto = json.get("idIngrediente").getAsString();

            if (!Validador.idValido(idAnaliseTexto) || !Validador.idValido(idAntigoTexto) || !Validador.idValido(idNovoTexto)) {
                response.setStatus(400);
                response.getWriter().write("Selecione o ingrediente.");
                return;
            }

            int idNovo = Integer.parseInt(idNovoTexto.trim());
            if (ingredientesDAO.readById(idNovo) == null) {
                response.setStatus(400);
                response.getWriter().write("O ingrediente não existe.");
                return;
            }

            int linhas = identificaDAO.update(Integer.parseInt(idAnaliseTexto.trim()),
                    Integer.parseInt(idAntigoTexto.trim()), idNovo);

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Registro não encontrado.");
            }
        } catch (JsonParseException | IllegalStateException | UnsupportedOperationException e) {
            response.setStatus(400);
            response.getWriter().write("Dados inválidos.");
        } catch (SQLException sqle) {
            if ("23505".equals(sqle.getSQLState())) {
                response.setStatus(409);
                response.getWriter().write("Esse ingrediente já foi identificado nesta análise.");
            } else {
                throw new ServletException("Erro ao atualizar ingrediente identificado.", sqle);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        String idAnaliseTexto = request.getParameter("idAnalise");
        String idIngredienteTexto = request.getParameter("idIngrediente");

        try {
            int linhas;

            if (Validador.idValido(idAnaliseTexto) && Validador.idValido(idIngredienteTexto)) {
                linhas = identificaDAO.delete(Integer.parseInt(idAnaliseTexto.trim()), Integer.parseInt(idIngredienteTexto.trim()));
            } else if (Validador.idValido(idAnaliseTexto)) {
                linhas = identificaDAO.deleteByIdAnalise(Integer.parseInt(idAnaliseTexto.trim()));
            } else {
                response.setStatus(400);
                response.getWriter().write("Id não informado ou inválido.");
                return;
            }

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Registro não encontrado.");
            }
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao excluir ingrediente identificado.", sqle);
        }
    }
}
