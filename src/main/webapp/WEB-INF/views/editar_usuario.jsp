<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar usuário</title>
</head>
<body>
    <h1>Editar usuário</h1>

    <form id="formEditar">
        <input type="hidden" id="idUsuario" value="${usuario.idUsuario}">

        <p>Nome: <input type="text" id="nome" value="${usuario.nomeCompleto}" required></p>
        <p>Gênero: <input type="text" id="genero" value="${usuario.genero}"></p>
        <p>Email: <input type="email" id="email" value="${usuario.email}" required></p>
        <p>Telefone: <input type="text" id="telefone" value="${usuario.telefone}"></p>
        <p>Nascimento: <input type="date" id="dt_nascimento" value="${usuario.dtNascimento}" required></p>

        <button type="submit">Salvar</button>
        <a href="${pageContext.request.contextPath}/usuarios">Cancelar</a>
    </form>

    <script>
        document.getElementById('formEditar').addEventListener('submit', async function (e) {
            e.preventDefault();

            var dados = {
                idUsuario: document.getElementById('idUsuario').value,
                nome: document.getElementById('nome').value,
                genero: document.getElementById('genero').value,
                email: document.getElementById('email').value,
                telefone: document.getElementById('telefone').value,
                dt_nascimento: document.getElementById('dt_nascimento').value
            };

            var resp = await fetch('${pageContext.request.contextPath}/usuarios', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(dados)
            });

            if (resp.ok) {
                window.location.href = '${pageContext.request.contextPath}/usuarios';
            } else {
                alert(await resp.text());
            }
        });
    </script>
</body>
</html>