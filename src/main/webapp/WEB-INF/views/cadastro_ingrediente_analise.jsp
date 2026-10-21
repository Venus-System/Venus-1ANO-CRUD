<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Vincular ingrediente à análise</h1>
        <p>Selecione o ingrediente que faz parte da análise.</p>
    </div>
</div>

<div class="panel white form-card">
    <c:if test="${not empty erro}">
        <div class="alert-erro"><c:out value="${erro}"/></div>
    </c:if>

    <form action="${pageContext.request.contextPath}/ingredienteAnalises" method="post">

        <c:choose>
            <c:when test="${not empty param.idAnalise}">
                <input type="hidden" name="idAnalise" value="<c:out value='${param.idAnalise}'/>">
            </c:when>
            <c:otherwise>
                <label class="form-field">
                    Id da análise
                    <input type="number" name="idAnalise" min="1" required>
                </label>
            </c:otherwise>
        </c:choose>

        <label class="form-field">
            Ingrediente
            <select name="idIngrediente" required>
                <option value="">Selecione...</option>
                <c:forEach var="ing" items="${ingredientes}">
                    <option value="${ing.idIngrediente}"
                        ${param.idIngrediente == ing.idIngrediente ? 'selected' : ''}>
                        #${ing.idIngrediente} — <c:out value="${ing.tipo}"/>
                    </option>
                </c:forEach>
            </select>
        </label>

        <div class="form-actions">
            <a class="btn"
               href="${pageContext.request.contextPath}/ingredienteAnalises${not empty param.idAnalise ? '?idAnalise='.concat(param.idAnalise) : ''}">
                Cancelar
            </a>
            <button type="submit" class="btn primary">Vincular</button>
        </div>
    </form>
</div>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
