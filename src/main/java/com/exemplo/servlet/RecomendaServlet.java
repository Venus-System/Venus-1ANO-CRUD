package com.exemplo.servlet;

import com.exemplo.dao.ProdutoDAO;
import com.exemplo.dao.RecomendaDAO;
import com.exemplo.dao.UsuarioDAO;
import com.exemplo.model.Recomenda;
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

@WebServlet("/recomendas")
public class RecomendaServlet extends HttpServlet {

    private final RecomendaDAO recomendaDAO = new RecomendaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    private void abrirCadastro(HttpServletRequest request, HttpServletResponse response, String erro)
            throws ServletException, IOException {
        try {
            if (erro != null) {
                request.setAttribute("erro", erro);
            }
            request.setAttribute("usuarios", usuarioDAO.read());
            request.setAttribute("produtos", produtoDAO.read());
            request.getRequestDispatcher("/WEB-INF/views/cadastro_recomenda.jsp").forward(request, response);
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao carregar usuários e produtos.", sqle);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idUsuarioTexto = request.getParameter("idUsuario");
        String idProdutoTexto = request.getParameter("idProduto");

        if (!Validador.idValido(idUsuarioTexto) || !Validador.idValido(idProdutoTexto)) {
            abrirCadastro(request, response, "Selecione o usuário e o produto.");
            return;
        }

        int idUsuario = Integer.parseInt(idUsuarioTexto.trim());
        int idProduto = Integer.parseInt(idProdutoTexto.trim());

        try {
            if (usuarioDAO.readById(idUsuario) == null || produtoDAO.readById(idProduto) == null) {
                abrirCadastro(request, response, "O usuário ou o produto não existe.");
                return;
            }
            recomendaDAO.cadastrarRecomenda(new Recomenda(idProduto, idUsuario));
            if ("1".equals(request.getParameter("fixo"))) {
                response.sendRedirect(request.getContextPath() + "/usuarioDetalhe?id=" + idUsuario + "#recomendacoes");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/recomendas?idUsuario=" + idUsuario);
        } catch (SQLException sqle) {
            if ("23505".equals(sqle.getSQLState())) {
                abrirCadastro(request, response, "Esse produto já foi recomendado para este usuário.");
            } else {
                throw new ServletException("Erro ao cadastrar produto recomendado.", sqle);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idUsuarioTexto = request.getParameter("idUsuario");
        String idProdutoTexto = request.getParameter("idProduto");

        try {
            if ("novo".equals(request.getParameter("acao"))) {
                abrirCadastro(request, response, null);
                return;
            }

            if (Validador.idValido(idUsuarioTexto) && Validador.idValido(idProdutoTexto)) {
                Recomenda recomenda = recomendaDAO.readByChave(
                        Integer.parseInt(idUsuarioTexto.trim()), Integer.parseInt(idProdutoTexto.trim()));
                if (recomenda == null) {
                    response.sendRedirect(request.getContextPath() + "/recomendas");
                    return;
                }
                request.setAttribute("recomenda", recomenda);
                request.setAttribute("produtos", produtoDAO.read());
                request.getRequestDispatcher("/WEB-INF/views/editar_recomenda.jsp").forward(request, response);
                return;
            }

            if (Validador.idValido(idUsuarioTexto)) {
                request.setAttribute("recomendas", recomendaDAO.readByIdUsuario(Integer.parseInt(idUsuarioTexto.trim())));
            } else {
                request.setAttribute("recomendas", recomendaDAO.read());
            }
            request.getRequestDispatcher("/WEB-INF/views/lista_recomenda.jsp").forward(request, response);

        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar produtos recomendados.", sqle);
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

            if (!json.has("idUsuario") || !json.has("idProdutoAntigo") || !json.has("idProduto")) {
                response.setStatus(400);
                response.getWriter().write("Dados incompletos.");
                return;
            }

            String idUsuarioTexto = json.get("idUsuario").getAsString();
            String idAntigoTexto = json.get("idProdutoAntigo").getAsString();
            String idNovoTexto = json.get("idProduto").getAsString();

            if (!Validador.idValido(idUsuarioTexto) || !Validador.idValido(idAntigoTexto) || !Validador.idValido(idNovoTexto)) {
                response.setStatus(400);
                response.getWriter().write("Selecione o produto.");
                return;
            }

            int idNovo = Integer.parseInt(idNovoTexto.trim());
            if (produtoDAO.readById(idNovo) == null) {
                response.setStatus(400);
                response.getWriter().write("O produto não existe.");
                return;
            }

            int linhas = recomendaDAO.update(Integer.parseInt(idUsuarioTexto.trim()),
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
                response.getWriter().write("Esse produto já foi recomendado para este usuário.");
            } else {
                throw new ServletException("Erro ao atualizar produto recomendado.", sqle);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        String idUsuarioTexto = request.getParameter("idUsuario");
        String idProdutoTexto = request.getParameter("idProduto");

        try {
            int linhas;

            if (Validador.idValido(idUsuarioTexto) && Validador.idValido(idProdutoTexto)) {
                linhas = recomendaDAO.delete(Integer.parseInt(idUsuarioTexto.trim()), Integer.parseInt(idProdutoTexto.trim()));
            } else if (Validador.idValido(idUsuarioTexto)) {
                linhas = recomendaDAO.deleteByIdUsuario(Integer.parseInt(idUsuarioTexto.trim()));
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
            throw new ServletException("Erro ao excluir produto recomendado.", sqle);
        }
    }
}
