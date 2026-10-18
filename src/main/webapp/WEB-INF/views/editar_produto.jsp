<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Editar produto"/>
<c:set var="paginaAtiva" value="produtos"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Editar produto</h1>
        <p>Atualize os dados do produto.</p>
    </div>
</div>

<section class="panel white form-card">
    <%-- Os id dos campos são usados pelo JavaScript abaixo (PUT para o doPut do servlet) --%>
    <form id="formEditar">
        <input type="hidden" id="idProduto" value="${produto.idProduto}">

        <label class="form-field">Nome do produto *
            <input type="text" id="nome" value="<c:out value='${produto.nome}'/>" maxlength="100" required>
        </label>

        <div class="form-row">
            <label class="form-field">Marca *
                <input type="text" id="marca" value="<c:out value='${produto.marca}'/>" maxlength="50" required>
            </label>
            <label class="form-field">Categoria *
                <input type="text" id="categoria" value="<c:out value='${produto.categoria}'/>" maxlength="50" required>
            </label>
        </div>

        <label class="form-field">Descrição
            <textarea id="descricao" rows="3" maxlength="255"><c:out value='${produto.descricao}'/></textarea>
        </label>

        <label class="form-field">Lista de ingredientes
            <textarea id="listaIngredientes" rows="4" maxlength="255"><c:out value='${produto.listaIngredientes}'/></textarea>
        </label>

        <div class="form-row">
            <label class="form-field">
                <span>
                    <input type="checkbox" id="ehVegano" style="width:auto;margin-right:8px" ${produto.ehVegano ? 'checked' : ''}>
                    É vegano
                </span>
            </label>
            <label class="form-field">
                <span>
                    <input type="checkbox" id="ehCrueltyFree" style="width:auto;margin-right:8px" ${produto.ehCrueltyFree ? 'checked' : ''}>
                    É cruelty free
                </span>
            </label>
        </div>

        <label class="form-field">Pontuação (0 a 100)
            <input type="number" id="pontuacao" min="0" max="100" step="1" value="${produto.pontuacao}">
        </label>

        <p id="msg-erro" class="alert-erro" role="alert" hidden></p>

        <div class="form-actions">
            <a class="btn" href="${ctx}/produtos">Cancelar</a>
            <button type="submit" id="btnSalvar" class="btn primary">Salvar alterações</button>
        </div>
    </form>
</section>

<script>
    document.getElementById('formEditar').addEventListener('submit', async function (e) {
        e.preventDefault();
        var erro = document.getElementById('msg-erro');
        var botao = document.getElementById('btnSalvar');
        erro.hidden = true;
        botao.disabled = true; // evita enviar duas vezes

        // As chaves do JSON são as que o doPut do ProdutoServlet espera.
        // Os checkbox vão como true/false, e a pontuação vazia vira 0.
        var dados = {
            idProduto: document.getElementById('idProduto').value,
            nome: document.getElementById('nome').value,
            marca: document.getElementById('marca').value,
            categoria: document.getElementById('categoria').value,
            descricao: document.getElementById('descricao').value,
            ehVegano: document.getElementById('ehVegano').checked,
            ehCrueltyFree: document.getElementById('ehCrueltyFree').checked,
            pontuacao: document.getElementById('pontuacao').value || '0',
            listaIngredientes: document.getElementById('listaIngredientes').value
        };

        try {
            var resp = await fetch('${ctx}/produtos', {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(dados)
            });

            if (resp.ok) {
                window.location.href = '${ctx}/produtos';
            } else if (resp.status >= 500) {
                // erro do servidor: mensagem genérica, sem mostrar a página de erro do Tomcat
                erro.textContent = 'Erro no servidor. Tente novamente.';
                erro.hidden = false;
            } else {
                // 400, 404, 409: o servlet mandou uma frase curta, pode mostrar
                erro.textContent = await resp.text();
                erro.hidden = false;
            }
        } catch (err) {
            // sem conexão com o servidor
            erro.textContent = 'Não foi possível conectar ao servidor.';
            erro.hidden = false;
        } finally {
            botao.disabled = false;
        }
    });
</script>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
