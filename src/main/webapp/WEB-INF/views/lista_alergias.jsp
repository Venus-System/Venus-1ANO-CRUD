<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="tituloPagina" value="Alergias"/>
<c:set var="paginaAtiva" value="usuarios"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Gerenciamento de alergias</h1>
        <p>Gerencie as alergias disponíveis na plataforma.</p>
    </div>
    <div class="heading-actions">
        <a class="btn primary" href="${ctx}/alergias?acao=novo">
            <span class="figma-icon" style="--icon-size:20px"><img src="${ctx}/assets/21d8c3ca-d1c2-4fee-8539-28c320da8381.svg" alt=""></span>
            Adicionar nova alergia
        </a>
    </div>
</div>

<section class="stats" aria-label="Indicadores">
    <article class="stat" data-icon-kind="usuarios">
        <span class="bubble" aria-hidden="true">
            <span class="figma-icon" style="--icon-size:33px"><img src="${ctx}/assets/114998ae-07f2-474e-a199-3038b9bbeb93.svg" alt=""></span>
        </span>
        <div>
            <span class="stat-label">Alergias cadastradas</span>
            <strong>${alergias.size()}</strong>
        </div>
    </article>
</section>

<div class="toolbar">
    <label class="search">
        <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/05487dae-bbcf-4cbf-b4f6-dac8001dd2c1.svg" alt=""></span>
        <input id="busca" type="search" aria-label="Buscar alergias" placeholder="Buscar por nome...">
    </label>
</div>

<section class="table-panel">
    <div class="table-scroll" role="region" aria-label="Tabela de alergias" tabindex="0">
        <table id="tabela-alergias">
            <thead>
            <tr>
                <th scope="col">Id</th>
                <th scope="col">Alergia</th>
                <th scope="col">Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="a" items="${alergias}">
                <tr>
                    <td>${a.idAlergia}</td>
                    <td><c:out value="${a.nomeAlergia}"/></td>
                    <td>
                        <div class="actions">
                            <a class="icon-btn" href="${ctx}/alergias?id=${a.idAlergia}" aria-label="Editar ${fn:escapeXml(a.nomeAlergia)}">
                                <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/106e6b38-e528-4239-9191-5b8babefdc63.svg" alt=""></span>
                            </a>
                            <button class="icon-btn" type="button" data-excluir="${a.idAlergia}" aria-label="Excluir ${fn:escapeXml(a.nomeAlergia)}">
                                <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/174bca6f-6692-44c0-9769-3c921855c872.svg" alt=""></span>
                            </button>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty alergias}">
                <tr><td colspan="3" class="empty">Nenhuma alergia cadastrada.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</section>

<script>
    // Busca na própria página
    document.getElementById('busca').addEventListener('input', function (e) {
        var termo = e.target.value.toLowerCase();
        document.querySelectorAll('#tabela-alergias tbody tr').forEach(function (tr) {
            if (tr.querySelector('.empty')) return;
            tr.style.display = tr.textContent.toLowerCase().indexOf(termo) >= 0 ? '' : 'none';
        });
    });

    // Exclusão: chama o doDelete do AlergiaServlet
    document.querySelectorAll('[data-excluir]').forEach(function (botao) {
        botao.addEventListener('click', async function () {
            if (!confirm('Excluir esta alergia?')) return;
            var resp = await fetch('${ctx}/alergias?id=' + botao.dataset.excluir, {method: 'DELETE'});

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
