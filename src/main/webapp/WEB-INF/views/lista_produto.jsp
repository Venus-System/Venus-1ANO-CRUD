<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="tituloPagina" value="Produtos"/>
<c:set var="paginaAtiva" value="produtos"/>
<c:set var="descricaoTopo" value="Aqui você pode gerenciar os produtos cadastrados na plataforma."/>
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

<c:set var="veganos" value="0"/>
<c:set var="crueltyFree" value="0"/>
<c:forEach var="p" items="${produtos}">
    <c:if test="${p.ehVegano}"><c:set var="veganos" value="${veganos + 1}"/></c:if>
    <c:if test="${p.ehCrueltyFree}"><c:set var="crueltyFree" value="${crueltyFree + 1}"/></c:if>
</c:forEach>

<section class="cabecalho-pagina">
    <div>
        <h1 class="cabecalho-pagina__titulo">Produtos</h1>
        <p class="cabecalho-pagina__texto">Gerencie os produtos cadastrados na plataforma.</p>
    </div>
    <a class="btn btn--painel btn--roxo" href="${ctx}/produtos?acao=novo">
        <img src="${ctx}/assets/img/icones-crud/mais.svg" alt="">
        Adicionar produto
    </a>
</section>

<section class="resumo" aria-label="Resumo dos produtos">
    <article class="resumo__card">
        <span class="resumo__icone"><img src="${ctx}/assets/img/icones-crud/produtos-total.svg" alt=""></span>
        <div>
            <span class="resumo__rotulo">Produtos encontrados</span>
            <strong class="resumo__numero">${fn:length(produtos)}</strong>
        </div>
    </article>
    <article class="resumo__card">
        <span class="resumo__icone"><img src="${ctx}/assets/img/icones-crud/produtos-ativos.svg" alt=""></span>
        <div>
            <span class="resumo__rotulo">Veganos</span>
            <strong class="resumo__numero">${veganos}</strong>
        </div>
    </article>
    <article class="resumo__card">
        <span class="resumo__icone"><img src="${ctx}/assets/img/icones-crud/produtos-categorias.svg" alt=""></span>
        <div>
            <span class="resumo__rotulo">Cruelty free</span>
            <strong class="resumo__numero">${crueltyFree}</strong>
        </div>
    </article>
</section>

<div class="filtros">
    <label class="busca">
        <img src="${ctx}/assets/img/icones-crud/buscar.svg" alt="">
        <input type="search" data-busca="tabela-produtos" aria-label="Buscar produtos"
               placeholder="Buscar por nome, marca ou categoria...">
    </label>
</div>

<section class="tabela-painel">
    <div class="tabela-painel__rolagem">
        <table class="tabela" id="tabela-produtos">
            <caption class="sr-only">Lista de produtos</caption>
            <thead>
            <tr>
                <th scope="col">Produto</th>
                <th scope="col">Marca</th>
                <th scope="col">Categoria</th>
                <th scope="col">Vegano</th>
                <th scope="col">Cruelty free</th>
                <th scope="col">Pontuação</th>
                <th scope="col">Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="p" items="${produtos}">
                <tr>
                    <td>
                        <div class="celula-foto">
                            <img src="${ctx}/assets/img/icones-crud/produto-recente.svg" alt="">
                            <span>
                                <c:out value="${p.nome}"/>
                                <small class="celula-detalhe">Código: #${p.idProduto}</small>
                            </span>
                        </div>
                    </td>
                    <td><c:out value="${p.marca}"/></td>
                    <td><c:out value="${p.categoria}"/></td>
                    <td>
                        <span class="etiqueta ${p.ehVegano ? 'etiqueta--verde' : ''}">${p.ehVegano ? 'Sim' : 'Não'}</span>
                    </td>
                    <td>
                        <span class="etiqueta ${p.ehCrueltyFree ? 'etiqueta--verde' : ''}">${p.ehCrueltyFree ? 'Sim' : 'Não'}</span>
                    </td>
                    <td><span class="etiqueta ${p.pontuacao >= 80 ? 'etiqueta--verde' : (p.pontuacao >= 35 ? 'etiqueta--amarela' : 'etiqueta--rosa')}">${p.pontuacao}</span></td>
                    <td>
                        <div class="acoes">
                            <a class="botao-detalhes" href="${ctx}/produtoIngredientes?idProduto=${p.idProduto}">+ detalhes</a>
                            <a class="botao-icone" href="${ctx}/produtos?id=${p.idProduto}" title="Editar">
                                <img src="${ctx}/assets/img/icones-crud/editar.svg" alt="Editar ${fn:escapeXml(p.nome)}">
                            </a>
                            <button class="botao-icone botao-icone--excluir" type="button" title="Excluir"
                                    data-excluir-url="${ctx}/produtos?id=${p.idProduto}"
                                    data-excluir-nome="${fn:escapeXml(p.nome)}">
                                <img src="${ctx}/assets/img/icones-crud/excluir.svg" alt="Excluir ${fn:escapeXml(p.nome)}">
                            </button>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty produtos}">
                <tr>
                    <td colspan="7" class="tabela__vazia">Nenhum produto encontrado.</td>
                </tr>
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
