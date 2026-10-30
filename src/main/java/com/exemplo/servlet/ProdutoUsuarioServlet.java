package com.exemplo.servlet;


import com.exemplo.dao.ProdutoDAO;
import com.exemplo.dao.ProdutoUsuarioDAO;
import com.exemplo.dao.UsuarioDAO;
import com.exemplo.model.ProdutoUsuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/produtoUsuarios")
public class ProdutoUsuarioServlet extends HttpServlet {
    private final ProdutoUsuarioDAO produtoUsuarioDAO = new ProdutoUsuarioDAO();

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    //os selects de produto e usuário precisam de listas: todos os produtos e todos os usário do banco.
    //sem o abrir cadastro, as listas só chegam ao jsp se o servlet buscar no banco e guradar na requisição com o setAtribute.
    //sem isso o jsp guarda algo que não existe, e os selects aparecem vazios.
    private void abrirCadastro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("produtos", produtoDAO.read());
            request.setAttribute("usuarios", usuarioDAO.read());
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao carregar produtos e usuários para o formulário.", sqle);
        }
        request.getRequestDispatcher("/WEB-INF/views/cadastro_produto_usuario.jsp").forward(request, response);
    }

    //create
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idProdutoTexto = request.getParameter("id_produto");
        String idUsuarioTexto = request.getParameter("id_usuario");

        if (idProdutoTexto == null || idProdutoTexto.isBlank() ||
                idUsuarioTexto == null || idUsuarioTexto.isBlank()) {
            //Aqui o if barra se qualquer campo estiver vazio.

            request.setAttribute("erro", "Preencha todos os campos obrigatórios.");
            abrirCadastro(request, response);
            return;
        }

        try {
            int idProduto = Integer.parseInt(idProdutoTexto);
            int idUsuario = Integer.parseInt(idUsuarioTexto);

            if (produtoUsuarioDAO.existeVinculo(idProduto, idUsuario)) {
                request.setAttribute("erro", "Este produto já está vinculado a este usuário.");
                abrirCadastro(request, response);
                return;
            }

            ProdutoUsuario produtoUsuario = new ProdutoUsuario(idProduto, idUsuario);

            if (produtoUsuarioDAO.inserirProdutoUsuario(produtoUsuario)) {
                if ("1".equals(request.getParameter("fixo"))) {
                    response.sendRedirect(request.getContextPath() + "/usuarioDetalhe?id=" + idUsuario + "#produtos");
                    return;
                }
                response.sendRedirect(request.getContextPath() + "/produtoUsuarios");
                //O getContextPath devolve o nome da aplicação (o nome do VENUS na URL).
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar o produto do usuário.");
                abrirCadastro(request, response);

            }
        } catch (NumberFormatException nfe) {
            request.setAttribute("erro", "Dados inválidos. Selecione o produto e o usuário e tente novamente.");
            abrirCadastro(request, response);
        } catch (SQLException sqle) {
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")) {
                request.setAttribute("erro", "Produto ou usuário não existe, ou o registro já está cadastrado.");
                abrirCadastro(request, response);
                //se o erro for por causa dos dados informados, então a resposta voltará ao formulário cm uma mensagem.
            } else {
                throw new ServletException("Erro ao cadastrar produto do usuário.", sqle);
                //aqui é qualquer outro erro com o banco, o que não for 'culpa' de quem preencheu o formulário
            }
        }
    }

    //read
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //abrir o formulário pelo servlet.
        if ("novo".equals(request.getParameter("acao"))) {
            abrirCadastro(request, response);
            return;
        }

        String idUsuarioTexto = request.getParameter("idUsuario"); //aqui filtrará por usuário.

        try {
            if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                int idUsuario = Integer.parseInt(idUsuarioTexto);
                ArrayList<ProdutoUsuario> listaDoUsuario = produtoUsuarioDAO.readByIdUsuario(idUsuario);

                request.setAttribute("produtosUsuarios", listaDoUsuario);
                request.getRequestDispatcher("/WEB-INF/views/lista_produto_usuario.jsp").forward(request, response);
                return;
                //Return serve para o código não continuar executando as linhas seguintes.
            }

            ArrayList<ProdutoUsuario> listaProdutoUsuarios = produtoUsuarioDAO.read();
            //Guarda todos os registros de produto_usuario em uma lista.
            request.setAttribute("produtosUsuarios", listaProdutoUsuarios);
            request.getRequestDispatcher("/WEB-INF/views/lista_produto_usuario.jsp").forward(request, response);
            //o código guarda a lista com setAttribute e faz o forward para o JSP, que percorre ${produtosUsuarios} para montar a tabela.
        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/produtoUsuarios");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar produtos no usuário.", sqle);
        }
    }

    //delete
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        String idTexto = request.getParameter("id");
        String idProdutoTexto = request.getParameter("idProduto");
        String idUsuarioTexto = request.getParameter("idUsuario");

        try {
            int linhas;

            if (idTexto != null && !idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto);
                linhas = produtoUsuarioDAO.deleteById(id);
            } else if (idProdutoTexto != null && !idProdutoTexto.isBlank()) {
                int idProduto = Integer.parseInt(idProdutoTexto);
                linhas = produtoUsuarioDAO.deleteByIdProduto(idProduto);
            } else if (idUsuarioTexto != null && !idUsuarioTexto.isBlank()) {
                int idUsuario = Integer.parseInt(idUsuarioTexto);
                linhas = produtoUsuarioDAO.deleteByIdUsuario(idUsuario);
            } else {
                response.setStatus(400);
                response.getWriter().write("Id não informado.");
                return;
            }

            if (linhas > 0) {
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.getWriter().write("Registro não encontrado.");
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(400);
            response.getWriter().write("Id inválido.");
        } catch (SQLException sqle) {
            throw new ServletException("Erro ao excluir o produto do usuário.", sqle);
        }

    }

}
