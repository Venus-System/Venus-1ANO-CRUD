<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de usuários</title>
</head>

<p><a href="${pageContext.request.contextPath}/cadastro_usuario.jsp">Novo usuário</a></p>

<script>
    async function excluir(id) {
        if (!confirm('Excluir este usuário?')) return;

        var resp = await fetch('${pageContext.request.contextPath}/usuarios?id=' + id, {
            method: 'DELETE'
        });

        if (resp.ok) {
            location.reload();
        } else {
            alert(await resp.text());
        }
    }
</script>
<body>
    <h1>Usuários</h1>
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Nome completo</th>
            <th>Gênero</th>
            <th>Email</th>
            <th>Telefone</th>
            <th>Nascimento</th>
            <th>Cadastro</th>
        </tr>
        <c:forEach var="u" items="${usuarios}">
            <tr>
                <td>${u.idUsuario}</td>
                <td>${u.nomeCompleto}</td>
                <td>${u.genero}</td>
                <td>${u.email}</td>
                <td>${u.telefone}</td>
                <td>${u.dtNascimento}</td>
                <td>${u.dtCadastro}</td>

                <td>
                    <a href="${pageContext.request.contextPath}/usuarios?id=${u.idUsuario}">Editar</a>
                    <button type="button" onclick="excluir(${u.idUsuario})">Excluir</button>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>