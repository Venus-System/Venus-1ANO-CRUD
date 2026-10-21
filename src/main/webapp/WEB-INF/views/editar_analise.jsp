<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Editar análise"/>
<c:set var="paginaAtiva" value="analises"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Editar análise</h1>
        <p>Análise de ${analise.dtHrAnaliseFormatada}</p>
    </div>
</div>

<div class="panel white form-card">
    <div id="erro" class="alert-erro" role="alert" hidden></div>

    <form id="form-editar">
        <label class="form-field">
            Resumo do resultado
            <textarea id="resumoResultado" maxlength="500" required><c:out value="${analise.resumoResultado}"/></textarea>
        </label>

        <label class="form-field">
            Pontuação (0 a 100)
            <input id="pontuacao" type="number" min="0" max="100" required value="${analise.pontuacao}">
        </label>

        <div class="form-actions">
            <a class="btn secondary" href="${ctx}/analises?idUsuario=${analise.idUsuario}">Cancelar</a>
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
            var resp = await fetch('${ctx}/analises', {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    idAnalise: ${analise.idAnalise},
                    resumoResultado: document.getElementById('resumoResultado').value,
                    pontuacao: document.getElementById('pontuacao').value
                })
            });

            if (resp.ok) {
                window.location.href = '${ctx}/analises?idUsuario=${analise.idUsuario}';
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