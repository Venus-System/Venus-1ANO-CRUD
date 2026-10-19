<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Produtos dos usuários"/>
<c:set var="paginaAtiva" value="usuarios"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Produtos dos usuários</h1>
        <p>Produtos vinculados a cada usuário.</p>
    </div>
    <div class="heading-actions">
        <c:if test="${not empty param.idUsuario}">
            <a class="btn secondary" href="${ctx}/produtoUsuarios">Ver todos</a>
        </c:if>
        <a class="btn primary" href="${ctx}/produtoUsuarios?acao=novo">
            <span class="figma-icon" style="--icon-size:20px"><img src="${ctx}/assets/21d8c3ca-d1c2-4fee-8539-28c320da8381.svg" alt=""></span>
            Novo vínculo
        </a>
    </div>
</div>

<div class="toolbar">
    <label class="search">
        <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/05487dae-bbcf-4cbf-b4f6-dac8001dd2c1.svg" alt=""></span>
        <input id="busca" type="search" aria-label="Buscar vínculos" placeholder="Buscar por id...">
    </label>
</div>

<section class="table-panel">
    <div class="table-scroll" role="region" aria-label="Tabela de produtos dos usuários" tabindex="0">
        <table id="tabela-produto-usuario">
            <thead>
            <tr>
                <th scope="col">Id</th>
                <th scope="col">Id do produto</th>
                <th scope="col">Id do usuário</th>
                <th scope="col">Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="pu" items="${produtosUsuarios}">
                <tr>
                    <td>${pu.idProdutoUsuario}</td>
                    <td>${pu.idProduto}</td>
                    <td>${pu.idUsuario}</td>
                    <td>
                        <div class="actions">
                            <button class="icon-btn" type="button" data-excluir="${pu.idProdutoUsuario}" aria-label="Excluir vínculo ${pu.idProdutoUsuario}">
                                <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/174bca6f-6692-44c0-9769-3c921855c872.svg" alt=""></span>
                            </button>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty produtosUsuarios}">
                <tr><td colspan="4" class="empty">Nenhum produto vinculado encontrado.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</section>

<script>
    // Busca na própria página
    document.getElementById('busca').addEventListener('input', function (e) {
        var termo = e.target.value.toLowerCase();
        document.querySelectorAll('#tabela-produto-usuario tbody tr').forEach(function (tr) {
            if (tr.querySelector('.empty')) return;
            tr.style.display = tr.textContent.toLowerCase().indexOf(termo) >= 0 ? '' : 'none';
        });
    });

    // Exclusão: chama o doDelete do ProdutoUsuarioServlet
    document.querySelectorAll('[data-excluir]').forEach(function (botao) {
        botao.addEventListener('click', async function () {
            if (!confirm('Excluir este vínculo?')) return;
            var resp = await fetch('${ctx}/produtoUsuarios?id=' + botao.dataset.excluir, {method: 'DELETE'});

            if (resp.ok) {
                location.reload();
            } else if (resp.status >= 500) {
                alert('Erro no servidor. Tente novamente.');
            } else {
                alert(await resp.text());
            }
        });
    });
</script>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
