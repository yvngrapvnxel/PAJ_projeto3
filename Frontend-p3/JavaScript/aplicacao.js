// aplicacao.js - ficheiro principal da aplicação

// para apagar mais tarde
console.log("Aplicação iniciada.")

const content = document.getElementById("content");

document.addEventListener("DOMContentLoaded", () => {
    
    const form = document.getElementById("loginForm");

    const regForm = document.getElementById("registerForm");
    
    if (form) {
        form.addEventListener("submit", login);
    }
    if (regForm) {
    regForm.addEventListener("submit", registar);
}


});


function loadHeader(page) {
    const headerDiv = document.getElementById("header");
    const currentPage = window.location.pathname;

    // Se for Login OU Registo, mostra o header simples
    if (currentPage.includes("login.html") || currentPage.includes("register.html")) {
        headerDiv.innerHTML = `
            <header id="header">
                <img src="/imagens/favicon1.png" class="logo">
                <h1>Customer Relationship Management</h1>
            </header>
        `;
    } else {

        const firstName = sessionStorage.getItem("userFirstName") || "Utilizador";
        headerDiv.innerHTML = `
        
            <header class="header-app">
                <div class="header-container">
                    <div class="header-left">
                        <img src="/imagens/favicon1.png" class="logo">
                        <h1>Customer Relationship Management</h1>
                    </div>
                
                    <div class="header-right">
                        <button class="btn" onclick="verPerfil()"><i class="fa-solid fa-circle-user"></i>Meu Perfil</button>
                        <button class="btn" onclick="logout()"><i class="fa-solid fa-arrow-right-from-bracket"></i>Logout</button>
                    </div>
                </div>

                <div class="header-welcome">
                    <strong><span class="Welcome">Bem-vindo ${firstName}</span></strong>
                </div>
            </header>
        `;
    }
}

function loadAside(){
    const asideDiv = document.getElementById("aside");

    asideDiv.innerHTML = `
        <aside id="sidebar">
            <button onclick="loadLeads()">Leads</button>
            <br>
            <button onclick="loadClientes()">Clientes</button>
            <br>
            <button onclick="loadProjetos()">Projetos</button>
            <br>
            <button onclick="loadTarefas()">Tarefas</button>
        </aside>
    `
}

function loadFooter() {
    const footerDiv = document.getElementById("footer");
    
    // Adicionamos um parágrafo limpo e uma mensagem mais profissional
    footerDiv.innerHTML = `<p>${new Date().getFullYear()} © Customer Relationship Management - Todos os direitos reservados.</p>`;
}

// Funções para carregar as Leads

function loadLeads() {
  if (window.location.hash !== "#leads") {
    window.location.hash = "#leads";
  }
  carregarLeads();
}

// Funções para carregar os Clientes

function loadClientes() {

    // Muda a URL lá em cima (ex: dashboard.html#clientes) para ser partilhável
    if (window.location.hash !== "#clientes") {
        window.location.hash = "#clientes"; 
    }

    content.innerHTML = `
    <div class="barra-clientes">
        <h2>Clientes</h2>
        <button class="btn" type="button"onclick="formNovoCliente()"><i class="fa-solid fa-user-plus"></i>Novo Cliente</button>
    </div>
    <!-- lista não ordenada de clientes -->
    <ul id="listaClientes"></ul> 
    <br>
    `;
    
    carregarClientes();
}

// Funções de login e logout

async function login(event) {
    if(event) event.preventDefault(); 

    const usernameInput = document.getElementById("username").value;
    const passwordInput = document.getElementById("password").value;
    const url = "http://localhost:8080/backend-p2-1.0-SNAPSHOT/rest/users/login";

    const dados = { username: usernameInput, password: passwordInput };

    try {
        const resposta = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dados)
        });

        if (resposta.ok) {
            // CORREÇÃO: Usar 'username' para ser compatível com o teu clientes.js
            sessionStorage.setItem("username", usernameInput);
            sessionStorage.setItem("password", passwordInput);
            
            // Opcional: manter o currentUser se tiveres outras partes a usá-lo
            localStorage.setItem("currentUser", usernameInput);

            const profileUrl = `http://localhost:8080/backend-p2-1.0-SNAPSHOT/rest/users/${usernameInput}`;
            const resProfile = await fetch(profileUrl);
            const userData = await resProfile.json();

            sessionStorage.setItem("userFirstName", userData.primeiroNome);
            
            window.location.href = "dashboard.html";
        } else {
            alert("Credenciais inválidas.");
        }
    } catch (erro) {
        console.error("Erro:", erro);
        alert("Erro na ligação ao servidor.");
    }
}

async function logout() {
    try {
        await fetch("http://localhost:8080/backend-p2-1.0-SNAPSHOT/rest/users/logout", {
            method: 'POST'
        });
    } catch (e) {
        console.error("Erro ao fechar sessão no servidor.");
    }
    
    //apaga TODOS os dados 
    localStorage.clear();
    sessionStorage.clear();
    console.log("Sessão terminada.");
    window.location.href = "login.html";
}

// Função de Registo (Backend Integration)
async function registar(event) {
    event.preventDefault();

    const novoUtilizador = {
        primeiroNome: document.getElementById("regPrimeiroNome").value,
        ultimoNome: document.getElementById("regUltimoNome").value,
        email: document.getElementById("regEmail").value,
        username: document.getElementById("regUsername").value,
        password: document.getElementById("regPassword").value,
        fotoUrl: document.getElementById("regFotoUrl").value,
        telefone: document.getElementById("regTelefone").value // Campo opcional conforme o teu UserPojo
    };

    try {
        const resposta = await fetch("http://localhost:8080/backend-p2-1.0-SNAPSHOT/rest/users/register", {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(novoUtilizador)
        });

        if (resposta.status === 201) {
            alert("Utilizador registado com sucesso!");
            window.location.href = "login.html";
        } else {
            alert("Erro no registo. Verifique se o username já existe.");
        }
    } catch (erro) {
        console.error("Erro na ligação:", erro);
    }
}

// ==========================================
// FUNÇÕES DO PERFIL DO UTILIZADOR
// ==========================================

async function verPerfil() {
    if (window.location.hash !== "#perfil") {
        window.location.hash = "#perfil";
    }

    const username = sessionStorage.getItem("username");
    if (!username) return;

    try {
        const response = await fetch(`http://localhost:8080/backend-p2-1.0-SNAPSHOT/rest/users/${username}`);

        if (response.ok) {
            const user = await response.json();
            const main = document.getElementById("content");

            main.innerHTML = `
                <div class="perfil-container" style="max-width: 600px; margin: 0 auto; text-align: left; background: #f9f9f9; padding: 20px; border-radius: 8px;">
                    <h2 style="text-align: center;">Meu Perfil</h2>
                    
                    <div style="text-align: center; margin-bottom: 20px;">
                        <img id="imgPerfilVisivel" src="${user.fotoUrl || '/imagens/favicon1.png'}" alt="Foto" style="width: 120px; height: 120px; border-radius: 50%; object-fit: cover; border: 2px solid #ccc;">                    </div>
                    
                    <label>Username</label>
                    <input type="text" value="${user.username}" disabled title="O username não pode ser alterado"><br><br>

                    <label>Primeiro Nome</label>
                    <input id="perfilPrimeiroNome" type="text" value="${user.primeiroNome || ''}"><br><br>

                    <label>Último Nome</label>
                    <input id="perfilUltimoNome" type="text" value="${user.ultimoNome || ''}"><br><br>

                    <label>Email</label>
                    <input id="perfilEmail" type="email" value="${user.email || ''}"><br><br>

                    <label>Telefone</label>
                    <input id="perfilTelefone" type="text" value="${user.telefone || ''}"><br><br>

                    <label>URL da Foto de Perfil</label>
                    <input id="perfilFotoUrl" type="text" value="${user.fotoUrl || ''}"><br><br>

                    <hr style="margin: 20px 0;">

                    <label><strong>Password Atual (Obrigatória para guardar)</strong></label>
                    <input id="perfilPassAtual" type="password" required style="border: 2px solid #0056b3;"><br><br>

                    <label>Nova Password (Opcional - deixa em branco para manter a mesma)</label>
                    <input id="perfilPassNova" type="password"><br><br>

                    <div style="display: flex; gap: 10px; justify-content: center;">
                        <button class="btn" onclick="guardarPerfil()"><i class="fa-solid fa-floppy-disk"></i> Guardar Alterações</button>
                    </div>
                </div>
            `;
        }
    } catch (error) {
        console.error("Erro na ligação ao servidor:", error);
    }
}

async function guardarPerfil() {
    const username = sessionStorage.getItem("username");
    const passAtual = document.getElementById("perfilPassAtual").value;

    // Validação de segurança no Frontend
    if (!passAtual) {
        alert("Por favor, insere a tua Password Atual para confirmar as alterações.");
        return;
    }

    const dadosAtualizados = {
        username: username,
        primeiroNome: document.getElementById("perfilPrimeiroNome").value.trim(),
        ultimoNome: document.getElementById("perfilUltimoNome").value.trim(),
        email: document.getElementById("perfilEmail").value.trim(),
        telefone: document.getElementById("perfilTelefone").value.trim(),
        fotoUrl: document.getElementById("perfilFotoUrl").value.trim(),
        password: document.getElementById("perfilPassNova").value // Vai vazio se o user não quiser mudar
    };

    try {
        // Atenção: O teu Backend está a usar POST para atualizar o perfil
        const response = await fetch(`http://localhost:8080/backend-p2-1.0-SNAPSHOT/rest/users/${username}`, {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
                'username': username,      // Header exigido pelo teu UserService
                'password': passAtual      // Header exigido pelo teu UserService
            },
            body: JSON.stringify(dadosAtualizados)
        });

        if (response.ok) {
            alert("Perfil atualizado com sucesso!");
            
            const novaUrl = document.getElementById("perfilFotoUrl").value.trim();
            // Atualiza o "src" da imagem imediatamente! (usa o ícone padrão se o URL estiver vazio)
            document.getElementById("imgPerfilVisivel").src = novaUrl || '/imagens/favicon1.png';

            // Atualiza a memória e o cabeçalho (que já tinhas)
            sessionStorage.setItem("userFirstName", dadosAtualizados.primeiroNome);
            loadHeader(); 
            
        } else {
            alert("Erro: " + await response.text());
        }
    } catch (error) {
        console.error("Erro:", error);
    }
}


// as funções seguintes serão terminadas em projetos futuros
function loadProjetos() {

    if (window.location.hash !== "#projetos") {
        window.location.hash = "#projetos";
    }

    content.innerHTML = `
    <h2>Projetos</h2>
    <p>Funcionalidade futura</p>
    `;
}

function loadTarefas() {

    if (window.location.hash !== "#tarefas") {
        window.location.hash = "#tarefas";
    }

    content.innerHTML = `
    <h2>Tarefas</h2>
    <p>Funcionalidade futura</p>
    `;
}

function loadDashboardHome() {
    content.innerHTML = `
        <section class="dashboard-home">
            <h2>Bem-vindo ao CRM</h2>
        </section>
    `;
}

// Função para inicializar a aplicação

// Função que decide o que desenhar no ecrã com base na URL
function roteador() {
    const hash = window.location.hash;

    if (hash === "#clientes") {
        carregarClientes(); // Chama diretamente a função do clientes.js
    } else if (hash === "#perfil") {
        verPerfil();
    } else if (hash === "#leads") {
        loadLeads();
    } else if (hash === "#projetos") {
        loadProjetos();
    } else if (hash === "#tarefas") {
        loadTarefas();
    } else {
        loadDashboardHome(); // Se não houver hash ou for desconhecido, mostra o Bem-vindo
    }
}

// Inicializador
window.onload = function() {
    const user = localStorage.getItem("currentUser"); //
    const path = window.location.pathname; //

    if (!user && !path.includes("login.html") && !path.includes("register.html")) { //
        window.location.href = "login.html"; //
        return; 
    }

    loadHeader(); //
    loadFooter(); //

    if (path.includes("dashboard")) { //
        roteador(); // Lê a URL e decide para onde ir!
    }
};

// MAGIA: Faz os botões de "Recuar" e "Avançar" do próprio navegador funcionarem!
window.addEventListener("hashchange", roteador);



