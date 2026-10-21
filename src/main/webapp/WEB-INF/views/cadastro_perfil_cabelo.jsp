<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Adicionar perfil de cabelo"/>
<c:set var="paginaAtiva" value="usuarios"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Adicionar perfil de cabelo</h1>
    </div>
</div>

<div class="panel white form-card">
    <c:if test="${not empty erro}">
        <div class="alert-erro" role="alert"><c:out value="${erro}"/></div>
    </c:if>

    <form method="post" action="${ctx}/perfisCabelo">
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

        <div class="form-row">
            <label class="form-field">
                Curvatura (1 a 5)
                <select name="curvatura" required>
                    <option value="">Selecione</option>
                    <c:forEach var="n" begin="1" end="4">
                        <option value="${n}" ${param.curvatura == n ? 'selected' : ''}>${n}</option>
                    </c:forEach>
                </select>
            </label>

            <label class="form-field">
                Oleosidade (1 a 5)
                <select name="oleosidade" required>
                    <option value="">Selecione</option>
                    <c:forEach var="n" begin="1" end="10">
                        <option value="${n}" ${param.oleosidade == n ? 'selected' : ''}>${n}</option>
                    </c:forEach>
                </select>
            </label>
        </div>

        <label class="form-field">
            Espessura (1 a 10)
            <select name="espessura" required>
                <option value="">Selecione</option>
                <c:forEach var="n" begin="1" end="5">
                    <option value="${n}" ${param.espessura == n ? 'selected' : ''}>${n}</option>
                </c:forEach>
            </select>
        </label>

        <div class="form-actions">
            <a class="btn secondary" href="${ctx}/perfisCabelo">Cancelar</a>
            <button class="btn primary" type="submit">Salvar</button>
        </div>
    </form>
</div>

<%@ include file="/WEB-INF/views/rodape.jspf" %>