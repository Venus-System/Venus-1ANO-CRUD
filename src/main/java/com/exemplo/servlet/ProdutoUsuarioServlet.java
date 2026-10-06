package com.exemplo.servlet;


import com.exemplo.dao.ProdutoUsuarioDAO;
import com.exemplo.model.ProdutoUsuario;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet ("/produto-usuarios")
public class ProdutoUsuarioServlet extends HttpServlet {
    private final ProdutoUsuarioDAO produtoUsuarioDAO = new ProdutoUsuarioDAO();

    //create
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{

        request.setCharacterEncoding("UTF-8");

        String idProdutoTexto = request.getParameter("id_produto");
        String idUsuarioTexto = request.getParameter("id_usuario");

        if (idProdutoTexto == null || idProdutoTexto.isBlank() ||
            idUsuarioTexto == null || idUsuarioTexto.isBlank()){
            //Aqui o if barra se qualquer campo estiver vazio.

            request.setAttribute("erro", "Preencha todos os campos obrigatórios.");
            request.getRequestDispatcher("/cadastro_produto_usuario.jsp").forward(request, response);

            return;
        }

        try {
            int idProduto = Integer.parseInt(idProdutoTexto);
            int idUsuario = Integer.parseInt(idUsuarioTexto);

            ProdutoUsuario produtoUsuario = new ProdutoUsuario(idProduto, idUsuario);

            if (produtoUsuarioDAO.inserirProdutoUsuario(produtoUsuario)){
                response.sendRedirect(request.getContextPath()+ "/produtoUsuarios");
                //O getContextPath devolve o nome da aplicação (o nome do VENUS na URL).
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar o produto do usuário.");
                request.getRequestDispatcher("/cadastro_produto_usuario.jsp").forward(request, response);
            }
        }catch (NumberFormatException nfe){
            request.setAttribute("erro", "Não foi possível cadastrar o produto do usuário.");
            request.getRequestDispatcher("/cadastrar_produto_usuario.jsp").forward(request, response);
        }catch (SQLException sqle){
            if (sqle.getSQLState() != null && sqle.getSQLState().startsWith("23")){
                request.setAttribute("erro", "Produto ou usuário não existe, ou o registro já está cadastrado.");
                request.getRequestDispatcher("/cadastro_produto_usuario.jsp").forward(request, response);
                //se o erro for por causa dos dados informados, então a resposta voltará ao formulário cm uma mensagem.
            }else {
                throw new ServletException("Erro ao cadastrar produto do usuário.", sqle);
                //aqui é qualquer outro erro com o banco, o que não for 'culpa' de quem preencheu o formulário
            }
        }
    }



}
