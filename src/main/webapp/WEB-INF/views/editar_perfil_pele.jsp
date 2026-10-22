<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Editar perfil de pele"/>
<c:set var="paginaAtiva" value="usuarios"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Editar perfil de pele</h1>
    </div>
</div>

<div class="panel white form-card">
    <div id="erro" class="alert-erro" role="alert" hidden></div>

    <form id="form-editar">
        <label class="form-field">
            Tipo de pele
            <input id="tipoPele" type="text" maxlength="50" list="tipos-pele" required
                   value="<c:out value='${perfilPele.tipoPele}'/>">
            <datalist id="tipos-pele">
                <option value="Normal">
                <option value="Seca">
                <option value="Oleosa">
                <option value="Mista">
                <option value="Sensível">
            </datalist>
        </label>

        <div class="form-row">
            <label class="form-field">
                Sensibilidade (1 a 5)
                <select id="sensibilidade" required>
                    <c:forEach var="n" begin="1" end="5">
                        <option value="${n}" ${perfilPele.sensibilidade == n ? 'selected' : ''}>${n}</option>
                    </c:forEach>
                </select>
            </label>

            <label class="form-field">
                Oleosidade (1 a 5)
                <select id="nivelOleosidade" required>
                    <c:forEach var="n" begin="1" end="5">
                        <option value="${n}" ${perfilPele.nivelOleosidade == n ? 'selected' : ''}>${n}</option>
                    </c:forEach>
                </select>
            </label>
        </div>

        <div class="form-actions">
            <a class="btn secondary" href="${ctx}/perfisPele?idUsuario=${perfilPele.idUsuario}">Cancelar</a>
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
            var resp = await fetch('${ctx}/perfisPele', {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    idPerfilPele: ${perfilPele.idPerfilPele},
                    tipoPele: document.getElementById('tipoPele').value,
                    sensibilidade: document.getElementById('sensibilidade').value,
                    nivelOleosidade: document.getElementById('nivelOleosidade').value
                })
            });

            if (resp.ok) {
                window.location.href = '${ctx}/perfisPele?idUsuario=${perfilPele.idUsuario}';
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