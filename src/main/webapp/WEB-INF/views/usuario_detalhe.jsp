<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="tituloPagina" value="Usuário"/>
<c:set var="paginaAtiva" value="usuarios"/>
<c:set var="descricaoTopo" value="Tudo o que está ligado a este usuário, separado por assunto."/>
<c:set var="visualNovo" value="true"/>
<c:set var="cssPagina" value="usuario.css"/>
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

<c:set var="totalPele" value="${empty perfilPele ? 0 : 1}"/>
<c:set var="totalCabelo" value="${empty perfilCabelo ? 0 : 1}"/>

<a class="link-voltar" href="${ctx}/usuarios">← Voltar para usuários</a>

<section class="perfil-usuario painel">
    <img class="perfil-usuario__foto" src="${ctx}/assets/img/icones-crud/avatar.svg" alt="">
    <div class="perfil-usuario__dados">
        <h1 class="perfil-usuario__nome"><c:out value="${usuario.nomeCompleto}"/></h1>
        <p class="perfil-usuario__info">
            <span><c:out value="${usuario.email}"/></span>
            <span><c:out value="${usuario.telefone}"/></span>
            <span><c:out value="${empty usuario.genero ? 'Gênero não informado' : usuario.genero}"/></span>
            <span>Nascimento: <span data-data="${usuario.dtNascimento}">${usuario.dtNascimento}</span></span>
        </p>
    </div>
    <a class="btn btn--painel" href="${ctx}/usuarios?id=${usuario.idUsuario}">
        <img src="${ctx}/assets/img/icones-crud/editar.svg" alt="">
        Editar usuário
    </a>
    <a class="btn btn--painel" href="${ctx}/recomendas?idUsuario=${usuario.idUsuario}">
        Recomendações
    </a>
</section>

<div class="abas" role="tablist" aria-label="Dados do usuário">
    <button class="aba" type="button" role="tab" data-aba="alergias">
        Alergias <span class="aba__total">${fn:length(alergias)}</span>
    </button>
    <button class="aba" type="button" role="tab" data-aba="produtos">
        Produtos <span class="aba__total">${fn:length(produtos)}</span>
    </button>
    <button class="aba" type="button" role="tab" data-aba="preferencias">
        Preferências <span class="aba__total">${fn:length(preferencias)}</span>
    </button>
    <button class="aba" type="button" role="tab" data-aba="pele">
        Pele <span class="aba__total">${totalPele}</span>
    </button>
    <button class="aba" type="button" role="tab" data-aba="cabelo">
        Cabelo <span class="aba__total">${totalCabelo}</span>
    </button>
    <button class="aba" type="button" role="tab" data-aba="analises">
        Análises <span class="aba__total">${fn:length(analises)}</span>
    </button>
</div>

<section class="conteudo-aba painel" id="aba-alergias" role="tabpanel" hidden>
    <div class="painel__cabecalho">
        <h2 class="painel__titulo">Alergias (${fn:length(alergias)})</h2>
        <a class="btn btn--painel btn--roxo" href="${ctx}/usuarioAlergias?acao=novo&idUsuario=${usuario.idUsuario}">Vincular alergia</a>
    </div>
    <c:choose>
        <c:when test="${empty alergias}">
            <p class="aba-vazia">Nenhuma alergia cadastrada para este usuário.</p>
        </c:when>
        <c:otherwise>
            <div class="tabela-painel__rolagem">
                <table class="tabela">
                    <thead>
                    <tr><th scope="col">Alergia</th><th scope="col">Grau</th><th scope="col">Registro</th><th scope="col">Ações</th></tr>
                    </thead>
                    <tbody>
                    <c:forEach var="a" items="${alergias}">
                        <tr>
                            <td><c:out value="${a.nomeAlergia}"/></td>
                            <td><span class="etiqueta etiqueta--roxa">Grau ${a.grau}</span></td>
                            <td data-data="${a.dtRegistro}">${a.dtRegistro}</td>
                            <td>
                                <div class="acoes">
                                    <a class="botao-icone" href="${ctx}/usuarioAlergias?id=${a.idUsuarioAlergia}" title="Editar">
                                        <img src="${ctx}/assets/img/icones-crud/editar.svg" alt="Editar">
                                    </a>
                                    <button class="botao-icone botao-icone--excluir" type="button" title="Excluir"
                                            data-excluir-url="${ctx}/usuarioAlergias?id=${a.idUsuarioAlergia}"
                                            data-excluir-nome="${fn:escapeXml(a.nomeAlergia)}">
                                        <img src="${ctx}/assets/img/icones-crud/excluir.svg" alt="Excluir">
                                    </button>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</section>

<section class="conteudo-aba painel" id="aba-produtos" role="tabpanel" hidden>
    <div class="painel__cabecalho">
        <h2 class="painel__titulo">Produtos (${fn:length(produtos)})</h2>
        <a class="btn btn--painel btn--roxo" href="${ctx}/produtoUsuarios?acao=novo">Vincular produto</a>
    </div>
    <c:choose>
        <c:when test="${empty produtos}">
            <p class="aba-vazia">Nenhum produto ligado a este usuário.</p>
        </c:when>
        <c:otherwise>
            <div class="tabela-painel__rolagem">
                <table class="tabela">
                    <thead>
                    <tr><th scope="col">Produto</th><th scope="col">Ações</th></tr>
                    </thead>
                    <tbody>
                    <c:forEach var="p" items="${produtos}">
                        <tr>
                            <td><c:out value="${p.nomeProduto}"/></td>
                            <td>
                                <div class="acoes">
                                    <button class="botao-icone botao-icone--excluir" type="button" title="Excluir"
                                            data-excluir-url="${ctx}/produtoUsuarios?id=${p.idProdutoUsuario}"
                                            data-excluir-nome="${fn:escapeXml(p.nomeProduto)}">
                                        <img src="${ctx}/assets/img/icones-crud/excluir.svg" alt="Excluir">
                                    </button>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</section>

<section class="conteudo-aba painel" id="aba-preferencias" role="tabpanel" hidden>
    <div class="painel__cabecalho">
        <h2 class="painel__titulo">Preferências (${fn:length(preferencias)})</h2>
        <a class="btn btn--painel btn--roxo" href="${ctx}/preferencias?acao=novo&idUsuario=${usuario.idUsuario}">Adicionar preferências</a>
    </div>
    <c:choose>
        <c:when test="${empty preferencias}">
            <p class="aba-vazia">Nenhuma preferência cadastrada para este usuário.</p>
        </c:when>
        <c:otherwise>
            <div class="tabela-painel__rolagem">
                <table class="tabela">
                    <thead>
                    <tr>
                        <th scope="col">Faixa de preço</th><th scope="col">Vegano</th><th scope="col">Categoria</th>
                        <th scope="col">Marcas favoritas</th><th scope="col">Restrições</th><th scope="col">Ações</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="p" items="${preferencias}">
                        <tr>
                            <td><c:out value="${empty p.faixaPreco ? '—' : p.faixaPreco}"/></td>
                            <td>${p.prefereVegano ? 'Sim' : 'Não'}</td>
                            <td><c:out value="${empty p.categoriaPref ? '—' : p.categoriaPref}"/></td>
                            <td><c:out value="${empty p.marcasFav ? '—' : p.marcasFav}"/></td>
                            <td><c:out value="${empty p.restricoesDieta ? '—' : p.restricoesDieta}"/></td>
                            <td>
                                <div class="acoes">
                                    <a class="botao-icone" href="${ctx}/preferencias?id=${p.idPreferencias}" title="Editar">
                                        <img src="${ctx}/assets/img/icones-crud/editar.svg" alt="Editar">
                                    </a>
                                    <button class="botao-icone botao-icone--excluir" type="button" title="Excluir"
                                            data-excluir-url="${ctx}/preferencias?id=${p.idPreferencias}"
                                            data-excluir-nome="estas preferências">
                                        <img src="${ctx}/assets/img/icones-crud/excluir.svg" alt="Excluir">
                                    </button>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</section>

<section class="conteudo-aba painel" id="aba-pele" role="tabpanel" hidden>
    <div class="painel__cabecalho">
        <h2 class="painel__titulo">Perfil de pele</h2>
        <c:choose>
            <c:when test="${empty perfilPele}">
                <a class="btn btn--painel btn--roxo" href="${ctx}/perfisPele?acao=novo&idUsuario=${usuario.idUsuario}">Adicionar perfil de pele</a>
            </c:when>
            <c:otherwise>
                <div class="acoes">
                    <a class="botao-icone" href="${ctx}/perfisPele?id=${perfilPele.idPerfilPele}" title="Editar">
                        <img src="${ctx}/assets/img/icones-crud/editar.svg" alt="Editar">
                    </a>
                    <button class="botao-icone botao-icone--excluir" type="button" title="Excluir"
                            data-excluir-url="${ctx}/perfisPele?id=${perfilPele.idPerfilPele}"
                            data-excluir-nome="o perfil de pele">
                        <img src="${ctx}/assets/img/icones-crud/excluir.svg" alt="Excluir">
                    </button>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <c:choose>
        <c:when test="${empty perfilPele}">
            <p class="aba-vazia">Este usuário ainda não tem perfil de pele.</p>
        </c:when>
        <c:otherwise>
            <div class="cartoes-info">
                <div class="cartao-info">
                    <span class="cartao-info__rotulo">Tipo de pele</span>
                    <strong class="cartao-info__valor"><c:out value="${perfilPele.tipoPele}"/></strong>
                </div>
                <div class="cartao-info">
                    <span class="cartao-info__rotulo">Sensibilidade</span>
                    <strong class="cartao-info__valor">${perfilPele.sensibilidade} de 10</strong>
                </div>
                <div class="cartao-info">
                    <span class="cartao-info__rotulo">Oleosidade</span>
                    <strong class="cartao-info__valor">${perfilPele.nivelOleosidade} de 10</strong>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</section>

<section class="conteudo-aba painel" id="aba-cabelo" role="tabpanel" hidden>
    <div class="painel__cabecalho">
        <h2 class="painel__titulo">Perfil de cabelo</h2>
        <c:choose>
            <c:when test="${empty perfilCabelo}">
                <a class="btn btn--painel btn--roxo" href="${ctx}/perfisCabelo?acao=novo&idUsuario=${usuario.idUsuario}">Adicionar perfil de cabelo</a>
            </c:when>
            <c:otherwise>
                <div class="acoes">
                    <a class="botao-icone" href="${ctx}/perfisCabelo?id=${perfilCabelo.idPerfilCabelo}" title="Editar">
                        <img src="${ctx}/assets/img/icones-crud/editar.svg" alt="Editar">
                    </a>
                    <button class="botao-icone botao-icone--excluir" type="button" title="Excluir"
                            data-excluir-url="${ctx}/perfisCabelo?id=${perfilCabelo.idPerfilCabelo}"
                            data-excluir-nome="o perfil de cabelo">
                        <img src="${ctx}/assets/img/icones-crud/excluir.svg" alt="Excluir">
                    </button>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <c:choose>
        <c:when test="${empty perfilCabelo}">
            <p class="aba-vazia">Este usuário ainda não tem perfil de cabelo.</p>
        </c:when>
        <c:otherwise>
            <div class="cartoes-info">
                <div class="cartao-info">
                    <span class="cartao-info__rotulo">Curvatura</span>
                    <strong class="cartao-info__valor">${perfilCabelo.curvatura} de 4</strong>
                </div>
                <div class="cartao-info">
                    <span class="cartao-info__rotulo">Oleosidade</span>
                    <strong class="cartao-info__valor">${perfilCabelo.oleosidade} de 10</strong>
                </div>
                <div class="cartao-info">
                    <span class="cartao-info__rotulo">Espessura</span>
                    <strong class="cartao-info__valor">${perfilCabelo.espessura} de 10</strong>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</section>

<section class="conteudo-aba painel" id="aba-analises" role="tabpanel" hidden>
    <div class="painel__cabecalho">
        <h2 class="painel__titulo">Análises (${fn:length(analises)})</h2>
        <a class="btn btn--painel btn--roxo" href="${ctx}/analises?acao=novo&idUsuario=${usuario.idUsuario}">Adicionar análise</a>
    </div>
    <c:choose>
        <c:when test="${empty analises}">
            <p class="aba-vazia">Nenhuma análise feita por este usuário.</p>
        </c:when>
        <c:otherwise>
            <div class="tabela-painel__rolagem">
                <table class="tabela">
                    <thead>
                    <tr><th scope="col">Data</th><th scope="col">Nota</th><th scope="col">Resumo</th><th scope="col">Ações</th></tr>
                    </thead>
                    <tbody>
                    <c:forEach var="a" items="${analises}">
                        <tr>

                            <td data-data="${fn:substring(a.dtHrAnalise, 0, 10)}">${a.dtHrAnalise}</td>
                            <td><span class="etiqueta etiqueta--roxa">${a.pontuacao} / 10</span></td>
                            <td><c:out value="${a.resumoResultado}"/></td>
                            <td>
                                <div class="acoes">
                                    <a class="botao-icone" href="${ctx}/analises?id=${a.idAnalise}" title="Editar">
                                        <img src="${ctx}/assets/img/icones-crud/editar.svg" alt="Editar">
                                    </a>
                                    <button class="botao-icone botao-icone--excluir" type="button" title="Excluir"
                                            data-excluir-url="${ctx}/analises?id=${a.idAnalise}"
                                            data-excluir-nome="esta análise">
                                        <img src="${ctx}/assets/img/icones-crud/excluir.svg" alt="Excluir">
                                    </button>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
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
