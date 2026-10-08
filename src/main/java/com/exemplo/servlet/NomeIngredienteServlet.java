package com.exemplo.servlet;


import com.exemplo.dao.NomeIngredienteDAO;
import com.exemplo.model.NomeIngrediente;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

@WebServlet("/nomeIngredientes")
public class NomeIngredienteServlet extends HttpServlet {

    private final NomeIngredienteDAO nomeIngredienteDAO = new NomeIngredienteDAO();

    //create
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");
        String idIngredienteTexto = request.getParameter("id_ingrediente");

        if (nome == null || nome.isBlank() ||
                idIngredienteTexto == null || idIngredienteTexto.isBlank()) {

            request.setAttribute("erro", "Preencha todos os campos obrigatórios.");
            request.getRequestDispatcher("/cadastro_nome_ingrediente.jsp").forward(request, response);
            return;
        }
        try {
            int idIngrediente = Integer.parseInt(idIngredienteTexto);

            NomeIngrediente nomeIngrediente = new NomeIngrediente(nome.trim(), idIngrediente);

            if (nomeIngredienteDAO.cadastrarNomeIngrediente(nomeIngrediente)) {
                response.sendRedirect(request.getContextPath() + "/nomeIngredientes");
                //sendRedirect --> O navegador faz uma nova requsição para a URL. É usado o doPost depois de cadastrar para o usuário não reenviar o formulário ao atualizar a página.
                // request.getContextPath --> Devolve o PREFIXO da URL (/Venus2). é necessário no sendRedirect porque o redirect é feito pelo navegador. que interpreta /usuarios como URL sem o nome Venus (app). Com o ContextPath, fica correto.
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar o nome do ingrediente.");
                request.getRequestDispatcher("/cadastro_nome_ingrediente.jsp").forward(request, response);

                //setAttribute guarda um objeto dentro da requisição atual. 'O atributo só existe durante essa requisição'
                //request.getRequestDispatcher junto com o forward (entregam a requisição) para o recurso que foi criado um despachante, geram a resposta.
            }
        } catch (NumberFormatException nfe) {
            request.setAttribute("erro", "Id do ingrediente inválido.");
            request.getRequestDispatcher("/cadastro_nome_ingrediente.jsp").forward(request, response);
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                request.setAttribute("erro", "Ingrediente informado não existe ou o registro já está cadastrado.");
                request.getRequestDispatcher("/cadastro_nome_ingrediente.jsp").forward(request, response);
            } else {
                throw new ServletException("Erro ao cadastrar nome do ingrediente", sqle);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String idTexto = request.getParameter("id");
        String idIngredienteTexto = request.getParameter("idIngrediente");

        try {
            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                NomeIngrediente nomeIngrediente = nomeIngredienteDAO.readById(id);

                if (nomeIngrediente == null) {
                    response.sendRedirect(request.getContextPath() + "/nomeIngredientes");
                    return;
                }

                request.setAttribute("nomeIngrediente", nomeIngrediente);
                request.getRequestDispatcher("/editar_nome_ingrediente.jsp").forward(request, response);
                return;
            }

            if (idIngredienteTexto != null && !idIngredienteTexto.isBlank()) {
                int idIngrediente = Integer.parseInt(idIngredienteTexto);
                ArrayList<NomeIngrediente> listaIngrediente = nomeIngredienteDAO.readByIdIngrediente(idIngrediente);

                request.setAttribute("nomeIngredientes", listaIngrediente);
                request.getRequestDispatcher("/lista_nome_ingredientes.jsp").forward(request, response);
                return;
            }

            ArrayList<NomeIngrediente> listaIngredientes = nomeIngredienteDAO.read();
            request.setAttribute("nomesIngredientes", listaIngredientes);
            request.getRequestDispatcher("/lista_nome_ingredientes.jsp").forward(request, response);

        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/nomeIngredientes");

        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar nomes de ingredientes", sqle);
        }
    }

    //update
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

       //define decodificação de caracteres.

        String corpo = request.getReader().lines().collect(Collectors.joining());
        /*
            - Abre um leitor de texto sobre o corpo.
            - Transforma o texto em um fluxo de linhas.
            - Junta todas as linhas em uma só STRING.
         */

        try {
            JsonObject json = JsonParser.parseString(corpo).getAsJsonObject();
            // Converte o texto JSON em um objeto Java Navegável. Interpreta a String como JSON.

            if (!json.has("idNomeIngrediente") || !json.has("nome")) {
                response.setStatus(400);
                //Define o código de status HTTP DA RESPOSTA.
                response.getWriter().write("Dados incompletos.");
                //Escreve texto no corpo da resposta enviada ao navegador "Dados incompletos"
                return;
            }

            int id = Integer.parseInt(json.get("idNomeIngrediente").getAsString());
            String nome = json.get("nome").getAsString();

            if (nome.isBlank()){
                response.setStatus(400);
                response.getWriter().write("Preencha todos os campos obrigatórios.");
                return;

            }

            NomeIngrediente nomeIngrediente = new NomeIngrediente(id, nome.trim());

            int linhas = nomeIngredienteDAO.alterarValores(nomeIngrediente);

            if (linhas > 0){
                response.setStatus(200);

            }else {
                response.setStatus(404);
                response.getWriter().write("Nome de ingrediente não encontrado.");
            }

        }catch (JsonParseException | IllegalStateException | UnsupportedOperationException| NumberFormatException e){
            //UnsupportedOperationException -> Quando Json traz null em algum campo.
            response.setStatus(400);
            response.getWriter().write("Dados inválidos.");
        }catch (SQLException sqle){
            throw new ServletException("Erro ao atualizar o nome do ingrediente.", sqle);
        }
    }

    //delete
    @Override
    protected void doDelete (HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        String idTexto = request.getParameter("id"); //exclui um NOME
        String idIngredienteTexto = request.getParameter("idIngrediente");

        try {
            int linhas;

            if (idTexto !=null && !idTexto.isBlank()){
                linhas = nomeIngredienteDAO.deleteById(Integer.parseInt(idTexto));
                //aqui exclui, pela chave da tabela IDNOME!
            } else if (idIngredienteTexto != null && !idIngredienteTexto.isBlank()) {
                linhas = nomeIngredienteDAO.deleteByIdIngrediente(Integer.parseInt(idIngredienteTexto));
                //aqui exclui TODOS os NOMES de um ingrediente

            }else {
                response.setStatus(400);
                response.getWriter().write("Id não informado.");
                return;

                //se não for nenhum dos dois responde 400.
            }

            if (linhas>0){
                response.setStatus(200);
                //aqui a resposta é baseada no número de linahs afetadas.
            }else {
                response.setStatus(404);
                response.getWriter().write("Nome de ingrediente não encontrado.");
                //se nada for encontrado, 404.
            }
        }catch (NumberFormatException nfe){
            response.setStatus(400);
            response.getWriter().write("Id inválido.");
        }catch (SQLException sqle){
            throw new ServletException("Erro ao excluir o nome do ingrediente.", sqle);
        }
    }


}
