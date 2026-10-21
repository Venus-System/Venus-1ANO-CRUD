<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Editar preferências"/>
<c:set var="paginaAtiva" value="usuarios"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Editar preferências</h1>
    </div>
</div>

<div class="panel white form-card">
    <div id="erro" class="alert-erro" role="alert" hidden></div>

    <form id="form-editar">
        <label class="form-field">
            Faixa de preço
            <input id="faixaPreco" type="text" maxlength="255"
                   value="<c:out value='${preferencia.faixaPreco}'/>">
        </label>

        <label class="switch-row">
            <span>Prefere produtos veganos</span>
            <input id="prefereVegano" type="checkbox" role="switch" ${preferencia.prefereVegano ? 'checked' : ''}>
        </label>

        <label class="form-field">
            Restrições de dieta
            <textarea id="restricoesDieta" maxlength="255"><c:out value="${preferencia.restricoesDieta}"/></textarea>
        </label>

        <label class="form-field">
            Categorias preferidas
            <input id="categoriaPref" type="text" maxlength="255"
                   value="<c:out value='${preferencia.categoriaPref}'/>">
        </label>

        <label class="form-field">
            Marcas favoritas
            <input id="marcasFav" type="text" maxlength="255"
                   value="<c:out value='${preferencia.marcasFav}'/>">
        </label>

        <div class="form-actions">
            <a class="btn secondary" href="${ctx}/preferencias?idUsuario=${preferencia.idUsuario}">Cancelar</a>
            <button class="btn primary" type="submit">Salvar</button>
        </div>
    </form>
</div>

<script>
    var faixaErro = document.getElementById('erro');

    function mostrarErro(mensagem) {
        faixaErro.textContent = mensagem;
        faixaErro.hidden = false;
    }

    document.getElementById('form-editar').addEventListener('submit', async function (e) {
        e.preventDefault();
        faixaErro.hidden = true;

        try {
            var resp = await fetch('${ctx}/preferencias', {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    idPreferencias: ${preferencia.idPreferencias},
                    faixaPreco: document.getElementById('faixaPreco').value,
                    prefereVegano: document.getElementById('prefereVegano').checked,
                    restricoesDieta: document.getElementById('restricoesDieta').value,
                    categoriaPref: document.getElementById('categoriaPref').value,
                    marcasFav: document.getElementById('marcasFav').value
                })
            });

            if (resp.ok) {
                window.location.href = '${ctx}/preferencias?idUsuario=${preferencia.idUsuario}';
            } else if (resp.status >= 500) {
                mostrarErro('Erro no servidor. Tente novamente.');
            } else {
                mostrarErro(await resp.text());
            }
        } catch (erro) {
            mostrarErro('Não foi possível conectar ao servidor.');
        }
    });
</script>

<%@ include file="/WEB-INF/views/rodape.jspf" %>