<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="tituloPagina" value="Nomes do ingrediente"/>
<c:set var="paginaAtiva" value="ingredientes"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Nomes do ingrediente</h1>
        <p>Nomes pelos quais cada ingrediente é conhecido.</p>
    </div>
    <div class="heading-actions">
        <a class="btn secondary" href="${ctx}/ingredientes">Voltar para ingredientes</a>
        <c:if test="${not empty param.idIngrediente}">
            <a class="btn primary" href="${ctx}/nomeIngredientes?acao=novo&idIngrediente=${param.idIngrediente}">Adicionar nome</a>
        </c:if>
    </div>
</div>

<div id="erro" class="alert-erro" role="alert" hidden></div>

<section class="table-panel">
    <div class="table-scroll">
        <table>
            <thead>
            <tr>
                <th scope="col">Nome</th>
                <th scope="col">Ingrediente (id)</th>
                <th scope="col">Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${nomesIngredientes}" var="n">
                <tr>
                    <td><c:out value="${n.nomeIngrediente}"/></td>
                    <td>${n.idIngrediente}</td>
                    <td>
                        <div class="actions">
                            <a class="icon-btn" href="${ctx}/nomeIngredientes?id=${n.idNomeIngrediente}" aria-label="Editar ${fn:escapeXml(n.nomeIngrediente)}">
                                <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/106e6b38-e528-4239-9191-5b8babefdc63.svg" alt=""></span>
                            </a>
                            <button class="icon-btn" type="button" onclick="excluir(${n.idNomeIngrediente})" aria-label="Excluir ${fn:escapeXml(n.nomeIngrediente)}">
                                <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/174bca6f-6692-44c0-9769-3c921855c872.svg" alt=""></span>
                            </button>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty nomesIngredientes}">
                <tr><td colspan="3" class="empty">Nenhum nome cadastrado.</td></tr>
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

    async function excluir(id) {
        if (!confirm('Excluir este nome?')) return;
        faixaErro.hidden = true;

        try {
            var resp = await fetch('${ctx}/nomeIngredientes?id=' + id, {method: 'DELETE'});

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