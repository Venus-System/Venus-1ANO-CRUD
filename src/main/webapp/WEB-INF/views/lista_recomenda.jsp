<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="tituloPagina" value="Produtos recomendados"/>
<c:set var="paginaAtiva" value="usuarios"/>
<c:set var="descricaoTopo" value="Produtos recomendados para cada usuário."/>
<c:set var="visualNovo" value="true"/>
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

    <link rel="stylesheet" href="${ctx}/assets/fonts.css">

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
        <h1 class="cabecalho-pagina__titulo">Produtos recomendados</h1>
        <p class="cabecalho-pagina__texto">
            <c:choose>
                <c:when test="${not empty param.idUsuario}">Produtos recomendados para o usuário #<c:out value="${param.idUsuario}"/>.</c:when>
                <c:otherwise>Todos os produtos recomendados nas análises.</c:otherwise>
            </c:choose>
        </p>
    </div>
    <a class="btn btn--painel btn--roxo" href="${ctx}/recomendas?acao=novo${not empty param.idUsuario ? '&idUsuario='.concat(fn:escapeXml(param.idUsuario)) : ''}">
        <img src="${ctx}/assets/img/icones-crud/mais.svg" alt="">
        Recomendar produto
    </a>
</section>

<section class="resumo" aria-label="Resumo">
    <article class="resumo__card">
        <span class="resumo__icone"><img src="${ctx}/assets/img/icones-crud/produtos-total.svg" alt=""></span>
        <div>
            <span class="resumo__rotulo">Produtos recomendados</span>
            <strong class="resumo__numero">${fn:length(recomendas)}</strong>
        </div>
    </article>
</section>

<div class="filtros">
    <label class="busca">
        <img src="${ctx}/assets/img/icones-crud/buscar.svg" alt="">
        <input type="search" data-busca="tabela-recomenda" aria-label="Buscar"
               placeholder="Buscar por usuário, produto ou marca...">
    </label>
    <c:if test="${not empty param.idUsuario}">
        <a class="btn btn--painel" href="${ctx}/recomendas">Ver todos</a>
    </c:if>
</div>

<section class="tabela-painel">
    <div class="tabela-painel__rolagem">
        <table class="tabela" id="tabela-recomenda">
            <caption class="sr-only">Produtos recomendados nas análises</caption>
            <thead>
            <tr>
                <th scope="col">Usuário</th>
                <th scope="col">Produto</th>
                <th scope="col">Marca</th>
                <th scope="col">Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="r" items="${recomendas}">
                <tr>
                    <td><a href="${ctx}/recomendas?idUsuario=${r.idUsuario}"><c:out value="${empty r.nomeUsuario ? 'Usuário #'.concat(r.idUsuario) : r.nomeUsuario}"/></a></td>
                    <td><c:out value="${r.nomeProduto}"/></td>
                    <td><c:out value="${empty r.marcaProduto ? '—' : r.marcaProduto}"/></td>
                    <td>
                        <div class="acoes">
                            <a class="botao-icone" href="${ctx}/recomendas?idUsuario=${r.idUsuario}&idProduto=${r.idProduto}" title="Editar">
                                <img src="${ctx}/assets/img/icones-crud/editar.svg" alt="Editar">
                            </a>
                            <button class="botao-icone botao-icone--excluir" type="button" title="Excluir"
                                    data-excluir-url="${ctx}/recomendas?idUsuario=${r.idUsuario}&idProduto=${r.idProduto}"
                                    data-excluir-nome="${fn:escapeXml(r.nomeProduto)} para ${fn:escapeXml(r.nomeUsuario)}">
                                <img src="${ctx}/assets/img/icones-crud/excluir.svg" alt="Excluir">
                            </button>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty recomendas}">
                <tr><td colspan="4" class="tabela__vazia">Nenhum produto recomendado.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</section>

<dialog id="popup-excluir" class="popup" aria-labelledby="titulo-excluir">
    <header class="popup__topo">
        <h2 id="titulo-excluir" class="popup__titulo">Excluir registro?</h2>
        <button class="popup__fechar" type="button" aria-label="Fechar janela" data-fechar-popup>
            <img src="${ctx}/assets/img/icones-crud/fechar.svg" alt="">
        </button>
    </header>

    <p class="registro-selecionado"></p>
    <p class="popup__texto">Essa ação não pode ser desfeita.</p>

    <p class="popup__erro" role="alert" hidden></p>

    <div class="popup__botoes">
        <button class="btn btn--painel" type="button" data-fechar-popup>Cancelar</button>
        <button class="btn btn--painel btn--perigo" type="button" data-confirmar-exclusao>Sim, excluir</button>
    </div>
</dialog>
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
