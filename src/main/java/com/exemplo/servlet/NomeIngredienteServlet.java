package com.exemplo.servlet;


import com.exemplo.dao.NomeIngredienteDAO;
import com.exemplo.model.NomeIngrediente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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
                request.getRequestDispatcher("cadastro_nome_ingrediente.jsp").forward(request, response);

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
                    response.sendRedirect(request.getContextPath() + "/nomeIngrediente");
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

            ArrayList<NomeIngrediente> listaNomesIngredientes = nomeIngredienteDAO.read();
            request.setAttribute("nomesIngredientes", listaNomesIngredientes);
            request.getRequestDispatcher("/lista_nome_ingredientes.jsp").forward(request, response);

        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/nomeIngredientes");

        } catch (SQLException sqle) {
            throw new ServletException("Erro ao buscar nomes de ingredientes", sqle);
        }
    }

}
