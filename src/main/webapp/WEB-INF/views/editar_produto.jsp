<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Editar produto"/>
<c:set var="paginaAtiva" value="produtos"/>
<c:set var="descricaoTopo" value="Aqui você pode gerenciar os produtos cadastrados na plataforma."/>
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
        <h1 class="cabecalho-pagina__titulo">Editar produto</h1>
        <p class="cabecalho-pagina__texto">Código: #${produto.idProduto}</p>
    </div>
</section>

<section class="painel formulario">
    <p id="msg-erro" class="alerta-erro" role="alert" hidden></p>

    <form id="form-editar">
        <input type="hidden" id="idProduto" value="${produto.idProduto}">

        <label class="campo">
            Nome do produto *
            <input type="text" id="nome" maxlength="60" required value="<c:out value='${produto.nome}'/>">
        </label>

        <div class="campos-linha">
            <label class="campo">
                Marca *
                <input type="text" id="marca" maxlength="50" required value="<c:out value='${produto.marca}'/>">
            </label>
            <label class="campo">
                Categoria *
                <input type="text" id="categoria" maxlength="50" required value="<c:out value='${produto.categoria}'/>">
            </label>
        </div>

        <label class="campo">
            Descrição
            <textarea id="descricao" maxlength="255"><c:out value='${produto.descricao}'/></textarea>
        </label>

        <label class="campo">
            Lista de ingredientes (como está no rótulo)
            <textarea id="listaIngredientes" maxlength="255"><c:out value='${produto.listaIngredientes}'/></textarea>
        </label>

        <div class="campos-linha">
            <label class="campo campo--checkbox">
                <input type="checkbox" id="ehVegano" ${produto.ehVegano ? 'checked' : ''}>
                É vegano
            </label>
            <label class="campo campo--checkbox">
                <input type="checkbox" id="ehCrueltyFree" ${produto.ehCrueltyFree ? 'checked' : ''}>
                É cruelty free
            </label>
        </div>

        <label class="campo">
            Pontuação (0 a 100)
            <input type="number" id="pontuacao" min="0" max="100" step="1" value="${produto.pontuacao}">
        </label>

        <div class="formulario__botoes">
            <a class="btn btn--painel" href="${ctx}/produtos">Cancelar</a>
            <button class="btn btn--painel btn--roxo" type="submit" id="btn-salvar">Salvar alterações</button>
        </div>
    </form>
</section>

<script>
    const formEditar = document.getElementById('form-editar');
    const msgErro = document.getElementById('msg-erro');
    const btnSalvar = document.getElementById('btn-salvar');

    formEditar.addEventListener('submit', async function (evento) {
        evento.preventDefault();
        msgErro.hidden = true;
        btnSalvar.disabled = true;

        const dados = {
            idProduto: document.getElementById('idProduto').value,
            nome: document.getElementById('nome').value,
            marca: document.getElementById('marca').value,
            categoria: document.getElementById('categoria').value,
            descricao: document.getElementById('descricao').value,
            ehVegano: document.getElementById('ehVegano').checked,
            ehCrueltyFree: document.getElementById('ehCrueltyFree').checked,
            pontuacao: document.getElementById('pontuacao').value || '0',
            listaIngredientes: document.getElementById('listaIngredientes').value
        };

        try {
            const resposta = await fetch('${ctx}/produtos', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(dados)
            });

            if (resposta.ok) {
                window.location.href = '${ctx}/produtos';
                return;
            }

            if (resposta.status >= 500) {
                msgErro.textContent = 'Erro no servidor. Tente novamente.';
            } else {
                msgErro.textContent = await resposta.text();
            }
        } catch (erro) {
            msgErro.textContent = 'Não foi possível conectar ao servidor.';
        }

        abrirPopupErro(msgErro.textContent);
        btnSalvar.disabled = false;
    });
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
