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

<div id="erro" class="alert-erro" role="alert" hidden></div>

<section class="table-panel">
    <div class="table-scroll">
        <table>
            <thead>
            <tr>
                <th scope="col">Ingrediente</th>
                <th scope="col">Tipo</th>
                <th scope="col">Nível de perigo</th>
                <th scope="col">Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${produtosIngredientes}" var="pi">
                <tr>
                    <td><c:out value="${pi.nomeIngrediente}"/></td>
                    <td><c:out value="${pi.tipo}"/></td>
                    <td><span class="badge ${pi.nivelPerigo <= 2 ? 'green' : (pi.nivelPerigo == 3 ? 'yellow' : 'pink')}">${pi.nivelPerigo}/5</span></td>
                    <td><div class="actions"><button type="button" class="btn secondary" onclick="excluir(${pi.idProdutoIngrediente})">Remover</button></div></td>
                </tr>
            </c:forEach>
            <c:if test="${empty produtosIngredientes}">
                <tr><td colspan="4" class="empty">Nenhum ingrediente vinculado a este produto.</td></tr>
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