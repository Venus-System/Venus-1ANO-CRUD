package com.exemplo.servlet;


import com.exemplo.dao.NomeIngredienteDAO;
import com.exemplo.model.NomeIngrediente;
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
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

@WebServlet("/nomeIngredientes")
public class NomeIngredienteServlet extends HttpServlet {

    private final NomeIngredienteDAO nomeIngredienteDAO = new NomeIngredienteDAO();

    // Cadastro
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");
        String idIngredienteTexto = request.getParameter("idIngrediente");
        String erro = null;

        if (nome == null || nome.isBlank() ||
                idIngredienteTexto == null || idIngredienteTexto.isBlank()) {
            erro = "Preencha todos os campos obrigatórios.";
        } else if (!Validador.nomeIngredienteValido(nome)) {
            erro = "Nome inválido. Use de 2 a 100 caracteres (letras, números e . , ' ( ) / % + -).";
        } else {

            try {
                int idIngrediente = Integer.parseInt(idIngredienteTexto);

                NomeIngrediente nomeIngrediente = new NomeIngrediente(nome.trim(), idIngrediente);

                if (nomeIngredienteDAO.cadastrarNomeIngrediente(nomeIngrediente)) {
                    response.sendRedirect(request.getContextPath() + "/nomeIngredientes?idIngrediente=" + idIngrediente);
                    // Redireciona após o cadastro para evitar reenvio do formulário ao atualizar a página.
                    // O caminho de contexto mantém o redirecionamento dentro da aplicação.
                    return;
                }
                erro = "Não foi possível cadastrar o nome do ingrediente.";


            } catch (NumberFormatException nfe) {
                erro = "Id do igrediente inválido.";
            } catch (SQLException sqle) {
                if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                    erro = "Ingrediente informado não existe ou o registro já está cadastrado.";
                } else {
                    throw new ServletException("Erro ao cadastrar nome do ingrediente", sqle);
                }
            }
        }
        //setAttribute guarda um objeto dentro da requisição atual. 'O atributo só existe durante essa requisição'
        //request.getRequestDispatcher junto com o forward (entregam a requisição) para o recurso que foi criado um despachante, geram a resposta.
        request.setAttribute("erro", erro);
        request.getRequestDispatcher("/WEB-INF/views/cadastro_nome_ingrediente.jsp").forward(request, response);
    }

    // Consulta
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idTexto = request.getParameter("id");
        String idIngredienteTexto = request.getParameter("idIngrediente");
        String acao = request.getParameter("acao");

        try {
            if ("novo".equals(acao)) {
                request.getRequestDispatcher("/WEB-INF/views/cadastro_nome_ingrediente.jsp").forward(request, response);
                return;
            }

            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                NomeIngrediente nomeIngrediente = nomeIngredienteDAO.readById(id);

                if (nomeIngrediente == null) {
                    response.sendRedirect(request.getContextPath() + "/nomeIngredientes");
                    return;
                }

                request.setAttribute("nomeIngredientes", nomeIngrediente);
                request.getRequestDispatcher("/WEB-INF/views/editar_nome_ingrediente.jsp").forward(request, response);
                return;
            }

            ArrayList<NomeIngrediente> listaIngredientes = nomeIngredienteDAO.read();
            if (idIngredienteTexto != null && !idIngredienteTexto.isBlank()) {
                listaIngredientes = nomeIngredienteDAO.readByIdIngrediente(Integer.parseInt(idIngredienteTexto));
            } else {
                listaIngredientes = nomeIngredienteDAO.read();
            }

            request.setAttribute("nomesIngredientes", listaIngredientes); // mesmo nome nos dois casos
            request.getRequestDispatcher("/WEB-INF/views/lista_nome_ingredientes.jsp").forward(request, response);

        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/nomeIngredientes");

        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar nomes de ingredientes", sqle);
        }
    }

    // Atualização
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

            if (nome.isBlank()) {
                response.setStatus(400);
                response.getWriter().write("Preencha todos os campos obrigatórios.");
                return;

            } else if (!Validador.nomeIngredienteValido(nome)) {
                response.setStatus(400);
                response.getWriter().write("Nome inválido. Use de 2 a 100 caracteres (letras, números e . , ' ( ) / % + -).");
                return;
            }

            NomeIngrediente nomeIngrediente = new NomeIngrediente(id, nome.trim());

            int linhas = nomeIngredienteDAO.alterarValores(nomeIngrediente);

            if (linhas > 0) {
                response.setStatus(200);

            } else {
                response.setStatus(404);
                response.getWriter().write("Nome de ingrediente não encontrado.");
            }

        } catch (JsonParseException | IllegalStateException | UnsupportedOperationException | NumberFormatException e) {
            //UnsupportedOperationException -> Quando Json traz null em algum campo.
            response.setStatus(400);
            response.getWriter().write("Dados inválidos.");
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                response.setStatus(409);
                response.getWriter().write("Esse nome já está cadastrado para este ingrediente.");
            } else {
                throw new ServletException("Erro ao atualizar o nome do ingrediente.", sqle);
            }
        }
    }

    // Exclusão
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        String idTexto = request.getParameter("id"); //exclui um NOME
        String idIngredienteTexto = request.getParameter("idIngrediente");

        try {
            int linhas;

            if (idTexto != null && !idTexto.isBlank()) {
                linhas = nomeIngredienteDAO.deleteById(Integer.parseInt(idTexto));
                // Exclui apenas o nome identificado pela chave id_nome.
            } else if (idIngredienteTexto != null && !idIngredienteTexto.isBlank()) {
                linhas = nomeIngredienteDAO.deleteByIdIngrediente(Integer.parseInt(idIngredienteTexto));
                // Exclui todos os nomes vinculados ao ingrediente informado.

            } else {
                response.setStatus(400);
                response.getWriter().write("Id não informado.");
                return;

                //se não for nenhum dos dois responde 400.
            }

            if (linhas > 0) {
                response.setStatus(200);
                // A resposta considera a quantidade de registros excluídos.
            } else {
                response.setStatus(404);
                response.getWriter().write("Nome de ingrediente não encontrado.");
                //se nada for encontrado, 404.
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(400);
            response.getWriter().write("Id inválido.");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao excluir o nome do ingrediente.", sqle);
        }
    }


}
