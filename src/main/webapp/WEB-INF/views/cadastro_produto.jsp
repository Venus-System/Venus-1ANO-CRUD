<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Novo produto"/>
<c:set var="paginaAtiva" value="produtos"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Novo produto</h1>
        <p>Preencha os dados para cadastrar um novo produto na plataforma.</p>
    </div>
</div>

<section class="panel white form-card">
    <%-- Envio normal de formulário (POST para o doPost do ProdutoServlet) --%>
    <form method="post" action="${ctx}/produtos">

        <label class="form-field">Nome do produto *
            <input type="text" name="nome" value="<c:out value='${param.nome}'/>" maxlength="100" required>
        </label>

        <div class="form-row">
            <label class="form-field">Marca *
                <input type="text" name="marca" value="<c:out value='${param.marca}'/>" maxlength="50" required>
            </label>
            <label class="form-field">Categoria *
                <input type="text" name="categoria" value="<c:out value='${param.categoria}'/>" maxlength="50" required>
            </label>
        </div>

        <label class="form-field">Descrição
            <textarea name="descricao" rows="3" maxlength="255"><c:out value='${param.descricao}'/></textarea>
        </label>

        <label class="form-field">Lista de ingredientes
            <textarea name="lista_ingredientes" rows="4" maxlength="255"><c:out value='${param.lista_ingredientes}'/></textarea>
        </label>

        <div class="form-row">
            <label class="form-field">
                <span>
                    <input type="checkbox" name="eh_vegano" value="on" style="width:auto;margin-right:8px" ${not empty param.eh_vegano ? 'checked' : ''}>
                    É vegano
                </span>
            </label>
            <label class="form-field">
                <span>
                    <input type="checkbox" name="eh_cruelty_free" value="on" style="width:auto;margin-right:8px" ${not empty param.eh_cruelty_free ? 'checked' : ''}>
                    É cruelty free
                </span>
            </label>
        </div>

        <label class="form-field">Pontuação (0 a 100)
            <input type="number" name="pontuacao" min="0" max="100" step="1" value="<c:out value='${empty param.pontuacao ? 0 : param.pontuacao}'/>">
        </label>

        <%-- Mensagem de erro enviada pelo servlet (setAttribute "erro") --%>
        <c:if test="${not empty erro}">
            <p class="alert-erro" role="alert"><c:out value="${erro}"/></p>
        </c:if>

        <div class="form-actions">
            <a class="btn" href="${ctx}/produtos">Cancelar</a>
            <button type="submit" class="btn primary">Cadastrar produto</button>
        </div>
    </form>
</section>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
