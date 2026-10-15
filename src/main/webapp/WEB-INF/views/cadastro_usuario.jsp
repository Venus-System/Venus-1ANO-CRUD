<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Editar usuário"/>
<c:set var="paginaAtiva" value="usuarios"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Editar usuário</h1>
        <p>Atualize os dados do usuário. A senha não é alterada aqui.</p>
    </div>
</div>

<section class="panel white form-card">
    <%-- Os id dos campos são usados pelo JavaScript abaixo (PUT para o doPut do servlet) --%>
    <form id="formEditar">
        <input type="hidden" id="idUsuario" value="${usuario.idUsuario}">

        <label class="form-field">Nome completo *
            <input type="text" id="nome" value="<c:out value='${usuario.nomeCompleto}'/>" maxlength="200" required>
        </label>

        <label class="form-field">Gênero
            <select id="genero">
                <option value="">Selecione</option>
                <option value="Feminino"  ${usuario.genero == 'Feminino'  ? 'selected' : ''}>Feminino</option>
                <option value="Masculino" ${usuario.genero == 'Masculino' ? 'selected' : ''}>Masculino</option>
                <option value="Outro"     ${usuario.genero == 'Outro'     ? 'selected' : ''}>Outro</option>
            </select>
        </label>

        <label class="form-field">E-mail *
            <input type="email" id="email" value="<c:out value='${usuario.email}'/>" maxlength="150" required>
        </label>

        <div class="form-row">
            <label class="form-field">Telefone
                <input type="tel" id="telefone" value="<c:out value='${usuario.telefone}'/>">
            </label>
            <label class="form-field">Data de nascimento *
                <input type="date" id="dt_nascimento" value="${usuario.dtNascimento}" required>
            </label>
        </div>

        <p id="msg-erro" class="alert-erro" role="alert" hidden></p>

        <div class="form-actions">
            <a class="btn" href="${ctx}/usuarios">Cancelar</a>
            <button type="submit" class="btn primary">Salvar alterações</button>
        </div>
    </form>
</section>

<script>
    document.getElementById('formEditar').addEventListener('submit', async function (e) {
        e.preventDefault();
        var erro = document.getElementById('msg-erro');
        erro.hidden = true;

        var dados = {
            idUsuario: document.getElementById('idUsuario').value,
            nome: document.getElementById('nome').value,
            genero: document.getElementById('genero').value,
            email: document.getElementById('email').value,
            telefone: document.getElementById('telefone').value,
            dt_nascimento: document.getElementById('dt_nascimento').value
        };

        var resp = await fetch('${ctx}/usuarios', {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(dados)
        });

        if (resp.ok) {
            window.location.href = '${ctx}/usuarios';
        } else {
            erro.textContent = await resp.text();
            erro.hidden = false;
        }
    });
</script>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
