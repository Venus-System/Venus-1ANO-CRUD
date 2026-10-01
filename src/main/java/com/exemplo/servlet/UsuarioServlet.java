package com.exemplo.servlet;

import com.exemplo.dao.UsuarioDAO;
import com.exemplo.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.stream.Collectors;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {
    //transforma a classe Java comum em um Servlet

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    //O tomcat cria só uma instância do servlet e reutiliza.

    @Override  //reescreve um metodo que já existe na classe HttpServlet
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        //request representa o pedido que chegou do navegador.
        //response representa a resposta que será construída.
            throws ServletException, IOException{
        try {
            ArrayList<Usuario> listaUsuarios = usuarioDAO.read();
            //busca os dados no banco através do DAO.

            request.setAttribute("usuarios", listaUsuarios);
            //lista está sendo guardada dentro do resquest usando a chave 'usuarios'.

            request.getRequestDispatcher("/lista_usuarios.jsp").forward(request, response);
            //caminho até o JSP(vitrine, a página que aparece para o usuário), o forward encaminha o pedido, mantendo os atributos.

        }catch (SQLException sqle){
            throw new RuntimeException("Erro ao listar usuários", sqle);

        }
    }

    //create
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

        request.setCharacterEncoding("UTF-8");

        //aqui ele pega cada valor do formulário.

        String nome = request.getParameter("nome");
        String genero = request.getParameter("genero");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String telefone = request.getParameter("telefone");
        String dtNascimentoTexto = request.getParameter("dt_nascimento");

        if(nome==null || nome.isBlank() ||
            email==null || email.isBlank() ||
            senha==null || senha.isBlank() ||
            dtNascimentoTexto==null || dtNascimentoTexto.isBlank()){

            request.setAttribute("erro", "Preencha todos os campos obrigatórios.");
            request.getRequestDispatcher("/cadastro_usuario.jsp").forward(request, response);
            return;
        }

        try {
            LocalDate dtNascimento = LocalDate.parse(dtNascimentoTexto);
            LocalDate dtCadastro = LocalDate.now();

            if(usuarioDAO.existeEmail(email)){
                request.setAttribute("erro", "Este e-mail já está cadastrado.");
                request.getRequestDispatcher("/cadastro_usuario.jsp").forward(request,response);
                return;
                // Sem o return o código continuaria e tentaria cadastrar, e depois fazer um segundo redirect.
            }

            //monta o objeto model com os dados.
            Usuario usuario = new Usuario(nome, genero, email, senha, telefone,  dtNascimento, dtCadastro);

            if(usuarioDAO.cadastrarUsuario(usuario)){
                response.sendRedirect(request.getContextPath()+ "/usuarios");
            }else{
                request.setAttribute("erro", "Não foi possível cadastrar o usuário.");
                request.getRequestDispatcher("/cadastro_usuario.jsp").forward(request, response);
            }

        }catch (DateTimeParseException dtpe){
            request.setAttribute("erro", "Data de nascimento inválida.");
            request.getRequestDispatcher("/cadastro_usuario.jsp").forward(request,response);
        }
        catch (SQLException e){
            throw new ServletException("Erro ao cadastrar usuário: ", e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

        //Lê o corpo (linha por linha) da requisição.
        String corpo = request.getReader().lines().collect(Collectors.joining());


    }
}