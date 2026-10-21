<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Ingredientes da Análise</h1>
        <p>
            <c:choose>
                <c:when test="${not empty param.idAnalise}">Ingredientes vinculados à análise ${param.idAnalise}.</c:when>
                <c:when test="${not empty param.idIngrediente}">Análises que contêm o ingrediente ${param.idIngrediente}.</c:when>
                <c:otherwise>Todos os vínculos entre ingredientes e análises.</c:otherwise>
            </c:choose>
        </p>
    </div>
    <div class="heading-actions">
        <c:if test="${not empty param.idAnalise}">
            <a class="btn" href="${pageContext.request.contextPath}/analises">Voltar</a>
        </c:if>
        <a class="btn primary"
           href="${pageContext.request.contextPath}/ingredienteAnalises?acao=novo&idAnalise=${param.idAnalise}">
            + Vincular ingrediente
        </a>
    </div>
</div>

<div class="table-panel">
    <div class="table-scroll">
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Ingrediente</th>
                <th>Análise</th>
                <th>Tipo</th>
                <th>Nível de perigo</th>
                <th>Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="ia" items="${ingredientesAnalises}">
                <tr>
                    <td>${ia.idIngredienteAnalise}</td>
                    <td>#${ia.idIngrediente}</td>
                    <td>#${ia.idAnalise}</td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty ia.tipo}"><c:out value="${ia.tipo}"/></c:when>
                            <c:otherwise>—</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${empty ia.tipo}">—</c:when>
                            <c:when test="${ia.nivelPerigo >= 7}"><span class="badge pink">${ia.nivelPerigo}</span></c:when>
                            <c:when test="${ia.nivelPerigo >= 4}"><span class="badge yellow">${ia.nivelPerigo}</span></c:when>
                            <c:otherwise><span class="badge green">${ia.nivelPerigo}</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <div class="actions">
                            <button type="button" class="icon-btn" title="Excluir vínculo"
                                    onclick="excluirVinculo(${ia.idIngredienteAnalise})">🗑</button>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty ingredientesAnalises}">
                <tr><td colspan="6" class="empty">Nenhum ingrediente vinculado.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<script>
    // DELETE /ingredienteAnalises?id=... (o servlet responde 200, 400 ou 404 em text/plain)
    function excluirVinculo(id) {
        if (!confirm('Remover este ingrediente da análise?')) return;

        fetch('${pageContext.request.contextPath}/ingredienteAnalises?id=' + id, {method: 'DELETE'})
            .then(function (resp) {
                if (resp.ok) {
                    location.reload();
                } else {
                    resp.text().then(function (msg) { alert(msg || 'Não foi possível excluir.'); });
                }
            })
            .catch(function () { alert('Erro de comunicação com o servidor.'); });
    }
</script>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
