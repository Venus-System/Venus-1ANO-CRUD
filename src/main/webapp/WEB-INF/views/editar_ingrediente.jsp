<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Editar ingrediente"/>
<c:set var="paginaAtiva" value="ingredientes"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Editar ingrediente</h1>
        <p>Altere o tipo ou o nível de perigo.</p>
    </div>
</div>

<div class="panel white form-card">
    <div id="erro" class="alert-erro" role="alert" hidden></div>

    <form id="form-editar" data-id="${ingrediente.idIngrediente}">
        <div class="form-field">
            <label for="tipo">Tipo</label>
            <input type="text" id="tipo" name="tipo" required maxlength="100"
                   value="<c:out value='${ingrediente.tipo}'/>">
        </div>

        <div class="form-field">
            <label for="nivelPerigo">Nível de perigo (1 a 5)</label>
            <input type="number" id="nivelPerigo" name="nivelPerigo" required min="1" max="5"
                   value="${ingrediente.nivelPerigo}">
        </div>

        <div class="form-actions">
            <a class="btn secondary" href="${ctx}/ingredientes">Cancelar</a>
            <button type="submit" id="salvar" class="btn primary">Salvar alterações</button>
        </div>
    </form>
</div>

<script>
    // Edição: chama o doPut do IngredientesServlet, enviando JSON {idIngrediente, nivelPerigo, tipo}
    var form = document.getElementById('form-editar');
    var botao = document.getElementById('salvar');
    var faixaErro = document.getElementById('erro');

    function mostrarErro(mensagem) {
        faixaErro.textContent = mensagem;
        faixaErro.hidden = false;
    }

    form.addEventListener('submit', async function (e) {
        e.preventDefault();
        faixaErro.hidden = true;
        botao.disabled = true;

        try {
            var resp = await fetch('${ctx}/ingredientes', {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    idIngrediente: form.dataset.id,
                    nivelPerigo: document.getElementById('nivelPerigo').value,
                    tipo: document.getElementById('tipo').value
                })
            });

            if (resp.ok) {
                window.location.href = '${ctx}/ingredientes';
            } else if (resp.status >= 500) {
                mostrarErro('Erro no servidor. Tente novamente.');
                botao.disabled = false;
            } else {
                mostrarErro(await resp.text());
                botao.disabled = false;
            }
        } catch (erro) {
            mostrarErro('Não foi possível conectar ao servidor.');
            botao.disabled = false;
        }
    });
</script>

<%@ include file="/WEB-INF/views/rodape.jspf" %>