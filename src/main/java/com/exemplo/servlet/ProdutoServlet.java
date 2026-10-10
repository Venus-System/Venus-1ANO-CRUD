package com.exemplo.servlet;


import com.exemplo.dao.ProdutoDAO;
import com.exemplo.model.Produto;
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

@WebServlet("/produtos")
public class ProdutoServlet extends HttpServlet {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    //create
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");
        String marca = request.getParameter("marca");
        String categoria = request.getParameter("categoria");
        String descricao = request.getParameter("descricao");
        String pontuacaoTexto = request.getParameter("pontuacao");
        String listaIngredientes = request.getParameter("lista_ingredientes");

        boolean ehVegano = request.getParameter("eh_vegano") != null;
        boolean ehCrueltyFree = request.getParameter("eh_cruelty_free") != null;

        if (nome == null || nome.isBlank() ||
                marca == null || marca.isBlank() ||
                categoria == null || categoria.isBlank()) {

            request.setAttribute("erro", "Preencha nome, marca e categoria.");
            request.getRequestDispatcher("/cadastro_produto.jsp").forward(request, response);
            return;
        }

        try {
            int pontuacao;

            //A pontuação não veio, ou veio vazia?
            if (pontuacaoTexto == null || pontuacaoTexto.isBlank()) {
                pontuacao = 0;
            } else {
                pontuacao = Integer.parseInt(pontuacaoTexto.trim());
            }

            Produto produto = new Produto(nome, marca, categoria, descricao, ehVegano, ehCrueltyFree, pontuacao, listaIngredientes);

            if (produtoDAO.cadastrarProduto(produto)) {
                response.sendRedirect(request.getContextPath() + "/produtos");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar o produto.");
                request.getRequestDispatcher("/cadastro_produto.jsp").forward(request, response);
            }
        } catch (NumberFormatException nfe) {
            request.setAttribute("erro", "Dados inválidos. Verifique a pontuação e tente novamente.");
            request.getRequestDispatcher("/cadastro_produto.jsp").forward(request, response);

        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                request.setAttribute("erro", "Este produto já está cadastrado ou algum dado não foi aceito.");
                request.getRequestDispatcher("/cadastro_produto.jsp").forward(request, response);
            } else {
                throw new ServletException("Erro ao cadastrar produto.", sqle);
            }
        }
    }

    //read
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idTexto = request.getParameter("id");
        String nomeBusca = request.getParameter("nome");
        String marcaBusca = request.getParameter("marca");

        try {
            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                Produto produto = produtoDAO.readById(id);

                if (produto == null) {
                    response.sendRedirect(request.getContextPath() + "/produtos");
                    return;
                }

                request.setAttribute("produto", produto);
                request.getRequestDispatcher("/editar_produto.jsp").forward(request, response);
                return;
            }

            if (nomeBusca != null && !nomeBusca.isBlank()) {
                ArrayList<Produto> listaNomesProdutos = produtoDAO.readByName(nomeBusca.trim());

                request.setAttribute("produtos", listaNomesProdutos);
                request.getRequestDispatcher("/lista_produtos.jsp").forward(request, response);
                return;
            }

            if (marcaBusca != null && !marcaBusca.isBlank()) {
                ArrayList<Produto> listaMarcasProdutos = produtoDAO.readByBrand(marcaBusca.trim());

                request.setAttribute("produtos", listaMarcasProdutos);
                request.getRequestDispatcher("/lista_produtos.jsp").forward(request, response);
                return;
            }

            ArrayList<Produto> listaProdutos = produtoDAO.read();
            request.setAttribute("produtos", listaProdutos);
            request.getRequestDispatcher("/lista_produtos.jsp").forward(request, response);
        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/produtos");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar produtos.", sqle);
        }
    }

    //update
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        //Lê o corpo (linha por linha) da requisição.
        //o getReader lê o corpo, lines separa em linhas, collectiors.joining junta tudo em um texto só.
        String corpo = request.getReader().lines().collect(Collectors.joining());

        try {
            JsonObject json = JsonParser.parseString(corpo).getAsJsonObject();

            // o update grava todos os campos, então todos precisam vir no JSON
            if (!json.has("idProduto") || !json.has("nome") ||
                    !json.has("marca") || !json.has("categoria") ||
                    !json.has("descricao") || !json.has("ehVegano")
                    || !json.has("ehCrueltyFree") || !json.has("pontuacao")
                    || !json.has("listaIngredientes")) {

                response.setStatus(400);
                response.getWriter().write("Dados incompletos.");
                return;
            }

            int id = Integer.parseInt(json.get("idProduto").getAsString());
            String nome = json.get("nome").getAsString();
            String marca = json.get("marca").getAsString();
            String categoria = json.get("categoria").getAsString();
            String descricao = json.get("descricao").getAsString();
            boolean ehVegano = json.get("ehVegano").getAsBoolean();
            boolean ehCrueltyFree = json.get("ehCrueltyFree").getAsBoolean();
            int pontuacao = Integer.parseInt(json.get("pontuacao").getAsString());
            String listaIngredientes = json.get("listaIngredientes").getAsString();

            if (nome.isBlank() || marca.isBlank() || categoria.isBlank()){
                response.setStatus(400);
                response.getWriter().write("Preencha nome, marca e categoria.");
                return;
            }

            Produto produto = new Produto(id, nome.trim(), marca.trim(), categoria.trim(), descricao, ehVegano, ehCrueltyFree, pontuacao, listaIngredientes);
            int linhas = produtoDAO.update(produto);

            if (linhas>0){
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Produto não encontrado.");
            }
        }catch (JsonParseException | IllegalStateException | UnsupportedOperationException | NumberFormatException e){
            response.setStatus(400);
            response.getWriter().write("Dados inválidos.");
        }catch (SQLException sqle){
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")){
                response.setStatus(409);
                response.getWriter().write("Já existe um produto com esses dados ou algum dado não foi aceito.");
            }else {
                throw new ServletException("Erro ao atualizar o produto.", sqle);
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
        }

        try {
            int id = Integer.parseInt(idTexto);
            int linhas = produtoDAO.deleteById(id);

            if (linhas>0){
                response.setStatus(200);
            }else {
                response.setStatus(404);
                response.getWriter().write("Produto não encontrado.");
            }
        }catch (NumberFormatException nfe ){
            response.setStatus(400);
            response.getWriter().write("Id inválido.");
        }catch (SQLException sqle){
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")){
                response.setStatus(409);
                response.getWriter().write("Não é possível excluir: o produto está vinculado a usuários ou ingredientes.");
            }else {
                throw new ServletException("Erro ao excluir o produto.", sqle);
            }
        }
        }

    }
