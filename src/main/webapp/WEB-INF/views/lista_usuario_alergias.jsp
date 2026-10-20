<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Alergias do usuário"/>
<c:set var="paginaAtiva" value="usuarios"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Alergias do usuário</h1>
    </div>
    <a class="btn secondary" href="${ctx}/usuarios">Voltar para usuários</a>

    <c:if test="${not empty param.idUsuario}">
        <a class="btn primary"
           href="${ctx}/usuarioAlergias?acao=novo&idUsuario=${param.idUsuario}">Vincular alergia</a>
    </c:if>
</div>

<div class="panel white">
    <c:choose>
        <c:when test="${empty usuariosAlergias}">
            <p>Nenhuma alergia vinculada.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th>Alergia</th>
                    <th>Grau</th>
                    <th>Registro</th>
                    <th>Ações</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${usuariosAlergias}" var="ua">
                    <tr>
                        <td><c:out value="${ua.nomeAlergia}"/></td>
                        <td>${ua.grau}</td>
                        <td>${ua.dtRegistro}</td>
                        <td>
                            <a class="btn secondary"
                               href="${ctx}/usuarioAlergias?id=${ua.idUsuarioAlergia}">Editar</a>
                            <button type="button" class="btn secondary"
                                    onclick="excluir(${ua.idUsuarioAlergia})">Excluir
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<script>
    function excluir(id) {
        if (!confirm("Remover esta alergia do usuário?")) return;

        fetch("${ctx}/usuarioAlergias?id=" + id, {method: "DELETE"})
            .then(resp => {
                if (resp.ok) {
                    location.reload();
                } else {
                    resp.text().then(msg => alert(msg));
                }
            });
    }
</script>

<%@ include file="/WEB-INF/views/rodape.jspf" %>