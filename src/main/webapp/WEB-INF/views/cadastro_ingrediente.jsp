<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Novo ingrediente"/>
<c:set var="paginaAtiva" value="ingredientes"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Adicionar ingrediente</h1>
        <p>Informe o tipo e o nível de perigo do ingrediente.</p>
    </div>
</div>

<div class="panel white form-card">
    <c:if test="${not empty erro}">
        <div class="alert-erro" role="alert"><c:out value="${erro}"/></div>
    </c:if>

    <form method="post" action="${ctx}/ingredientes">
        <div class="form-field">
            <label for="tipo">Tipo</label>
            <input type="text" id="tipo" name="tipo" required maxlength="100"
                   placeholder="Ex.: Conservante" value="<c:out value='${param.tipo}'/>">
        </div>

        <div class="form-field">
            <label for="nivelPerigo">Nível de perigo (1 a 5)</label>
            <input type="number" id="nivelPerigo" name="nivelPerigo" required min="1" max="5"
                   value="<c:out value='${param.nivelPerigo}'/>">
        </div>

        <div class="form-actions">
            <a class="btn secondary" href="${ctx}/ingredientes">Cancelar</a>
            <button type="submit" class="btn primary">Cadastrar</button>
        </div>
    </form>
</div>

<%@ include file="/WEB-INF/views/rodape.jspf" %>