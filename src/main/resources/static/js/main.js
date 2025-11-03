document.addEventListener('DOMContentLoaded', function () {
    const detalhesModal = document.getElementById('detalhesPlantaModal');
    if (detalhesModal) {
        detalhesModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const plantaId = button.getAttribute('data-planta-id');

            const modalTitle = detalhesModal.querySelector('#modalPlantaTitle');
            const modalBody = detalhesModal.querySelector('#modalPlantaBody');

            // Limpa o modal e mostra um spinner de carregamento
            modalTitle.textContent = 'Carregando...';
            modalBody.innerHTML = '<div class="spinner-border" role="status"><span class="visually-hidden">Loading...</span></div>';

            // Busca os dados na nossa API
            fetch(`/api/plantas/${plantaId}`)
                .then(response => response.json())
                .then(planta => {
                    // Atualiza o título do modal com o nomePopular
                    modalTitle.textContent = planta.nome_popular;

                    // Atualiza o corpo do modal com os dados da planta
                    // Usando os campos 'foto', 'nomeCientifico', 'tipo', e 'descricao'
                    modalBody.innerHTML = `
                        <div class="row">
                            <div class="col-md-5">
                                <img src="${planta.foto_url}" class="img-fluid rounded" alt="Foto da ${planta.nome_popular}">
                            </div>
                            <div class="col-md-7">
                                <h4 class="text-muted">${planta.nome_cientifico}</h4>
                                <hr>
                                <h5>Tipo: <span class="badge bg-secondary">${planta.tipo}</span></h5>
                                <h5 class="mt-4">Dicas de Manejo</h5>
                                <p style="white-space: pre-wrap;">${planta.descricao_manejo}</p>
                            </div>
                        </div>
                    `;
                })
                .catch(error => {
                    modalTitle.textContent = 'Erro';
                    modalBody.innerHTML = 'Não foi possível carregar os detalhes da planta.';
                    console.error('Erro:', error);
                });
        });
    }

    // Máscara para o campo de CPF / CNPJ
    const elementoCpf = document.getElementById('cpf');
    if (elementoCpf) {

        const mascaraCpfCnpj = {
            mask: [
                {
                    mask: '000.000.000-00',
                },
                {
                    mask: '00.000.000/0000-00'
                }
            ],
            // Função 'dispatch' decide qual máscara usar
            dispatch: function (appended, dynamicMasked) {

                const unmaskedValue = dynamicMasked.unmaskedValue;
                const totalLength = (unmaskedValue + appended).length;

                // Se o comprimento total for 11 ou menos, usamos a máscara de CPF.
                if (totalLength <= 11) {
                    return dynamicMasked.compiledMasks[0];
                }

                // Se o comprimento for maior que 11, usamos a máscara de CNPJ.
                return dynamicMasked.compiledMasks[1];
            }
        };
        IMask(elementoCpf, mascaraCpfCnpj);
    }

    // Máscara para o campo de Telefone
    const elementoTelefone = document.getElementById('telefone');
    if (elementoTelefone) {
        const mascaraTelefone = {
            mask: '(00) 00000-0000'
        };
        IMask(elementoTelefone, mascaraTelefone);
    }

});

function toggleSenha() {
    var senhaInput = document.getElementById("senha");
    if (senhaInput.type === "password") {
        senhaInput.type = "text";
    } else {
        senhaInput.type = "password";
    }
}

const descricaoModal = document.getElementById('descricaoModal');
if (descricaoModal) {
    descricaoModal.addEventListener('show.bs.modal', event => {
        const button = event.relatedTarget;
        // Pega a descrição e o nome guardados no botão
        const descricao_manejo = button.getAttribute('data-descricao');
        const nomePlanta = button.getAttribute('data-nome-planta');

        // Atualiza o título e o corpo do modal
        const modalTitle = descricaoModal.querySelector('.modal-title');
        const modalBody = descricaoModal.querySelector('.modal-body');

        modalTitle.textContent = `Manejo: ${nomePlanta}`;
        modalBody.textContent = descricao_manejo;
    });
}

