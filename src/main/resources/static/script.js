// API Base URL
const API_BASE_URL = 'http://localhost:8080/api';

// Variável Global: Armazena o ID do produto associado a cada Ordem de Compra
// Uso: ordensComProduto[idDaOC] -> { produtoId: 10 }
let ordensComProduto = {}; 

// Variável Global para armazenar os detalhes dos itens da OC selecionada
// Evita múltiplas chamadas à API
let itensDaOrdemAtual = [];

// VARIÁVEL GLOBAL: Armazenará os itens do pedido para ser usada na submissão final
let itensDoPedidoParaEnvio = []; 

let currentPage = 'recebimento';
let currentFilter = 'all';

// Função auxiliar para processar a resposta da API e lidar com 204/JSON vazio
async function processApiResponse(response) {
    // 204 No Content ou corpo vazio
    if (response.status === 204 || response.headers.get('content-length') === '0') {
        // Retorna um objeto Page vazio se for o caso de listagem, ou null se for um POST
        return response.ok ? { content: [] } : null; 
    }
    if (response.ok) {
        return response.json();
    }
    // Lança erro para ser pego pelo catch principal
    const errorText = await response.text();
    throw new Error(errorText || `Falha na requisição com status ${response.status}`);
}


// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    // Garante que a primeira página a ser mostrada é a de recebimento
    showPage('estoque', document.querySelector('[data-page="estoque"]')); 
    
    loadOrdensIniciais(); // Carrega ordens pendentes e em andamento
    loadResponsaveis();   // Carrega a lista de administradores
    setupFormHandler();   // Configura o envio do formulário
});

// Show alert messages
function showAlert(message, type = 'info') {
    const alertContainer = document.getElementById('alertContainer');
    const alert = document.createElement('div');
    alert.className = `alert ${type}`;
    alert.textContent = message;
    alertContainer.appendChild(alert);

    setTimeout(() => {
        alert.remove();
    }, 5000);
}

// Page navigation
function showPage(pageName, element) {
    // Oculta todas as seções de conteúdo
    document.getElementById('recebimentoPage').classList.add('hidden');
    document.getElementById('ordensPage').classList.add('hidden');
    document.getElementById('adminPage').classList.add('hidden');

    // Remove a classe 'active' de todos os itens da nav
    document.querySelectorAll('.nav-item').forEach(item => {
        item.classList.remove('active');
    });

    currentPage = pageName;

    if (pageName === 'estoque') {
        document.getElementById('recebimentoPage').classList.remove('hidden');
    } else if (pageName === 'ordens') {
        document.getElementById('ordensPage').classList.remove('hidden');
        loadOrdens('all'); // Carrega todas as ordens ao entrar na lista
    } else if (pageName === 'admin') {
        document.getElementById('adminPage').classList.remove('hidden');
        loadConnections();
    } else {
        showAlert('Página em desenvolvimento', 'info');
    }

    // Adiciona 'active' ao item clicado, se existir
    const navItem = element || document.querySelector(`[data-page="${pageName}"]`);
    if (navItem) {
        navItem.classList.add('active');
    }
}

// Função 1: loadOrdensIniciais (Carrega Ordens e Armazena o ID do Produto)
async function loadOrdensIniciais() {
    try {
        const responsePend = await fetch(`${API_BASE_URL}/ordens-de-compra?status=PEND`);
        const responseAnda = await fetch(`${API_BASE_URL}/ordens-de-compra?status=ANDA`);
        
        const ordensPendData = await processApiResponse(responsePend);
        const ordensAndaData = await processApiResponse(responseAnda);
        
        // Combina o array 'content' das duas respostas paginadas
        const ordens = [...(ordensPendData.content || []), ...(ordensAndaData.content || [])];
        
        const select = document.getElementById('ordemCompra');
        select.innerHTML = '<option value="">Selecione uma ordem de compra...</option>';
        
        // --- INÍCIO DA CORREÇÃO DE ARMAZENAMENTO DE DADOS ---
        ordensComProduto = {}; // Limpa o objeto global antes de popular
        
        ordens.forEach(ordem => {
            // Itera sobre os itens para criar uma opção para cada item/produto na OC
            if (ordem.itens && ordem.itens.length > 0) {
                ordem.itens.forEach(item => {
                    if (item.produto) {
                        const produtoNome = item.produto.nome || 'Produto Desconhecido';
                        const produtoId = item.produto.id;
                        
                        // Armazena a Ordem e seu ID de Produto no objeto global
                        ordensComProduto[ordem.id] = { produtoId: produtoId };
                        const option = document.createElement('option');
                        option.value = ordem.id;
                        option.textContent = `OC #${ordem.id} - ${produtoNome} (${item.quantidade} un.) - ${ordem.status}`;
                        select.appendChild(option);
                    }
                });
            }
        });

        // Adiciona o listener de alteração (Passo 1 - Evento Corrigido)
        select.addEventListener('change', function(e) {
            const selectedId = e.target.value;
            handleOrdemCompraChange(selectedId);
        });
        // --- FIM DA CORREÇÃO DE ARMAZENAMENTO DE DADOS ---
    } catch (error) {
        console.error('Erro ao carregar ordens:', error);
        showAlert('Erro ao carregar ordens de compra: ' + error.message, 'error');
    }
}

// Load responsibles from stock sector (Admin ID 1)
async function loadResponsaveis() {
    try {
        // Chama o endpoint que retorna apenas o ADM (ID 1), conforme a regra final
        let response = await fetch(`${API_BASE_URL}/profissionais/estoque`);
        
        const responsaveis = await processApiResponse(response);
        
        // Verifica se é um array (mesmo que vazio) antes de popular
        if (Array.isArray(responsaveis)) {
            populateResponsaveis(responsaveis);
        } else {
             // Caso o backend não retorne um array (e sim um erro ou objeto malformado)
             console.error('API de profissionais retornou um objeto inválido:', responsaveis);
             throw new Error('Formato de dados inválido.');
        }

    } catch (error) {
        console.error('Erro ao carregar responsáveis:', error);
        showAlert('Erro ao carregar responsáveis: ' + error.message, 'error');
        
        // Mock Data para garantir que o formulário possa ser testado
        const mockResponsaveis = [
            { id: 1, nomePessoa: 'Administrador (Teste)' } 
        ];
        populateResponsaveis(mockResponsaveis);
    }
}

function populateResponsaveis(responsaveis) {
    const select = document.getElementById('idResponsavel');
    select.innerHTML = '<option value="">Selecione o responsável...</option>';
    
    responsaveis.forEach(resp => {
        const option = document.createElement('option');
        option.value = resp.id;
        // O campo deve usar 'nomePessoa' conforme o DTO do seu backend
        option.textContent = resp.nomePessoa; 
        select.appendChild(option);
    });
}

// [NOVA FUNÇÃO] Função para buscar e construir a tabela de itens
async function loadItensDaOrdem(idOrdemCompra) {
    const container = document.getElementById('itensDaOrdemContainer');
    const formFields = document.getElementById('formFields');

    if (!idOrdemCompra) {
        container.innerHTML = '';
        formFields.classList.add('hidden');
        itensDoPedidoParaEnvio = [];
        return;
    }

    // Prepara a estrutura da tabela
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
                        <th>Data Vencimento</th>
                    </tr>
                </thead>
                <tbody id="itensLoteTableBody">
                    <tr><td colspan="6">Buscando itens...</td></tr>
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
            tbody.innerHTML = ''; // Limpa o "buscando"
            formFields.classList.remove('hidden');
            
            if (itens.length === 0) {
                tbody.innerHTML = '<tr><td colspan="6">Nenhum item encontrado para esta Ordem de Compra.</td></tr>';
                itensDoPedidoParaEnvio = [];
                return;
            }
            
            // Armazena a lista de IDs de produto para a submissão e mapeia para a interface
            itensDoPedidoParaEnvio = itens.map((item, index) => {
                const row = document.createElement('tr');
                
                // Mapeamento dos dados do item
                const produtoId = item.produto.id;
                const produtoNome = item.produto.nome;
                const quantidadePedida = item.quantidade;
                
                // CRIAÇÃO DOS INPUTS DINÂMICOS COM IDS ÚNICOS POR INDEX
                row.innerHTML = `
                    <td>${produtoId}</td>
                    <td>${produtoNome}</td>
                    <td>${quantidadePedida}</td>
                    <td><input type="number" id="qntdRecebida-${index}" class="form-input" placeholder="0" required></td>
                    <td><input type="text" id="codigoLote-${index}" class="form-input" placeholder="Ex: L2024-A1" required></td>
                    <td><input type="date" id="dataVencimento-${index}" class="form-input" required></td>
                `;
                tbody.appendChild(row);

                return {
                    idProduto: produtoId,
                    index: index // Retorna o index para ser usado na leitura dos inputs
                };
            });

        } else {
            const error = await response.text();
            showAlert(`Erro ao buscar itens: ${error}`, 'error');
            container.innerHTML = '';
            formFields.classList.add('hidden');
        }
    } catch (error) {
        console.error('Erro ao buscar itens:', error);
        showAlert('Erro ao conectar para buscar os detalhes do pedido.', 'error');
        container.innerHTML = '';
        formFields.classList.add('hidden');
    }
}

// Renomeando handleOrdemCompraChange para apenas chamar a nova função
function handleOrdemCompraChange(ordemId) {
    loadItensDaOrdem(ordemId);
}

// Substitua o setupFormHandler por esta versão (já corrige a leitura da lista)
function setupFormHandler() {
    const form = document.getElementById('recebimentoForm');
    form.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const submitBtn = form.querySelector('.submit-btn');
        submitBtn.disabled = true;
        submitBtn.textContent = 'Processando...';
        
        try {
            // 1. Constrói a lista de itens recebidos a partir da tabela dinâmica
            const itensRecebidosList = itensDoPedidoParaEnvio.map(item => {
                const index = item.index;
                const qntd = parseInt(document.getElementById(`qntdRecebida-${index}`).value);
                const dataVenc = document.getElementById(`dataVencimento-${index}`).value;
                const codigoLote = document.getElementById(`codigoLote-${index}`).value; // Leitura do novo campo

                // Só processa itens que tiveram quantidade digitada
                if (isNaN(qntd) || qntd <= 0) {
                    return null; // Será filtrado depois
                }

                // Validação de inputs na submissão
                if (!dataVenc || !codigoLote) {
                    throw new Error(`Para o produto ID ${item.idProduto}, preencha o Lote e a Data de Vencimento para a quantidade a receber.`);
                }

                return {
                    idProduto: item.idProduto,
                    quantidadeRecebida: qntd,
                    dataVencimento: dataVenc,
                    codigoLote: codigoLote // Envio para o backend
                };
            }).filter(item => item !== null); // Remove os itens não preenchidos

            // 2. Constrói o objeto de submissão principal
            const payload = {
                idOrdemCompra: parseInt(document.getElementById('ordemCompra').value),
                idResponsavel: parseInt(document.getElementById('idResponsavel').value),
                login: document.getElementById('login').value,
                senha: document.getElementById('senha').value,
                itensRecebidos: itensRecebidosList
            };

            // 3. Validação final
            if (isNaN(payload.idOrdemCompra) || payload.itensRecebidos.length === 0) { // Valida se há itens para enviar
                throw new Error('Selecione uma Ordem de Compra e preencha os itens recebidos.');
            }

            const response = await fetch(`${API_BASE_URL}/ordens-de-compra/receber-itens`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(payload)
            });

            const errorText = await response.text();

            if (response.ok) {
                // A mensagem de sucesso agora vem do backend e pode ser "Itens recebidos..." ou "Ordem concluída..."
                showAlert(errorText, 'success');
                // Recarrega a lista de itens da OC atual para mostrar o novo status
                loadItensDaOrdem(payload.idOrdemCompra);
                loadOrdensIniciais(); // Recarrega o dropdown de OCs para remover as concluídas
            } else {
                showAlert(`Erro ao receber itens: ${errorText}`, 'error');
            }
        } catch (error) {
            console.error('Erro:', error);
            showAlert('Erro ao processar recebimento: ' + error.message, 'error');
        } finally {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Concluir Recebimento';
        }
    });
}

// [NOVA FUNÇÃO] Valida e Conclui a OC manualmente a partir da tela de recebimento
function confirmarConclusaoManual() {
    const ordemId = parseInt(document.getElementById('ordemCompra').value);
    const ordemSelect = document.getElementById('ordemCompra');
    
    // 1. Pega o status da OC selecionada no Dropdown
    if (isNaN(ordemId)) {
        showAlert("Selecione uma Ordem de Compra válida para concluir.", 'error');
        return;
    }

    const selectedText = ordemSelect.options[ordemSelect.selectedIndex].text;
    const isEmAndamento = selectedText.includes("ANDA");

    if (!isEmAndamento) {
        showAlert("A Ordem de Compra deve estar 'Em Andamento' (ANDA) para ser concluída manualmente.", 'info');
        return;
    }

    if (confirm(`Deseja CONCLUIR permanentemente a OC #${ordemId}? Confirme que todos os lotes foram recebidos.`)) {
        // 2. Dispara a API de Conclusão Final
        concluirOrdemAPI(ordemId); 
    }
}

// A função abaixo já existia para o botão na lista de ordens, agora será usada aqui também.
async function concluirOrdemAPI(id) {
    try {
        const response = await fetch(`${API_BASE_URL}/ordens-de-compra/${id}/concluir`, { method: 'PUT' });
        const responseText = await response.text();
        showAlert(responseText, response.ok ? 'success' : 'error');
        if (response.ok) {
            loadOrdensIniciais(); // Recarrega o dropdown de OCs
            loadItensDaOrdem(null); // Limpa a tabela de itens
        }
    } catch (error) {
        showAlert('Erro ao conectar com o servidor para conclusão.', 'error');
    }
}


// Load orders with filter
async function loadOrdens(status) {
    const spinner = document.getElementById('loadingSpinner');
    const table = document.getElementById('ordensTable');
    const emptyMessage = document.getElementById('emptyMessage');
    
    spinner.classList.remove('hidden');
    table.classList.add('hidden');
    emptyMessage.classList.add('hidden');
    
    try {
        let url = `${API_BASE_URL}/ordens-de-compra`;
        if (status !== 'all') {
            url += `?status=${status}`;
        }
        
        const response = await fetch(url);
        const ordensPage = await processApiResponse(response);

        if (ordensPage && Array.isArray(ordensPage.content)) {
            displayOrdens(ordensPage.content);
        } else {
            displayOrdens([]); // Exibe vazio para evitar erros
        }

    } catch (error) {
        console.error('Erro:', error);
        showAlert('Erro ao conectar com o servidor para listar ordens: ' + error.message, 'error');
        displayOrdens([]);
    } finally {
        spinner.classList.add('hidden');
    }
}

// Display orders in table
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
        
        // Pega o nome e quantidade do primeiro item da ordem
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

// Filter orders
function filterOrdens(status, element) {
    currentFilter = status;
    
    // Update button states
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    if (element) {
        element.classList.add('active');
    }
    
    // Load filtered orders
    loadOrdens(status);
}

// Função para confirmar e executar a conclusão manual de uma OC
function confirmarConclusao(id) {
    if (confirm('Deseja CONCLUIR esta ordem de compra? Confirma que todos os itens já foram recebidos?')) {
        concluirOrdemAPI(id);
    }
}

// [NOVA FUNÇÃO] - Chama o endpoint PUT para concluir a ordem
async function concluirOrdemAPI(id) {
    try {
        const response = await fetch(`${API_BASE_URL}/ordens-de-compra/${id}/concluir`, { method: 'PUT' });
        const responseText = await response.text();
        showAlert(responseText, response.ok ? 'success' : 'error');
        if (response.ok) loadOrdens(currentFilter); // Recarrega a lista
    } catch (error) {
        showAlert('Erro ao conectar com o servidor para concluir a ordem.', 'error');
    }
}

// Format date
function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    // Verifica se a data é válida antes de formatar
    if (isNaN(date)) return 'N/A';
    return date.toLocaleDateString('pt-BR');
}

// Format currency
function formatCurrency(value) {
    if (value === null || value === undefined) return '0,00';
    // Converte Big Decimal ou String para float antes de formatar
    return parseFloat(value).toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

// Logout function
function logout() {
    if (confirm('Tem certeza que deseja sair?')) {
        showAlert('Logout realizado com sucesso', 'success');
        // Redireciona para página de login ou limpa a sessão
        // window.location.href = '/login'; // Descomente se tiver uma página de login
    }
}

// --- Funções para a página de Admin DB ---

async function loadConnections() {
    const tbody = document.getElementById('connectionsTableBody');
    const countEl = document.getElementById('connectionCount');
    tbody.innerHTML = '<tr><td colspan="7" style="text-align:center;">Carregando...</td></tr>';

    try {
        const response = await fetch(`${API_BASE_URL}/admin/db/connections`);
        if (!response.ok) {
            throw new Error('Falha ao carregar conexões.');
        }
        const connections = await response.json();
        displayConnections(connections);
        countEl.textContent = connections.length;

    } catch (error) {
        console.error('Erro ao carregar conexões:', error);
        showAlert(error.message, 'error');
        tbody.innerHTML = `<tr><td colspan="7" style="text-align:center; color: red;">${error.message || 'Falha ao carregar conexões.'}</td></tr>`;
        countEl.textContent = 'Erro';
    }
}

function displayConnections(connections) {
    const tbody = document.getElementById('connectionsTableBody');
    tbody.innerHTML = '';

    if (connections.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" style="text-align:center;">Nenhuma conexão ativa encontrada.</td></tr>';
        return;
    }

    connections.forEach(conn => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${conn.Id}</td>
            <td>${conn.User}</td>
            <td>${conn.Host}</td>
            <td>${conn.Command}</td>
            <td>${conn.Time}</td>
            <td style="max-width: 300px; word-wrap: break-word;">${conn.Info || 'N/A'}</td>
            <td>
                <button class="filter-btn" style="background-color: #e74c3c; color: white;" onclick="killConnection(${conn.Id})">
                    Kill
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

async function killConnection(id) {
    if (!confirm(`Tem certeza que deseja derrubar a conexão ID ${id}?`)) {
        return;
    }
    try {
        const response = await fetch(`${API_BASE_URL}/admin/db/kill/${id}`, { method: 'POST' });
        const resultText = await response.text();
        if (!response.ok) {
            throw new Error(resultText);
        }
        showAlert(`Conexão ${id} encerrada.`, 'success');
        loadConnections(); // Recarrega a lista
    } catch (error) {
        console.error(`Erro ao derrubar conexão ${id}:`, error);
        showAlert(`Erro: ${error.message}`, 'error');
    }
}

async function killAllConnections() {
    if (!confirm('ATENÇÃO: Isso irá derrubar TODAS as conexões, exceto a da própria aplicação. Deseja continuar?')) {
        return;
    }
    // Implementar chamada para /kill-all se desejar.
    // Por segurança, vamos manter o kill individual por enquanto.
    showAlert('Funcionalidade "Derrubar Todas" desativada por segurança. Use o botão "Kill" individual.', 'info');
}