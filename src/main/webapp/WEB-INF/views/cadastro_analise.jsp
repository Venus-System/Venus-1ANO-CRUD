<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Adicionar análise"/>
<c:set var="paginaAtiva" value="analises"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Adicionar análise</h1>
    </div>
</div>

<div class="panel white form-card">
    <c:if test="${not empty erro}">
        <div class="alert-erro" role="alert"><c:out value="${erro}"/></div>
    </c:if>

    <form method="post" action="${ctx}/analises">
        <label class="form-field">
            Usuário
            <select name="idUsuario" required>
                <option value="">Selecione o usuário</option>
                <c:forEach items="${usuarios}" var="u">
                    <option value="${u.idUsuario}" ${param.idUsuario == u.idUsuario ? 'selected' : ''}>
                        <c:out value="${u.nomeCompleto}"/>
                    </option>
                </c:forEach>
            </select>
        </label>

        <label class="form-field">
            Resumo do resultado
            <textarea name="resumoResultado" maxlength="500" required><c:out value="${param.resumoResultado}"/></textarea>
        </label>

        <label class="form-field">
            Pontuação (0 a 100)
            <input type="number" name="pontuacao" min="0" max="100" required
                   value="<c:out value='${param.pontuacao}'/>">
        </label>

        <div class="form-actions">
            <a class="btn secondary" href="${ctx}/analises?idUsuario=${param.idUsuario}">Cancelar</a>
            <button class="btn primary" type="submit">Salvar</button>
        </div>
    </form>
</div>

<%@ include file="/WEB-INF/views/rodape.jspf" %>