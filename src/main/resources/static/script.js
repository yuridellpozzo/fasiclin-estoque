// API Base URL
const API_BASE_URL = 'http://localhost:8080/api';

// Variáveis Globais
let itensDoPedidoParaEnvio = []; 
let currentTab = 'pendentes'; 

// Função auxiliar para processar resposta da API
async function processApiResponse(response) {
    if (response.status === 204 || response.headers.get('content-length') === '0') {
        return response.ok ? { content: [] } : null; 
    }
    if (response.ok) {
        return response.json();
    }
    const errorText = await response.text();
    throw new Error(errorText || `Falha na requisição com status ${response.status}`);
}

// Inicialização
document.addEventListener('DOMContentLoaded', function() {
    showPage('estoque');
    // Inicializa na aba de pendentes
    switchTab('pendentes'); 
    
    loadResponsaveis();   
    setupFormHandler();   
});

function showAlert(message, type = 'info') {
    const alertContainer = document.getElementById('alertContainer');
    const alert = document.createElement('div');
    alert.className = `alert ${type}`;
    alert.textContent = message;
    alertContainer.appendChild(alert);
    setTimeout(() => { alert.remove(); }, 5000);
}

// --- NAVEGAÇÃO PRINCIPAL ---
function showPage(pageName, element) {
    document.getElementById('estoquePage').classList.add('hidden');
    document.getElementById('detalheRecebimentoPage').classList.add('hidden');
    document.getElementById('adminPage').classList.add('hidden');
    
    document.querySelectorAll('.nav-item').forEach(item => item.classList.remove('active'));

    if (pageName === 'estoque') {
        document.getElementById('estoquePage').classList.remove('hidden');
        // Recarrega a aba atual ao voltar para a página
        switchTab(currentTab);
    } else if (pageName === 'admin') {
        document.getElementById('adminPage').classList.remove('hidden');
        loadConnections();
    } else {
        document.getElementById('estoquePage').classList.remove('hidden');
    }

    const navItem = element || document.querySelector(`[data-page="${pageName}"]`);
    if (navItem) navItem.classList.add('active');
}

// --- NAVEGAÇÃO DE ABAS ---
function switchTab(tabName) {
    currentTab = tabName;

    // Atualiza visual dos botões
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
        if(btn.getAttribute('onclick') && btn.getAttribute('onclick').includes(tabName)) {
            btn.classList.add('active');
        }
    });

    // Esconde todas as tabelas
    document.getElementById('tab-pendentes').classList.add('hidden');
    document.getElementById('tab-andamento').classList.add('hidden');
    document.getElementById('tab-concluidas').classList.add('hidden');

    // Mostra a tabela certa e CARREGA OS DADOS
    if (tabName === 'pendentes') {
        document.getElementById('tab-pendentes').classList.remove('hidden');
        loadListaOrdens('PEND', 'bodyPendentes', 'msgPendentes');
    } else if (tabName === 'andamento') {
        document.getElementById('tab-andamento').classList.remove('hidden');
        loadListaOrdens('ANDA', 'bodyAndamento', 'msgAndamento');
    } else if (tabName === 'concluidas') {
        document.getElementById('tab-concluidas').classList.remove('hidden');
        loadListaOrdens('CONC', 'bodyConcluidas', 'msgConcluidas');
    }
}

// --- CARREGAMENTO DE LISTAS ---
async function loadListaOrdens(status, tbodyId, msgId) {
    const tbody = document.getElementById(tbodyId);
    const msg = document.getElementById(msgId);
    
    tbody.innerHTML = '<tr><td colspan="5" style="text-align:center">Carregando...</td></tr>';
    msg.classList.add('hidden');

    try {
        // CORREÇÃO CRÍTICA:
        // size=2000: Garante que todas as ordens venham (não apenas as primeiras 20)
        // sort=id,desc: Mostra as ordens mais recentes (IDs maiores) primeiro
        const url = `${API_BASE_URL}/ordens-de-compra?status=${status}&size=2000&sort=id,desc`;
        
        const response = await fetch(url);
        const data = await processApiResponse(response);
        const ordens = data.content || [];

        tbody.innerHTML = '';

        if (ordens.length === 0) {
            msg.classList.remove('hidden');
            return;
        }

        ordens.forEach(ordem => {
            const row = document.createElement('tr');
            
            // Dados para exibição
            const produtoPrincipal = ordem.itens && ordem.itens.length > 0 ? ordem.itens[0].produto.nome : 'Vários Itens';
            const qtdItens = ordem.itens ? ordem.itens.length : 0;
            const dataPrev = formatDate(ordem.dataPrevisao);
            const dataEntr = formatDate(ordem.dataEntrega || new Date());
            const valor = formatCurrency(ordem.valor);

            let acoesHtml = '';
            
            // Botões conforme o status
            if (status === 'PEND') {
                acoesHtml = `<button class="btn-action btn-receber" onclick="abrirTelaRecebimento(${ordem.id})">Iniciar Recebimento</button>`;
            } else if (status === 'ANDA') {
                acoesHtml = `
                    <button class="btn-action btn-receber" onclick="abrirTelaRecebimento(${ordem.id})">Continuar</button>
                    <button class="btn-action btn-concluir" onclick="confirmarConclusaoManualLista(${ordem.id})">Concluir</button>
                `;
            } else {
                acoesHtml = `<span class="status-badge completed" style="background-color: #27ae60; color: white; padding: 4px 8px; border-radius: 4px;">Concluída</span>`;
            }

            // Colunas
            if (status === 'CONC') {
                row.innerHTML = `
                    <td>#${ordem.id}</td>
                    <td>${produtoPrincipal}</td>
                    <td>${dataEntr}</td>
                    <td>R$ ${valor}</td>
                    <td>${acoesHtml}</td>
                `;
            } else {
                row.innerHTML = `
                    <td>#${ordem.id}</td>
                    <td>${produtoPrincipal}</td>
                    <td>${qtdItens} Itens</td>
                    <td>${dataPrev}</td>
                    <td>${acoesHtml}</td>
                `;
            }
            tbody.appendChild(row);
        });

    } catch (error) {
        console.error('Erro ao carregar lista:', error);
        tbody.innerHTML = `<tr><td colspan="5" style="color:red">Erro: ${error.message}</td></tr>`;
    }
}

// --- TELA DE DETALHE ---
async function abrirTelaRecebimento(idOrdem) {
    document.getElementById('estoquePage').classList.add('hidden');
    document.getElementById('detalheRecebimentoPage').classList.remove('hidden');
    
    document.getElementById('spanOcId').textContent = idOrdem;
    document.getElementById('ordemCompraIdSelecionada').value = idOrdem;

    await loadItensDaOrdem(idOrdem);
}

function voltarParaLista() {
    document.getElementById('detalheRecebimentoPage').classList.add('hidden');
    document.getElementById('estoquePage').classList.remove('hidden');
    switchTab(currentTab); // Recarrega a lista atual para ver mudanças
}

async function loadItensDaOrdem(idOrdemCompra) {
    const container = document.getElementById('itensDaOrdemContainer');
    const formFields = document.getElementById('formFields');

    container.innerHTML = `
        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>Produto</th>
                        <th>Qtd. Pendente</th>
                        <th>Qtd. Recebida</th>
                        <th>Lote</th>
                        <th>Fabricação</th>
                        <th>Vencimento</th>
                    </tr>
                </thead>
                <tbody id="itensLoteTableBody">
                    <tr><td colspan="6" style="text-align:center">Carregando itens...</td></tr>
                </tbody>
            </table>
        </div>
    `;

    const tbody = document.getElementById('itensLoteTableBody');
    
    try {
        const url = `${API_BASE_URL}/ordens-de-compra/${idOrdemCompra}/itens`;
        const response = await fetch(url);
        
        if (response.ok) {
            const itens = await response.json();
            tbody.innerHTML = ''; 
            formFields.classList.remove('hidden');
            
            // Mostra apenas itens com pendência para preenchimento
            let itensPendentes = itens.filter(item => item.quantidade > 0);

            if (itensPendentes.length === 0) {
                tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: green; font-weight: bold; padding: 20px;">Todos os itens já foram recebidos! Volte para a lista e clique em "Concluir" para finalizar a ordem.</td></tr>';
                itensDoPedidoParaEnvio = [];
                document.querySelector('#recebimentoForm .submit-btn').classList.add('hidden');
                return;
            } else {
                document.querySelector('#recebimentoForm .submit-btn').classList.remove('hidden');
            }
            
            itensDoPedidoParaEnvio = itensPendentes.map((item, index) => {
                const row = document.createElement('tr');
                
                row.innerHTML = `
                    <td>${item.produto.nome}</td>
                    <td><strong style="font-size: 1.1em; color: #e67e22;">${item.quantidade}</strong></td>
                    <td><input type="number" id="qntdRecebida-${index}" class="form-input" placeholder="0" min="0" max="${item.quantidade}" style="width: 80px;"></td>
                    <td><input type="text" id="codigoLote-${index}" class="form-input" placeholder="Lote" style="width: 100px;"></td>
                    <td><input type="date" id="dataFabricacao-${index}" class="form-input"></td>
                    <td><input type="date" id="dataVencimento-${index}" class="form-input"></td>
                `;
                tbody.appendChild(row);

                return { 
                    idProduto: item.produto.id, 
                    index: index, 
                    nomeProduto: item.produto.nome 
                };
            });

        } else {
            const errorText = await response.text();
            tbody.innerHTML = `<tr><td colspan="6" style="color:red">${errorText}</td></tr>`;
        }
    } catch (error) {
        tbody.innerHTML = `<tr><td colspan="6" style="color:red">Erro de conexão.</td></tr>`;
    }
}

function setupFormHandler() {
    const form = document.getElementById('recebimentoForm');
    form.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const submitBtn = form.querySelector('.submit-btn');
        submitBtn.disabled = true;
        submitBtn.textContent = 'Processando...';
        
        try {
            const itensRecebidosList = [];
            let algumItemPreenchido = false;
            const idOrdem = document.getElementById('ordemCompraIdSelecionada').value;

            for (const item of itensDoPedidoParaEnvio) {
                const index = item.index;
                const qntdInput = document.getElementById(`qntdRecebida-${index}`);
                if (!qntdInput) continue;

                const qntd = parseInt(qntdInput.value);
                
                if (!isNaN(qntd) && qntd > 0) {
                    algumItemPreenchido = true;
                    
                    const dataVenc = document.getElementById(`dataVencimento-${index}`).value;
                    const dataFab = document.getElementById(`dataFabricacao-${index}`).value;
                    const codigoLote = document.getElementById(`codigoLote-${index}`).value;

                    if (!dataVenc || !codigoLote || !dataFab) {
                        throw new Error(`Preencha todos os campos (Lote, Fabricação, Vencimento) para o produto '${item.nomeProduto}'.`);
                    }

                    itensRecebidosList.push({
                        idProduto: item.idProduto,
                        quantidadeRecebida: qntd,
                        dataVencimento: dataVenc,
                        dataFabricacao: dataFab,
                        codigoLote: codigoLote,
                        numeroLote: codigoLote
                    });
                }
            }

            if (!algumItemPreenchido) {
                throw new Error('Preencha a quantidade de pelo menos um item.');
            }

            const payload = {
                idOrdemCompra: parseInt(idOrdem),
                idResponsavel: parseInt(document.getElementById('idResponsavel').value),
                login: document.getElementById('login').value,
                senha: document.getElementById('senha').value,
                itensRecebidos: itensRecebidosList
            };

            const response = await fetch(`${API_BASE_URL}/ordens-de-compra/receber-itens`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            const errorText = await response.text();

            if (response.ok) {
                showAlert(errorText, 'success');
                await loadItensDaOrdem(idOrdem);
                document.getElementById('senha').value = '';
            } else {
                showAlert(`Erro: ${errorText}`, 'error');
            }
        } catch (error) {
            showAlert(error.message, 'error'); 
        } finally {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Registrar Recebimento';
        }
    });
}

async function confirmarConclusaoManualLista(id) {
    if (confirm(`Deseja CONCLUIR permanentemente a OC #${id}? Isso indica que todo o processo foi finalizado.`)) {
        try {
            const response = await fetch(`${API_BASE_URL}/ordens-de-compra/${id}/concluir`, { method: 'PUT' });
            const text = await response.text();
            if (response.ok) {
                showAlert(text, 'success');
                switchTab('andamento'); // Atualiza a lista
            } else {
                showAlert(`Erro: ${text}`, 'error');
            }
        } catch (error) {
            showAlert('Erro de conexão.', 'error');
        }
    }
}

async function loadResponsaveis() {
    try {
        let response = await fetch(`${API_BASE_URL}/profissionais/estoque`);
        const data = await processApiResponse(response);
        const select = document.getElementById('idResponsavel');
        select.innerHTML = '<option value="">Selecione...</option>';
        if (Array.isArray(data)) {
            data.forEach(r => {
                const op = document.createElement('option');
                op.value = r.id;
                op.textContent = r.nomePessoa;
                select.appendChild(op);
            });
        }
    } catch (e) { 
        // Fallback para teste
        const select = document.getElementById('idResponsavel');
        const op = document.createElement('option');
        op.value = 1; op.textContent = 'Administrador (Teste)';
        select.appendChild(op);
    }
}

function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return isNaN(date) ? 'N/A' : date.toLocaleDateString('pt-BR');
}

function formatCurrency(value) {
    if (value == null) return '0,00';
    return parseFloat(value).toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

function logout() {
    if (confirm('Sair?')) showAlert('Logout realizado', 'success');
}

// Admin placeholders
async function loadConnections() { /*...*/ }
async function killConnection(id) { /*...*/ }
async function killAllConnections() { /*...*/ }