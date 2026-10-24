<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Criar conta"/>
<c:set var="cssPagina" value="criar_conta.css"/>
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

            <h1 class="titulo">Crie sua conta<em>.</em></h1>
            <p class="texto-intro">
                Com uma conta, cada análise passa a ser sobre a sua pele,
                <br>
                sua saúde e seus valores.
            </p>

            <p class="mensagem-erro" role="alert"><c:out value="${erro}"/></p>

            <form action="${ctx}/criar-conta" method="post">
                <div class="campo">
                    <label class="campo__rotulo" for="nome">NOME</label>
                    <div class="campo__caixa">
                        <input id="nome" name="nome" type="text" placeholder="Seu nome completo" required maxlength="100" autocomplete="name"
                               value="<c:out value='${param.nome}'/>">
                    </div>
                </div>
                <div class="campo">
                    <label class="campo__rotulo" for="email">E-MAIL</label>
                    <div class="campo__caixa">
                        <input id="email" name="email" type="email" placeholder="voce@email.com" required maxlength="100" autocomplete="email"
                               value="<c:out value='${param.email}'/>">
                    </div>
                </div>
                <div class="campo">
                    <label class="campo__rotulo" for="telefone">TELEFONE</label>
                    <div class="campo__caixa">
                        <input id="telefone" name="telefone" type="tel" placeholder="(11) 91234-5678" required maxlength="20" autocomplete="tel"
                               value="<c:out value='${param.telefone}'/>">
                    </div>
                </div>
                <div class="campo">
                    <label class="campo__rotulo" for="dt_nascimento">DATA DE NASCIMENTO</label>
                    <div class="campo__caixa">
                        <input id="dt_nascimento" name="dt_nascimento" type="date" required min="1900-01-02" max="${LocalDate.now().minusYears(18)}" autocomplete="bday"
                               value="<c:out value='${param.dt_nascimento}'/>">
                    </div>
                </div>
                <div class="campo">
                    <label class="campo__rotulo" for="senha">SENHA</label>
                    <div class="campo__caixa">
                        <input class="mostrar-senha" type="checkbox" id="ver-senha" aria-label="Mostrar ou ocultar senha">
                        <input class="campo__senha" id="senha" name="senha" type="password" placeholder="Mínimo 6 caracteres" required minlength="6" autocomplete="new-password">
                        <label class="olho" for="ver-senha" title="Mostrar ou ocultar senha">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M2 12s3-6 10-6 10 6 10 6-3 6-10 6S2 12 2 12Z"/>
                                <circle cx="12" cy="12" r="3"/>
                            </svg>
                        </label>
                    </div>
                </div>
                <div class="forca-senha">
                    <div class="forca-senha__barras">
                        <i></i>
                        <i></i>
                        <i></i>
                        <i></i>
                    </div>
                    <span>Use 6 ou mais caracteres. Você precisa ter 18 anos ou mais.</span>
                </div>
                <button class="botao botao--principal botao--cadastrar" type="submit">Criar minha conta</button>
            </form>
            <p class="rodape-form">
                Já tem conta?
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
