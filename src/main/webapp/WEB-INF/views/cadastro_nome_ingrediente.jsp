<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Adicionar nome"/>
<c:set var="paginaAtiva" value="ingredientes"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Adicionar nome do ingrediente</h1>
    </div>
</div>

<div class="panel white form-card">
    <c:if test="${not empty erro}">
        <div class="alert-erro" role="alert"><c:out value="${erro}"/></div>
    </c:if>

    <form method="post" action="${ctx}/nomeIngredientes">
        <input type="hidden" name="idIngrediente" value="<c:out value='${param.idIngrediente}'/>">

        <label class="form-field">
            Nome
            <input type="text" name="nome" maxlength="100" required
                   value="<c:out value='${param.nome}'/>" placeholder="Ex.: Água Purificada">
        </label>

        <div class="form-actions">
            <a class="btn secondary" href="${ctx}/nomeIngredientes?idIngrediente=${param.idIngrediente}">Cancelar</a>
            <button class="btn primary" type="submit">Salvar</button>
        </div>
    </form>
</div>

<%@ include file="/WEB-INF/views/rodape.jspf" %>