<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Adicionar preferências"/>
<c:set var="paginaAtiva" value="usuarios"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Adicionar preferências</h1>
    </div>
</div>

<div class="panel white form-card">
    <c:if test="${not empty erro}">
        <div class="alert-erro" role="alert"><c:out value="${erro}"/></div>
    </c:if>

    <form method="post" action="${ctx}/preferencias">
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
            Faixa de preço
            <input type="text" name="faixaPreco" maxlength="255"
                   value="<c:out value='${param.faixaPreco}'/>" placeholder="Ex.: R$ 30 - R$ 80">
        </label>

        <label class="switch-row">
            <span>Prefere produtos veganos</span>
            <input type="checkbox" role="switch" name="prefereVegano" ${not empty param.prefereVegano ? 'checked' : ''}>
        </label>

        <label class="form-field">
            Restrições de dieta
            <textarea name="restricoesDieta" maxlength="255"><c:out value="${param.restricoesDieta}"/></textarea>
        </label>

        <label class="form-field">
            Categorias preferidas
            <input type="text" name="categoriaPref" maxlength="255"
                   value="<c:out value='${param.categoriaPref}'/>" placeholder="Ex.: Skincare, Maquiagem">
        </label>

        <label class="form-field">
            Marcas favoritas
            <input type="text" name="marcasFav" maxlength="255"
                   value="<c:out value='${param.marcasFav}'/>" placeholder="Ex.: Marca A, Marca B">
        </label>

        <div class="form-actions">
            <a class="btn secondary" href="${ctx}/preferencias">Cancelar</a>
            <button class="btn primary" type="submit">Salvar</button>
        </div>
    </form>
</div>

<%@ include file="/WEB-INF/views/rodape.jspf" %>