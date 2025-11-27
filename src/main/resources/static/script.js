// API Base URL
const API_BASE_URL = 'http://localhost:8080/api';

// Variáveis Globais
let ordensComProduto = {}; 
let itensDaOrdemAtual = [];
let itensDoPedidoParaEnvio = []; 
let currentPage = 'recebimento';
let currentFilter = 'all';

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

// Inicialização da aplicação
document.addEventListener('DOMContentLoaded', function() {
    showPage('estoque', document.querySelector('[data-page="estoque"]')); 
    loadOrdensIniciais(); 
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

// Navegação entre abas
function showPage(pageName, element) {
    document.getElementById('recebimentoPage').classList.add('hidden');
    document.getElementById('ordensPage').classList.add('hidden');
    document.getElementById('adminPage').classList.add('hidden');
    document.querySelectorAll('.nav-item').forEach(item => item.classList.remove('active'));

    currentPage = pageName;

    if (pageName === 'estoque') {
        document.getElementById('recebimentoPage').classList.remove('hidden');
    } else if (pageName === 'ordens') {
        document.getElementById('ordensPage').classList.remove('hidden');
        loadOrdens('all');
    } else if (pageName === 'admin') {
        document.getElementById('adminPage').classList.remove('hidden');
        loadConnections();
    } else {
        showAlert('Página em desenvolvimento', 'info');
    }

    const navItem = element || document.querySelector(`[data-page="${pageName}"]`);
    if (navItem) navItem.classList.add('active');
}

// Carrega as ordens no Select
async function loadOrdensIniciais() {
    try {
        const responsePend = await fetch(`${API_BASE_URL}/ordens-de-compra?status=PEND`);
        const responseAnda = await fetch(`${API_BASE_URL}/ordens-de-compra?status=ANDA`);
        const ordensPendData = await processApiResponse(responsePend);
        const ordensAndaData = await processApiResponse(responseAnda);
        const ordens = [...(ordensPendData.content || []), ...(ordensAndaData.content || [])];
        
        const select = document.getElementById('ordemCompra');
        select.innerHTML = '<option value="">Selecione uma ordem de compra...</option>';
        
        ordensComProduto = {}; 
        
        ordens.forEach(ordem => {
            if (ordem.itens && ordem.itens.length > 0) {
                ordem.itens.forEach(item => {
                    if (item.produto) {
                        const produtoNome = item.produto.nome || 'Produto Desconhecido';
                        const produtoId = item.produto.id;
                        ordensComProduto[ordem.id] = { produtoId: produtoId };
                        const option = document.createElement('option');
                        option.value = ordem.id;
                        option.textContent = `OC #${ordem.id} - ${produtoNome} (${item.quantidade} un.) - ${ordem.status}`;
                        select.appendChild(option);
                    }
                });
            }
        });

        select.addEventListener('change', function(e) {
            handleOrdemCompraChange(e.target.value);
        });
    } catch (error) {
        console.error('Erro ao carregar ordens:', error);
        showAlert('Erro ao carregar ordens: ' + error.message, 'error');
    }
}

// Carrega responsáveis (Administradores)
async function loadResponsaveis() {
    try {
        let response = await fetch(`${API_BASE_URL}/profissionais/estoque`);
        const responsaveis = await processApiResponse(response);
        
        if (Array.isArray(responsaveis)) {
            populateResponsaveis(responsaveis);
        } else {
             throw new Error('Formato de dados inválido.');
        }
    } catch (error) {
        console.error('Erro ao carregar responsáveis:', error);
        showAlert('Erro ao carregar responsáveis: ' + error.message, 'error');
        const mockResponsaveis = [{ id: 1, nomePessoa: 'Administrador (Teste)' }];
        populateResponsaveis(mockResponsaveis);
    }
}

function populateResponsaveis(responsaveis) {
    const select = document.getElementById('idResponsavel');
    select.innerHTML = '<option value="">Selecione o responsável...</option>';
    responsaveis.forEach(resp => {
        const option = document.createElement('option');
        option.value = resp.id;
        option.textContent = resp.nomePessoa; 
        select.appendChild(option);
    });
}

// [ATUALIZADO] Carrega itens e monta a tabela COM CAMPO DE DATA DE FABRICAÇÃO
async function loadItensDaOrdem(idOrdemCompra) {
    const container = document.getElementById('itensDaOrdemContainer');
    const formFields = document.getElementById('formFields');

    if (!idOrdemCompra) {
        container.innerHTML = '';
        formFields.classList.add('hidden');
        itensDoPedidoParaEnvio = [];
        return;
    }

    container.innerHTML = `
        <h3 class="section-title" style="margin-top: 40px; font-size: 18px;">Itens da Ordem de Compra</h3>
        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>ID Produto</th>
                        <th>Produto</th>
                        <th>Qtd. Pedida</th>
                        <th>Qtd. Recebida</th>
                        <th>Lote - N° de Rastreio</th>
                        <th>Data Fabricação</th> <!-- Coluna Nova -->
                        <th>Data Vencimento</th>
                    </tr>
                </thead>
                <tbody id="itensLoteTableBody">
                    <tr><td colspan="7">Buscando itens...</td></tr>
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
            
            if (itens.length === 0) {
                tbody.innerHTML = '<tr><td colspan="7">Nenhum item encontrado.</td></tr>';
                itensDoPedidoParaEnvio = [];
                return;
            }
            
            itensDoPedidoParaEnvio = itens.map((item, index) => {
                const row = document.createElement('tr');
                const produtoId = item.produto.id;
                const produtoNome = item.produto.nome;
                const quantidadePedida = item.quantidade;
                
                // INPUTS DINÂMICOS - INCLUINDO DATA FABRICAÇÃO
                row.innerHTML = `
                    <td>${produtoId}</td>
                    <td>${produtoNome}</td>
                    <td>${quantidadePedida}</td>
                    <td><input type="number" id="qntdRecebida-${index}" class="form-input" placeholder="0" required></td>
                    <td><input type="text" id="codigoLote-${index}" class="form-input" placeholder="Ex: L2024-A1" required></td>
                    <td><input type="date" id="dataFabricacao-${index}" class="form-input" required></td>
                    <td><input type="date" id="dataVencimento-${index}" class="form-input" required></td>
                `;
                tbody.appendChild(row);

                return { idProduto: produtoId, index: index };
            });

        } else {
            const error = await response.text();
            showAlert(`Erro ao buscar itens: ${error}`, 'error');
            container.innerHTML = '';
            formFields.classList.add('hidden');
        }
    } catch (error) {
        console.error('Erro ao buscar itens:', error);
        showAlert('Erro de conexão.', 'error');
        container.innerHTML = '';
        formFields.classList.add('hidden');
    }
}

function handleOrdemCompraChange(ordemId) {
    loadItensDaOrdem(ordemId);
}

// [ATUALIZADO] Leitura dos campos incluindo Data Fabricação para envio
function setupFormHandler() {
    const form = document.getElementById('recebimentoForm');
    form.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const submitBtn = form.querySelector('.submit-btn');
        submitBtn.disabled = true;
        submitBtn.textContent = 'Processando...';
        
        try {
            // Constrói a lista de itens pegando os valores dos inputs dinâmicos
            const itensRecebidosList = itensDoPedidoParaEnvio.map(item => {
                const index = item.index;
                const qntd = parseInt(document.getElementById(`qntdRecebida-${index}`).value);
                const dataVenc = document.getElementById(`dataVencimento-${index}`).value;
                const dataFab = document.getElementById(`dataFabricacao-${index}`).value; // Leitura nova
                const codigoLote = document.getElementById(`codigoLote-${index}`).value;

                if (isNaN(qntd) || qntd <= 0) return null;

                if (!dataVenc || !codigoLote || !dataFab) {
                    throw new Error(`Preencha todos os campos (Lote, Fabricação, Vencimento) para o produto ID ${item.idProduto}.`);
                }

                return {
                    idProduto: item.idProduto,
                    quantidadeRecebida: qntd,
                    dataVencimento: dataVenc,
                    dataFabricacao: dataFab, // Envio para o backend
                    codigoLote: codigoLote,
                    numeroLote: codigoLote // Mapeado para o DTO
                };
            }).filter(item => item !== null);

            const payload = {
                idOrdemCompra: parseInt(document.getElementById('ordemCompra').value),
                idResponsavel: parseInt(document.getElementById('idResponsavel').value),
                login: document.getElementById('login').value,
                senha: document.getElementById('senha').value,
                itensRecebidos: itensRecebidosList
            };

            if (isNaN(payload.idOrdemCompra) || payload.itensRecebidos.length === 0) {
                throw new Error('Selecione uma Ordem e preencha os itens.');
            }

            const response = await fetch(`${API_BASE_URL}/ordens-de-compra/receber-itens`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            const errorText = await response.text();

            if (response.ok) {
                showAlert(errorText, 'success');
                loadItensDaOrdem(payload.idOrdemCompra);
                loadOrdensIniciais();
            } else {
                showAlert(`Erro ao receber itens: ${errorText}`, 'error');
            }
        } catch (error) {
            console.error('Erro:', error);
            showAlert('Erro ao processar: ' + error.message, 'error');
        } finally {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Concluir Recebimento';
        }
    });
}

// Conclusão Manual da Ordem
function confirmarConclusaoManual() {
    const ordemId = parseInt(document.getElementById('ordemCompra').value);
    const ordemSelect = document.getElementById('ordemCompra');
    
    if (isNaN(ordemId)) {
        showAlert("Selecione uma Ordem válida.", 'error');
        return;
    }

    const selectedText = ordemSelect.options[ordemSelect.selectedIndex].text;
    if (!selectedText.includes("ANDA")) {
        showAlert("A ordem deve estar 'Em Andamento' para ser concluída.", 'info');
        return;
    }

    if (confirm(`Deseja CONCLUIR permanentemente a OC #${ordemId}?`)) {
        concluirOrdemAPI(ordemId); 
    }
}

async function concluirOrdemAPI(id) {
    try {
        const response = await fetch(`${API_BASE_URL}/ordens-de-compra/${id}/concluir`, { method: 'PUT' });
        const responseText = await response.text();
        showAlert(responseText, response.ok ? 'success' : 'error');
        if (response.ok) {
            loadOrdensIniciais();
            loadItensDaOrdem(null);
        }
    } catch (error) {
        showAlert('Erro de conexão.', 'error');
    }
}

// Funções de Listagem e Filtro
async function loadOrdens(status) {
    const spinner = document.getElementById('loadingSpinner');
    const table = document.getElementById('ordensTable');
    const emptyMessage = document.getElementById('emptyMessage');
    
    spinner.classList.remove('hidden');
    table.classList.add('hidden');
    emptyMessage.classList.add('hidden');
    
    try {
        let url = `${API_BASE_URL}/ordens-de-compra`;
        if (status !== 'all') url += `?status=${status}`;
        
        const response = await fetch(url);
        const ordensPage = await processApiResponse(response);

        if (ordensPage && Array.isArray(ordensPage.content)) {
            displayOrdens(ordensPage.content);
        } else {
            displayOrdens([]);
        }
    } catch (error) {
        console.error('Erro:', error);
        showAlert('Erro ao listar ordens: ' + error.message, 'error');
        displayOrdens([]);
    } finally {
        spinner.classList.add('hidden');
    }
}

function displayOrdens(ordens) {
    const table = document.getElementById('ordensTable');
    const tbody = document.getElementById('ordensTableBody');
    const emptyMessage = document.getElementById('emptyMessage');
    
    tbody.innerHTML = '';
    
    if (ordens.length === 0) {
        table.classList.add('hidden');
        emptyMessage.classList.remove('hidden');
        return;
    }
    
    table.classList.remove('hidden');
    emptyMessage.classList.add('hidden');
    
    ordens.forEach(ordem => {
        const row = document.createElement('tr');
        const statusClass = ordem.status === 'PEND' ? 'pending' : 
                          ordem.status === 'ANDA' ? 'ongoing' : 'completed';
        const statusText = ordem.status === 'PEND' ? 'Pendente' : 
                         ordem.status === 'ANDA' ? 'Em Andamento' : 'Concluída';
        
        const produtoNome = ordem.itens && ordem.itens.length > 0 ? ordem.itens[0].produto.nome : 'N/A';
        const quantidade = ordem.itens && ordem.itens.length > 0 ? ordem.itens[0].quantidade : 0;
        const valorTotal = ordem.valor || 0; 

        row.innerHTML = `
            <td>#${ordem.id}</td>
            <td>${produtoNome}</td>
            <td>${quantidade}</td>
            <td><span class="status-badge ${statusClass}">${statusText}</span></td>
            <td>${formatDate(ordem.dataPrevisao)}</td>
            <td>${formatDate(ordem.dataEntrega)}</td>
            <td>R$ ${formatCurrency(valorTotal)}</td>
            <td>${ordem.status === 'ANDA' ? `<button class="btn-concluir" onclick="confirmarConclusao(${ordem.id})">Concluir</button>` : 'N/A'}</td>
        `;
        tbody.appendChild(row);
    });
}

function filterOrdens(status, element) {
    currentFilter = status;
    document.querySelectorAll('.filter-btn').forEach(btn => btn.classList.remove('active'));
    if (element) element.classList.add('active');
    loadOrdens(status);
}

function confirmarConclusao(id) {
    if (confirm('Deseja CONCLUIR esta ordem de compra?')) {
        concluirOrdemAPI(id);
    }
}

function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    if (isNaN(date)) return 'N/A';
    return date.toLocaleDateString('pt-BR');
}

function formatCurrency(value) {
    if (value === null || value === undefined) return '0,00';
    return parseFloat(value).toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

function logout() {
    if (confirm('Tem certeza que deseja sair?')) {
        showAlert('Logout realizado', 'success');
    }
}

async function loadConnections() { /* ... código admin ... */ }
function displayConnections(connections) { /* ... código admin ... */ }
async function killConnection(id) { /* ... código admin ... */ }
async function killAllConnections() { /* ... código admin ... */ }