package com.exemplo.servlet;


import com.exemplo.dao.AlergiaDAO;
import com.exemplo.model.Alergia;
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

// Servlet das alergias: recebe as requisições da tela e chama o AlergiaDAO.
@WebServlet("/alergias")
public class AlergiaServlet extends HttpServlet {

    private final AlergiaDAO alergiaDAO = new AlergiaDAO();

    //cadastrar
    // Cadastra uma alergia com o nome vindo do formulário e volta para a lista.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");

        if (nome == null || nome.isBlank()) {
            request.setAttribute("erro", "Informe o nome da alergia.");
            request.getRequestDispatcher("/WEB-INF/views/cadastro_alergia.jsp").forward(request, response);
            return;
        }

        nome = nome.trim();

        if (!Validador.nomeAlergiaValido(nome)) {
            request.setAttribute("erro", "Nome inválido. Use de 2 a 100 caracteres, começando por letra ou número.");
            request.getRequestDispatcher("/WEB-INF/views/cadastro_alergia.jsp").forward(request, response);
            return;
        }

        try {
            Alergia alergia = new Alergia(nome.trim());

            if (alergiaDAO.cadastrar(alergia)) {
                response.sendRedirect(request.getContextPath() + "/alergias");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar a alergia.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_alergia.jsp").forward(request, response);
            }
        } catch (SQLException sqle) {
            getServletContext().log("Erro SQL ao cadastrar alergia", sqle);   // no doPut: "Erro SQL ao atualizar alergia"
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                request.setAttribute("erro", "Esta alergia já está cadastrada.");
                request.getRequestDispatcher("/WEB-INF/views/cadastro_alergia.jsp").forward(request, response);
                //O insert falha e o Java lança um SQLException.
                //23 significa violação de restrição.
            } else {
                throw new ServletException("Erro ao cadastrar alergia", sqle);
            }
        }
    }

    //listar
    // Abre as telas: com acao=novo abre o cadastro, com id abre a alteração e sem nada mostra a lista.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idTexto = request.getParameter("id");
        String acao = request.getParameter("acao");

        if ("novo".equals(acao)) {
            request.getRequestDispatcher("/WEB-INF/views/cadastro_alergia.jsp").forward(request, response);
            return;                                           // 3) encerra o método aqui
        }

        try {
            if (idTexto != null && !idTexto.isBlank()) {
                // Converte o texto recebido em número. Se não for número, cai no NumberFormatException.
                int id = Integer.parseInt(idTexto);
                Alergia alergia = alergiaDAO.listarPorId(id);

                if (alergia == null) {
                    response.sendRedirect(request.getContextPath() + "/alergias");
                    return;
                }

                request.setAttribute("alergia", alergia);
                request.getRequestDispatcher("/WEB-INF/views/editar_alergia.jsp").forward(request, response);
                return;
            }

            ArrayList<Alergia> listaAlergias = alergiaDAO.listar();
            request.setAttribute("alergias", listaAlergias);
            request.getRequestDispatcher("/WEB-INF/views/lista_alergias.jsp").forward(request, response);
        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/alergias");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar alergias.", sqle);
        }
    }

    //alterar
    // Altera uma alergia com os dados que chegam em JSON pelo JavaScript da tela.
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

            if (!json.has("idAlergia") || !json.has("nome")) {
                response.setStatus(400);
                response.getWriter().write("Dados incompletos.");
                return;
                //encerra o mehtodo na hora, depois de definir o status e escrever a mensagem.

            }

            int id = Integer.parseInt(json.get("idAlergia").getAsString());
            //Lê o id que veio do json e o transforma em número. Pega o campo, lê o valor desse campo como texto, converte para int.
            String nome = json.get("nome").getAsString();
            //Pega o campo 'nome' do Json e o lê como texto.

            if (nome.isBlank()) {
                response.setStatus(400);
                response.getWriter().write("Informa o nome da alergia.");
                return;
                //encerra o mehtodo na hora, depois de definir o status e escrever a mensagem.

            }

            Alergia alergia = new Alergia(id, nome.trim());
            int linhas = alergiaDAO.alterar(alergia);

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Alergia não encontrada.");
            }
        } catch (JsonParseException | IllegalStateException | UnsupportedOperationException | NumberFormatException e) {
            response.setStatus(400);
            response.getWriter().write("Dados inválidos.");
        } catch (SQLException sqle) {
            getServletContext().log("Erro SQL ao cadastrar alergia", sqle);   // no doPut: "Erro SQL ao atualizar alergia"
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                //Confere se o código existe e se começa com 23
                response.setStatus(409);
                //caso o novo nome já pertença a outra alergia (unique)
                response.getWriter().write("Já existe uma alergia com esse nome.");
            } else {
                throw new ServletException("Erro ao atualizar a alergia.", sqle);
            }
        }
    }

    //deletar
    // Deleta a alergia pelo id e responde com o status do resultado.
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        String idTexto = request.getParameter("id");

        if (idTexto == null || idTexto.isBlank()){
            response.setStatus(400);
            response.getWriter().write("Id não informado.");
            return;
            //encerra o mehtodo na hora, depois de definir o status e escrever a mensagem.
        }

        try {
            int id = Integer.parseInt(idTexto);
            int linhas = alergiaDAO.deletarPorId(id);

            if (linhas>0){
                response.setStatus(200);
            }else {
                response.setStatus(404);
                response.getWriter().write("Alergia não encontrada.");
            }
        }catch (NumberFormatException nfe){
            response.setStatus(400);
            response.getWriter().write("Id inválido.");
        }catch (SQLException sqle){
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")){
                response.setStatus(409);
                //Conflict. Ele avisa que a requisição está correta, mas conflita com o estado atual dos dados no banco.
                response.getWriter().write("Não é possível excluir: a alergia está em uso por algum usuário.");
            }else {
                throw new ServletException("Erro ao excluir a alergia.", sqle);
            }
        }
    }
}
