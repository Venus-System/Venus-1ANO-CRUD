<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Adicionar perfil de pele"/>
<c:set var="paginaAtiva" value="usuarios"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Adicionar perfil de pele</h1>
    </div>
</div>

<div class="panel white form-card">
    <c:if test="${not empty erro}">
        <div class="alert-erro" role="alert"><c:out value="${erro}"/></div>
    </c:if>

    <form method="post" action="${ctx}/perfisPele">
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
            Tipo de pele
            <input type="text" name="tipoPele" maxlength="50" list="tipos-pele" required
                   value="<c:out value='${param.tipoPele}'/>" placeholder="Ex.: Mista">
            <datalist id="tipos-pele">
                <option value="Normal">
                <option value="Seca">
                <option value="Oleosa">
                <option value="Mista">
                <option value="Sensível">
            </datalist>
        </label>

        <div class="form-row">
            <label class="form-field">
                Sensibilidade (1 a 5)
                <select name="sensibilidade" required>
                    <option value="">Selecione</option>
                    <c:forEach var="n" begin="1" end="5">
                        <option value="${n}" ${param.sensibilidade == n ? 'selected' : ''}>${n}</option>
                    </c:forEach>
                </select>
            </label>

            <label class="form-field">
                Oleosidade (1 a 5)
                <select name="nivelOleosidade" required>
                    <option value="">Selecione</option>
                    <c:forEach var="n" begin="1" end="5">
                        <option value="${n}" ${param.nivelOleosidade == n ? 'selected' : ''}>${n}</option>
                    </c:forEach>
                </select>
            </label>
        </div>

        <div class="form-actions">
            <a class="btn secondary" href="${ctx}/perfisPele">Cancelar</a>
            <button class="btn primary" type="submit">Salvar</button>
        </div>
    </form>
</div>

<%@ include file="/WEB-INF/views/rodape.jspf" %>