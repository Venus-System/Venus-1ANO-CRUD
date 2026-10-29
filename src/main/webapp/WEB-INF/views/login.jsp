<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Login"/>
<c:set var="cssPagina" value="login.css"/>
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

            <h1 class="titulo">Login</h1>
            <p class="texto-intro">
                Seu perfil, histórico e favoritos continuam exatamente onde
                <br>
                você deixou.
            </p>

            <c:if test="${not empty sucesso}">
                <p class="mensagem-sucesso" role="status"><c:out value="${sucesso}"/></p>
            </c:if>
            <p class="mensagem-erro" role="alert"><c:out value="${erro}"/></p>

            <form action="${ctx}/login" method="post">
                <div class="campo">
                    <label class="campo__rotulo" for="email">E-MAIL</label>
                    <div class="campo__caixa">
                        <input id="email" name="email" type="email" placeholder="voce@email.com" required autocomplete="email"
                               value="<c:out value='${email}'/>">
                    </div>
                </div>
                <div class="campo">
                    <label class="campo__rotulo" for="senha">SENHA</label>
                    <div class="campo__caixa">
                        <input class="mostrar-senha" type="checkbox" id="ver-senha" aria-label="Mostrar ou ocultar senha">
                        <input class="campo__senha" id="senha" name="senha" type="password" placeholder="Sua senha" required autocomplete="current-password">
                        <label class="olho" for="ver-senha" title="Mostrar ou ocultar senha">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M2 12s3-6 10-6 10 6 10 6-3 6-10 6S2 12 2 12Z"/>
                                <circle cx="12" cy="12" r="3"/>
                                <path class="olho__risco" d="M3 3l18 18"/>
                            </svg>
                        </label>
                    </div>
                </div>
                <div class="opcoes">
                    <a href="${ctx}/esqueceu-a-senha">Esqueci minha senha</a>
                </div>
                <button class="botao botao--principal" type="submit">Entrar</button>
            </form>
            <p class="rodape-form">
                Ainda não tem conta?
                <a href="${ctx}/criar-conta">Criar conta grátis</a>
            </p>

        </main>
    </section>
    <aside class="lado-colorido" aria-hidden="true">
        <span>Venus</span>
    </aside>
    <script src="${ctx}/assets/js/acesso.js"></script>
</body>
</html>
