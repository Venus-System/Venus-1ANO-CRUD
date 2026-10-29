package com.exemplo.servlet;

import com.exemplo.dao.PerfilCabeloDAO;
import com.exemplo.dao.UsuarioDAO;
import com.exemplo.model.PerfilCabelo;
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

@WebServlet("/perfisCabelo")
public class PerfilCabeloServlet extends HttpServlet {

    private final PerfilCabeloDAO perfilCabeloDAO = new PerfilCabeloDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private static final String MSG_ESCALA_INVALIDA =
            "Curvatura deve ficar entre 1 e 4. Oleosidade e espessura devem ficar entre 1 e 10.";

    //create
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String curvaturaTexto = request.getParameter("curvatura");
        String oleosidadeTexto = request.getParameter("oleosidade");
        String espessuraTexto = request.getParameter("espessura");
        String idUsuarioTexto = request.getParameter("idUsuario");

        String erro = null;

        if (idUsuarioTexto == null || idUsuarioTexto.isBlank()) {
            erro = "Selecione o usuário.";
        } else if (curvaturaTexto == null || curvaturaTexto.isBlank()
                || oleosidadeTexto == null || oleosidadeTexto.isBlank()
                || espessuraTexto == null || espessuraTexto.isBlank()) {
            erro = "Preencha todos os campos obrigatórios.";
        } else {
            try {
                int curvatura = Integer.parseInt(curvaturaTexto.trim());
                int oleosidade = Integer.parseInt(oleosidadeTexto.trim());
                int espessura = Integer.parseInt(espessuraTexto.trim());
                int idUsuario = Integer.parseInt(idUsuarioTexto.trim());

                if (!Validador.curvaturaValida(curvatura)
                        || !Validador.espessuraEOleosidadeValida(oleosidade)
                        || !Validador.espessuraEOleosidadeValida(espessura)) {
                    erro = MSG_ESCALA_INVALIDA;
                } else if (perfilCabeloDAO.existePerfilParaUsuario(idUsuario)) {
                    erro = "Este usuário já possui um perfil de cabelo cadastrado.";
                } else {
                    PerfilCabelo perfilCabelo = new PerfilCabelo(curvatura, oleosidade, espessura, idUsuario, 0);

                    if (perfilCabeloDAO.cadastrarPerfilCabelo(perfilCabelo)) {
                        response.sendRedirect(request.getContextPath() + "/perfisCabelo?idUsuario=" + idUsuario);
                        return;
                    }
                    erro = "Não foi possível cadastrar o perfil de cabelo.";
                }
            } catch (NumberFormatException nfe) {
                erro = "Dados inválidos. Verifique os valores informados.";
            } catch (SQLException sqle) {
                String estado = sqle.getSQLState();
                if ("23503".equals(estado)) {
                    erro = "Usuário não existe.";
                } else if (estado != null && estado.startsWith("23")) {
                    erro = "Algum dado não foi aceito pelo banco.";
                } else {
                    throw new ServletException("Erro ao cadastrar perfil de cabelo.", sqle);
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
        request.getRequestDispatcher("/WEB-INF/views/cadastro_perfil_cabelo.jsp").forward(request, response);
    }

    //read
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");
        String idTexto = request.getParameter("id");
        String idUsuarioTexto = request.getParameter("idUsuario");

        try {
            if ("novo".equals(acao)) {
                request.setAttribute("usuarios", usuarioDAO.read());
                request.getRequestDispatcher("/WEB-INF/views/cadastro_perfil_cabelo.jsp").forward(request, response);
                return;
            }

            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                PerfilCabelo perfilCabelo = perfilCabeloDAO.readById(id);

                if (perfilCabelo == null) {
                    response.sendRedirect(request.getContextPath() + "/perfisCabelo");
                    return;
                }

                request.setAttribute("perfilCabelo", perfilCabelo);
                request.getRequestDispatcher("/WEB-INF/views/editar_perfil_cabelo.jsp").forward(request, response);
                return;
            }

            ArrayList<PerfilCabelo> lista = new ArrayList<>();

            if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                PerfilCabelo perfilUsuario = perfilCabeloDAO.readByIdUsuario(Integer.parseInt(idUsuarioTexto));
                if (perfilUsuario != null) {
                    lista.add(perfilUsuario);
                }
            } else {
                lista = perfilCabeloDAO.read();
            }

            request.setAttribute("perfisCabelo", lista);
            request.getRequestDispatcher("/WEB-INF/views/lista_perfil_cabelo.jsp").forward(request, response);

        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/perfisCabelo");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar perfis de cabelo.", sqle);
        }
    }

    //update
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        String corpo = request.getReader().lines().collect(Collectors.joining());

        try {
            JsonObject json = JsonParser.parseString(corpo).getAsJsonObject();

            if (!json.has("idPerfilCabelo") || !json.has("curvatura")
                    || !json.has("oleosidade") || !json.has("espessura")) {
                response.setStatus(400);
                response.getWriter().write("Dados incompletos.");
                return;
            }

            int id = Integer.parseInt(json.get("idPerfilCabelo").getAsString());
            int curvatura = Integer.parseInt(json.get("curvatura").getAsString());
            int oleosidade = Integer.parseInt(json.get("oleosidade").getAsString());
            int espessura = Integer.parseInt(json.get("espessura").getAsString());

            if (!Validador.curvaturaValida(curvatura)
                    || !Validador.espessuraEOleosidadeValida(oleosidade)
                    || !Validador.espessuraEOleosidadeValida(espessura)) {
                response.setStatus(400);
                response.getWriter().write(MSG_ESCALA_INVALIDA);
                return;
            }

            PerfilCabelo perfilCabelo = new PerfilCabelo(id, curvatura, oleosidade, espessura);
            int linhas = perfilCabeloDAO.alterarValores(perfilCabelo);

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Perfil de cabelo não encontrado.");
            }
        } catch (JsonParseException | IllegalStateException | UnsupportedOperationException | NumberFormatException e) {
            response.setStatus(400);
            response.getWriter().write("Dados inválidos.");
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                response.setStatus(400);
                response.getWriter().write("Algum dado não foi aceito pelo banco.");
            } else {
                throw new ServletException("Erro ao atualizar o perfil de cabelo.", sqle);
            }
        }
    }

    //delete
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
                linhas = perfilCabeloDAO.deleteById(Integer.parseInt(idTexto));
            } else if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                linhas = perfilCabeloDAO.deleteByIdUsuario(Integer.parseInt(idUsuarioTexto));
            } else {
                response.setStatus(400);
                response.getWriter().write("Id não informado.");
                return;
                //encerra o mehtodo na hora, depois de definir o status e escrever a mensagem.

            }

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Perfil de cabelo não encontrado.");
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(400);
            response.getWriter().write("Id inválido.");
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                response.setStatus(409);
                //Conflict. Ele avisa que a requisição está correta, mas conflita com o estado atual dos dados no banco.

                response.getWriter().write("Não é possível excluir: o perfil de cabelo está em uso.");
            } else {
                throw new ServletException("Erro ao excluir o perfil de cabelo.", sqle);
            }
        }
    }
}