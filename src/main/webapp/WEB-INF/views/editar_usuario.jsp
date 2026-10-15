<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Usuários"/>
<c:set var="paginaAtiva" value="usuarios"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Gerenciamento de usuários</h1>
        <p>Gerencie os usuários cadastrados na plataforma.</p>
    </div>
    <div class="heading-actions">
        <a class="btn primary" href="${ctx}/usuarios?acao=novo">
            <span class="figma-icon" style="--icon-size:20px"><img src="${ctx}/assets/21d8c3ca-d1c2-4fee-8539-28c320da8381.svg" alt=""></span>
            Adicionar novo usuário
        </a>
    </div>
</div>

<section class="stats" aria-label="Indicadores">
    <article class="stat" data-icon-kind="usuarios">
        <span class="bubble" aria-hidden="true">
            <span class="figma-icon" style="--icon-size:33px"><img src="${ctx}/assets/114998ae-07f2-474e-a199-3038b9bbeb93.svg" alt=""></span>
        </span>
        <div>
            <span class="stat-label">Usuários totais</span>
            <strong>${usuarios.size()}</strong>
        </div>
    </article>
</section>

<div class="toolbar">
    <label class="search">
        <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/05487dae-bbcf-4cbf-b4f6-dac8001dd2c1.svg" alt=""></span>
        <input id="busca" type="search" aria-label="Buscar usuários" placeholder="Buscar por nome ou e-mail...">
    </label>
</div>

<section class="table-panel">
    <div class="table-scroll" role="region" aria-label="Tabela de usuários" tabindex="0">
        <table id="tabela-usuarios">
            <thead>
            <tr>
                <th scope="col">Usuário</th>
                <th scope="col">E-mail</th>
                <th scope="col">Gênero</th>
                <th scope="col">Telefone</th>
                <th scope="col">Nascimento</th>
                <th scope="col">Cadastro</th>
                <th scope="col">Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="u" items="${usuarios}">
                <tr>
                    <td>
                        <div class="user-cell">
                            <img src="${ctx}/assets/155edb06-a3f3-4451-933a-96518d45898a.svg" alt="">
                            <span><c:out value="${u.nomeCompleto}"/></span>
                        </div>
                    </td>
                    <td><c:out value="${u.email}"/></td>
                    <td><c:out value="${u.genero}"/></td>
                    <td><c:out value="${u.telefone}"/></td>
                    <td data-data="${u.dtNascimento}">${u.dtNascimento}</td>
                    <td data-data="${u.dtCadastro}">${u.dtCadastro}</td>
                    <td>
                        <div class="actions">
                            <a class="icon-btn" href="${ctx}/usuarios?id=${u.idUsuario}" aria-label="Editar ${u.nomeCompleto}">
                                <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/106e6b38-e528-4239-9191-5b8babefdc63.svg" alt=""></span>
                            </a>
                            <button class="icon-btn" type="button" data-excluir="${u.idUsuario}" aria-label="Excluir ${u.nomeCompleto}">
                                <span class="figma-icon" style="--icon-size:16px"><img src="${ctx}/assets/174bca6f-6692-44c0-9769-3c921855c872.svg" alt=""></span>
                            </button>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty usuarios}">
                <tr><td colspan="7" class="empty">Nenhum usuário cadastrado.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</section>

<script>
    // Datas ISO (2026-10-14) -> 14/10/2026
    document.querySelectorAll('[data-data]').forEach(function (td) {
        var iso = td.getAttribute('data-data');
        if (/^\d{4}-\d{2}-\d{2}$/.test(iso)) {
            td.textContent = new Intl.DateTimeFormat('pt-BR').format(new Date(iso + 'T12:00:00'));
        }
    });

    // Busca na própria página (nome, e-mail, etc.)
    document.getElementById('busca').addEventListener('input', function (e) {
        var termo = e.target.value.toLowerCase();
        document.querySelectorAll('#tabela-usuarios tbody tr').forEach(function (tr) {
            if (tr.querySelector('.empty')) return;
            tr.style.display = tr.textContent.toLowerCase().indexOf(termo) >= 0 ? '' : 'none';
        });
    });

    // Exclusão: chama o doDelete do UsuarioServlet
    document.querySelectorAll('[data-excluir]').forEach(function (botao) {
        botao.addEventListener('click', async function () {
            if (!confirm('Excluir este usuário?')) return;
            var resp = await fetch('${ctx}/usuarios?id=' + botao.dataset.excluir, {method: 'DELETE'});
            if (resp.ok) {
                location.reload();
            } else {
                alert(await resp.text());
            }
        });
    });
</script>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
