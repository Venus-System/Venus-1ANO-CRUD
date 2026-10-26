const botoesOlho = document.querySelectorAll('.mostrar-senha');

for (const botao of botoesOlho) {
  botao.addEventListener('change', function () {
    // O campo de senha deve ser o elemento imediatamente após o controle de visibilidade.
    const campoSenha = botao.nextElementSibling;

    if (botao.checked) {
      campoSenha.type = 'text';
    } else {
      campoSenha.type = 'password';
    }
  });
}

const formulario = document.getElementById('formulario-senha');

if (formulario) {
  const senha = document.getElementById('senha');
  const confirmacao = document.getElementById('confirmar-senha');
  const mensagem = document.getElementById('erro-senha');

  formulario.addEventListener('submit', function (evento) {
    if (senha.value !== confirmacao.value) {
      evento.preventDefault();
      mensagem.textContent = 'As senhas devem ser iguais.';
      confirmacao.setAttribute('aria-invalid', 'true');
      confirmacao.focus();
    }
  });

  function limparAviso() {
    mensagem.textContent = '';
    confirmacao.removeAttribute('aria-invalid');
  }

  senha.addEventListener('input', limparAviso);
  confirmacao.addEventListener('input', limparAviso);
}
