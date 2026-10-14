<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cadastro de Usuário</title>
    <%-- <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css"> --%>
</head>
<body>
    <h1>Cadastro de Usuário</h1>

    <c:if test="${not empty erro}">
        <p class="erro" style="color:red"><c:out value="${erro}"/></p>
    </c:if>

    <%-- NÃO MUDAR: action, method e os name dos campos (iguais ao doPost) --%>
    <form action="${pageContext.request.contextPath}/usuarios" method="post">

        <label for="nome">Nome completo *</label>
        <input type="text" id="nome" name="nome" value="<c:out value='${param.nome}'/>" required>

        <label for="genero">Gênero</label>
        <select id="genero" name="genero">
            <option value="">Selecione</option>
            <option value="Feminino"  ${param.genero == 'Feminino'  ? 'selected' : ''}>Feminino</option>
            <option value="Masculino" ${param.genero == 'Masculino' ? 'selected' : ''}>Masculino</option>
            <option value="Outro"     ${param.genero == 'Outro'     ? 'selected' : ''}>Outro</option>
        </select>

        <label for="email">E-mail *</label>
        <input type="email" id="email" name="email" value="<c:out value='${param.email}'/>" required>

        <label for="senha">Senha *</label>
        <input type="password" id="senha" name="senha" required>

        <label for="telefone">Telefone</label>
        <input type="tel" id="telefone" name="telefone" value="<c:out value='${param.telefone}'/>">

        <label for="dt_nascimento">Data de nascimento *</label>
        <input type="date" id="dt_nascimento" name="dt_nascimento" value="<c:out value='${param.dt_nascimento}'/>" required>

        <button type="submit">Cadastrar</button>
    </form>

    <a href="${pageContext.request.contextPath}/usuarios">Voltar para a lista</a>
</body>
</html>