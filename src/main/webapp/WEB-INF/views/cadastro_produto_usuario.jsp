<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Vincular produto"/>
<c:set var="paginaAtiva" value="usuarios"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Vincular produto ao usuário</h1>
        <p>Escolha o produto e o usuário que serão vinculados.</p>
    </div>
</div>

<div class="panel white form-card">
    <c:if test="${not empty erro}">
        <div class="alert-erro" role="alert"><c:out value="${erro}"/></div>
    </c:if>

    <form method="post" action="${ctx}/produtoUsuarios">
        <div class="form-field">
            <label for="id_produto">Produto</label>
            <select id="id_produto" name="id_produto" required>
                <option value="">Selecione um produto</option>
                <c:forEach var="p" items="${produtos}">
                    <option value="${p.idProduto}" ${param.id_produto eq p.idProduto ? 'selected' : ''}>
                        <c:out value="${p.nome}"/>
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-field">
            <label for="id_usuario">Usuário</label>
            <select id="id_usuario" name="id_usuario" required>
                <option value="">Selecione um usuário</option>
                <c:forEach var="u" items="${usuarios}">
                    <option value="${u.idUsuario}" ${param.id_usuario eq u.idUsuario ? 'selected' : ''}>
                        <c:out value="${u.nomeCompleto}"/>
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-actions">
            <a class="btn secondary" href="${ctx}/produtoUsuarios">Cancelar</a>
            <button type="submit" class="btn primary">Cadastrar</button>
        </div>
    </form>
</div>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
