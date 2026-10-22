<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Perfil de pele"/>
<c:set var="paginaAtiva" value="usuarios"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Perfil de pele</h1>
        <p>Perfil de pele dos usuários.</p>
    </div>
    <div class="heading-actions">
        <a class="btn secondary" href="${ctx}/usuarios">Voltar para usuários</a>
        <c:if test="${empty param.idUsuario or empty perfisPele}">
            <a class="btn primary" href="${ctx}/perfisPele?acao=novo&idUsuario=${param.idUsuario}">Adicionar perfil de pele</a>
        </c:if>
    </div>
</div>

<div class="toolbar">
    <label class="search">
        <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/05487dae-bbcf-4cbf-b4f6-dac8001dd2c1.svg" alt=""></span>
        <input id="busca" type="search" aria-label="Buscar perfis de pele" placeholder="Buscar por tipo de pele...">
    </label>
</div>

<div id="erro" class="alert-erro" role="alert" hidden></div>

<section class="table-panel">
    <div class="table-scroll">
        <table id="tabela-perfis">
            <thead>
            <tr>
                <th scope="col">Usuário (id)</th>
                <th scope="col">Tipo de pele</th>
                <th scope="col">Sensibilidade</th>
                <th scope="col">Oleosidade</th>
                <th scope="col">Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${perfisPele}" var="p">
                <tr>
                    <td>${p.idUsuario}</td>
                    <td><c:out value="${p.tipoPele}"/></td>
                    <td><span class="badge purple">${p.sensibilidade}/5</span></td>
                    <td><span class="badge purple">${p.nivelOleosidade}/5</span></td>
                    <td>
                        <div class="actions">
                            <a class="icon-btn" href="${ctx}/perfisPele?id=${p.idPerfilPele}" aria-label="Editar perfil de pele">
                                <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/106e6b38-e528-4239-9191-5b8babefdc63.svg" alt=""></span>
                            </a>
                            <button class="icon-btn" type="button" onclick="excluir(${p.idPerfilPele})" aria-label="Excluir perfil de pele">
                                <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/174bca6f-6692-44c0-9769-3c921855c872.svg" alt=""></span>
                            </button>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty perfisPele}">
                <tr><td colspan="5" class="empty">Nenhum perfil de pele encontrado.</td></tr>
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

    document.getElementById('busca').addEventListener('input', function (e) {
        var termo = e.target.value.toLowerCase();
        document.querySelectorAll('#tabela-perfis tbody tr').forEach(function (tr) {
            if (tr.querySelector('.empty')) return;
            tr.style.display = tr.textContent.toLowerCase().indexOf(termo) >= 0 ? '' : 'none';
        });
    });

    async function excluir(id) {
        if (!confirm('Excluir este perfil de pele?')) return;
        faixaErro.hidden = true;

        try {
            var resp = await fetch('${ctx}/perfisPele?id=' + id, {method: 'DELETE'});

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