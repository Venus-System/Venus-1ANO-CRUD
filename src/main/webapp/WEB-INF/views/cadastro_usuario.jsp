<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Novo usuário"/>
<c:set var="paginaAtiva" value="usuarios"/>
<c:set var="descricaoTopo" value="Aqui você pode gerenciar as pessoas que usam a plataforma."/>
<c:set var="visualNovo" value="true"/>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="primeiroNome" value="${fn:substringBefore(sessionScope.usuarioLogado.nomeCompleto.concat(' '), ' ')}"/>
<c:if test="${empty primeiroNome}"><c:set var="primeiroNome" value="Visitante"/></c:if>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${tituloPagina}"/> | Venus</title>
    <link rel="icon" href="${ctx}/assets/img/favicon.svg" type="image/svg+xml">

    <link rel="stylesheet" href="${ctx}/assets/css/global.css">
    <link rel="stylesheet" href="${ctx}/assets/css/painel.css">
    <link rel="stylesheet" href="${ctx}/assets/css/paginas/crud.css">

    <c:if test="${not empty cssPagina}">
        <link rel="stylesheet" href="${ctx}/assets/css/paginas/${cssPagina}">
    </c:if>

    <c:if test="${visualNovo != 'true'}">
        <link rel="stylesheet" href="${ctx}/assets/css/venus.css">
    </c:if>

    <script src="${ctx}/assets/js/painel.js" defer></script>
</head>
<body class="pagina-painel" data-usuario="${sessionScope.usuarioLogado.idUsuario}">
<div id="layout">

    <button class="menu-fundo" type="button" aria-label="Fechar menu" tabindex="-1"></button>

    <aside id="menu-lateral">
        <button class="fechar-menu" type="button">Fechar menu</button>

        <a class="menu-lateral__marca" href="${ctx}/dashboard">
            <img src="${ctx}/assets/img/icones-crud/logo-painel.svg" alt="" width="43" height="47">
            <span>
                <span class="menu-lateral__nome">Venus</span>
                <span class="menu-lateral__subtitulo">Painel Administrativo</span>
            </span>
        </a>

        <nav aria-label="Menu do painel">
            <ul class="menu-lateral__lista">
                <li>
                    <a class="menu-lateral__link" href="${ctx}/dashboard" ${paginaAtiva == 'dashboard' ? 'aria-current="page"' : ''}>
                        <img src="${ctx}/assets/img/icones-crud/menu-dashboard.svg" alt="">
                        Dashboard
                    </a>
                </li>
                <li>
                    <a class="menu-lateral__link" href="${ctx}/usuarios" ${paginaAtiva == 'usuarios' ? 'aria-current="page"' : ''}>
                        <img src="${ctx}/assets/img/icones-crud/menu-usuarios.svg" alt="">
                        Usuários
                    </a>
                </li>
                <li>
                    <a class="menu-lateral__link" href="${ctx}/produtos" ${paginaAtiva == 'produtos' ? 'aria-current="page"' : ''}>
                        <img src="${ctx}/assets/img/icones-crud/menu-produtos.svg" alt="">
                        Produtos
                    </a>
                </li>
                <li>
                    <a class="menu-lateral__link" href="${ctx}/ingredientes" ${paginaAtiva == 'ingredientes' ? 'aria-current="page"' : ''}>
                        <img src="${ctx}/assets/img/icones-crud/menu-ingredientes.svg" alt="">
                        Ingredientes
                    </a>
                </li>
                <li>
                    <a class="menu-lateral__link" href="${ctx}/analises" ${paginaAtiva == 'analises' ? 'aria-current="page"' : ''}>
                        <img src="${ctx}/assets/img/icones-crud/card-analises.svg" alt="">
                        Análises
                    </a>
                </li>
                <li>
                    <a class="menu-lateral__link" href="${ctx}/alergias" ${paginaAtiva == 'alergias' ? 'aria-current="page"' : ''}>
                        <img src="${ctx}/assets/img/icones-crud/ingredientes-atencao.svg" alt="">
                        Alergias
                    </a>
                </li>
                <li>
                    <a class="menu-lateral__link" href="${ctx}/admins" ${paginaAtiva == 'admins' ? 'aria-current="page"' : ''}>
                        <img src="${ctx}/assets/img/icones-crud/usuarios-admin.svg" alt="">
                        Admins
                    </a>
                </li>
                <li>
                    <a class="menu-lateral__link" href="${ctx}/perfil" ${paginaAtiva == 'perfil' ? 'aria-current="page"' : ''}>
                        <img src="${ctx}/assets/img/icones-crud/conta.svg" alt="">
                        Perfil
                    </a>
                </li>
            </ul>
        </nav>

        <a class="menu-lateral__link menu-lateral__sair" href="${ctx}/sair">
            <img src="${ctx}/assets/img/icones-crud/sair.svg" alt="">
            Sair
        </a>
    </aside>

    <div id="area-principal">
        <header class="topo">
            <button class="botao-hamburguer" type="button" aria-label="Abrir menu"
                    aria-controls="menu-lateral" aria-expanded="false">
                <span></span>
                <span></span>
                <span></span>
            </button>

            <div class="topo__texto">

                <p class="topo__saudacao">Olá, <span data-perfil-nome="${fn:escapeXml(primeiroNome)}"><c:out value="${primeiroNome}"/></span>!</p>
                <small class="topo__descricao">
                    <c:out value="${empty descricaoTopo ? 'Aqui você gerencia os dados da plataforma.' : descricaoTopo}"/>
                </small>
            </div>

            <a class="topo__perfil" href="${ctx}/perfil" aria-label="Abrir meu perfil">
                <img class="topo__perfil-foto" src="${ctx}/assets/img/icones-crud/avatar.svg" alt="" width="34" height="34">
                <span class="topo__perfil-nome">
                    <span data-perfil-nome="${fn:escapeXml(primeiroNome)}"><c:out value="${primeiroNome}"/></span>
                    <small class="topo__perfil-cargo">Admin</small>
                </span>
            </a>
        </header>

        <main id="conteudo">

<section class="cabecalho-pagina">
    <div>
        <h1 class="cabecalho-pagina__titulo">Adicionar usuário</h1>
        <p class="cabecalho-pagina__texto">Campos com * são obrigatórios.</p>
    </div>
</section>

<section class="painel formulario">

    <c:if test="${not empty erro}">
        <p class="alerta-erro" role="alert"><c:out value="${erro}"/></p>
    </c:if>

    <form method="post" action="${ctx}/usuarios">
        <label class="campo">
            Nome completo *
            <input type="text" name="nome" maxlength="100" autocomplete="name" required
                   value="<c:out value='${param.nome}'/>">
        </label>

        <div class="campos-linha">
            <label class="campo">
                E-mail *
                <input type="email" name="email" maxlength="100" autocomplete="email" required
                       value="<c:out value='${param.email}'/>">
            </label>

            <label class="campo">
                Gênero
                <select name="genero">
                    <option value="">Selecione</option>
                    <option value="Feminino" ${param.genero == 'Feminino' ? 'selected' : ''}>Feminino</option>
                    <option value="Masculino" ${param.genero == 'Masculino' ? 'selected' : ''}>Masculino</option>
                    <option value="Outro" ${param.genero == 'Outro' ? 'selected' : ''}>Outro</option>
                    <option value="Prefiro não informar" ${param.genero == 'Prefiro não informar' ? 'selected' : ''}>Prefiro não informar</option>
                </select>
            </label>
        </div>

        <label class="campo">
            Senha *
            <input type="password" name="senha" minlength="6" maxlength="72" autocomplete="new-password" required>
            <span class="campo__dica">Mínimo de 6 caracteres. Ela é guardada no banco como hash (BCrypt).</span>
        </label>

        <div class="campos-linha">
            <label class="campo">
                Telefone *
                <input type="tel" name="telefone" autocomplete="tel" required placeholder="(11) 91234-5678"
                       value="<c:out value='${param.telefone}'/>">
                <span class="campo__dica">Com DDD. O sistema guarda só os números.</span>
            </label>
            <label class="campo">
                Data de nascimento *
                <input type="date" name="dt_nascimento" required min="1900-01-02" max="${LocalDate.now().minusYears(18)}"
                       value="<c:out value='${param.dt_nascimento}'/>">
                <span class="campo__dica">É preciso ter pelo menos 18 anos (e ter nascido depois de 1900).</span>
            </label>
        </div>

        <div class="formulario__botoes">
            <a class="btn btn--painel" href="${ctx}/usuarios">Cancelar</a>
            <button class="btn btn--painel btn--roxo" type="submit">Salvar usuário</button>
        </div>
    </form>
</section>

        </main>

        <footer class="rodape-painel">
            &copy; 2026 Venus · Painel administrativo
        </footer>
    </div>
</div>

<dialog id="popup-erro" class="popup" aria-labelledby="titulo-erro">
    <header class="popup__topo">
        <h2 id="titulo-erro" class="popup__titulo">Algo deu errado</h2>
        <button class="popup__fechar" type="button" aria-label="Fechar janela" data-fechar-popup>
            <img src="${ctx}/assets/img/icones-crud/fechar.svg" alt="">
        </button>
    </header>
    <p id="texto-erro" class="popup__texto"></p>
    <div class="popup__botoes">
        <button class="btn btn--painel btn--roxo" type="button" data-fechar-popup>Entendi</button>
    </div>
</dialog>
</body>
</html>
