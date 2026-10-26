package com.exemplo.servlet;

import com.exemplo.dao.PerfilPeleDAO;
import com.exemplo.dao.UsuarioDAO;
import com.exemplo.model.PerfilPele;
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

@WebServlet("/perfisPele")
public class PerfilPeleServlet extends HttpServlet {

    private final PerfilPeleDAO perfilPeleDAO = new PerfilPeleDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private static final String MSG_TIPO_INVALIDO =
            "Tipo de pele inválido. Escolha Seca, Oleosa, Sensível, Mista ou Normal.";
    private static final String MSG_ESCALA_INVALIDA =
            "Sensibilidade e oleosidade devem ficar entre 1 e 10.";

    private String valor(String texto) {
        return texto == null ? "" : texto.trim();
    }

    // Cadastro
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String sensibilidadeTexto = request.getParameter("sensibilidade");
        String tipoPele = valor(request.getParameter("tipoPele"));
        String nivelOleosidadeTexto = request.getParameter("nivelOleosidade");
        String idUsuarioTexto = request.getParameter("idUsuario");

        String erro = null;

        if (idUsuarioTexto == null || idUsuarioTexto.isBlank()) {
            erro = "Selecione o usuário.";
        } else if (sensibilidadeTexto == null || sensibilidadeTexto.isBlank()
                || tipoPele.isEmpty()
                || nivelOleosidadeTexto == null || nivelOleosidadeTexto.isBlank()) {
            erro = "Preencha todos os campos obrigatórios.";
        } else if (!Validador.tipoPeleValido(tipoPele)) {
            erro = MSG_TIPO_INVALIDO;
        } else {
            try {
                int sensibilidade = Integer.parseInt(sensibilidadeTexto.trim());
                int nivelOleosidade = Integer.parseInt(nivelOleosidadeTexto.trim());
                int idUsuario = Integer.parseInt(idUsuarioTexto.trim());

                if (!Validador.escalaValida(sensibilidade) || !Validador.escalaValida(nivelOleosidade)) {
                    erro = MSG_ESCALA_INVALIDA;
                } else if (perfilPeleDAO.existePerfilParaUsuario(idUsuario)) {
                    erro = "Este usuário já possui um perfil de pele cadastrado.";
                } else {
                    PerfilPele perfilPele = new PerfilPele(sensibilidade, tipoPele, nivelOleosidade, idUsuario);

                    if (perfilPeleDAO.cadastrarPerfilPele(perfilPele)) {
                        response.sendRedirect(request.getContextPath() + "/perfisPele?idUsuario=" + idUsuario);
                        return;
                    }
                    erro = "Não foi possível cadastrar o perfil de pele.";
                }
            } catch (NumberFormatException nfe) {
                erro = "Dados inválidos. Verifique sensibilidade, oleosidade e usuário.";
            } catch (SQLException sqle) {
                String estado = sqle.getSQLState();
                if ("23503".equals(estado)) {
                    erro = "Usuário não existe.";
                } else if (estado != null && estado.startsWith("23")) {
                    erro = "Algum dado não foi aceito pelo banco.";
                } else {
                    throw new ServletException("Erro ao cadastrar perfil de pele.", sqle);
                }
            }
        }

        // único forward: só chega aqui quando houve erro
        try {
            request.setAttribute("usuarios", usuarioDAO.read());
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar usuários.", sqle);
        }
        request.setAttribute("erro", erro);
        request.getRequestDispatcher("/WEB-INF/views/cadastro_perfil_pele.jsp").forward(request, response);
    }

    // Consulta
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");
        String idTexto = request.getParameter("id");
        String idUsuarioTexto = request.getParameter("idUsuario");

        try {
            if ("novo".equals(acao)) {
                request.setAttribute("usuarios", usuarioDAO.read());
                request.getRequestDispatcher("/WEB-INF/views/cadastro_perfil_pele.jsp").forward(request, response);
                return;
            }

            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                PerfilPele perfilPele = perfilPeleDAO.readById(id);

                if (perfilPele == null) {
                    response.sendRedirect(request.getContextPath() + "/perfisPele");
                    return;
                }

                request.setAttribute("perfilPele", perfilPele);
                request.getRequestDispatcher("/WEB-INF/views/editar_perfil_pele.jsp").forward(request, response);
                return;
            }

            ArrayList<PerfilPele> lista = new ArrayList<>();

            if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                PerfilPele perfilUsuario = perfilPeleDAO.readByIdUsuario(Integer.parseInt(idUsuarioTexto));
                if (perfilUsuario != null) {
                    lista.add(perfilUsuario);
                }
            } else {
                lista = perfilPeleDAO.read();
            }

            request.setAttribute("perfisPele", lista);
            request.getRequestDispatcher("/WEB-INF/views/lista_perfis_pele.jsp").forward(request, response);

        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/perfisPele");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar perfis de pele.", sqle);
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

            if (!json.has("idPerfilPele") || !json.has("sensibilidade")
                    || !json.has("tipoPele") || !json.has("nivelOleosidade")) {
                response.setStatus(400);
                response.getWriter().write("Dados incompletos.");
                return;
            }

            int id = Integer.parseInt(json.get("idPerfilPele").getAsString());
            int sensibilidade = Integer.parseInt(json.get("sensibilidade").getAsString());
            String tipoPele = valor(json.get("tipoPele").getAsString());
            int nivelOleosidade = Integer.parseInt(json.get("nivelOleosidade").getAsString());

            if (tipoPele.isEmpty()) {
                response.setStatus(400);
                response.getWriter().write("Informe o tipo de pele.");
                return;
            }

            if (!Validador.tipoPeleValido(tipoPele)) {
                response.setStatus(400);
                response.getWriter().write(MSG_TIPO_INVALIDO);
                return;
            }

            if (!Validador.escalaValida(sensibilidade) || !Validador.escalaValida(nivelOleosidade)) {
                response.setStatus(400);
                response.getWriter().write(MSG_ESCALA_INVALIDA);
                return;
            }

            PerfilPele perfilPele = new PerfilPele(id, sensibilidade, tipoPele, nivelOleosidade);
            int linhas = perfilPeleDAO.alterarValores(perfilPele);

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Perfil de pele não encontrado.");
            }
        } catch (JsonParseException | IllegalStateException
                 | UnsupportedOperationException | NumberFormatException e) {
            response.setStatus(400);
            response.getWriter().write("Dados inválidos.");
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                response.setStatus(400);
                response.getWriter().write("Algum dado não foi aceito pelo banco.");
            } else {
                throw new ServletException("Erro ao atualizar o perfil de pele.", sqle);
            }
        }
    }

    // Exclusão
    // Exclui pelo ID do perfil ou pelo usuário quando o ID do perfil não é informado.
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
                linhas = perfilPeleDAO.deleteById(Integer.parseInt(idTexto));
            } else if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                linhas = perfilPeleDAO.deleteByIdUsuario(Integer.parseInt(idUsuarioTexto));
            } else {
                response.setStatus(400);
                response.getWriter().write("Id não informado.");
                return;
            }

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Perfil de pele não encontrado.");
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(400);
            response.getWriter().write("Id inválido.");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao excluir o perfil de pele.", sqle);
        }
    }
}
