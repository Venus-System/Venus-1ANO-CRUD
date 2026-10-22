<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Editar nome"/>
<c:set var="paginaAtiva" value="ingredientes"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Editar nome do ingrediente</h1>
    </div>
</div>

<div class="panel white form-card">
    <div id="erro" class="alert-erro" role="alert" hidden></div>

    <form id="form-editar">
        <label class="form-field">
            Nome
            <input id="nome" type="text" maxlength="100" required
                   value="<c:out value='${nomeIngrediente.nomeIngrediente}'/>">
        </label>

        <div class="form-actions">
            <a class="btn secondary" href="${ctx}/nomeIngredientes?idIngrediente=${nomeIngrediente.idIngrediente}">Cancelar</a>
            <button class="btn primary" type="submit">Salvar</button>
        </div>
    </form>
</div>

<script>
    var faixaErro = document.getElementById('erro');

    document.getElementById('form-editar').addEventListener('submit', async function (e) {
        e.preventDefault();
        faixaErro.hidden = true;

        try {
            var resp = await fetch('${ctx}/nomeIngredientes', {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    idNomeIngrediente: ${nomeIngrediente.idNomeIngrediente},
                    nome: document.getElementById('nome').value
                })
            });

            if (resp.ok) {
                window.location.href = '${ctx}/nomeIngredientes?idIngrediente=${nomeIngrediente.idIngrediente}';
            } else if (resp.status >= 500) {
                faixaErro.textContent = 'Erro no servidor. Tente novamente.';
                faixaErro.hidden = false;
            } else {
                faixaErro.textContent = await resp.text();
                faixaErro.hidden = false;
            }
        } catch (erro) {
            faixaErro.textContent = 'Não foi possível conectar ao servidor.';
            faixaErro.hidden = false;
        }
    });
</script>

<%@ include file="/WEB-INF/views/rodape.jspf" %>