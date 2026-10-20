<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Editar grau da alergia"/>
<c:set var="paginaAtiva" value="usuarios"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Editar grau da alergia</h1>
        <p>Altere o grau da alergia deste usuário.</p>
    </div>
</div>

<div class="panel white form-card">
    <div id="erro" class="alert-erro" role="alert" hidden></div>

    <form id="form-editar" data-id="${usuarioAlergia.idUsuarioAlergia}">
        <div class="form-field">
            <label for="grau">Grau</label>
            <select id="grau" name="grau" required>
                <option value="1" ${usuarioAlergia.grau == 1 ? 'selected' : ''}>1 - Leve</option>
                <option value="2" ${usuarioAlergia.grau == 2 ? 'selected' : ''}>2 - Moderado</option>
                <option value="3" ${usuarioAlergia.grau == 3 ? 'selected' : ''}>3 - Grave</option>
            </select>
        </div>

        <div class="form-actions">
            <a class="btn secondary"
               href="${ctx}/usuarioAlergias?idUsuario=${usuarioAlergia.idUsuario}">Cancelar</a>
            <button type="submit" id="salvar" class="btn primary">Salvar alterações</button>
        </div>
    </form>
</div>

<script>
    // Edição: chama o doPut do UsuarioAlergiaServlet, enviando JSON {idUsuarioAlergia, grau}
    var form = document.getElementById('form-editar');
    var botao = document.getElementById('salvar');
    var faixaErro = document.getElementById('erro');
    var voltar = '${ctx}/usuarioAlergias?idUsuario=${usuarioAlergia.idUsuario}';

    function mostrarErro(mensagem) {
        faixaErro.textContent = mensagem;
        faixaErro.hidden = false;
    }

    form.addEventListener('submit', async function (e) {
        e.preventDefault();
        faixaErro.hidden = true;
        botao.disabled = true;

        try {
            var resp = await fetch('${ctx}/usuarioAlergias', {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    idUsuarioAlergia: form.dataset.id,
                    grau: document.getElementById('grau').value
                })
            });

            if (resp.ok) {
                window.location.href = voltar;
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