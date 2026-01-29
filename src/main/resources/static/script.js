// Configuração da API
// String vazia porque o frontend está sendo servido pelo mesmo servidor Spring Boot
const API_BASE_URL = '';

// Estado da aplicação
const state = {
    token: localStorage.getItem('token') || null,
    user: JSON.parse(localStorage.getItem('user')) || null
};

// Utilitários
const showScreen = (screenId) => {
    document.querySelectorAll('.screen').forEach(screen => {
        screen.classList.remove('active');
    });
    document.getElementById(screenId).classList.add('active');
};

const showSection = (sectionId) => {
    document.querySelectorAll('.content-section').forEach(section => {
        section.classList.remove('active');
    });
    document.getElementById(sectionId + '-section').classList.add('active');

    document.querySelectorAll('.menu-item').forEach(item => {
        item.classList.remove('active');
    });
    document.querySelector(`[data-section="${sectionId}"]`).classList.add('active');
};

const showError = (elementId, message) => {
    const element = document.getElementById(elementId);
    element.textContent = message;
    element.classList.add('show');
    setTimeout(() => element.classList.remove('show'), 5000);
};

const showSuccess = (elementId, message) => {
    const element = document.getElementById(elementId);
    element.textContent = message;
    element.classList.add('show');
    setTimeout(() => element.classList.remove('show'), 5000);
};

const formatCurrency = (value) => {
    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(value);
};

const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
};

const formatCPF = (cpf) => {
    return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
};

// Requisições HTTP
const request = async (endpoint, options = {}) => {
    const url = `${API_BASE_URL}${endpoint}`;
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };

    if (state.token) {
        headers['Authorization'] = `Bearer ${state.token}`;
    }

    try {
        const response = await fetch(url, {
            ...options,
            headers
        });

        // Tenta fazer parse do JSON
        let data;
        try {
            data = await response.json();
        } catch (e) {
            // Se não conseguir fazer parse, retorna texto
            const text = await response.text();
            throw new Error(text || 'Erro ao processar resposta do servidor');
        }

        if (!response.ok) {
            // Tratamento especial para erros de validação (400)
            if (response.status === 400 && data.errors) {
                // Spring Boot validation errors
                const errorMessages = Object.values(data.errors).join(', ');
                throw new Error(errorMessages);
            } else if (response.status === 400 && Array.isArray(data)) {
                // Formato alternativo de erros de validação
                const errorMessages = data.map(err => err.defaultMessage || err.message).join(', ');
                throw new Error(errorMessages);
            } else if (data.message) {
                throw new Error(data.message);
            } else if (data.error) {
                throw new Error(data.error);
            } else {
                throw new Error(`Erro ${response.status}: ${response.statusText}`);
            }
        }

        return data;
    } catch (error) {
        console.error('Erro na requisição:', error);
        throw error;
    }
};

// Autenticação
const register = async (userData) => {
    try {
        const response = await request('/auth/register', {
            method: 'POST',
            body: JSON.stringify(userData)
        });

        showSuccess('register-success', `Conta criada com sucesso! Número da conta: ${response.accountNumber}`);

        // Limpa o formulário
        document.getElementById('register-form').reset();

        // Aguarda 2 segundos e redireciona para o login
        setTimeout(() => {
            document.getElementById('show-login').click();
        }, 2000);

        return response;
    } catch (error) {
        showError('register-error', error.message);
        throw error;
    }
};

const login = async (credentials) => {
    try {
        const response = await request('/auth/login', {
            method: 'POST',
            body: JSON.stringify(credentials)
        });

        state.token = response.token;
        state.user = {
            name: credentials.email // Será atualizado no dashboard
        };

        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify(state.user));

        showScreen('dashboard-screen');
        loadDashboard();

        return response;
    } catch (error) {
        showError('login-error', error.message);
        throw error;
    }
};

const logout = () => {
    state.token = null;
    state.user = null;
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    showScreen('login-screen');
    document.getElementById('login-form').reset();
};

// Dashboard
const loadDashboard = async () => {
    try {
        const data = await request('/accounts/dashboard');

        // Atualiza informações do usuário
        document.getElementById('user-name').textContent = data.fullName || 'Usuário';
        document.getElementById('account-balance').textContent = formatCurrency(data.balance);
        document.getElementById('account-number').textContent = data.accountNumber;
        document.getElementById('account-agency').textContent = data.agency;

        // Atualiza o estado do usuário
        state.user = {
            name: data.fullName,
            accountNumber: data.accountNumber,
            agency: data.agency
        };
        localStorage.setItem('user', JSON.stringify(state.user));

        // Carrega extrato para pegar transações recentes
        await loadRecentTransactions();

    } catch (error) {
        console.error('Erro ao carregar dashboard:', error);
    }
};

const loadRecentTransactions = async () => {
    try {
        const transactions = await request('/transactions/statement');

        if (transactions && transactions.length > 0) {
            const recentTransactionsHtml = transactions.slice(0, 5).map(transaction => {
                const isIncome = transaction.direction === 'INBOUND';
                return `
                    <div class="transaction-item">
                        <div class="transaction-info">
                            <div class="transaction-type">${isIncome ? 'Recebido' : 'Enviado'}</div>
                            <div class="transaction-description">${transaction.description || 'Sem descrição'}</div>
                            <div class="transaction-date">${formatDate(transaction.date)}</div>
                        </div>
                        <div class="transaction-amount ${isIncome ? 'income' : 'expense'}">
                            ${isIncome ? '+' : '-'} ${formatCurrency(transaction.amount)}
                        </div>
                    </div>
                `;
            }).join('');
            document.getElementById('recent-transactions').innerHTML = recentTransactionsHtml;
        } else {
            document.getElementById('recent-transactions').innerHTML = '<p class="no-data">Nenhuma transação recente</p>';
        }

    } catch (error) {
        console.error('Erro ao carregar transações recentes:', error);
        document.getElementById('recent-transactions').innerHTML = '<p class="no-data">Nenhuma transação recente</p>';
    }
};

// Extrato
const loadStatement = async () => {
    try {
        const transactions = await request('/transactions/statement');

        if (transactions && transactions.length > 0) {
            const statementHtml = transactions.map(transaction => {
                const isIncome = transaction.direction === 'INBOUND';
                return `
                    <div class="transaction-item">
                        <div class="transaction-info">
                            <div class="transaction-type">${transaction.type}</div>
                            <div class="transaction-description">
                                ${isIncome ? 'De: ' : 'Para: '}${transaction.otherPartyName || 'Não informado'}
                            </div>
                            <div class="transaction-description">${transaction.description || 'Sem descrição'}</div>
                            <div class="transaction-date">${formatDate(transaction.date)}</div>
                        </div>
                        <div class="transaction-amount ${isIncome ? 'income' : 'expense'}">
                            ${isIncome ? '+' : '-'} ${formatCurrency(transaction.amount)}
                        </div>
                    </div>
                `;
            }).join('');
            document.getElementById('statement-list').innerHTML = statementHtml;
        } else {
            document.getElementById('statement-list').innerHTML = '<p class="no-data">Nenhuma transação encontrada</p>';
        }

    } catch (error) {
        document.getElementById('statement-list').innerHTML = '<p class="no-data">Erro ao carregar extrato</p>';
        console.error('Erro ao carregar extrato:', error);
    }
};

// PIX
const checkReceiver = async (pixKey) => {
    try {
        const receiver = await request(`/transfers/check-receiver/${encodeURIComponent(pixKey)}`);

        document.getElementById('receiver-name').textContent = receiver.fullName;
        document.getElementById('receiver-document').textContent = receiver.document;
        document.getElementById('receiver-bank').textContent = receiver.bankName;
        document.getElementById('receiver-agency').textContent = receiver.agency;
        document.getElementById('receiver-account').textContent = receiver.accountNumber;
        document.getElementById('receiver-info').style.display = 'block';

        return receiver;
    } catch (error) {
        showError('pix-error', error.message);
        document.getElementById('receiver-info').style.display = 'none';
        throw error;
    }
};

const executePix = async (transferData) => {
    try {
        const response = await request('/transfers/pix', {
            method: 'POST',
            body: JSON.stringify(transferData)
        });

        showSuccess('pix-success', `Transferência realizada com sucesso! ID: ${response.transactionId}`);
        document.getElementById('pix-form').reset();
        document.getElementById('receiver-info').style.display = 'none';

        // Atualiza o dashboard
        loadDashboard();

        return response;
    } catch (error) {
        showError('pix-error', error.message);
        throw error;
    }
};

// Chaves PIX
const loadPixKeys = async () => {
    try {
        const keys = await request('/pix-keys');

        if (keys && keys.length > 0) {
            const keysHtml = keys.map(key => `
                <div class="pix-key-item">
                    <div class="pix-key-info">
                        <div class="pix-key-type">${key.keyType}</div>
                        <div class="pix-key-value">${key.keyValue}</div>
                    </div>
                </div>
            `).join('');
            document.getElementById('pix-keys-list').innerHTML = keysHtml;
        } else {
            document.getElementById('pix-keys-list').innerHTML = '<p class="no-data">Nenhuma chave cadastrada</p>';
        }

    } catch (error) {
        document.getElementById('pix-keys-list').innerHTML = '<p class="no-data">Erro ao carregar chaves</p>';
        console.error('Erro ao carregar chaves PIX:', error);
    }
};

const createPixKey = async (keyData) => {
    try {
        const response = await request('/pix-keys', {
            method: 'POST',
            body: JSON.stringify(keyData)
        });

        showSuccess('pix-key-success', 'Chave PIX cadastrada com sucesso!');
        document.getElementById('pix-key-form').reset();
        loadPixKeys();

        return response;
    } catch (error) {
        showError('pix-key-error', error.message);
        throw error;
    }
};

// Event Listeners
document.addEventListener('DOMContentLoaded', () => {
    // Navegação entre login e cadastro
    document.getElementById('show-register').addEventListener('click', (e) => {
        e.preventDefault();
        showScreen('register-screen');
    });

    document.getElementById('show-login').addEventListener('click', (e) => {
        e.preventDefault();
        showScreen('login-screen');
    });

    // Formulário de cadastro
    document.getElementById('register-form').addEventListener('submit', async (e) => {
        e.preventDefault();

        const userData = {
            fullName: document.getElementById('register-name').value,
            document: document.getElementById('register-cpf').value.replace(/\D/g, ''),
            email: document.getElementById('register-email').value,
            password: document.getElementById('register-password').value,
            transactionPin: document.getElementById('register-pin').value
        };

        await register(userData);
    });

    // Formatação de CPF
    document.getElementById('register-cpf').addEventListener('input', (e) => {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length > 11) value = value.slice(0, 11);

        if (value.length > 9) {
            e.target.value = value.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
        } else if (value.length > 6) {
            e.target.value = value.replace(/(\d{3})(\d{3})(\d{3})/, '$1.$2.$3');
        } else if (value.length > 3) {
            e.target.value = value.replace(/(\d{3})(\d{3})/, '$1.$2');
        } else {
            e.target.value = value;
        }
    });

    // Validação do PIN - apenas números
    document.getElementById('register-pin').addEventListener('input', (e) => {
        e.target.value = e.target.value.replace(/\D/g, '').slice(0, 4);
    });

    document.getElementById('pix-pin').addEventListener('input', (e) => {
        e.target.value = e.target.value.replace(/\D/g, '').slice(0, 4);
    });

    // Formulário de login
    document.getElementById('login-form').addEventListener('submit', async (e) => {
        e.preventDefault();

        const credentials = {
            email: document.getElementById('login-email').value,
            password: document.getElementById('login-password').value
        };

        await login(credentials);
    });

    // Logout
    document.getElementById('logout-btn').addEventListener('click', logout);

    // Menu de navegação
    document.querySelectorAll('.menu-item').forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();
            const section = e.target.getAttribute('data-section');
            showSection(section);

            // Carrega dados da seção
            if (section === 'statement') {
                loadStatement();
            } else if (section === 'pix-keys') {
                loadPixKeys();
            } else if (section === 'home') {
                loadDashboard();
            }
        });
    });

    // Verificar destinatário PIX
    document.getElementById('check-receiver-btn').addEventListener('click', async () => {
        const pixKey = document.getElementById('pix-key').value;
        if (pixKey) {
            await checkReceiver(pixKey);
        } else {
            showError('pix-error', 'Digite a chave PIX do destinatário');
        }
    });

    // Formulário de transferência PIX
    document.getElementById('pix-form').addEventListener('submit', async (e) => {
        e.preventDefault();

        const transferData = {
            targetKey: document.getElementById('pix-key').value,
            amount: parseFloat(document.getElementById('pix-amount').value),
            transactionPin: document.getElementById('pix-pin').value,
            description: document.getElementById('pix-description').value || null
        };

        await executePix(transferData);
    });

    // Formulário de chave PIX - ATUALIZADO PARA SEGURANÇA
    document.getElementById('key-type').addEventListener('change', (e) => {
        const keyValueInput = document.getElementById('key-value');
        const keyValueGroup = document.getElementById('key-value-group');

        // CPF e EMAIL usam dados do usuário, RANDOM é gerado automaticamente
        if (e.target.value === 'RANDOM' || e.target.value === 'CPF' || e.target.value === 'EMAIL') {
            keyValueGroup.style.display = 'none';
            keyValueInput.required = false;

            // Mensagem informativa
            const messageElement = document.getElementById('key-info-message');
            if (messageElement) {
                if (e.target.value === 'CPF') {
                    messageElement.textContent = 'Seu CPF cadastrado será usado como chave PIX';
                    messageElement.style.display = 'block';
                } else if (e.target.value === 'EMAIL') {
                    messageElement.textContent = 'Seu email cadastrado será usado como chave PIX';
                    messageElement.style.display = 'block';
                } else {
                    messageElement.textContent = 'Uma chave aleatória será gerada automaticamente';
                    messageElement.style.display = 'block';
                }
            }
        } else {
            keyValueGroup.style.display = 'block';
            keyValueInput.required = true;
            keyValueInput.disabled = false;

            // Esconde mensagem informativa
            const messageElement = document.getElementById('key-info-message');
            if (messageElement) {
                messageElement.style.display = 'none';
            }

            if (e.target.value === 'PHONE') {
                keyValueInput.placeholder = '(00) 00000-0000';
            } else {
                keyValueInput.placeholder = '';
            }
        }
    });

    document.getElementById('pix-key-form').addEventListener('submit', async (e) => {
        e.preventDefault();

        const keyType = document.getElementById('key-type').value;
        let keyValue = document.getElementById('key-value').value;

        // Para CPF, EMAIL e RANDOM, o backend usa os dados do usuário
        // O keyValue é enviado como null para que o backend saiba usar os dados corretos
        if (keyType === 'CPF' || keyType === 'EMAIL' || keyType === 'RANDOM') {
            keyValue = null;
        }

        const keyData = {
            keyType: keyType,
            keyValue: keyValue
        };

        await createPixKey(keyData);
    });

    // Verifica se já está autenticado
    if (state.token) {
        showScreen('dashboard-screen');
        loadDashboard();
    }
});