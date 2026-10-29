<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Verificar código"/>
<c:set var="cssPagina" value="nova_senha.css"/>
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

            <h1 class="titulo">Confirme que é você</h1>
            <p class="mensagem-sucesso" role="status">Enviamos um código de 6 números para <b><c:out value="${sessionScope.usuarioPendente.email}"/></b>. Veja também a caixa de spam.</p>
            <form action="${ctx}/verificar-codigo" method="post">
                <div class="campo">
                    <label class="campo__rotulo" for="codigo">CÓDIGO</label>
                    <div class="campo__caixa">
                        <input id="codigo" name="codigo" type="text" inputmode="numeric" pattern="[0-9]{6}" maxlength="6" placeholder="000000" required autocomplete="one-time-code" autofocus>
                    </div>
                </div>
                <p class="mensagem-erro" role="alert"><c:out value="${erro}"/></p>
                <button class="botao botao--principal" type="submit">Entrar</button>
            </form>
            <p class="rodape-form">
                Não recebeu?
                <a href="${ctx}/verificar-codigo?reenviar=1">Enviar outro código</a>
            </p>

        </main>
    </section>
    <aside class="lado-colorido" aria-hidden="true">
        <span>Venus</span>
    </aside>
</body>
</html>
