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

// Servlet da tabela identifica: liga as análises aos ingredientes identificados.
@WebServlet("/identificas")
public class IdentificaServlet extends HttpServlet {

    private final IdentificaDAO identificaDAO = new IdentificaDAO();
    private final AnaliseDAO analiseDAO = new AnaliseDAO();
    private final IngredientesDAO ingredientesDAO = new IngredientesDAO();

    // Carrega as análises e os ingredientes para os selects e abre o formulário de cadastro.
    private void abrirCadastro(HttpServletRequest request, HttpServletResponse response, String erro)
            throws ServletException, IOException {
        try {
            if (erro != null) {
                request.setAttribute("erro", erro);
            }
            request.setAttribute("analises", analiseDAO.listar());
            request.setAttribute("ingredientes", ingredientesDAO.listar());
            request.getRequestDispatcher("/WEB-INF/views/cadastro_identifica.jsp").forward(request, response);
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao carregar análises e ingredientes.", sqle);
        }
    }

    // Confere se a análise e o ingrediente existem e cadastra a ligação entre eles.
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

        // Converte o texto recebido em número. Se não for número, cai no NumberFormatException.
        int idAnalise = Integer.parseInt(idAnaliseTexto.trim());
        int idIngrediente = Integer.parseInt(idIngredienteTexto.trim());

        try {
            if (analiseDAO.listarPorId(idAnalise) == null || ingredientesDAO.listarPorId(idIngrediente) == null) {
                abrirCadastro(request, response, "A análise ou o ingrediente não existe.");
                return;
            }
            identificaDAO.cadastrar(new Identifica(idIngrediente, idAnalise));
            response.sendRedirect(request.getContextPath() + "/identificas?idAnalise=" + idAnalise);
        } catch (SQLException sqle) {
            // 23505 é o código do banco para registro repetido.
            if ("23505".equals(sqle.getSQLState())) {
                abrirCadastro(request, response, "Esse ingrediente já foi identificado nesta análise.");
            } else {
                throw new ServletException("Erro ao cadastrar ingrediente identificado.", sqle);
            }
        }
    }

    // Abre as telas: cadastro, alteração ou lista (de todas ou só de uma análise).
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
                Identifica identifica = identificaDAO.listarPorChave(
                        Integer.parseInt(idAnaliseTexto.trim()), Integer.parseInt(idIngredienteTexto.trim()));
                if (identifica == null) {
                    response.sendRedirect(request.getContextPath() + "/identificas");
                    return;
                }
                request.setAttribute("identifica", identifica);
                request.setAttribute("ingredientes", ingredientesDAO.listar());
                request.getRequestDispatcher("/WEB-INF/views/editar_identifica.jsp").forward(request, response);
                return;
            }

            if (Validador.idValido(idAnaliseTexto)) {
                request.setAttribute("identificas", identificaDAO.listarPorIdAnalise(Integer.parseInt(idAnaliseTexto.trim())));
            } else {
                request.setAttribute("identificas", identificaDAO.listar());
            }
            request.getRequestDispatcher("/WEB-INF/views/lista_identifica.jsp").forward(request, response);

        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar ingredientes identificados.", sqle);
        }
    }

    // Troca o ingrediente ligado a uma análise com os dados em JSON.
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        // Lê o corpo da requisição: no PUT os dados chegam em JSON, e não pelo getParameter.
        String corpo = request.getReader().lines().collect(Collectors.joining());

        try {
            // Transforma o texto JSON em um objeto, para ler cada campo pelo nome.
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
            if (ingredientesDAO.listarPorId(idNovo) == null) {
                response.setStatus(400);
                response.getWriter().write("O ingrediente não existe.");
                return;
            }

            int linhas = identificaDAO.alterar(Integer.parseInt(idAnaliseTexto.trim()),
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

    // Deleta uma ligação ou todas as ligações de uma análise.
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
                linhas = identificaDAO.deletar(Integer.parseInt(idAnaliseTexto.trim()), Integer.parseInt(idIngredienteTexto.trim()));
            } else if (Validador.idValido(idAnaliseTexto)) {
                linhas = identificaDAO.deletarPorIdAnalise(Integer.parseInt(idAnaliseTexto.trim()));
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
