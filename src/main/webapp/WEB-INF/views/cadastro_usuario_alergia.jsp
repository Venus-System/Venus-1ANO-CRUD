<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Vincular alergia"/>
<c:set var="paginaAtiva" value="usuarios"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Vincular alergia ao usuário</h1>
        <p>Escolha a alergia e informe o grau.</p>
    </div>
</div>

<div class="panel white form-card">
    <c:if test="${not empty erro}">
        <div class="alert-erro" role="alert"><c:out value="${erro}"/></div>
    </c:if>

    <form method="post" action="${ctx}/usuarioAlergias">
        <input type="hidden" name="idUsuario" value="<c:out value='${param.idUsuario}'/>">

        <div class="form-field">
            <label for="idAlergia">Alergia</label>
            <select id="idAlergia" name="idAlergia" required>
                <option value="">Selecione...</option>
                <c:forEach items="${alergias}" var="a">
                    <option value="${a.idAlergia}"
                        ${param.idAlergia == a.idAlergia ? 'selected' : ''}>
                        <c:out value="${a.nomeAlergia}"/>
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-field">
            <label for="grau">Grau</label>
            <select id="grau" name="grau" required>
                <option value="">Selecione...</option>
                <option value="1" ${param.grau == '1' ? 'selected' : ''}>1 - Leve</option>
                <option value="2" ${param.grau == '2' ? 'selected' : ''}>2 - Moderado</option>
                <option value="3" ${param.grau == '3' ? 'selected' : ''}>3 - Grave</option>
            </select>
        </div>

        <div class="form-actions">
            <a class="btn secondary"
               href="${ctx}/usuarioAlergias?idUsuario=${param.idUsuario}">Cancelar</a>
            <button type="submit" class="btn primary">Vincular</button>
        </div>
    </form>
</div>

<%@ include file="/WEB-INF/views/rodape.jspf" %>