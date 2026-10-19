<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Editar alergia"/>
<c:set var="paginaAtiva" value="usuarios"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Editar alergia</h1>
        <p>Altere o nome da alergia.</p>
    </div>
</div>

<div class="panel white form-card">
    <div id="erro" class="alert-erro" role="alert" hidden></div>

    <form id="form-editar" data-id="${alergia.idAlergia}">
        <div class="form-field">
            <label for="nome">Nome da alergia</label>
            <input type="text" id="nome" name="nome" required minlength="2" maxlength="100"
                   value="<c:out value='${alergia.nomeAlergia}'/>">
        </div>

        <div class="form-actions">
            <a class="btn secondary" href="${ctx}/alergias">Cancelar</a>
            <button type="submit" id="salvar" class="btn primary">Salvar alterações</button>
        </div>
    </form>
</div>

<script>
    // Edição: chama o doPut do AlergiaServlet, enviando JSON {idAlergia, nome}
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
            var resp = await fetch('${ctx}/alergias', {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    idAlergia: form.dataset.id,
                    nome: document.getElementById('nome').value
                })
            });

            if (resp.ok) {
                window.location.href = '${ctx}/alergias';
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
