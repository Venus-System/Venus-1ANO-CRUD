<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Senha alterada"/>
<c:set var="cssPagina" value="senha_alterada.css"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${tituloPagina}"/> · Venus</title>
    <link rel="icon" href="${ctx}/assets/img/favicon.svg" type="image/svg+xml">
    <link rel="stylesheet" href="${ctx}/assets/css/acesso.css">
    <link rel="stylesheet" href="${ctx}/assets/css/paginas/${cssPagina}">
</head>
<body>
    <section class="lado-formulario">
        <a class="marca" href="${ctx}/" aria-label="Venus, voltar para a página inicial">
            <img src="${ctx}/assets/img/logo-acesso.png" alt="">
            <span>Venus</span>
        </a>
        <main class="conteudo">

            <div class="icone-certo" aria-hidden="true">✓</div>
            <h1 class="titulo">
                Senha trocada.
                <br>
                <span>Que bom te ver de volta</span>
            </h1>
            <p class="texto-intro">Sua conta está segura. Entre com a senha nova e continue de onde parou.</p>
            <a class="botao botao--principal" href="${ctx}/login">Ir para o login</a>

        </main>
    </section>
    <aside class="lado-colorido" aria-hidden="true">
        <span>Venus</span>
    </aside>
    <script src="${ctx}/assets/js/acesso.js"></script>
</body>
</html>
