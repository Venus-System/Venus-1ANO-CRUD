<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Nova alergia"/>
<c:set var="paginaAtiva" value="usuarios"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Adicionar nova alergia</h1>
        <p>Informe o nome da alergia que será cadastrada.</p>
    </div>
</div>

<div class="panel white form-card">
    <c:if test="${not empty erro}">
        <div class="alert-erro" role="alert"><c:out value="${erro}"/></div>
    </c:if>

    <form method="post" action="${ctx}/alergias">
        <div class="form-field">
            <label for="nome">Nome da alergia</label>
            <input type="text" id="nome" name="nome" required minlength="2" maxlength="100"
                   placeholder="Ex.: Lactose" value="<c:out value='${param.nome}'/>">
        </div>

        <div class="form-actions">
            <a class="btn secondary" href="${ctx}/alergias">Cancelar</a>
            <button type="submit" class="btn primary">Cadastrar</button>
        </div>
    </form>
</div>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
