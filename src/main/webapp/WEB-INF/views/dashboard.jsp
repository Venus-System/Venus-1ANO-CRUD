<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Dashboard"/>
<c:set var="paginaAtiva" value="dashboard"/>
<c:set var="descricaoTopo" value="Aqui está o resumo dos dados do sistema."/>
<c:set var="visualNovo" value="true"/>
<c:set var="cssPagina" value="dashboard.css"/>
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
        <h1 class="cabecalho-pagina__titulo">Dashboard</h1>
        <p class="cabecalho-pagina__texto">Visão geral da plataforma, com os números do banco de dados.</p>
    </div>
</section>

<section class="resumo" aria-label="Resumo da plataforma">
    <a class="resumo__card" href="${ctx}/usuarios">
        <span class="resumo__icone"><img src="${ctx}/assets/img/icones-crud/card-usuarios.svg" alt=""></span>
        <span>
            <span class="resumo__rotulo">Usuários cadastrados</span>
            <strong class="resumo__numero">${totalUsuarios}</strong>
        </span>
    </a>
    <a class="resumo__card" href="${ctx}/produtos">
        <span class="resumo__icone"><img src="${ctx}/assets/img/icones-crud/card-produtos.svg" alt=""></span>
        <span>
            <span class="resumo__rotulo">Produtos cadastrados</span>
            <strong class="resumo__numero">${totalProdutos}</strong>
        </span>
    </a>
    <a class="resumo__card" href="${ctx}/ingredientes">
        <span class="resumo__icone"><img src="${ctx}/assets/img/icones-crud/card-ingredientes.svg" alt=""></span>
        <span>
            <span class="resumo__rotulo">Ingredientes cadastrados</span>
            <strong class="resumo__numero">${totalIngredientes}</strong>
        </span>
    </a>
    <a class="resumo__card" href="${ctx}/analises">
        <span class="resumo__icone"><img src="${ctx}/assets/img/icones-crud/card-analises.svg" alt=""></span>
        <span>
            <span class="resumo__rotulo">Análises realizadas</span>
            <strong class="resumo__numero">${totalAnalises}</strong>
        </span>
    </a>
</section>

<div id="grade-dashboard">
    <section class="painel">
        <header class="painel__cabecalho">
            <h2 class="painel__titulo">Ingredientes por nível de perigo</h2>
            <a class="link-painel" href="${ctx}/ingredientes">Ver todos</a>
        </header>

        <ul class="medidores">
            <li class="medidor">
                <p class="medidor__texto">
                    <span>Seguros (1 e 2)</span>
                    <span class="legenda__valor">${ingredientesSeguros}</span>
                </p>
                <progress class="medidor__progresso medidor__progresso--verde" value="${ingredientesSeguros}" max="${totalIngredientes}"></progress>
            </li>
            <li class="medidor">
                <p class="medidor__texto">
                    <span>Em atenção (3)</span>
                    <span class="legenda__valor">${ingredientesAtencao}</span>
                </p>
                <progress class="medidor__progresso medidor__progresso--amarela" value="${ingredientesAtencao}" max="${totalIngredientes}"></progress>
            </li>
            <li class="medidor">
                <p class="medidor__texto">
                    <span>Alto risco (4 e 5)</span>
                    <span class="legenda__valor">${ingredientesAltoRisco}</span>
                </p>
                <progress class="medidor__progresso medidor__progresso--vermelha" value="${ingredientesAltoRisco}" max="${totalIngredientes}"></progress>
            </li>
        </ul>
    </section>

    <section class="painel">
        <header class="painel__cabecalho">
            <h2 class="painel__titulo">Últimos produtos cadastrados</h2>
            <a class="link-painel" href="${ctx}/produtos">Ver todos</a>
        </header>

        <ul class="produtos-recentes">
            <c:forEach var="p" items="${ultimosProdutos}">
                <li class="produto-recente">
                    <img src="${ctx}/assets/img/icones-crud/produto-recente.svg" alt="">
                    <span>
                        <c:out value="${p.nome}"/>
                        <small class="celula-detalhe"><c:out value="${p.marca}"/> · <c:out value="${p.categoria}"/></small>
                    </span>
                    <span class="etiqueta ${p.pontuacao >= 80 ? 'etiqueta--verde' : (p.pontuacao >= 35 ? 'etiqueta--amarela' : 'etiqueta--rosa')}">Nota ${p.pontuacao}</span>
                </li>
            </c:forEach>
            <c:if test="${empty ultimosProdutos}">
                <li class="produto-recente">Nenhum produto cadastrado ainda.</li>
            </c:if>
        </ul>
    </section>
</div>

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
