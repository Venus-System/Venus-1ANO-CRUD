<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Editar perfil de cabelo"/>
<c:set var="paginaAtiva" value="usuarios"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Editar perfil de cabelo</h1>
    </div>
</div>

<div class="panel white form-card">
    <div id="erro" class="alert-erro" role="alert" hidden></div>

    <form id="form-editar">
        <div class="form-row">
            <label class="form-field">
                Curvatura (1 a 4)
                <select id="curvatura" required>
                    <c:forEach var="n" begin="1" end="4">
                        <option value="${n}" ${perfilCabelo.curvatura == n ? 'selected' : ''}>${n}</option>
                    </c:forEach>
                </select>
            </label>

            <label class="form-field">
                Oleosidade (1 a 10)
                <select id="oleosidade" required>
                    <c:forEach var="n" begin="1" end="10">
                        <option value="${n}" ${perfilCabelo.oleosidade == n ? 'selected' : ''}>${n}</option>
                    </c:forEach>
                </select>
            </label>
        </div>

        <label class="form-field">
            Espessura (1 a 10)
            <select id="espessura" required>
                <c:forEach var="n" begin="1" end="10">
                    <option value="${n}" ${perfilCabelo.espessura == n ? 'selected' : ''}>${n}</option>
                </c:forEach>
            </select>
        </label>

        <div class="form-actions">
            <a class="btn secondary" href="${ctx}/perfisCabelo?idUsuario=${perfilCabelo.idUsuario}">Cancelar</a>
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
            var resp = await fetch('${ctx}/perfisCabelo', {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    idPerfilCabelo: ${perfilCabelo.idPerfilCabelo},
                    curvatura: document.getElementById('curvatura').value,
                    oleosidade: document.getElementById('oleosidade').value,
                    espessura: document.getElementById('espessura').value
                })
            });

            if (resp.ok) {
                window.location.href = '${ctx}/perfisCabelo?idUsuario=${perfilCabelo.idUsuario}';
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