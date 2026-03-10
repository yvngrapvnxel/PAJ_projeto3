// ==========================================
// admin.js - Painel de Administração Dinâmico
// ==========================================
let clientesAlvoAdmin = [];
let leadsAlvoAdmin = [];

const token = localStorage.getItem("token");

// 1. Ponto de entrada: Carrega a estrutura base e chama a lista
async function carregarPaginaAdmin() {
    const content = document.getElementById("content");

    if (localStorage.getItem("isAdmin") !== "true") {
        content.innerHTML = `<h2 style="color:red; text-align:center; margin-top: 50px;">Acesso Negado</h2>`;
        return;
    }

    if (window.location.hash !== "#admin") window.location.hash = "#admin";

    content.innerHTML = `
        <div class="barra-clientes" style="margin-bottom: 20px;">
            <h2>Painel de Administração</h2>
            <button class="btn" onclick="carregarListaUtilizadores()"><i class="fa-solid fa-arrow-left"></i> Voltar à Lista</button>
        </div>
        
        <div id="adminConteudo" class="admin-container">
            <p><i class="fa-solid fa-spinner fa-spin"></i> A carregar utilizadores...</p>
        </div>
    `;

    await carregarListaUtilizadores();
}

// 2. Mostra a lista de TODOS os utilizadores
async function carregarListaUtilizadores() {
    const conteudo = document.getElementById("adminConteudo");

    try {
        const response = await fetch("http://localhost:8080/projeto3/rest/admin/users", {
            method: "GET", headers: { "token": token }
        });

        if (response.ok) {
            const users = await response.json();

            let html = `
                <div class="admin-panel">
                    <h3><i class="fa-solid fa-users"></i> Utilizadores Registados (${users.length})</h3>
                    <ul class="admin-list">
            `;

            users.forEach(u => {
                html += `
                    <li class="admin-list-item">
                        <div>
                            <strong>${u.primeiroNome} ${u.ultimoNome}</strong> 
                            <span style="color: #888;">(@${u.username})</span>
                            ${u.admin ? '<i class="fa-solid fa-crown" style="color: gold; margin-left: 5px;" title="Administrador"></i>' : ''}
                        </div>
                        <button class="btn" onclick="abrirDetalhesUtilizador('${u.username}')">
                            Gerir <i class="fa-solid fa-arrow-right"></i>
                        </button>
                    </li>
                `;
            });

            html += `</ul></div>`;
            conteudo.innerHTML = html;
        } else {
            conteudo.innerHTML = `<p style="color:red;">Erro ao carregar utilizadores: ${await response.text()}</p>`;
        }
    } catch (error) {
        conteudo.innerHTML = `<p style="color:red;">Falha na ligação ao servidor.</p>`;
    }
}

// 3. Abre os detalhes, clientes e leads de um utilizador específico
async function abrirDetalhesUtilizador(username) {
    const conteudo = document.getElementById("adminConteudo");
    conteudo.innerHTML = `<p><i class="fa-solid fa-spinner fa-spin"></i> A carregar dados de ${username}...</p>`;

    try {
        const resUser = await fetch(`http://localhost:8080/projeto3/rest/admin/users/${username}`, { method: 'GET', headers: { "token": token } });
        const resClients = await fetch(`http://localhost:8080/projeto3/rest/admin/users/${username}/clients`, { method: 'GET', headers: { "token": token } });
        const resLeads = await fetch(`http://localhost:8080/projeto3/rest/admin/users/${username}/leads`, { method: 'GET', headers: { "token": token } });

        if (resUser.ok && resClients.ok && resLeads.ok) {
            const user = await resUser.json();
            const clientes = await resClients.json();
            const leads = await resLeads.json();

            clientesAlvoAdmin = clientes;

            let html = `                
                <div class="user-card">
                    <div class="user-card-info">
                        <img src="${user.fotoUrl || './imagens/favicon1.png'}" class="user-avatar">
                        <div>
                            <h3>${user.primeiroNome} ${user.ultimoNome} ${user.admin ? '<i class="fa-solid fa-crown" style="color: gold;"></i>' : ''}</h3>
                            <p class="user-details-text"><i class="fa-solid fa-at"></i> ${user.username} | <i class="fa-solid fa-envelope"></i> ${user.email} | <i class="fa-solid fa-phone"></i> ${user.telefone}</p>
                        </div>
                    </div>
                    <div class="user-card-actions">
                        <button class="btn btn-warning" onclick="apagarUtilizadorAdmin('${user.username}', false)"><i class="fa-solid fa-user-slash"></i> Inativar Conta</button>
                        <button class="btn btn-danger" onclick="apagarUtilizadorAdmin('${user.username}', true)"><i class="fa-solid fa-trash"></i> Excluir Conta</button>
                    </div>
                </div>

                <div class="cards-container">
                    
                    <div class="info-card clientes">
                        <div class="card-header">
                            <h3><i class="fa-solid fa-briefcase"></i> Clientes (${clientes.length})</h3>
                            <div class="action-buttons">
                                <button class="btn btn-warning btn-sm" onclick="apagarTodosClientesAdmin('${user.username}', false)" title="Inativar Todos"><i class="fa-solid fa-ban"></i> Inativar Todos</button>
                                <button class="btn btn-danger btn-sm" onclick="apagarTodosClientesAdmin('${user.username}', true)" title="Excluir Definitivamente Todos"><i class="fa-solid fa-fire"></i> Excluir Todos</button>
                            </div>
                        </div>
            `;

            if(clientes.length === 0) {
                html += `<p style="color: #888;">Nenhum cliente registado.</p>`;
            } else {
                html += `<ul class="admin-list">`;
                clientes.forEach((c, index) => {
                    html += `
                        <li class="admin-list-item">
                            <div>
                                <strong>${c.nome}</strong><br>
                                <span class="item-subtitle"><i class="fa-regular fa-building"></i> ${c.empresa}</span>
                            </div>
                            <div class="action-buttons">
                                <button class="btn btn-warning btn-sm" onclick="mostrarFormEdicaoClienteAdmin(${index}, '${user.username}')" title="Editar Cliente"><i class="fa-solid fa-pen"></i></button>
                                <button class="btn btn-danger btn-sm" onclick="apagarClienteAdminBtn(${c.id}, true, '${user.username}')" title="Excluir Cliente"><i class="fa-solid fa-trash"></i></button>
                            </div>
                        </li>`;
                });
                html += `</ul>`;
            }

            leadsAlvoAdmin = leads;

            html += `
                    </div>

                    <div class="info-card leads">
                        <div class="card-header">
                            <h3><i class="fa-solid fa-filter"></i> Leads (${leads.length})</h3>
                            <div class="action-buttons">
                                <button class="btn btn-warning btn-sm" onclick="apagarTodasLeadsAdmin('${user.username}', false)" title="Inativar Todas"><i class="fa-solid fa-ban"></i> Inativar Todas</button>
                                <button class="btn btn-danger btn-sm" onclick="apagarTodasLeadsAdmin('${user.username}', true)" title="Excluir Definitivamente Todas"><i class="fa-solid fa-fire"></i> Excluir Todas</button>
                            </div>
                        </div>
            `;

            if(leads.length === 0) {
                html += `<p style="color: #888;">Nenhuma lead registada.</p>`;
            } else {
                html += `<ul class="admin-list">`;
                leads.forEach((l, index) => {
                    const statusOptions = ["Novo", "Em análise", "Proposta", "Ganho", "Perdido"];
                    const nomeEstado = statusOptions[Number(l.estado)] ?? "Desconhecido";

                    html += `
                        <li class="admin-list-item">
                            <div>
                                <strong>${l.titulo || 'Sem Título'}</strong><br>
                                <span class="item-subtitle"><i class="fa-solid fa-flag"></i> Estado: ${nomeEstado}</span>
                            </div>
                            <div class="action-buttons">
                                <button class="btn btn-warning btn-sm" onclick="mostrarFormEdicaoLeadAdmin(${index}, '${user.username}')" title="Editar Lead"><i class="fa-solid fa-pen"></i></button>
                                
                                <button class="btn btn-danger btn-sm" onclick="apagarLeadAdminBtn(${l.id}, true, '${user.username}')" title="Excluir Lead"><i class="fa-solid fa-trash"></i></button>
                            </div>
                        </li>`;
                });
                html += `</ul>`;
            }

            html += `
                    </div>
                </div>
            `;

            conteudo.innerHTML = html;
        } else {
            conteudo.innerHTML = `<p style="color:red;">Erro ao carregar detalhes do utilizador.</p>`;
        }
    } catch (error) {
        conteudo.innerHTML = `<p style="color:red;">Falha na ligação ao servidor.</p>`;
    }
}

// ==========================================
// FUNÇÕES DE AÇÃO (DELETE / INATIVAR)
// ==========================================

async function apagarUtilizadorAdmin(username, permanente) {
    if (!confirm(`Tens a certeza que queres ${permanente ? 'EXCLUIR DEFINITIVAMENTE' : 'inativar'} o utilizador @${username}?`)) return;

    try {
        let url = `http://localhost:8080/projeto3/rest/admin/users/${username}` + (permanente ? "?permanente=true" : "");
        const response = await fetch(url, {
            method: 'DELETE',
            headers: { "token": token }
        });

        const alerta = await response.text();
        alert(alerta);

        if (permanente) {
            await carregarListaUtilizadores();
        }
    } catch (error) {
        alert("Falha na comunicação.");
    }
}

async function apagarTodosClientesAdmin(username, permanente) {
    const acao = permanente ? 'EXCLUIR DEFINITIVAMENTE' : 'inativar';

    if (!confirm(`Tens a certeza que queres ${acao} TODOS os clientes de @${username}?`)) return;

    try {
        let url = `http://localhost:8080/projeto3/rest/admin/users/${username}/clients` + (permanente ? "?permanente=true" : "");
        const response = await fetch(url, { method: 'DELETE', headers: { "token": token }});
        alert(await response.text());
        await abrirDetalhesUtilizador(username);
    } catch (error) {
        alert("Falha na comunicação.");
    }
}

async function apagarClienteAdminBtn(idCliente, permanente, usernameToRefresh) {
    if (!confirm(`Excluir definitivamente este Cliente?`)) return;
    try {
        let url = `http://localhost:8080/projeto3/rest/admin/clients/${idCliente}` + (permanente ? "?permanente=true" : "");
        const response = await fetch(url, { method: 'DELETE', headers: { "token": token }});
        alert(await response.text());
        await abrirDetalhesUtilizador(usernameToRefresh);
    } catch (error) {
        alert("Falha na comunicação.");
    }
}

// ==========================================
// FUNÇÕES DE EDIÇÃO DE CLIENTES (ADMIN)
// ==========================================

function mostrarFormEdicaoClienteAdmin(index, username) {
    const c = clientesAlvoAdmin[index];
    const conteudo = document.getElementById("adminConteudo");

    conteudo.innerHTML = `
        <button class="btn" style="margin-bottom: 20px;" onclick="abrirDetalhesUtilizador('${username}')"><i class="fa-solid fa-arrow-left"></i> Voltar ao perfil do utilizador</button>

        <div class="edit-form-card">
            <h3 style="margin-top:0;"><i class="fa-solid fa-pen"></i> Editar Cliente</h3>
            <p style="color: #666; margin-bottom: 20px; font-size: 14px;">A alterar dados do cliente pertencente a <strong>@${username}</strong>.</p>

            <label class="form-label">Nome</label>
            <input id="editAdminCliNome" type="text" value="${c.nome}" class="form-input">

            <label class="form-label">Email</label>
            <input id="editAdminCliEmail" type="email" value="${c.email || ''}" class="form-input">

            <label class="form-label">Telefone</label>
            <input id="editAdminCliTelefone" type="text" value="${c.telefone || ''}" class="form-input">

            <label class="form-label">Empresa</label>
            <input id="editAdminCliEmpresa" type="text" value="${c.empresa}" class="form-input" style="margin-bottom: 25px;">

            <button class="btn btn-success btn-block" onclick="guardarEdicaoClienteAdmin(${c.id}, '${username}')">
                <i class="fa-solid fa-floppy-disk"></i> Guardar Alterações
            </button>
        </div>
    `;
}

async function guardarEdicaoClienteAdmin(idCliente, username) {
    const nome = document.getElementById("editAdminCliNome").value.trim();
    const email = document.getElementById("editAdminCliEmail").value.trim();
    const telefone = document.getElementById("editAdminCliTelefone").value.trim();
    const empresa = document.getElementById("editAdminCliEmpresa").value.trim();

    if (!nome || !empresa || (!email && !telefone)) {
        alert("Nome, Empresa e pelo menos um contacto são obrigatórios.");
        return;
    }

    const dados = { id: idCliente, nome, email, telefone, empresa };

    try {
        const response = await fetch(`http://localhost:8080/projeto3/rest/admin/clients/${idCliente}`, {
            method: 'PUT',
            headers: {
                "Content-Type": "application/json",
                "token": token
            },
            body: JSON.stringify(dados)
        });

        const msg = await response.text();

        if (response.ok) {
            alert("Sucesso: " + msg);
            await abrirDetalhesUtilizador(username);
        } else {
            alert("Erro ao editar: " + msg);
        }
    } catch (error) {
        alert("Falha na comunicação com o servidor.");
    }
}

// Ações de Leads (Admin)
async function apagarTodasLeadsAdmin(username, permanente) {
    const acao = permanente ? 'EXCLUIR DEFINITIVAMENTE' : 'inativar';
    if (!confirm(`Tens a certeza que queres ${acao} TODAS as leads de @${username}?`)) return;
    try {
        let url = `http://localhost:8080/projeto3/rest/admin/users/${username}/leads` + (permanente ? "?permanente=true" : "");
        const response = await fetch(url, { method: 'DELETE', headers: { "token": token }});
        alert(await response.text());
        await abrirDetalhesUtilizador(username);
    } catch (error) {
        alert("Falha na comunicação.");
    }
}

async function apagarLeadAdminBtn(idLead, permanente, usernameToRefresh) {
    if (!confirm(`Excluir definitivamente esta Lead?`)) return;
    try {
        let url = `http://localhost:8080/projeto3/rest/admin/leads/${idLead}` + (permanente ? "?permanente=true" : "");
        const response = await fetch(url, { method: 'DELETE', headers: { "token": token }});
        alert(await response.text());
        await abrirDetalhesUtilizador(usernameToRefresh);
    } catch (error) {
        alert("Falha na comunicação.");
    }
}

// ==========================================
// FUNÇÕES DE EDIÇÃO DE LEADS (ADMIN)
// ==========================================

function mostrarFormEdicaoLeadAdmin(index, username) {
    const l = leadsAlvoAdmin[index];
    const conteudo = document.getElementById("adminConteudo");

    // Os mesmos estados que tens no leads.js
    const statusOptions = ["Novo", "Em análise", "Proposta", "Ganho", "Perdido"];
    let opcoesHtml = statusOptions.map((s, idx) => `<option value="${idx}" ${Number(l.estado) === idx ? "selected" : ""}>${s}</option>`).join("");

    conteudo.innerHTML = `
        <button class="btn" style="margin-bottom: 20px;" onclick="abrirDetalhesUtilizador('${username}')"><i class="fa-solid fa-arrow-left"></i> Voltar ao perfil do utilizador</button>

        <div class="edit-form-card" style="border-top: 4px solid #5cb85c;">
            <h3 style="margin-top:0;"><i class="fa-solid fa-pen"></i> Editar Lead</h3>
            <p style="color: #666; margin-bottom: 20px; font-size: 14px;">A alterar dados da lead pertencente a <strong>@${username}</strong>.</p>

            <label class="form-label">Título</label>
            <input id="editAdminLeadTitulo" type="text" value="${l.titulo}" class="form-input">

            <label class="form-label">Descrição</label>
            <textarea id="editAdminLeadDescricao" class="form-input" style="min-height: 80px;">${l.descricao}</textarea>

            <label class="form-label">Estado</label>
            <select id="editAdminLeadEstado" class="form-input" style="margin-bottom: 25px;">
                ${opcoesHtml}
            </select>

            <button class="btn btn-success btn-block" onclick="guardarEdicaoLeadAdmin(${l.id}, '${username}')">
                <i class="fa-solid fa-floppy-disk"></i> Guardar Alterações
            </button>
        </div>
    `;
}

async function guardarEdicaoLeadAdmin(idLead, username) {
    const titulo = document.getElementById("editAdminLeadTitulo").value.trim();
    const descricao = document.getElementById("editAdminLeadDescricao").value.trim();
    const estado = parseInt(document.getElementById("editAdminLeadEstado").value);

    if (!titulo || !descricao) {
        alert("Título e Descrição são obrigatórios.");
        return;
    }

    const dados = { id: idLead, titulo, descricao, estado };

    try {
        const response = await fetch(`http://localhost:8080/projeto3/rest/admin/leads/${idLead}`, {
            method: 'PUT',
            headers: {
                "Content-Type": "application/json",
                "token": token
            },
            body: JSON.stringify(dados)
        });

        const msg = await response.text();

        if (response.ok) {
            alert("Sucesso: " + msg);
            await abrirDetalhesUtilizador(username);
        } else {
            alert("Erro ao editar: " + msg);
        }
    } catch (error) {
        alert("Falha na comunicação com o servidor.");
    }
}