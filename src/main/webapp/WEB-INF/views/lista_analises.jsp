<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Análises"/>
<c:set var="paginaAtiva" value="analises"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Análises</h1>
        <p>Histórico de análises dos usuários.</p>
    </div>
    <div class="heading-actions">
        <c:if test="${not empty param.idUsuario}">
            <a class="btn secondary" href="${ctx}/usuarios">Voltar para usuários</a>
        </c:if>
        <a class="btn primary" href="${ctx}/analises?acao=novo&idUsuario=${param.idUsuario}">Adicionar análise</a>
    </div>
</div>

<div class="toolbar">
    <label class="search">
        <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/05487dae-bbcf-4cbf-b4f6-dac8001dd2c1.svg" alt=""></span>
        <input id="busca" type="search" aria-label="Buscar análises" placeholder="Buscar por resumo, data ou pontuação...">
    </label>
</div>

<div id="erro" class="alert-erro" role="alert" hidden></div>

<section class="table-panel">
    <div class="table-scroll">
        <table id="tabela-analises">
            <thead>
            <tr>
                <th scope="col">Data</th>
                <th scope="col">Usuário (id)</th>
                <th scope="col">Resumo do resultado</th>
                <th scope="col">Pontuação</th>
                <th scope="col">Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${analises}" var="a">
                <tr>
                    <td>${a.dtHrAnaliseFormatada}</td>
                    <td>${a.idUsuario}</td>
                    <td><c:out value="${a.resumoResultado}"/></td>
                    <td><span class="badge purple">${a.pontuacao}</span></td>
                    <td>
                        <div class="actions">
                            <a class="icon-btn" href="${ctx}/analises?id=${a.idAnalise}" aria-label="Editar análise">
                                <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/106e6b38-e528-4239-9191-5b8babefdc63.svg" alt=""></span>
                            </a>
                            <button class="icon-btn" type="button" onclick="excluir(${a.idAnalise})" aria-label="Excluir análise">
                                <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/174bca6f-6692-44c0-9769-3c921855c872.svg" alt=""></span>
                            </button>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty analises}">
                <tr><td colspan="5" class="empty">Nenhuma análise encontrada.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</section>

<script>
    var faixaErro = document.getElementById('erro');

    function mostrarErro(mensagem) {
        faixaErro.textContent = mensagem;
        faixaErro.hidden = false;
    }

    document.getElementById('busca').addEventListener('input', function (e) {
        var termo = e.target.value.toLowerCase();
        document.querySelectorAll('#tabela-analises tbody tr').forEach(function (tr) {
            if (tr.querySelector('.empty')) return;
            tr.style.display = tr.textContent.toLowerCase().indexOf(termo) >= 0 ? '' : 'none';
        });
    });

    async function excluir(id) {
        if (!confirm('Excluir esta análise?')) return;
        faixaErro.hidden = true;

        try {
            var resp = await fetch('${ctx}/analises?id=' + id, {method: 'DELETE'});

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