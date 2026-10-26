const botaoMenu = document.querySelector('.botao-hamburguer');
const fundoMenu = document.querySelector('.menu-fundo');
const botaoFecharMenu = document.querySelector('.fechar-menu');

function abrirMenu(aberto) {
  document.body.classList.toggle('menu-aberto', aberto);
  botaoMenu.setAttribute('aria-expanded', String(aberto));
}

if (botaoMenu) {
  botaoMenu.addEventListener('click', function () { abrirMenu(true); });
  fundoMenu.addEventListener('click', function () { abrirMenu(false); });
  botaoFecharMenu.addEventListener('click', function () { abrirMenu(false); });

  document.addEventListener('keydown', function (evento) {
    if (evento.key === 'Escape') {
      abrirMenu(false);
    }
  });
}

document.querySelectorAll('[data-fechar-popup]').forEach(function (botao) {
  botao.addEventListener('click', function () {
    botao.closest('dialog').close();
  });
});

const popupExcluir = document.getElementById('popup-excluir');
let urlParaExcluir = '';

document.querySelectorAll('[data-excluir-url]').forEach(function (botao) {
  botao.addEventListener('click', function () {
    urlParaExcluir = botao.dataset.excluirUrl;
    popupExcluir.querySelector('.registro-selecionado').textContent = botao.dataset.excluirNome;
    popupExcluir.querySelector('.popup__erro').hidden = true;
    popupExcluir.showModal();
  });
});

if (popupExcluir) {
  const botaoConfirmar = popupExcluir.querySelector('[data-confirmar-exclusao]');
  const mensagemErro = popupExcluir.querySelector('.popup__erro');

  botaoConfirmar.addEventListener('click', async function () {
    botaoConfirmar.disabled = true;

    try {
      const resposta = await fetch(urlParaExcluir, { method: 'DELETE' });

      if (resposta.ok) {
        location.reload();
        return;
      }

      if (resposta.status >= 500) {
        mensagemErro.textContent = 'Erro no servidor. Tente novamente.';
      } else {
        mensagemErro.textContent = await resposta.text();
      }
    } catch (erro) {
      mensagemErro.textContent = 'Não foi possível conectar ao servidor.';
    }

    mensagemErro.hidden = false;
    botaoConfirmar.disabled = false;
  });
}

document.querySelectorAll('[data-busca]').forEach(function (campo) {
  const tabela = document.getElementById(campo.dataset.busca);

  campo.addEventListener('input', function () {
    const termo = campo.value.trim().toLowerCase();

    tabela.querySelectorAll('tbody tr').forEach(function (linha) {
      const textoDaLinha = linha.textContent.toLowerCase();
      linha.hidden = !textoDaLinha.includes(termo);
    });
  });
});

document.querySelectorAll('[data-data]').forEach(function (elemento) {
  const partes = elemento.dataset.data.split('-');

  if (partes.length === 3) {
    elemento.textContent = partes[2] + '/' + partes[1] + '/' + partes[0];
  }
});

const botoesAba = document.querySelectorAll('[data-aba]');

function abrirAba(nome) {
  botoesAba.forEach(function (botao) {
    const ativa = botao.dataset.aba === nome;
    botao.setAttribute('aria-selected', String(ativa));
    document.getElementById('aba-' + botao.dataset.aba).hidden = !ativa;
  });
}

if (botoesAba.length > 0) {
  botoesAba.forEach(function (botao) {
    botao.addEventListener('click', function () {
      abrirAba(botao.dataset.aba);
      history.replaceState(null, '', '#' + botao.dataset.aba);
    });
  });

  // Usa o fragmento da URL para reabrir a aba ao retornar ao detalhe do usuário.
  const abaDoEndereco = location.hash.replace('#', '');
  if (document.getElementById('aba-' + abaDoEndereco)) {
    abrirAba(abaDoEndereco);
  } else {
    abrirAba(botoesAba[0].dataset.aba);
  }
}

// Separa os dados locais de perfil pelo ID do usuário; o salvamento ocorre neste navegador.
const chavePerfil = 'venus-perfil-' + (document.body.dataset.usuario || '');

function lerPerfil() {
  try {
    return JSON.parse(localStorage.getItem(chavePerfil)) || {};
  } catch (erro) {
    return {};
  }
}

function mostrarPerfilNoTopo(perfil) {
  document.querySelectorAll('[data-perfil-nome]').forEach(function (elemento) {
    elemento.textContent = perfil.nome || elemento.dataset.perfilNome;
  });
  const fotoTopo = document.querySelector('.topo__perfil-foto');
  if (fotoTopo && !fotoTopo.dataset.original) {
    fotoTopo.dataset.original = fotoTopo.getAttribute('src');
  }
  if (fotoTopo) {
    fotoTopo.src = perfil.foto || fotoTopo.dataset.original;
  }
}

mostrarPerfilNoTopo(lerPerfil());

const formularioPerfil = document.getElementById('formulario-perfil');

if (formularioPerfil) {
  const fotoPerfil = document.querySelector('.foto-perfil__imagem');
  const campoArquivo = document.getElementById('foto');
  const campoNome = document.getElementById('perfil-nome');
  const campoEmail = document.getElementById('perfil-email');
  const campoTelefone = document.getElementById('perfil-telefone');
  const avisoPerfil = document.getElementById('aviso-perfil');
  let fotoEscolhida = '';

  function carregarPerfil() {
    const perfil = lerPerfil();
    campoNome.value = perfil.nome || campoNome.defaultValue;
    campoEmail.value = perfil.email || campoEmail.defaultValue;
    campoTelefone.value = perfil.telefone || campoTelefone.defaultValue;
    fotoEscolhida = perfil.foto || '';
    fotoPerfil.src = fotoEscolhida || fotoPerfil.dataset.avatarPadrao;
  }

  carregarPerfil();

  document.getElementById('trocar-foto').addEventListener('click', function () {
    campoArquivo.click();
  });

  document.getElementById('excluir-foto').addEventListener('click', function () {
    fotoEscolhida = '';
    fotoPerfil.src = fotoPerfil.dataset.avatarPadrao;
    avisoPerfil.textContent = 'Foto removida. Clique em "Salvar alterações" para confirmar.';
  });

  campoArquivo.addEventListener('change', function () {
    const arquivo = campoArquivo.files[0];
    campoArquivo.value = '';
    if (!arquivo) {
      return;
    }
    if (!['image/png', 'image/jpeg', 'image/webp'].includes(arquivo.type)) {
      avisoPerfil.textContent = 'Escolha uma imagem PNG, JPEG ou WebP.';
      return;
    }

    const imagem = new Image();
    imagem.onload = function () {
      const tela = document.createElement('canvas');
      tela.width = 200;
      tela.height = 200;

      // Recorta o centro da imagem em formato quadrado e reduz a foto para 200 por 200 pixels.
      const lado = Math.min(imagem.width, imagem.height);
      const x = (imagem.width - lado) / 2;
      const y = (imagem.height - lado) / 2;
      tela.getContext('2d').drawImage(imagem, x, y, lado, lado, 0, 0, 200, 200);
      fotoEscolhida = tela.toDataURL('image/jpeg', 0.85);
      fotoPerfil.src = fotoEscolhida;
      URL.revokeObjectURL(imagem.src);
      avisoPerfil.textContent = 'Nova foto escolhida. Clique em "Salvar alterações" para confirmar.';
    };
    imagem.onerror = function () {
      avisoPerfil.textContent = 'Não foi possível abrir essa imagem. Escolha outro arquivo.';
    };
    imagem.src = URL.createObjectURL(arquivo);
  });

  formularioPerfil.addEventListener('submit', function (evento) {
    evento.preventDefault();

    if (campoNome.value.trim() === '') {
      avisoPerfil.textContent = 'Preencha o nome.';
      campoNome.focus();
      return;
    }

    const perfil = {
      nome: campoNome.value.trim(),
      email: campoEmail.value.trim(),
      telefone: campoTelefone.value.trim(),
      foto: fotoEscolhida
    };

    try {
      localStorage.setItem(chavePerfil, JSON.stringify(perfil));
      mostrarPerfilNoTopo(perfil);
      avisoPerfil.textContent = 'Alterações salvas neste navegador.';
    } catch (erro) {
      avisoPerfil.textContent = 'Não foi possível salvar. A foto pode ser grande demais.';
    }
  });

  document.getElementById('desfazer-perfil').addEventListener('click', function () {
    carregarPerfil();
    avisoPerfil.textContent = 'Alterações desfeitas.';
  });
}

function abrirPopupErro(mensagem) {
  const popupErro = document.getElementById('popup-erro');

  if (!popupErro) {
    alert(mensagem);
    return;
  }

  document.getElementById('texto-erro').textContent = mensagem;

  if (!popupErro.open) {
    popupErro.showModal();
  }
}

document.querySelectorAll('.alerta-erro, .alert-erro').forEach(function (aviso) {
  const mensagem = aviso.textContent.trim();

  if (!aviso.hidden && mensagem !== '') {
    aviso.hidden = true;
    abrirPopupErro(mensagem);
  }
});

const LINHAS_POR_PAGINA = 10;

// A busca usa hidden ou display; a paginação usa a classe fora-da-pagina para preservar esse filtro.
function linhaPassaNaBusca(linha) {
  return !linha.hidden && linha.style.display !== 'none';
}

document.querySelectorAll('#conteudo table').forEach(function (tabela) {
  const corpo = tabela.querySelector('tbody');
  if (!corpo) {
    return;
  }

  const caixa = document.createElement('div');
  caixa.className = 'paginacao';
  caixa.innerHTML =
    '<button class="paginacao__botao" type="button" aria-label="Página anterior">' +
    '<svg viewBox="0 0 24 24" width="16" height="16"><path d="M15 6l-6 6l6 6"/></svg></button>' +
    '<span class="paginacao__texto"></span>' +
    '<button class="paginacao__botao" type="button" aria-label="Próxima página">' +
    '<svg viewBox="0 0 24 24" width="16" height="16"><path d="M9 6l6 6l-6 6"/></svg></button>';
  tabela.parentElement.after(caixa);

  const botaoAnterior = caixa.querySelectorAll('.paginacao__botao')[0];
  const botaoProxima = caixa.querySelectorAll('.paginacao__botao')[1];
  const texto = caixa.querySelector('.paginacao__texto');
  let pagina = 1;

  function atualizar() {
    const linhas = Array.from(corpo.rows).filter(function (linha) {
      return !linha.querySelector('.tabela__vazia, .empty');
    });
    const linhasDaBusca = linhas.filter(linhaPassaNaBusca);
    const totalDePaginas = Math.max(1, Math.ceil(linhasDaBusca.length / LINHAS_POR_PAGINA));

    if (pagina > totalDePaginas) {
      pagina = totalDePaginas;
    }

    linhas.forEach(function (linha) {
      linha.classList.add('fora-da-pagina');
    });
    linhasDaBusca.forEach(function (linha, posicao) {
      if (Math.floor(posicao / LINHAS_POR_PAGINA) === pagina - 1) {
        linha.classList.remove('fora-da-pagina');
      }
    });

    texto.textContent = 'Página ' + pagina + ' de ' + totalDePaginas;
    botaoAnterior.disabled = pagina === 1;
    botaoProxima.disabled = pagina === totalDePaginas;
    caixa.hidden = linhasDaBusca.length <= LINHAS_POR_PAGINA;
  }

  botaoAnterior.addEventListener('click', function () {
    pagina = pagina - 1;
    atualizar();
  });

  botaoProxima.addEventListener('click', function () {
    pagina = pagina + 1;
    atualizar();
  });

  document.addEventListener('input', function () {
    pagina = 1;
    atualizar();
  });

  atualizar();
});
