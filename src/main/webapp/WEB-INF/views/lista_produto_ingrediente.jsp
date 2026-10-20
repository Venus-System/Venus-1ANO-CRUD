<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Ingredientes do produto"/>
<c:set var="paginaAtiva" value="produtos"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Ingredientes do produto</h1>
    </div>
    <div class="heading-actions">
        <a class="btn secondary" href="${ctx}/produtos">Voltar para produtos</a>
        <c:if test="${not empty param.idProduto}">
            <a class="btn primary"
               href="${ctx}/produtoIngredientes?acao=novo&idProduto=${param.idProduto}">Vincular ingrediente</a>
        </c:if>
    </div>
</div>

<div class="panel white">
    <div id="erro" class="alert-erro" role="alert" hidden></div>

    <c:choose>
        <c:when test="${empty produtosIngredientes}">
            <p>Nenhum ingrediente vinculado.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr><th>Ingrediente</th><th>Ações</th></tr>
                </thead>
                <tbody>
                <c:forEach items="${produtosIngredientes}" var="pi">
                    <tr>
                        <td><c:out value="${pi.nomeIngrediente}"/></td>
                        <td>
                            <button type="button" class="btn secondary"
                                    onclick="excluir(${pi.idProdutoIngrediente})">Remover</button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<script>
    var faixaErro = document.getElementById('erro');

    function mostrarErro(mensagem) {
        faixaErro.textContent = mensagem;
        faixaErro.hidden = false;
    }

    async function excluir(id) {
        if (!confirm('Remover este ingrediente do produto?')) return;
        faixaErro.hidden = true;

        try {
            var resp = await fetch('${ctx}/produtoIngredientes?id=' + id, {method: 'DELETE'});

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