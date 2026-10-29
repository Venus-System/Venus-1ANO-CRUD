<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
        <h1>Ingredientes da Análise</h1>
        <p>
            <c:choose>
                <c:when test="${not empty param.idAnalise}">Ingredientes vinculados à análise ${param.idAnalise}.</c:when>
                <c:when test="${not empty param.idIngrediente}">Análises que contêm o ingrediente ${param.idIngrediente}.</c:when>
                <c:otherwise>Todos os vínculos entre ingredientes e análises.</c:otherwise>
            </c:choose>
        </p>
    </div>
    <div class="heading-actions">
        <c:if test="${not empty param.idAnalise}">
            <a class="btn" href="${pageContext.request.contextPath}/analises">Voltar</a>
        </c:if>
        <a class="btn primary"
           href="${pageContext.request.contextPath}/ingredienteAnalises?acao=novo&idAnalise=${param.idAnalise}">
            + Vincular ingrediente
        </a>
    </div>
</div>

<div class="table-panel">
    <div class="table-scroll">
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Ingrediente</th>
                <th>Análise</th>
                <th>Tipo</th>
                <th>Nível de perigo</th>
                <th>Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="ia" items="${ingredientesAnalises}">
                <tr>
                    <td>${ia.idIngredienteAnalise}</td>
                    <td>#${ia.idIngrediente}</td>
                    <td>#${ia.idAnalise}</td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty ia.tipo}"><c:out value="${ia.tipo}"/></c:when>
                            <c:otherwise>—</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${empty ia.tipo}">—</c:when>
                            <c:when test="${ia.nivelPerigo >= 7}"><span class="badge pink">${ia.nivelPerigo}</span></c:when>
                            <c:when test="${ia.nivelPerigo >= 4}"><span class="badge yellow">${ia.nivelPerigo}</span></c:when>
                            <c:otherwise><span class="badge green">${ia.nivelPerigo}</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <div class="actions">
                            <button type="button" class="icon-btn" title="Excluir vínculo"
                                    onclick="excluirVinculo(${ia.idIngredienteAnalise})">🗑</button>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty ingredientesAnalises}">
                <tr><td colspan="6" class="empty">Nenhum ingrediente vinculado.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<script>

    function excluirVinculo(id) {
        if (!confirm('Remover este ingrediente da análise?')) return;

        fetch('${pageContext.request.contextPath}/ingredienteAnalises?id=' + id, {method: 'DELETE'})
            .then(function (resp) {
                if (resp.ok) {
                    location.reload();
                } else {
                    resp.text().then(function (msg) { abrirPopupErro(msg || 'Não foi possível excluir.'); });
                }
            })
            .catch(function () { abrirPopupErro('Erro de comunicação com o servidor.'); });
    }
</script>

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
