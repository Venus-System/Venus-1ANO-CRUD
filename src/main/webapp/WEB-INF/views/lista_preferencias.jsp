<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="tituloPagina" value="Preferências"/>
<c:set var="paginaAtiva" value="usuarios"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Preferências</h1>
        <p>Preferências de consumo dos usuários.</p>
    </div>
    <div class="heading-actions">
        <a class="btn secondary" href="${ctx}/usuarios">Voltar para usuários</a>
        <a class="btn primary" href="${ctx}/preferencias?acao=novo&idUsuario=${param.idUsuario}">Adicionar preferências</a>
    </div>
</div>

<div class="toolbar">
    <label class="search">
        <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/05487dae-bbcf-4cbf-b4f6-dac8001dd2c1.svg" alt=""></span>
        <input id="busca" type="search" aria-label="Buscar preferências" placeholder="Buscar por faixa de preço, categoria ou marca...">
    </label>
</div>

<div id="erro" class="alert-erro" role="alert" hidden></div>

<section class="table-panel">
    <div class="table-scroll">
        <table id="tabela-preferencias">
            <thead>
            <tr>
                <th scope="col">Usuário (id)</th>
                <th scope="col">Faixa de preço</th>
                <th scope="col">Vegano</th>
                <th scope="col">Restrições</th>
                <th scope="col">Categorias</th>
                <th scope="col">Marcas favoritas</th>
                <th scope="col">Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${listaPreferencias}" var="p">
                <tr>
                    <td>${p.idUsuario}</td>
                    <td><c:out value="${p.faixaPreco}"/></td>
                    <td>${p.prefereVegano ? 'Sim' : 'Não'}</td>
                    <td><c:out value="${p.restricoesDieta}"/></td>
                    <td><c:out value="${p.categoriaPref}"/></td>
                    <td><c:out value="${p.marcasFav}"/></td>
                    <td>
                        <div class="actions">
                            <a class="icon-btn" href="${ctx}/preferencias?id=${p.idPreferencias}" aria-label="Editar preferências">
                                <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/106e6b38-e528-4239-9191-5b8babefdc63.svg" alt=""></span>
                            </a>
                            <button class="icon-btn" type="button" onclick="excluir(${p.idPreferencias})" aria-label="Excluir preferências">
                                <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/174bca6f-6692-44c0-9769-3c921855c872.svg" alt=""></span>
                            </button>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty listaPreferencias}">
                <tr><td colspan="7" class="empty">Nenhuma preferência encontrada.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</section>

<script>
    document.getElementById('busca').addEventListener('input', function (e) {
        var termo = e.target.value.toLowerCase();
        document.querySelectorAll('#tabela-preferencias tbody tr').forEach(function (tr) {
            if (tr.querySelector('.empty')) return;
            tr.style.display = tr.textContent.toLowerCase().indexOf(termo) >= 0 ? '' : 'none';
        });
    });

    var faixaErro = document.getElementById('erro');

    function mostrarErro(mensagem) {
        faixaErro.textContent = mensagem;
        faixaErro.hidden = false;
    }

    async function excluir(id) {
        if (!confirm('Excluir estas preferências?')) return;
        faixaErro.hidden = true;

        try {
            var resp = await fetch('${ctx}/preferencias?id=' + id, {method: 'DELETE'});

            if (resp.ok) {
                window.location.reload();
            } else if (resp.status >= 500) {
                mostrarErro('Erro no servidor. Tente novamente.');
            } else {
                mostrarErro(await resp.text());
            }
        } catch (erro) {
            mostrarErro('Não foi possível conectar ao servidor.');
        }
    }
</script>

<%@ include file="/WEB-INF/views/rodape.jspf" %>