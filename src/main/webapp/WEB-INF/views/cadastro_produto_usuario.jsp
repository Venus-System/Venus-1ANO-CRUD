<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Vincular produto"/>
<c:set var="paginaAtiva" value="usuarios"/>
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

<div class="page-heading">
    <div>
        <h1>Vincular produto ao usuário</h1>
        <p>Escolha o produto e o usuário que serão vinculados.</p>
    </div>
</div>

<div class="panel white form-card">
    <c:if test="${not empty erro}">
        <div class="alert-erro" role="alert"><c:out value="${erro}"/></div>
    </c:if>

    <form method="post" action="${ctx}/produtoUsuarios">
        <div class="form-field">
            <label for="id_produto">Produto</label>
            <select id="id_produto" name="id_produto" required>
                <option value="">Selecione um produto</option>
                <c:forEach var="p" items="${produtos}">
                    <option value="${p.idProduto}" ${param.id_produto eq p.idProduto ? 'selected' : ''}>
                        <c:out value="${p.nome}"/>
                    </option>
                </c:forEach>
            </select>
        </div>

        <c:choose>
            <c:when test="${param.fixo == '1'}">
                <input type="hidden" name="fixo" value="1">
                <input type="hidden" name="id_usuario" value="<c:out value='${param.id_usuario}'/>">
                <div class="form-field">
                    <label for="usuario-fixo">Usuário</label>
                    <c:forEach var="u" items="${usuarios}">
                        <c:if test="${param.id_usuario == u.idUsuario.toString()}">
                            <input id="usuario-fixo" type="text" value="<c:out value='${u.nomeCompleto}'/>" readonly>
                        </c:if>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="form-field">
                    <label for="id_usuario">Usuário</label>
                    <select id="id_usuario" name="id_usuario" required>
                        <option value="">Selecione um usuário</option>
                        <c:forEach var="u" items="${usuarios}">
                            <option value="${u.idUsuario}" ${param.id_usuario eq u.idUsuario ? 'selected' : ''}>
                                <c:out value="${u.nomeCompleto}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </c:otherwise>
        </c:choose>

        <div class="form-actions">
            <c:choose>
                <c:when test="${param.fixo == '1'}">
                    <a class="btn secondary" href="${ctx}/usuarioDetalhe?id=${fn:escapeXml(param.id_usuario)}#produtos">Cancelar</a>
                </c:when>
                <c:otherwise>
                    <a class="btn secondary" href="${ctx}/produtoUsuarios">Cancelar</a>
                </c:otherwise>
            </c:choose>
            <button type="submit" class="btn primary">Cadastrar</button>
        </div>
    </form>
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
