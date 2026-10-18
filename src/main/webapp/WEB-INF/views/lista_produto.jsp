<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="tituloPagina" value="Produtos"/>
<c:set var="paginaAtiva" value="produtos"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Gerenciamento de produtos</h1>
        <p>Gerencie os produtos cadastrados na plataforma.</p>
    </div>
    <div class="heading-actions">
        <a class="btn primary" href="${ctx}/produtos?acao=novo">
            <span class="figma-icon" style="--icon-size:20px"><img src="${ctx}/assets/21d8c3ca-d1c2-4fee-8539-28c320da8381.svg" alt=""></span>
            Adicionar novo produto
        </a>
    </div>
</div>

<section class="stats" aria-label="Indicadores">
    <article class="stat" data-icon-kind="usuarios">
        <span class="bubble" aria-hidden="true">
            <span class="figma-icon" style="--icon-size:33px"><img src="${ctx}/assets/114998ae-07f2-474e-a199-3038b9bbeb93.svg" alt=""></span>
        </span>
        <div>
            <span class="stat-label">Produtos totais</span>
            <strong>${produtos.size()}</strong>
        </div>
    </article>
</section>

<div class="toolbar">
    <label class="search">
        <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/05487dae-bbcf-4cbf-b4f6-dac8001dd2c1.svg" alt=""></span>
        <input id="busca" type="search" aria-label="Buscar produtos" placeholder="Buscar por nome, marca ou categoria...">
    </label>
</div>

<section class="table-panel">
    <div class="table-scroll" role="region" aria-label="Tabela de produtos" tabindex="0">
        <table id="tabela-produtos">
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
                    <td><c:out value="${p.nome}"/></td>
                    <td><c:out value="${p.marca}"/></td>
                    <td><c:out value="${p.categoria}"/></td>
                    <td>${p.ehVegano ? 'Sim' : 'Não'}</td>
                    <td>${p.ehCrueltyFree ? 'Sim' : 'Não'}</td>
                    <td>${p.pontuacao}</td>
                    <td>
                        <div class="actions">
                            <a class="icon-btn" href="${ctx}/produtos?id=${p.idProduto}" aria-label="Editar ${fn:escapeXml(p.nome)}">
                                <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/106e6b38-e528-4239-9191-5b8babefdc63.svg" alt=""></span>
                            </a>
                            <button class="icon-btn" type="button" data-excluir="${p.idProduto}" aria-label="Excluir ${fn:escapeXml(p.nome)}">
                                <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/174bca6f-6692-44c0-9769-3c921855c872.svg" alt=""></span>
                            </button>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty produtos}">
                <tr><td colspan="7" class="empty">Nenhum produto cadastrado.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</section>

<script>
    // Busca na própria página (nome, marca, categoria...)
    document.getElementById('busca').addEventListener('input', function (e) {
        var termo = e.target.value.toLowerCase();
        document.querySelectorAll('#tabela-produtos tbody tr').forEach(function (tr) {
            if (tr.querySelector('.empty')) return;
            tr.style.display = tr.textContent.toLowerCase().indexOf(termo) >= 0 ? '' : 'none';
        });
    });

    // Exclusão: chama o doDelete do ProdutoServlet
    document.querySelectorAll('[data-excluir]').forEach(function (botao) {
        botao.addEventListener('click', async function () {
            if (!confirm('Excluir este produto?')) return;

            try {
                var resp = await fetch('${ctx}/produtos?id=' + botao.dataset.excluir, {method: 'DELETE'});

                if (resp.ok) {
                    location.reload();
                } else if (resp.status >= 500) {
                    alert('Erro no servidor. Tente novamente.');
                } else {
                    // 400, 404, 409 (produto vinculado): o servlet mandou uma frase curta
                    alert(await resp.text());
                }
            } catch (err) {
                alert('Não foi possível conectar ao servidor.');
            }
        });
    });
</script>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
