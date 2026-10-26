<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Nova senha"/>
<c:set var="cssPagina" value="nova_senha.css"/>
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

            <h1 class="titulo">Crie uma senha nova</h1>
            <form id="formulario-senha" action="${ctx}/senha-alterada" method="get">
                <div class="campo">
                    <label class="campo__rotulo" for="senha">NOVA SENHA</label>
                    <div class="campo__caixa">
                        <input class="mostrar-senha" type="checkbox" id="ver-senha" aria-label="Mostrar ou ocultar nova senha">
                        <input class="campo__senha" id="senha" type="password" placeholder="Mínimo 6 caracteres" required minlength="6" autocomplete="off">
                        <label class="olho" for="ver-senha" title="Mostrar ou ocultar senha">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M2 12s3-6 10-6 10 6 10 6-3 6-10 6S2 12 2 12Z"/>
                            <circle cx="12" cy="12" r="3"/>
                        </svg>
                        </label>
                    </div>
                </div>
                <div class="campo">
                    <label class="campo__rotulo" for="confirmar-senha">CONFIRME A SENHA</label>
                    <div class="campo__caixa">
                        <input class="mostrar-senha" type="checkbox" id="ver-confirmacao" aria-label="Mostrar ou ocultar confirmação da senha">
                        <input class="campo__senha" id="confirmar-senha" type="password" placeholder="Repita a senha" required minlength="6" autocomplete="off">
                        <label class="olho" for="ver-confirmacao" title="Mostrar ou ocultar senha">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M2 12s3-6 10-6 10 6 10 6-3 6-10 6S2 12 2 12Z"/>
                            <circle cx="12" cy="12" r="3"/>
                        </svg>
                        </label>
                    </div>
                </div>
                <p id="erro-senha" class="mensagem-erro" role="alert"></p>
                <button class="botao botao--principal" type="submit">Salvar nova senha</button>
            </form>

        </main>
    </section>
    <aside class="lado-colorido" aria-hidden="true">
        <span>Venus</span>
    </aside>
    <script src="${ctx}/assets/js/acesso.js"></script>
</body>
</html>
