<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Novo usuário"/>
<c:set var="paginaAtiva" value="usuarios"/>
<%@ include file="/WEB-INF/views/topo.jspf" %>

<div class="page-heading">
    <div>
        <h1>Novo usuário</h1>
        <p>Preencha os dados para cadastrar um novo usuário na plataforma.</p>
    </div>
</div>

<section class="panel white form-card">
    <%-- Envio normal de formulário (POST para o doPost do UsuarioServlet) --%>
    <form method="post" action="${ctx}/usuarios">

        <label class="form-field">Nome completo *
            <input type="text" name="nome" value="<c:out value='${param.nome}'/>" maxlength="200" required>
        </label>

        <label class="form-field">Gênero
            <select name="genero">
                <option value="">Selecione</option>
                <option value="Feminino"  ${param.genero == 'Feminino'  ? 'selected' : ''}>Feminino</option>
                <option value="Masculino" ${param.genero == 'Masculino' ? 'selected' : ''}>Masculino</option>
                <option value="Outro"     ${param.genero == 'Outro'     ? 'selected' : ''}>Outro</option>
            </select>
        </label>

        <label class="form-field">E-mail *
            <input type="email" name="email" value="<c:out value='${param.email}'/>" maxlength="150" required>
        </label>

        <%-- A senha nunca volta preenchida depois de um erro --%>
        <label class="form-field">Senha * (mínimo de 6 caracteres)
            <input type="password" name="senha" minlength="6" maxlength="72" autocomplete="new-password" required>
        </label>

        <div class="form-row">
            <label class="form-field">Telefone
                <input type="tel" name="telefone" value="<c:out value='${param.telefone}'/>" placeholder="(11) 91234-5678">
            </label>
            <label class="form-field">Data de nascimento *
                <input type="date" name="dt_nascimento" id="dt_nascimento" value="<c:out value='${param.dt_nascimento}'/>" required>
            </label>
        </div>

        <%-- Mensagem de erro enviada pelo servlet (setAttribute "erro") --%>
        <c:if test="${not empty erro}">
            <p class="alert-erro" role="alert"><c:out value="${erro}"/></p>
        </c:if>

        <div class="form-actions">
            <a class="btn" href="${ctx}/usuarios">Cancelar</a>
            <button type="submit" class="btn primary">Cadastrar usuário</button>
        </div>
    </form>
</section>

<script>
    // Não deixa escolher data de nascimento no futuro
    document.getElementById('dt_nascimento').max = new Date().toISOString().slice(0, 10);
</script>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
