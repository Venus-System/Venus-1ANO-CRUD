<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Esqueceu a senha"/>
<c:set var="cssPagina" value="esqueceu_a_senha.css"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${tituloPagina}"/> · Venus</title>
    <link rel="icon" href="${ctx}/assets/img/favicon.svg" type="image/svg+xml">
    <link rel="stylesheet" href="${ctx}/assets/css/global.css">
    <link rel="stylesheet" href="${ctx}/assets/css/acesso.css">
    <link rel="stylesheet" href="${ctx}/assets/css/paginas/${cssPagina}">
</head>
<body>
    <section class="lado-formulario">
        <a class="marca" href="${ctx}/" aria-label="Venus, voltar para a página inicial">
            <img src="${ctx}/assets/img/logo-roxa.png" alt="">
            <span>Venus</span>
        </a>
        <main class="conteudo">

            <h1 class="titulo">Esqueceu a senha?</h1>
            <p class="texto-intro">
                Digite o e-mail da sua conta. Enviaremos um código para você
                <br>
                criar uma senha nova.
            </p>
            <c:if test="${param.expirado == '1'}">
                <p class="mensagem-erro" role="alert">Esse código expirou. Peça um novo abaixo.</p>
            </c:if>
            <c:if test="${not empty erro}">
                <p class="mensagem-erro" role="alert"><c:out value="${erro}"/></p>
            </c:if>
            <form action="${ctx}/esqueceu-a-senha" method="post">
                <div class="campo">
                    <label class="campo__rotulo" for="email">E-MAIL</label>
                    <div class="campo__caixa">
                        <input id="email" name="email" type="email" value="<c:out value='${email}'/>" placeholder="voce@email.com" required autocomplete="email">
                    </div>
                </div>
                <button class="botao botao--principal" type="submit">Enviar código</button>
            </form>
            <p class="rodape-form">
                Lembrou a senha?
                <a href="${ctx}/login">Entrar</a>
            </p>

        </main>
    </section>
    <aside class="lado-colorido" aria-hidden="true">
        <span>Venus</span>
    </aside>
    <script src="${ctx}/assets/js/acesso.js"></script>
</body>
</html>
