<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Perfil de cabelo"/>
<c:set var="paginaAtiva" value="usuarios"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Perfil de cabelo</h1>
        <p>Perfil de cabelo dos usuários.</p>
    </div>
    <div class="heading-actions">
        <a class="btn secondary" href="${ctx}/usuarios">Voltar para usuários</a>
        <c:if test="${empty param.idUsuario or empty perfisCabelo}">
            <a class="btn primary" href="${ctx}/perfisCabelo?acao=novo&idUsuario=${param.idUsuario}">Adicionar perfil de cabelo</a>
        </c:if>
    </div>
</div>

<div id="erro" class="alert-erro" role="alert" hidden></div>

<section class="table-panel">
    <div class="table-scroll">
        <table>
            <thead>
            <tr>
                <th scope="col">Usuário (id)</th>
                <th scope="col">Curvatura</th>
                <th scope="col">Oleosidade</th>
                <th scope="col">Espessura</th>
                <th scope="col">Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${perfisCabelo}" var="p">
                <tr>
                    <td>${p.idUsuario}</td>
                    <td><span class="badge purple">${p.curvatura}/4</span></td>
                    <td><span class="badge purple">${p.oleosidade}/10</span></td>
                    <td><span class="badge purple">${p.espessura}/10</span></td>
                    <td>
                        <div class="actions">
                            <a class="icon-btn" href="${ctx}/perfisCabelo?id=${p.idPerfilCabelo}" aria-label="Editar perfil de cabelo">
                                <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/106e6b38-e528-4239-9191-5b8babefdc63.svg" alt=""></span>
                            </a>
                            <button class="icon-btn" type="button" onclick="excluir(${p.idPerfilCabelo})" aria-label="Excluir perfil de cabelo">
                                <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/174bca6f-6692-44c0-9769-3c921855c872.svg" alt=""></span>
                            </button>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty perfisCabelo}">
                <tr><td colspan="5" class="empty">Nenhum perfil de cabelo encontrado.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</section>

<script>
    var faixaErro = document.getElementById('erro');

    function mostrarErro(mensagem) {
        faixaErro.textContent = mensagem;
        faixaErro.hidden = false;
    }

    async function excluir(id) {
        if (!confirm('Excluir este perfil de cabelo?')) return;
        faixaErro.hidden = true;

        try {
            var resp = await fetch('${ctx}/perfisCabelo?id=' + id, {method: 'DELETE'});

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