<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Vincular ingrediente"/>
<c:set var="paginaAtiva" value="produtos"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Vincular ingrediente ao produto</h1>
        <p>Escolha o ingrediente que o produto contém.</p>
    </div>
</div>

<div class="panel white form-card">
    <c:if test="${not empty erro}">
        <div class="alert-erro" role="alert"><c:out value="${erro}"/></div>
    </c:if>

    <form method="post" action="${ctx}/produtoIngredientes">
        <input type="hidden" name="idProduto" value="${param.idProduto}">
        <input type="hidden" name="idProduto" value="<c:out value='${param.idProduto}'/>">

        <div class="form-field">
            <label for="idIngrediente">Ingrediente</label>
            <select id="idIngrediente" name="idIngrediente" required>
                <option value="">Selecione...</option>
                <c:forEach items="${ingredientes}" var="i">
                    <option value="${i.idIngrediente}"
                        ${param.idIngrediente == i.idIngrediente ? 'selected' : ''}>
                        <c:out value="${i.tipo}"/>
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-actions">
            <a class="btn secondary"
               href="${ctx}/produtoIngredientes?idProduto=${param.idProduto}">Cancelar</a>
            <button type="submit" class="btn primary">Vincular</button>
        </div>
    </form>
</div>

<%@ include file="/WEB-INF/views/rodape.jspf" %>