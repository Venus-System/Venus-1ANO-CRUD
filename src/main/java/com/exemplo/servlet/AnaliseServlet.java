package com.exemplo.servlet;

import com.exemplo.dao.AnaliseDAO;
import com.exemplo.dao.UsuarioDAO;
import com.exemplo.model.Analise;
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

// Servlet das análises: recebe as requisições da tela e chama o AnaliseDAO.
@WebServlet("/analises")
public class AnaliseServlet extends HttpServlet {

    private final AnaliseDAO analiseDAO = new AnaliseDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private static final String MSG_RESUMO_INVALIDO = "O resumo deve ter no máximo 500 caracteres.";
    private static final String MSG_PONTUACAO_INVALIDA = "A pontuação deve ficar entre 0 e 100.";

    //cadastrar
    // Cadastra uma análise para o usuário escolhido e volta para a lista dele.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String resumoResultado = request.getParameter("resumoResultado");
        String pontuacaoTexto = request.getParameter("pontuacao");
        String idUsuarioTexto = request.getParameter("idUsuario");

        String erro = null;

        if (idUsuarioTexto == null || idUsuarioTexto.isBlank()) {
            erro = "Selecione o usuário.";
        } else if (resumoResultado == null || resumoResultado.isBlank()
                || pontuacaoTexto == null || pontuacaoTexto.isBlank()) {
            erro = "Preencha todos os campos obrigatórios.";
        } else if (!Validador.resumoAnaliseValido(resumoResultado)) {
            erro = MSG_RESUMO_INVALIDO;
        } else {
            try {
                // Converte o texto recebido em número. Se não for número, cai no NumberFormatException.
                int pontuacao = Integer.parseInt(pontuacaoTexto.trim());
                int idUsuario = Integer.parseInt(idUsuarioTexto.trim());

                if (!Validador.pontuacaoAnaliseValida(pontuacao)) {
                    erro = MSG_PONTUACAO_INVALIDA;
                } else {
                    Analise analise = new Analise(resumoResultado.trim(), pontuacao, idUsuario);

                    if (analiseDAO.cadastrar(analise)) {
                        response.sendRedirect(request.getContextPath() + "/analises?idUsuario=" + idUsuario);
                        return;
                    }
                    erro = "Não foi possível cadastrar a análise.";
                }
            } catch (NumberFormatException nfe) {
                erro = "Dados inválidos. Verifique a pontuação e o usuário.";
            } catch (SQLException sqle) {
                String estado = sqle.getSQLState();
                if ("23503".equals(estado)) {
                    erro = "Usuário não existe.";
                // Códigos que começam com 23 são erros de regra do banco (chave estrangeira, UNIQUE ou CHECK).
                } else if (estado != null && estado.startsWith("23")) {
                    erro = "Algum dado não foi aceito pelo banco.";
                } else {
                    throw new ServletException("Erro ao cadastrar a análise.", sqle);
                }
            }
        }

        // único forward: só chega aqui quando houve erro
        try {
            request.setAttribute("usuarios", usuarioDAO.listar());
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar usuários.", sqle);
        }
        request.setAttribute("erro", erro);
        request.getRequestDispatcher("/WEB-INF/views/cadastro_analise.jsp").forward(request, response);
    }

    //listar
    // Abre as telas: cadastro, alteração ou lista (de todas as análises ou só as de um usuário).
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");
        String idTexto = request.getParameter("id");
        String idUsuarioTexto = request.getParameter("idUsuario");

        try {
            if ("novo".equals(acao)) {
                request.setAttribute("usuarios", usuarioDAO.listar());
                request.getRequestDispatcher("/WEB-INF/views/cadastro_analise.jsp").forward(request, response);
                return;
            }

            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                Analise analise = analiseDAO.listarPorId(id);

                if (analise == null) {
                    response.sendRedirect(request.getContextPath() + "/analises");
                    return;
                }

                request.setAttribute("analise", analise);
                request.getRequestDispatcher("/WEB-INF/views/editar_analise.jsp").forward(request, response);
                return;
            }

            ArrayList<Analise> lista;
            if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                lista = analiseDAO.listarPorIdUsuario(Integer.parseInt(idUsuarioTexto));
            } else {
                lista = analiseDAO.listar();
            }

            request.setAttribute("analises", lista);
            request.getRequestDispatcher("/WEB-INF/views/lista_analises.jsp").forward(request, response);

        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/analises");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar análises.", sqle);
        }
    }

    //alterar
    // Altera o resumo e a pontuação da análise com os dados em JSON.
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

            if (!json.has("idAnalise") || !json.has("resumoResultado") || !json.has("pontuacao")) {
                response.setStatus(400);
                response.getWriter().write("Dados incompletos.");
                return;
            }

            int id = Integer.parseInt(json.get("idAnalise").getAsString());
            String resumoResultado = json.get("resumoResultado").getAsString();
            int pontuacao = Integer.parseInt(json.get("pontuacao").getAsString());

            if (resumoResultado.isBlank()) {
                response.setStatus(400);
                response.getWriter().write("Preencha todos os campos obrigatórios.");
                return;
            }

            if (!Validador.resumoAnaliseValido(resumoResultado)) {
                response.setStatus(400);
                response.getWriter().write(MSG_RESUMO_INVALIDO);
                return;
            }

            if (!Validador.pontuacaoAnaliseValida(pontuacao)) {
                response.setStatus(400);
                response.getWriter().write(MSG_PONTUACAO_INVALIDA);
                return;
            }

            Analise analise = new Analise(id, resumoResultado.trim(), pontuacao);
            int linhas = analiseDAO.alterar(analise);

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Análise não encontrada.");
            }
        } catch (JsonParseException | IllegalStateException | UnsupportedOperationException | NumberFormatException e) {
            response.setStatus(400);
            response.getWriter().write("Dados inválidos.");
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                response.setStatus(400);
                response.getWriter().write("Algum dado não foi aceito pelo banco.");
            } else {
                throw new ServletException("Erro ao atualizar a análise.", sqle);
            }
        }
    }

    //deletar
    // Deleta uma análise pelo id ou todas as análises de um usuário.
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
                linhas = analiseDAO.deletarPorId(Integer.parseInt(idTexto));
            } else if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                linhas = analiseDAO.deletarPorIdUsuario(Integer.parseInt(idUsuarioTexto));
            } else {
                response.setStatus(400);
                response.getWriter().write("Id não informado.");
                return;
            }

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Análise não encontrada.");
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(400);
            response.getWriter().write("Id inválido.");
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                response.setStatus(409);
                response.getWriter().write("Não é possível excluir: a análise está em uso.");
            } else {
                throw new ServletException("Erro ao excluir a análise.", sqle);
            }
        }
    }
}