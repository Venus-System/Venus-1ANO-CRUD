package com.exemplo.servlet;


import com.exemplo.dao.AlergiaDAO;
import com.exemplo.model.Alergia;
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

@WebServlet("/alergias")
public class AlergiaServlet extends HttpServlet {

    private final AlergiaDAO alergiaDAO = new AlergiaDAO();

    //create
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");

        if (nome == null || nome.isBlank()) {
            request.setAttribute("erro", "Informe o nome da alergia.");
            request.getRequestDispatcher("/cadastro_alergia.jsp").forward(request, response);
            return;
        }

        try {
            Alergia alergia = new Alergia(nome.trim());

            if (alergiaDAO.inserirAlergia(alergia)) {
                response.sendRedirect(request.getContextPath() + "/alergias");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar a alergia.");
                request.getRequestDispatcher("/cadastro_alergia.jsp").forward(request, response);
            }
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                request.setAttribute("erro", "Esta alergia já está cadastrada.");
                request.getRequestDispatcher("/cadastro_alergia.jsp").forward(request, response);
                //O insert falha e o Java lança um SQLException.
                //23 significa violação de restrição.
            } else {
                throw new ServletException("Erro ao cadastrar alergia", sqle);
            }
        }
    }

    //read
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idTexto = request.getParameter("id");

        try {
            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                Alergia alergia = alergiaDAO.readById(id);

                if (alergia == null) {
                    response.sendRedirect(request.getContextPath() + "/alergias");
                    return;
                }

                request.setAttribute("alergia", alergia);
                request.getRequestDispatcher("/editar_alergia.jsp").forward(request, response);
                return;
            }

            ArrayList<Alergia> listaAlergias = alergiaDAO.read();
            request.setAttribute("alergias", listaAlergias);
            request.getRequestDispatcher("/lista_alergias.jsp").forward(request, response);
        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/alergias");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar alergias.", sqle);
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
            int linhas = alergiaDAO.update(alergia);

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

    //delete
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
            int linhas = alergiaDAO.deleteById(id);

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
