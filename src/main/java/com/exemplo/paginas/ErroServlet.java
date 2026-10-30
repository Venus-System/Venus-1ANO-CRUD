package com.exemplo.paginas;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/erro")
public class ErroServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Throwable erro = (Throwable) request.getAttribute("jakarta.servlet.error.exception");
        String mensagem = "Algo deu errado ao abrir esta página. Tente de novo.";

        while (erro != null) {
            if (erro instanceof SQLException) {
                String estado = ((SQLException) erro).getSQLState();
                if (estado != null && estado.startsWith("28")) {
                    mensagem = "O banco de dados recusou o usuário ou a senha. "
                            + "Confira DB_USER e DB_PASSWORD no arquivo src/main/resources/.env e reinicie o Tomcat.";
                } else if (estado != null && estado.startsWith("08")) {
                    mensagem = "Não foi possível conectar ao banco de dados. "
                            + "Confira a internet e o DB_URL no arquivo src/main/resources/.env.";
                }
            }
            erro = erro.getCause();
        }

        response.setStatus(500);
        request.setAttribute("mensagemErro", mensagem);
        request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
    }
}
