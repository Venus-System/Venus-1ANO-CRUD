<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Ingredientes"/>
<c:set var="paginaAtiva" value="ingredientes"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Ingredientes</h1>
        <p>Ingredientes cadastrados e o nível de perigo de cada um.</p>
    </div>
    <a class="btn primary" href="${ctx}/ingredientes?acao=novo">Adicionar ingrediente</a>
</div>

<div class="panel white">
    <div id="erro" class="alert-erro" role="alert" hidden></div>

    <c:choose>
        <c:when test="${empty ingredientes}">
            <p>Nenhum ingrediente cadastrado.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th>Tipo</th>
                    <th>Nível de perigo</th>
                    <th>Última atualização</th>
                    <th>Ações</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${ingredientes}" var="i">
                    <tr>
                        <td><c:out value="${i.tipo}"/></td>
                        <td>${i.nivelPerigo}</td>
                        <td>${i.dtAtualizacao}</td>
                        <td>
                            <a class="btn secondary"
                               href="${ctx}/ingredientes?id=${i.idIngrediente}">Editar</a>
                            <button type="button" class="btn secondary"
                                    onclick="excluir(${i.idIngrediente})">Excluir</button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<script>
    var faixaErro = document.getElementById('erro');

    function mostrarErro(mensagem) {
        faixaErro.textContent = mensagem;
        faixaErro.hidden = false;
    }

    async function excluir(id) {
        if (!confirm('Excluir este ingrediente?')) return;
        faixaErro.hidden = true;

        try {
            var resp = await fetch('${ctx}/ingredientes?id=' + id, {method: 'DELETE'});

            if (resp.ok) {
                window.location.reload();
            } else if (resp.status >= 500) {
                mostrarErro('Erro no servidor. Tente novamente.');
            } else {
                mostrarErro(await resp.text());
            }
        } catch (erro) {
            mostrarErro('Não foi possível conectar ao servidor.');
        }
    }
</script>

<%@ include file="/WEB-INF/views/rodape.jspf" %>