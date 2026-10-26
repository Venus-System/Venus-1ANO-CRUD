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

@WebServlet("/preferencias")
public class PreferenciasServlet extends HttpServlet {

    private static final String MSG_CAMPOS_INVALIDOS =
            "Campo inválido. Faixa de preço: até 20 caracteres; demais campos: até 100 caracteres. "
                    + "Use letras, números e . , ; : ' ( ) / % $ & + -.";

    private final PreferenciasDAO preferenciasDAO = new PreferenciasDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final RecomendaDAO recomendaDAO = new RecomendaDAO();

    private String valor(String texto) {
        return texto == null ? "" : texto.trim();
    }

    private boolean camposValidos(String faixaPreco, String restricoesDieta,
                                  String categoriaPref, String marcasFav) {
        return Validador.preferenciaOpcionalValida(faixaPreco, 20)
                && Validador.preferenciaOpcionalValida(restricoesDieta, 100)
                && Validador.preferenciaOpcionalValida(categoriaPref, 100)
                && Validador.preferenciaOpcionalValida(marcasFav, 100);
    }

    // Cadastro
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String faixaPreco = request.getParameter("faixaPreco");
        String restricoesDieta = request.getParameter("restricoesDieta");
        String categoriaPref = request.getParameter("categoriaPref");
        String marcasFav = request.getParameter("marcasFav");
        String idUsuarioTexto = request.getParameter("idUsuario");

        // O checkbox desmarcado não envia parâmetro e representa false.
        boolean prefereVegano = request.getParameter("prefereVegano") != null;

        String erro = null;

        if (idUsuarioTexto == null || idUsuarioTexto.isBlank()) {
            erro = "Selecione o usuário.";
        } else if (!camposValidos(faixaPreco, restricoesDieta, categoriaPref, marcasFav)) {
            erro = MSG_CAMPOS_INVALIDOS;
        } else {

            try {
                int idUsuario = Integer.parseInt(idUsuarioTexto.trim());

                Preferencias preferencias = new Preferencias(faixaPreco, prefereVegano, restricoesDieta, categoriaPref, marcasFav, idUsuario);

                if (preferenciasDAO.cadastrarPreferencias(preferencias)) {
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
                } else if ("23505".equals(estado)) {
                    erro = "Esta preferência já está cadastrada para o usuário.";
                } else {
                    throw new ServletException("Erro ao cadastrar preferências.", sqle);
                }
            }

            try {
                request.setAttribute("usuarios", usuarioDAO.read());
            } catch (SQLException sqle) {
                throw new ServletException("Erro ao buscar usuários.", sqle);
            }

        }
        request.setAttribute("erro", erro);
        request.getRequestDispatcher("/WEB-INF/views/cadastro_preferencias.jsp").forward(request, response);
    }

    // Consulta
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
                request.setAttribute("usuarios", usuarioDAO.read());
                request.getRequestDispatcher("/WEB-INF/views/cadastro_preferencias.jsp").forward(request, response);
                return;
            }

            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                Preferencias preferencias = preferenciasDAO.readById(id);

                if (preferencias == null) {
                    response.sendRedirect(request.getContextPath() + "/preferencias");
                    return;
                }

                request.setAttribute("preferencia", preferencias);
                request.getRequestDispatcher("/WEB-INF/views/editar_preferencias.jsp").forward(request, response);
                return;
            }

            ArrayList<Preferencias> listaPreferencias = preferenciasDAO.read();
            if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                listaPreferencias = preferenciasDAO.readByIdUsuario(Integer.parseInt(idUsuarioTexto));
            } else if (faixaPrecoBusca != null && !faixaPrecoBusca.isBlank()) {
                listaPreferencias = preferenciasDAO.readByPreco(faixaPrecoBusca.trim());
            } else if (marcaBusca != null && !marcaBusca.isBlank()) {
                listaPreferencias = preferenciasDAO.readByBrand(marcaBusca.trim());
            } else {
                listaPreferencias = preferenciasDAO.read();
            }

            request.setAttribute("listaPreferencias", listaPreferencias);
            request.getRequestDispatcher("/WEB-INF/views/lista_preferencias.jsp").forward(request, response);

        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/preferencias");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar preferências.", sqle);
        }
    }

    // Atualização
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        String corpo = request.getReader().lines().collect(Collectors.joining());

        try {
            JsonObject json = JsonParser.parseString(corpo).getAsJsonObject();
            // A atualização recebe os campos no corpo da requisição em JSON.
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

            if (!camposValidos(faixaPreco, restricoesDieta, categoriaPref, marcasFav)) {
                response.setStatus(400);
                response.getWriter().write(MSG_CAMPOS_INVALIDOS);
                return;
            }

            Preferencias preferencias = new Preferencias(id, faixaPreco, prefereVegano, restricoesDieta, categoriaPref, marcasFav);

            int linhas = preferenciasDAO.update(preferencias);

            if (linhas > 0) {
                recomendaDAO.recomendarPelasPreferencias(preferenciasDAO.readById(id).getIdUsuario());
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

    // Exclusão
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
                Preferencias preferencias = preferenciasDAO.readById(id);
                linhas = preferenciasDAO.deleteById(id);
                if (preferencias != null) {
                    recomendaDAO.recomendarPelasPreferencias(preferencias.getIdUsuario());
                }
            } else if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                int idUsuario = Integer.parseInt(idUsuarioTexto);
                linhas = preferenciasDAO.deleteByIdUsuario(idUsuario);
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
