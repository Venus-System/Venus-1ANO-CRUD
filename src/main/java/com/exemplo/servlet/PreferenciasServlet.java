package com.exemplo.servlet;

import com.exemplo.dao.PreferenciasDAO;
import com.exemplo.dao.RecomendaDAO;
import com.exemplo.dao.UsuarioDAO;
import com.exemplo.model.Preferencias;
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
import java.util.ArrayList;
import java.util.stream.Collectors;

// Servlet das preferências: chama o PreferenciasDAO e atualiza as recomendações.
@WebServlet("/preferencias")
public class PreferenciasServlet extends HttpServlet {

    private final PreferenciasDAO preferenciasDAO = new PreferenciasDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final RecomendaDAO recomendaDAO = new RecomendaDAO();

    // Devolve o texto sem espaços nas pontas, ou texto vazio quando vier null.
    private String valor(String texto) {
        return texto == null ? "" : texto.trim();
    }

    //cadastrar
    // Cadastra as preferências e já refaz as recomendações do usuário com base nelas.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String faixaPreco = request.getParameter("faixaPreco");
        String restricoesDieta = request.getParameter("restricoesDieta");
        String categoriaPref = request.getParameter("categoriaPref");
        String marcasFav = request.getParameter("marcasFav");
        String idUsuarioTexto = request.getParameter("idUsuario");

        //igual ao produto sobre ehVegano
        boolean prefereVegano = request.getParameter("prefereVegano") != null;

        String erro = null;

        if (idUsuarioTexto == null || idUsuarioTexto.isBlank()) {
            erro = "Selecione o usuário.";
        } else if (!Validador.preferenciaOpcionalValida(faixaPreco) ||
                !Validador.preferenciaOpcionalValida(restricoesDieta)
                || !Validador.preferenciaOpcionalValida(categoriaPref)
                || !Validador.preferenciaOpcionalValida(marcasFav)) {
            erro = "Campo inválido. Use letras, números e . , ; : ' ( ) / % $ & + - (até 255 caracteres).";
        } else {

            try {
                // Converte o texto recebido em número. Se não for número, cai no NumberFormatException.
                int idUsuario = Integer.parseInt(idUsuarioTexto.trim());

                Preferencias preferencias = new Preferencias(faixaPreco, prefereVegano, restricoesDieta, categoriaPref, marcasFav, idUsuario);

                if (preferenciasDAO.cadastrar(preferencias)) {
                    recomendaDAO.recomendarPelasPreferencias(idUsuario);
                    response.sendRedirect(request.getContextPath() + "/preferencias");
                    return;
                }
                erro = "Não foi possível cadastrar as preferências.";

            } catch (NumberFormatException nfe) {
                erro = "Usuário Inválido.";
            } catch (SQLException sqle) {
                String estado = sqle.getSQLState();
                if ("23503".equals(estado)) {
                    erro = "Usuário não existe.";
                // 23505 é o código do banco para registro repetido.
                } else if ("23505".equals(estado)) {
                    erro = "Esta preferência já está cadastrada para o usuário.";
                } else {
                    throw new ServletException("Erro ao cadastrar preferências.", sqle);
                }
            }

            try {
                request.setAttribute("usuarios", usuarioDAO.listar());
            } catch (SQLException sqle) {
                throw new ServletException("Erro ao buscar usuários.", sqle);
            }

        }
        request.setAttribute("erro", erro);
        request.getRequestDispatcher("/WEB-INF/views/cadastro_preferencias.jsp").forward(request, response);
    }

    //listar
    // Abre as telas e mostra a lista com filtros: por usuário, por faixa de preço ou por marca.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idTexto = request.getParameter("id");
        String idUsuarioTexto = request.getParameter("idUsuario");
        String faixaPrecoBusca = request.getParameter("faixaPreco");
        String marcaBusca = request.getParameter("marca");
        String acao = request.getParameter("acao");

        try {
            if ("novo".equals(acao)) {
                request.setAttribute("usuarios", usuarioDAO.listar());
                request.getRequestDispatcher("/WEB-INF/views/cadastro_preferencias.jsp").forward(request, response);
                return;
            }

            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                Preferencias preferencias = preferenciasDAO.listarPorId(id);

                if (preferencias == null) {
                    response.sendRedirect(request.getContextPath() + "/preferencias");
                    return;
                }

                request.setAttribute("preferencia", preferencias);
                request.getRequestDispatcher("/WEB-INF/views/editar_preferencias.jsp").forward(request, response);
                return;
            }

            ArrayList<Preferencias> listaPreferencias = preferenciasDAO.listar();
            if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                listaPreferencias = preferenciasDAO.listarPorIdUsuario(Integer.parseInt(idUsuarioTexto));
            } else if (faixaPrecoBusca != null && !faixaPrecoBusca.isBlank()) {
                listaPreferencias = preferenciasDAO.listarPorPreco(faixaPrecoBusca.trim());
            } else if (marcaBusca != null && !marcaBusca.isBlank()) {
                listaPreferencias = preferenciasDAO.listarPorMarca(marcaBusca.trim());
            } else {
                listaPreferencias = preferenciasDAO.listar();
            }

            request.setAttribute("listaPreferencias", listaPreferencias);
            request.getRequestDispatcher("/WEB-INF/views/lista_preferencias.jsp").forward(request, response);

        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/preferencias");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar preferências.", sqle);
        }
    }

    //alterar
    // Altera as preferências com os dados em JSON e refaz as recomendações do usuário.
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
            //formato de texto para representar dados. É escolhido o formato do corpo da requisição. Json é simples, legível e funciona em qualquer linguagem.
            if (!json.has("idPreferencias") || !json.has("faixaPreco")
                    || !json.has("prefereVegano") || !json.has("restricoesDieta")
                    || !json.has("categoriaPref") || !json.has("marcasFav")) {

                response.setStatus(400);
                response.getWriter().write("Dados incompletos.");
                return;
            }

            int id = Integer.parseInt(json.get("idPreferencias").getAsString());
            boolean prefereVegano = json.get("prefereVegano").getAsBoolean();
            String faixaPreco = json.get("faixaPreco").getAsString();
            String restricoesDieta = json.get("restricoesDieta").getAsString();
            String categoriaPref = json.get("categoriaPref").getAsString();
            String marcasFav = json.get("marcasFav").getAsString();

            if (!Validador.preferenciaOpcionalValida(faixaPreco)
                    || !Validador.preferenciaOpcionalValida(restricoesDieta)
                    || !Validador.preferenciaOpcionalValida(categoriaPref)
                    || !Validador.preferenciaOpcionalValida(marcasFav)) {
                response.setStatus(400);
                response.getWriter().write("Campo inválido. Use letras, números e . , ; : ' ( ) / % $ & + - (até 255 caracteres).");
                return;
            }

            Preferencias preferencias = new Preferencias(id, faixaPreco, prefereVegano, restricoesDieta, categoriaPref, marcasFav);

            int linhas = preferenciasDAO.alterar(preferencias);

            if (linhas > 0) {
                recomendaDAO.recomendarPelasPreferencias(preferenciasDAO.listarPorId(id).getIdUsuario());
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Preferências não encontradas.");
            }
        } catch (JsonParseException | IllegalStateException | UnsupportedOperationException | NumberFormatException e) {
            response.setStatus(400);
            response.getWriter().write("Dados inválidos.");
        } catch (SQLException sqle) {
            if ("23505".equals(sqle.getSQLState())) {
                response.setStatus(409);
                response.getWriter().write("Esta preferência já está cadastrada para o usuário.");
            } else {
                throw new ServletException("Erro ao atualizar as preferências.", sqle);
            }
        }
    }

    //deletar
    // Deleta uma preferência ou todas de um usuário e refaz as recomendações.
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        String idTexto = request.getParameter("id");
        String idUsuarioTexto = request.getParameter("idUsuario");

        try {
            int linhas;

            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                Preferencias preferencias = preferenciasDAO.listarPorId(id);
                linhas = preferenciasDAO.deletarPorId(id);
                if (preferencias != null) {
                    recomendaDAO.recomendarPelasPreferencias(preferencias.getIdUsuario());
                }
            } else if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                int idUsuario = Integer.parseInt(idUsuarioTexto);
                linhas = preferenciasDAO.deletarPorIdUsuario(idUsuario);
                recomendaDAO.recomendarPelasPreferencias(idUsuario);
            } else {
                response.setStatus(400);
                response.getWriter().write("Id não informado.");
                return;
            }

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Preferências não encontradas.");
            }

        } catch (NumberFormatException nfe) {
            response.setStatus(400);
            response.getWriter().write("Id inválido.");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao excluir as preferências.", sqle);
        }

    }

}
