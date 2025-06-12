// --- Funciones Generales ---
// Variable global para el producto seleccionado
let productoSeleccionadoPrecio = null;
// Variable global para el producto en edición
let productoEnEdicion = null;
// Actualiza la fecha y hora en tiempo real en la interfaz.
function updateDateTime() {
  const now = new Date();

  // Opciones de formato para la hora (ej. 04:00 PM).
  const timeOptions = {
    hour: "2-digit",
    minute: "2-digit",
    hour12: true,
  };
  const timeString = now.toLocaleTimeString("es-ES", timeOptions).toUpperCase();

  // Opciones de formato para la fecha (ej. 12 DE MAYO 2025).
  const dateOptions = {
    day: "2-digit",
    month: "long",
    year: "numeric",
  };
  const dateString = now.toLocaleDateString("es-ES", dateOptions).toUpperCase();

  // Actualiza los elementos HTML con la hora y fecha actuales.
  const timeElements = document.querySelectorAll("#currentTime");
  const dateElements = document.querySelectorAll("#currentDate");

  timeElements.forEach((element) => {
    if (element) element.textContent = timeString;
  });

  dateElements.forEach((element) => {
    if (element) element.textContent = dateString;
  });
}

/**
 * Alterna el estado del sidebar (colapsado/expandido en desktop, mostrar/ocultar en móvil).
 * Si se invoca sin argumentos, alterna el estado. Si se fuerza, establece el estado.
 * @param {boolean|null} forceCollapse - Si es true, fuerza el colapso; si es false, fuerza la expansión.
 * Si es null, alterna el estado actual.
 */
function toggleSidebar(forceCollapse = null) {
  const sidebar = document.getElementById("sidebar");
  const dashboardContainer = document.querySelector(".dashboard-container");
  const toggleBtnIcon = sidebar ? sidebar.querySelector(".sidebar-toggle-btn i") : null;

  if (!sidebar || !dashboardContainer || !toggleBtnIcon) {
    console.error("Sidebar, dashboardContainer, or toggle button icon not found.");
    return;
  }

  // Comportamiento para móvil (sidebar se oculta/muestra completamente)
  if (window.innerWidth <= 991) { // Usamos 991px como breakpoint para móvil/tablet
    if (forceCollapse === true) {
      sidebar.classList.remove("show");
    } else if (forceCollapse === false) {
      sidebar.classList.add("show");
    } else {
      sidebar.classList.toggle("show");
    }

    // Gestiona el listener de clic fuera solo para el estado "show" en móvil
    if (sidebar.classList.contains("show")) {
      document.addEventListener("click", closeSidebarOnOutsideClick);
    } else {
      document.removeEventListener("click", closeSidebarOnOutsideClick);
    }
  } else {
    // Comportamiento para desktop (sidebar se colapsa/expande a 70px)
    let isCollapsed;
    if (forceCollapse === true) {
      isCollapsed = true;
    } else if (forceCollapse === false) {
      isCollapsed = false;
    } else {
      isCollapsed = sidebar.classList.contains("collapsed");
      isCollapsed = !isCollapsed; // Alternar
    }

    if (isCollapsed) {
      sidebar.classList.add("collapsed");
      dashboardContainer.classList.add("sidebar-collapsed"); // Agrega la clase al contenedor principal
      toggleBtnIcon.classList.remove("bi-chevron-left");
      toggleBtnIcon.classList.add("bi-chevron-right");
    } else {
      sidebar.classList.remove("collapsed");
      dashboardContainer.classList.remove("sidebar-collapsed"); // Remueve la clase del contenedor principal
      toggleBtnIcon.classList.remove("bi-chevron-right");
      toggleBtnIcon.classList.add("bi-chevron-left");
    }
  }
}

/**
 * Cierra el sidebar al hacer clic fuera de él.
 * Esto se aplica principalmente para desktop (colapsar) y móvil (ocultar) cuando
 * el sidebar está visible y el clic no proviene del sidebar mismo.
 * @param {Event} event - El evento de clic.
 */
function closeSidebarOnOutsideClick(event) {
  const sidebar = document.getElementById("sidebar");
  const toggleButton = document.querySelector(".sidebar-toggle-btn");

  if (!sidebar || !toggleButton) return;

  // Si el clic no fue dentro del sidebar Y el clic no fue en el botón de toggle
  if (!sidebar.contains(event.target) && !toggleButton.contains(event.target)) {
    // En desktop, si el sidebar no está ya colapsado, lo colapsa.
    if (window.innerWidth > 991 && !sidebar.classList.contains("collapsed")) {
      toggleSidebar(true);
    }
    // En móvil, si el sidebar está "show" (visible), lo oculta.
    else if (window.innerWidth <= 991 && sidebar.classList.contains("show")) {
      toggleSidebar(true);
    }
  }
}

// Función para cerrar sesión (muestra una confirmación al usuario).
function logout() {
  // NOTA: Reemplazar 'confirm' con un modal personalizado para mejor UX/UI.
  if (window.confirm("¿Está seguro que desea cerrar sesión?")) {
    window.location.href = "/";
  }
}

// Función para aplicar una animación de "fade in up" a un elemento.
function fadeInUp(element) {
  element.style.opacity = "0";
  element.style.transform = "translateY(30px)";
  element.style.transition = "all 0.5s ease";

  setTimeout(() => {
    element.style.opacity = "1";
    element.style.transform = "translateY(0)";
  }, 100);
}

// Opciones para el Intersection Observer (para detectar cuándo los elementos entran en la vista).
const observerOptions = {
  threshold: 0.1,
  rootMargin: "0px 0px -50px 0px",
};

// Crea un Intersection Observer para aplicar animaciones cuando los elementos son visibles.
const observer = new IntersectionObserver((entries) => {
  entries.forEach((entry) => {
    if (entry.isIntersecting) {
      entry.target.classList.add("fade-in-up");
      observer.unobserve(entry.target);
    }
  });
}, observerOptions);


// --- Funciones de Tema (Modo Claro/Oscuro) ---
function toggleTheme() {
    const body = document.body;
    body.classList.toggle('dark-mode');
    // Guarda la preferencia del usuario en localStorage
    if (body.classList.contains('dark-mode')) {
        localStorage.setItem('theme', 'dark');
    } else {
        localStorage.setItem('theme', 'light');
    }
}

function loadTheme() {
    const savedTheme = localStorage.getItem('theme');
    const themeToggle = document.getElementById('themeToggle');
    const body = document.body; // Obtener referencia al cuerpo

    // Siempre comenzar asumiendo el modo claro
    body.classList.remove('dark-mode');
    if (themeToggle) {
        themeToggle.checked = false; // Asegurar que el toggle esté desmarcado por defecto
    }

    // Si hay una preferencia guardada y es 'dark', aplicar modo oscuro
    if (savedTheme === 'dark') {
        body.classList.add('dark-mode');
        if (themeToggle) {
            themeToggle.checked = true;
        }
    }

    // Adjuntar el event listener al botón de alternancia
    if (themeToggle) {
        themeToggle.addEventListener('change', toggleTheme);
    }
}


let usuarioEditandoId = null;

// Cargar empleados desde el backend
async function cargarEmpleados() {
    try {
        const response = await fetch('/api/empleados');
        if (!response.ok) throw new Error('Error al obtener empleados');

        const empleados = await response.json();
        const tbody = document.getElementById('usersTableBody');
        tbody.innerHTML = '';

        empleados.forEach(emp => {
            const row = `
                <tr>
                    <td><strong>${emp.nombreUsuario}</strong></td>
                    <td>${emp.correoElectronico}</td>
                    <td><span class="role-badge role-${emp.rol.toLowerCase()}">${emp.rol}</span></td>
                    <td><span class="status-badge status-${emp.estado.toLowerCase()}">${emp.estado}</span></td>
                    <td>
                        <div class="action-buttons">
                            <button class="btn btn-edit" onclick="editarEmpleado('${emp.id}', '${emp.nombreUsuario}', '${emp.correoElectronico}', '${emp.rol}', '${emp.estado}')" title="Editar">
                                <i class="bi bi-pencil"></i>
                            </button>
                            <button class="btn btn-delete" onclick="eliminarEmpleado('${emp.id}')" title="Eliminar">
                                <i class="bi bi-trash"></i>
                            </button>
                        </div>
                    </td>
                </tr>`;
            tbody.innerHTML += row;
        });
    } catch (error) {
        console.error('Error:', error);
    }
}

// Editar empleado - abre el modal con los datos
function editarEmpleado(id, nombre, correo, rol, estado) {
    usuarioEditandoId = id;
    document.getElementById('newUserModalLabel').textContent = 'Editar Usuario';
    document.getElementById('userName').value = nombre;
    document.getElementById('userEmail').value = correo;
    document.getElementById('userPassword').value = '';
    document.getElementById('userRole').value = rol;
    document.getElementById('userStatus').value = estado;

    const modal = new bootstrap.Modal(document.getElementById('newUserModal'));
    modal.show();
}

// Guardar nuevo o editar usuario
async function saveUser() {
    const nombre = document.getElementById('userName').value.trim();
    const correo = document.getElementById('userEmail').value.trim();
    const contrasena = document.getElementById('userPassword').value.trim();
    const rol = document.getElementById('userRole').value;
    const estado = document.getElementById('userStatus').value;

    if (!nombre || !correo || !rol || !estado) {
        alert("Por favor complete todos los campos obligatorios.");
        return;
    }

    const dto = {
        nombreUsuario: nombre,
        correoElectronico: correo,
        contrasena: contrasena || undefined,
        rol: rol,
        activo: estado === "ACTIVO"
    };

    const esEdicion = !!usuarioEditandoId;
    const url = esEdicion ? `/api/empleados/${usuarioEditandoId}` : '/api/empleados';
    const method = esEdicion ? 'PUT' : 'POST';

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dto)
        });

        if (response.ok) {
            alert(esEdicion ? 'Empleado actualizado exitosamente.' : 'Empleado creado exitosamente.');
            bootstrap.Modal.getInstance(document.getElementById('newUserModal')).hide();
            cargarEmpleados();
            usuarioEditandoId = null;
            document.getElementById('userForm').reset();
            document.getElementById('newUserModalLabel').textContent = 'Nuevo Usuario';
        } else {
            const errorData = await response.json();
            alert(`Error: ${errorData.message || 'No se pudo guardar el empleado.'}`);
        }
    } catch (error) {
        console.error('Error al guardar empleado:', error);
        alert('Hubo un problema al conectarse con el servidor.');
    }
}

// Eliminar empleado
async function eliminarEmpleado(id) {
    if (!confirm("¿Está seguro de eliminar este empleado?")) return;

    try {
        const response = await fetch(`/api/empleados/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            alert('Empleado eliminado correctamente.');
            cargarEmpleados(); // Recargar lista
        } else {
            alert('No se pudo eliminar el empleado.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Hubo un problema al conectarse con el servidor.');
    }
}

// Asignar funciones globales para acceso desde HTML
window.editarEmpleado = editarEmpleado;
window.eliminarEmpleado = eliminarEmpleado;
window.saveUser = saveUser;

// Variables globales para productos (NUEVAS)
let productosData = [];
let insumosDisponibles = [];
let tiposCantidadDisponibles = [];
let currentPlatoEditando = null;

// Configuración de la API (NUEVA)
const API_BASE_URL = 'http://localhost:8080';
const PRODUCTO_API_URL = `${API_BASE_URL}/api/producto`;
const INSUMO_API_URL = `${API_BASE_URL}/api/insumo`;
const TIPO_CANTIDAD_API_URL = `${API_BASE_URL}/api/tipo-cantidad`;
// --- Funciones de Gestión de Platos (desde platos.html) ---
let currentPlatoInsumos = [];

// Mapa de insumos a unidades
const unitMap = {
    "Arroz": "kg",
    "Cebolla": "unidad",
    "Tomate": "kg",
    "Pollo": "kg",
    "Carne": "kg",
    "Pescado": "kg",
    "Aceite": "Lt",
    "Sal": "g",
    "Pimienta": "g",
    // Puedes añadir más insumos y sus unidades aquí
};

// REEMPLAZAR la función clearPlatoForm existente con esta versión mejorada:
function clearPlatoForm() {
    // Verificar si estamos en modo edición
    if (productoEnEdicion) {
        if (confirm('¿Está seguro de cancelar la edición? Se perderán los cambios no guardados.')) {
            cancelarEdicion();
        }
        return;
    }

    // Limpiar formulario normal (modo creación)
    limpiarFormularioCreacion();
}
// Función para actualizar información de ayuda
function actualizarInfoAyuda(modo) {
    const helpInfo = document.getElementById('form-help-info');
    if (!helpInfo) return;

    if (modo === 'editar') {
        helpInfo.innerHTML = `
            <small>
                <i class="bi bi-pencil-square me-1"></i>
                <strong>Modo Editar:</strong>
                <br>• Modifique el nombre del producto si es necesario
                <br>• El precio no se puede cambiar aquí (use "Cambiar Precio")
                <br>• Agregue, elimine o modifique los insumos
                <br>• Haga clic en "Actualizar" para guardar los cambios
            </small>
        `;
        helpInfo.className = 'alert alert-warning py-2';
    } else {
        helpInfo.innerHTML = `
            <small>
                <i class="bi bi-lightbulb me-1"></i>
                <strong>Modo Crear:</strong>
                <br>• Complete el nombre y precio del producto
                <br>• Agregue los insumos necesarios
                <br>• Haga clic en "Guardar" para crear el producto
            </small>
        `;
        helpInfo.className = 'alert alert-info py-2';
    }
}
function editPlato(nombre, precio) {
    document.getElementById('nombrePlato').value = nombre;
    const precioNumerico = parseFloat(precio.replace('S/. ', '')) || 0.00;
    document.getElementById('precioPlato').value = precioNumerico.toFixed(2);

    // Simular carga de insumos para el plato (en un caso real, esto vendría de una DB)
    if (nombre === 'PLATO 1') {
        currentPlatoInsumos = [{type: 'Arroz', quantity: 0.5, unit: 'kg'}, {type: 'Pollo', quantity: 0.2, unit: 'kg'}];
    } else if (nombre === 'PLATO 2') {
        currentPlatoInsumos = [{type: 'Cebolla', quantity: 1, unit: 'unidad'}, {type: 'Tomate', quantity: 2, unit: 'unidad'}];
    } else {
        currentPlatoInsumos = [];
    }
    updateInsumosTablePlatos();
}

/**
 * Funciones para el Historial de Precios (desde platos.html)
 */
function showPriceHistoryModal(productName) {
    document.getElementById('productNameHistory').textContent = productName;
    const priceHistoryTableBody = document.getElementById('priceHistoryTableBody');
    priceHistoryTableBody.innerHTML = '';

    // Simular historial de precios
    const historyData = {
        "PLATO 1": [
            {date: "2024-01-15", price: "S/. 12.00"},
            {date: "2024-03-01", price: "S/. 13.50"},
            {date: "2024-05-20", price: "S/. 15.00"}
        ],
        "PLATO 2": [
            {date: "2023-11-10", price: "S/. 18.00"},
            {date: "2024-02-01", price: "S/. 19.50"},
            {date: "2024-04-15", price: "S/. 20.50"}
        ],
        "PLATO 3": [
            {date: "2023-10-01", price: "S/. 10.00"},
            {date: "2024-01-05", price: "S/. 11.50"},
            {date: "2024-03-10", price: "S/. 12.00"}
        ],
        "PLATO 4": [
            {date: "2024-01-20", price: "S/. 22.00"},
            {date: "2024-04-01", price: "S/. 25.00"}
        ],
        "PLATO 5": [
            {date: "2023-12-01", price: "S/. 17.00"},
            {date: "2024-03-15", price: "S/. 18.75"}
        ],
        "PLATO 6": [
            {date: "2024-02-10", price: "S/. 28.00"},
            {date: "2024-05-01", price: "S/. 30.00"}
        ],
        "PLATO 7": [
            {date: "2023-09-01", price: "S/. 9.00"},
            {date: "2024-01-01", price: "S/. 10.00"}
        ]
    };

    const productHistory = historyData[productName] || [];
    if (productHistory.length > 0) {
        productHistory.forEach(entry => {
            const row = document.createElement('tr');
            row.innerHTML = `<td>${entry.date}</td><td>${entry.price}</td>`;
            priceHistoryTableBody.appendChild(row);
        });
    } else {
        const row = document.createElement('tr');
        row.innerHTML = `<td colspan="2" class="text-center text-muted">No hay historial de precios disponible.</td>`;
        priceHistoryTableBody.appendChild(row);
    }

    const modal = new bootstrap.Modal(document.getElementById('priceHistoryModal'));
    modal.show();
}
// Función para eliminar insumo directamente
function eliminarInsumoDirecto(index) {
    if (index >= 0 && index < currentPlatoInsumos.length) {
        const insumo = currentPlatoInsumos[index];

        if (confirm(`¿Está seguro de eliminar "${insumo.type}"?`)) {
            currentPlatoInsumos.splice(index, 1);
            updateInsumosTablePlatos();

            // Limpiar selección
            selectedIngredientRow = null;

            console.log(`🗑️ Insumo eliminado: ${insumo.type}`);
        }
    }
}

// Función para limpiar todos los insumos
function limpiarTodosInsumos() {
    if (currentPlatoInsumos.length === 0) {
        mostrarInfo('No hay insumos para limpiar');
        return;
    }

    if (confirm(`¿Está seguro de eliminar todos los ${currentPlatoInsumos.length} insumos?`)) {
        currentPlatoInsumos = [];
        updateInsumosTablePlatos();
        selectedIngredientRow = null;
        console.log('🧹 Todos los insumos eliminados');
    }
}
/**
 * Funciones para la Gestión de Insumos (para el form de platos, en platos.html)
 */
function updateInsumosTablePlatos() {
    const insumosTableBody = document.getElementById('insumosTableBody');
    const contador = document.getElementById('insumos-counter');

    if (!insumosTableBody) return;

    insumosTableBody.innerHTML = '';

    // Actualizar contador
    if (contador) {
        contador.textContent = `${currentPlatoInsumos.length} insumo${currentPlatoInsumos.length !== 1 ? 's' : ''}`;
        contador.className = `badge ${currentPlatoInsumos.length > 0 ? 'bg-success' : 'bg-secondary'}`;
    }

    if (currentPlatoInsumos.length === 0) {
        insumosTableBody.innerHTML = `
            <tr>
                <td colspan="4" class="text-center text-muted py-3">
                    <i class="bi bi-info-circle"></i> No hay insumos agregados
                </td>
            </tr>
        `;
        return;
    }

    currentPlatoInsumos.forEach((insumo, index) => {
        const row = document.createElement('tr');
        row.dataset.index = index;
        row.style.cursor = 'pointer';

        // Event listeners para selección
        row.addEventListener('click', () => selectIngredientRow(row));
        row.addEventListener('mouseenter', () => {
            if (!row.classList.contains('table-active')) {
                row.style.backgroundColor = '#f8f9fa';
            }
        });
        row.addEventListener('mouseleave', () => {
            if (!row.classList.contains('table-active')) {
                row.style.backgroundColor = '';
            }
        });

        row.innerHTML = `
            <td>
                <strong>${insumo.type}</strong>
                ${insumo.idInsumo ? `<br><small class="text-muted">ID: ${insumo.idInsumo}</small>` : ''}
            </td>
            <td class="text-center">
                <span class="badge bg-primary">${insumo.quantity}</span>
            </td>
            <td class="text-center">
                <span class="text-muted">${insumo.unit}</span>
            </td>
            <td class="text-center">
                <button type="button" class="btn btn-sm btn-outline-danger"
                        onclick="event.stopPropagation(); eliminarInsumoDirecto(${index})"
                        title="Eliminar insumo">
                    <i class="bi bi-trash"></i>
                </button>
            </td>
        `;

        insumosTableBody.appendChild(row);
    });

    console.log(`📊 Tabla de insumos actualizada: ${currentPlatoInsumos.length} insumos`);
}

let selectedIngredientRow = null;

function selectIngredientRow(row) {
    if (selectedIngredientRow) {
        selectedIngredientRow.classList.remove('table-active');
    }
    selectedIngredientRow = row;
    row.classList.add('table-active');
}

// Rellena el select de tipos de insumo
function populateIngredientTypes() {
    const ingredientTypeSelect = document.getElementById('ingredientType');
    if (!ingredientTypeSelect) return;

    ingredientTypeSelect.innerHTML = '<option value="">Seleccionar...</option>';

    // Si tenemos insumos de la API, usarlos
    if (insumosDisponibles && insumosDisponibles.length > 0) {
        insumosDisponibles.forEach(insumo => {
            const option = document.createElement('option');
            option.value = insumo.idInsumo;
            option.textContent = insumo.nomInsumo;
            option.dataset.unidad = insumo.nomTipoCantidad;
            option.dataset.idTipoCantidad = insumo.idTipoCantidad;
            ingredientTypeSelect.appendChild(option);
        });
    } else {
        // Fallback al mapa estático si no hay datos de API
        for (const type in unitMap) {
            const option = document.createElement('option');
            option.value = type;
            option.textContent = type;
            option.dataset.unidad = unitMap[type];
            ingredientTypeSelect.appendChild(option);
        }
    }

    console.log('✅ Select de insumos poblado');
}

// Actualiza el span de la unidad de insumo al seleccionar un tipo
function updateDisplayIngredientUnit() {
    const ingredientTypeSelect = document.getElementById('ingredientType');
    const displayUnitSpan = document.getElementById('displayIngredientUnit');
    if (!ingredientTypeSelect || !displayUnitSpan) return;

    const selectedOption = ingredientTypeSelect.options[ingredientTypeSelect.selectedIndex];

    if (selectedOption && selectedOption.value) {
        // Si es de la API, usar dataset.unidad
        if (selectedOption.dataset.unidad) {
            displayUnitSpan.textContent = selectedOption.dataset.unidad;
        } else {
            // Fallback al mapa estático
            displayUnitSpan.textContent = unitMap[selectedOption.value] || '';
        }
    } else {
        displayUnitSpan.textContent = 'Seleccione un insumo primero';
    }
}

// ===================== FUNCIÓN PARA VALIDAR EN TIEMPO REAL =====================
function configurarValidacionTiempoReal() {
    // Validación del nombre
    const nombreInput = document.getElementById('nombrePlato');
    if (nombreInput) {
        nombreInput.addEventListener('input', function() {
            const nombre = this.value.trim();

            this.classList.remove('is-valid', 'is-invalid');

            if (nombre.length === 0) {
                return; // No mostrar validación si está vacío
            } else if (nombre.length < 2) {
                this.classList.add('is-invalid');
            } else if (nombre.length > 100) {
                this.classList.add('is-invalid');
            } else if (productosData.some(p => p.nomProducto.toLowerCase() === nombre.toLowerCase())) {
                this.classList.add('is-invalid');
            } else {
                this.classList.add('is-valid');
            }
        });
    }

    // Validación del precio
    const precioInput = document.getElementById('precioPlato');
    if (precioInput) {
        precioInput.addEventListener('input', function() {
            if (this.disabled) return; // No validar si está deshabilitado (modo edición)

            const precio = parseFloat(this.value);

            this.classList.remove('is-valid', 'is-invalid');

            if (isNaN(precio)) {
                return; // No mostrar validación si no es número
            } else if (precio <= 0) {
                this.classList.add('is-invalid');
            } else if (precio > 9999.99) {
                this.classList.add('is-invalid');
            } else {
                this.classList.add('is-valid');
            }
        });
    }

    console.log('⚙️ Validación en tiempo real configurada');
}
// ===================== FUNCIÓN DE DEBUGGING PARA CREACIÓN =====================
window.debugCreacion = function() {
    console.log('🐛 DEBUG CREACIÓN:');
    console.log('1. Nombre:', document.getElementById('nombrePlato').value);
    console.log('2. Precio:', document.getElementById('precioPlato').value);
    console.log('3. Insumos:', currentPlatoInsumos);
    console.log('4. Validación:', validarFormularioCreacion());
    console.log('5. Prerequisites:', verificarPrerequisitosCreacion());

    return {
        nombre: document.getElementById('nombrePlato').value,
        precio: document.getElementById('precioPlato').value,
        insumos: currentPlatoInsumos,
        errores: validarFormularioCreacion(),
        prerequisites: verificarPrerequisitosCreacion()
    };
};
// ===================== FUNCIÓN PARA VERIFICAR PREREQUISITES =====================
function verificarPrerequisitosCreacion() {
    const errores = [];

    if (insumosDisponibles.length === 0) {
        errores.push('No se han cargado los insumos disponibles');
    }

    if (tiposCantidadDisponibles.length === 0) {
        errores.push('No se han cargado los tipos de cantidad');
    }

    if (errores.length > 0) {
        mostrarError('No se puede crear productos:\n' + errores.join('\n') + '\n\nRefresque la página e intente nuevamente.');
        return false;
    }

    return true;
}



// ===================== FUNCIÓN MEJORADA PARA AGREGAR INSUMOS =====================
// Reemplazar la función saveIngredient existente con validaciones mejoradas
function saveIngredient() {
    const typeSelect = document.getElementById('ingredientType');
    const quantity = parseFloat(document.getElementById('ingredientQuantity').value);

    // Validaciones básicas
    if (!typeSelect.value) {
        mostrarError('Por favor, seleccione un tipo de insumo');
        return;
    }

    if (isNaN(quantity) || quantity <= 0) {
        mostrarError('Por favor, ingrese una cantidad válida mayor a 0');
        return;
    }

    const selectedOption = typeSelect.options[typeSelect.selectedIndex];

    // Verificar si el insumo ya existe
    const insumoExistente = currentPlatoInsumos.find(insumo =>
        insumo.idInsumo && parseInt(insumo.idInsumo) === parseInt(typeSelect.value)
    );

    if (insumoExistente) {
        // Preguntar si quiere actualizar la cantidad
        const confirmar = confirm(
            `El insumo "${selectedOption.textContent}" ya está agregado.\n\n` +
            `Cantidad actual: ${insumoExistente.quantity} ${insumoExistente.unit}\n` +
            `Nueva cantidad: ${quantity} ${selectedOption.dataset.unidad || 'unidad'}\n\n` +
            `¿Desea actualizar la cantidad?`
        );

        if (confirmar) {
            insumoExistente.quantity = quantity;
            insumoExistente.unit = selectedOption.dataset.unidad || unitMap[typeSelect.value] || 'unidad';
            updateInsumosTablePlatos();
            console.log(`✅ Cantidad actualizada para: ${insumoExistente.type}`);
        }
    } else {
        // Agregar nuevo insumo
        const insumo = {
            type: selectedOption.textContent,
            quantity: quantity,
            unit: selectedOption.dataset.unidad || unitMap[typeSelect.value] || 'unidad'
        };

        // Si es de la API, agregar IDs (obligatorio para creación)
        if (selectedOption.dataset.unidad) {
            insumo.idInsumo = parseInt(typeSelect.value);
            insumo.idTipoCantidad = parseInt(selectedOption.dataset.idTipoCantidad);
        } else {
            // Para insumos del fallback (unitMap), necesitamos encontrar los IDs
            mostrarError('No se pueden agregar insumos sin conexión a la base de datos. Refresque la página.');
            return;
        }

        currentPlatoInsumos.push(insumo);
        updateInsumosTablePlatos();
        console.log(`✅ Insumo agregado: ${insumo.type} - ${insumo.quantity} ${insumo.unit}`);
    }

    // Cerrar modal y limpiar formulario
    const modal = bootstrap.Modal.getInstance(document.getElementById('addIngredientModal'));
    modal.hide();
    document.getElementById('ingredientForm').reset();
    updateDisplayIngredientUnit();

    console.log(`📊 Total de insumos: ${currentPlatoInsumos.length}`);
}
function mostrarInfoEdicion(producto) {
    // Crear o actualizar banner de información
    let infoBanner = document.getElementById('edicion-info-banner');

    if (!infoBanner) {
        infoBanner = document.createElement('div');
        infoBanner.id = 'edicion-info-banner';
        infoBanner.className = 'alert alert-warning d-flex align-items-center mb-3';

        // Insertar antes del formulario
        const form = document.getElementById('platoForm');
        if (form && form.parentNode) {
            form.parentNode.insertBefore(infoBanner, form);
        }
    }

    infoBanner.innerHTML = `
        <i class="bi bi-pencil-square me-2"></i>
        <div>
            <strong>Editando:</strong> ${producto.nomProducto}<br>
            <small>
                ID: ${producto.idProducto} •
                Precio actual: S/. ${(producto.precioActual || 0).toFixed(2)} •
                ${producto.insumos ? producto.insumos.length : 0} insumo(s)
            </small>
        </div>
        <button type="button" class="btn btn-sm btn-outline-secondary ms-auto" onclick="cancelarEdicion()">
            <i class="bi bi-x"></i> Cancelar
        </button>
    `;
}

// ===================== FUNCIONES AUXILIARES DE ACTUALIZACIÓN =====================
async function actualizarNombreProducto(idProducto, nuevoNombre) {
    const response = await fetch(`${PRODUCTO_API_URL}/actualizar-nombre`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            idProducto: idProducto,
            nomProducto: nuevoNombre
        })
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.mensaje || 'Error al actualizar nombre');
    }

    return await response.json();
}
async function actualizarInsumosProducto(idProducto, insumos) {
    // Convertir insumos del formulario al formato de la API
    const insumosAPI = insumos.map(insumo => ({
        idInsumo: insumo.idInsumo,
        cantPorInsumo: insumo.quantity,
        idTipoCantidad: insumo.idTipoCantidad
    }));

    const response = await fetch(`${PRODUCTO_API_URL}/actualizar-insumos`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            idProducto: idProducto,
            insumos: insumosAPI
        })
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.mensaje || 'Error al actualizar insumos');
    }

    return await response.json();
}
// ===================== FUNCIÓN PARA ACTUALIZAR PRODUCTO =====================
async function actualizarProducto() {
    try {
        console.log('💾 Iniciando actualización del producto...');

        if (!productoEnEdicion) {
            throw new Error('No hay producto en edición');
        }

        // Validaciones
        const errores = validarFormularioEdicion();
        if (errores.length > 0) {
            mostrarError('Errores de validación:\n' + errores.join('\n'));
            return;
        }

        const nombreActual = document.getElementById('nombrePlato').value.trim();
        const nombreOriginal = productoEnEdicion.nomProducto;

        // Mostrar indicador de carga
        const submitBtn = document.querySelector('#platoForm button[type="submit"]');
        const textoOriginal = submitBtn.innerHTML;
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Actualizando...';

        let actualizacionExitosa = false;

        try {
            // 1. Actualizar nombre si cambió
            if (nombreActual !== nombreOriginal) {
                console.log('📝 Actualizando nombre del producto...');
                await actualizarNombreProducto(productoEnEdicion.idProducto, nombreActual);
                console.log('✅ Nombre actualizado');
            }

            // 2. Actualizar insumos
            console.log('🥬 Actualizando insumos del producto...');
            await actualizarInsumosProducto(productoEnEdicion.idProducto, currentPlatoInsumos);
            console.log('✅ Insumos actualizados');

            actualizacionExitosa = true;

        } catch (error) {
            throw error;
        }

        if (actualizacionExitosa) {
            mostrarExito(`Producto "${nombreActual}" actualizado exitosamente`);

            // Recargar datos de productos
            await cargarProductos();

            // Cancelar edición y limpiar formulario
            cancelarEdicion();
        }

    } catch (error) {
        console.error('❌ Error al actualizar producto:', error);
        mostrarError(`Error al actualizar el producto: ${error.message}`);

    } finally {
        // Restaurar botón
        const submitBtn = document.querySelector('#platoForm button[type="submit"]');
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.innerHTML = '<i class="bi bi-pencil-square me-2"></i>ACTUALIZAR PRODUCTO';
        }
    }
}

// FUNCIÓN PARA MOSTRAR AYUDA CONTEXTUAL:
function mostrarAyudaContextual(tipo = 'crear') {
    const helpInfo = document.getElementById('form-help-info');
    if (!helpInfo) return;

    if (tipo === 'crear') {
        helpInfo.innerHTML = `
            <small>
                <i class="bi bi-lightbulb me-1"></i>
                <strong>Creando nuevo producto:</strong>
                <br>• Complete el nombre (mínimo 2 caracteres)
                <br>• Ingrese el precio inicial
                <br>• Agregue todos los insumos necesarios
                <br>• Haga clic en "Guardar" para crear
            </small>
        `;
        helpInfo.className = 'alert alert-info py-2';
    } else if (tipo === 'validacion') {
        helpInfo.innerHTML = `
            <small>
                <i class="bi bi-info-circle me-1"></i>
                <strong>Validaciones:</strong>
                <br>• Los campos marcados en verde son válidos ✓
                <br>• Los campos marcados en rojo necesitan corrección ✗
                <br>• Debe agregar al menos un insumo
            </small>
        `;
        helpInfo.className = 'alert alert-warning py-2';
    }
}
// FUNCIÓN PARA ACTUALIZAR EL TÍTULO DE LA TARJETA SEGÚN EL MODO:
function actualizarTituloFormulario(modo = 'crear') {
    const cardHeader = document.querySelector('#platoForm .card-header h5');
    if (!cardHeader) return;

    if (modo === 'editar') {
        cardHeader.innerHTML = '<i class="bi bi-pencil-square me-2"></i>EDITAR PLATO';
        cardHeader.parentElement.className = 'card-header bg-warning text-dark text-center';
    } else {
        cardHeader.innerHTML = '<i class="bi bi-plus-circle me-2"></i>NUEVO PLATO';
        cardHeader.parentElement.className = 'card-header bg-success text-white text-center';
    }
}
// ===================== VALIDACIONES PARA CREACIÓN =====================
function validarFormularioCreacion() {
    const errores = [];

    // Validar nombre
    const nombre = document.getElementById('nombrePlato').value.trim();
    if (!nombre) {
        errores.push('El nombre del producto es obligatorio');
    } else if (nombre.length < 2) {
        errores.push('El nombre debe tener al menos 2 caracteres');
    } else if (nombre.length > 100) {
        errores.push('El nombre no puede exceder los 100 caracteres');
    }

    // Validar precio
    const precio = parseFloat(document.getElementById('precioPlato').value);
    if (isNaN(precio) || precio <= 0) {
        errores.push('El precio debe ser mayor a 0');
    } else if (precio > 9999.99) {
        errores.push('El precio no puede exceder S/. 9,999.99');
    }

    // Validar insumos
    if (currentPlatoInsumos.length === 0) {
        errores.push('Debe agregar al menos un insumo');
    } else {
        // Validar cada insumo
        currentPlatoInsumos.forEach((insumo, index) => {
            const prefijo = `Insumo ${index + 1} (${insumo.type})`;

            if (!insumo.idInsumo) {
                errores.push(`${prefijo}: falta ID del insumo`);
            }

            if (!insumo.quantity || insumo.quantity <= 0) {
                errores.push(`${prefijo}: cantidad inválida`);
            }

            if (!insumo.idTipoCantidad) {
                errores.push(`${prefijo}: falta tipo de cantidad`);
            }
        });

        // Validar que no haya insumos duplicados
        const idsInsumos = currentPlatoInsumos.map(i => i.idInsumo);
        const insumosDuplicados = idsInsumos.filter((id, index) => idsInsumos.indexOf(id) !== index);
        if (insumosDuplicados.length > 0) {
            errores.push('No puede agregar el mismo insumo más de una vez');
        }
    }

    // Validar que no exista un producto con el mismo nombre
    if (nombre && productosData.some(p => p.nomProducto.toLowerCase() === nombre.toLowerCase())) {
        errores.push('Ya existe un producto con ese nombre');
    }

    return errores;
}

// ===================== LIMPIAR FORMULARIO DESPUÉS DE CREAR =====================
function limpiarFormularioCreacion() {
    console.log('🧹 Limpiando formulario después de crear producto...');

    // Limpiar campos
    document.getElementById('nombrePlato').value = '';
    document.getElementById('precioPlato').value = '0.00';

    // Limpiar insumos
    currentPlatoInsumos = [];
    updateInsumosTablePlatos();

    // Limpiar selección de insumo
    selectedIngredientRow = null;

    // Restaurar validaciones visuales
    const inputs = document.querySelectorAll('#platoForm input');
    inputs.forEach(input => {
        input.classList.remove('is-valid', 'is-invalid');
    });

    // Limpiar modal de insumos si está abierto
    const ingredientTypeSelect = document.getElementById('ingredientType');
    if (ingredientTypeSelect) {
        ingredientTypeSelect.value = '';
        updateDisplayIngredientUnit();
    }

    // Enfocar en el campo nombre
    const nombreInput = document.getElementById('nombrePlato');
    if (nombreInput) {
        nombreInput.focus();
    }

    console.log('✅ Formulario limpiado completamente');
}


// ===================== CONSTRUIR DTO PARA LA API =====================
function construirProductoDTO(nombre, precio, insumos) {
    // Validar insumos antes de construir
    const insumosValidados = insumos.map((insumo, index) => {
        console.log(`🔍 Validando insumo ${index + 1}:`, insumo);

        if (!insumo.idInsumo) {
            throw new Error(`El insumo "${insumo.type}" no tiene ID válido`);
        }

        if (!insumo.quantity || insumo.quantity <= 0) {
            throw new Error(`El insumo "${insumo.type}" no tiene cantidad válida`);
        }

        if (!insumo.idTipoCantidad) {
            throw new Error(`El insumo "${insumo.type}" no tiene tipo de cantidad válido`);
        }

        return {
            idInsumo: parseInt(insumo.idInsumo),
            cantPorInsumo: parseFloat(insumo.quantity),
            idTipoCantidad: parseInt(insumo.idTipoCantidad)
        };
    });

    const productoDTO = {
        nomProducto: nombre,
        precioInicial: precio,
        idEmpleado: 1, // TODO: Obtener del usuario logueado
        insumos: insumosValidados
    };

    console.log('✅ DTO construido:', productoDTO);
    return productoDTO;
}
// ===================== MOSTRAR CARGA EN FORMULARIO =====================
function mostrarCargandoFormulario(mostrar) {
    const form = document.getElementById('platoForm');
    if (!form) return;

    if (mostrar) {
        // Agregar overlay de carga
        let overlay = document.getElementById('form-loading-overlay');
        if (!overlay) {
            overlay = document.createElement('div');
            overlay.id = 'form-loading-overlay';
            overlay.className = 'position-absolute w-100 h-100 d-flex align-items-center justify-content-center';
            overlay.style.cssText = `
                background: rgba(255, 255, 255, 0.8);
                top: 0;
                left: 0;
                z-index: 1000;
                border-radius: 0.375rem;
            `;
            overlay.innerHTML = `
                <div class="text-center">
                    <div class="spinner-border text-primary mb-2"></div>
                    <div>Cargando producto...</div>
                </div>
            `;

            form.style.position = 'relative';
            form.appendChild(overlay);
        }
    } else {
        // Remover overlay
        const overlay = document.getElementById('form-loading-overlay');
        if (overlay) {
            overlay.remove();
        }
    }
}
// ===================== VALIDACIONES PARA EDICIÓN =====================
function validarFormularioEdicion() {
    const errores = [];

    const nombre = document.getElementById('nombrePlato').value.trim();
    if (!nombre) {
        errores.push('El nombre del producto es obligatorio');
    }

    if (nombre.length < 2) {
        errores.push('El nombre debe tener al menos 2 caracteres');
    }

    if (currentPlatoInsumos.length === 0) {
        errores.push('El producto debe tener al menos un insumo');
    }

    // Validar que no haya insumos duplicados
    const idsInsumos = currentPlatoInsumos.map(i => i.idInsumo);
    const insumosDuplicados = idsInsumos.filter((id, index) => idsInsumos.indexOf(id) !== index);
    if (insumosDuplicados.length > 0) {
        errores.push('No puede agregar el mismo insumo más de una vez');
    }

    return errores;
}
// ===================== CANCELAR EDICIÓN =====================
function cancelarEdicion() {
    try {
        console.log('❌ Cancelando edición...');

        // Limpiar estado de edición
        productoEnEdicion = null;

        // Restaurar formulario
        clearPlatoForm();

        // Restaurar botón principal
        const submitBtn = document.querySelector('#platoForm button[type="submit"]');
        if (submitBtn) {
            submitBtn.innerHTML = '<i class="bi bi-floppy me-2"></i>GUARDAR';
            submitBtn.classList.remove('btn-warning');
            submitBtn.classList.add('btn-primary');
        }

        // Habilitar campo de precio
        const precioInput = document.getElementById('precioPlato');
        if (precioInput) {
            precioInput.disabled = false;
            precioInput.style.backgroundColor = '';
            precioInput.style.color = '';
        }

        // Remover estilo de edición del nombre
        const nombreInput = document.getElementById('nombrePlato');
        if (nombreInput) {
            nombreInput.style.borderLeft = '';
        }

        // Remover banner de información
        const infoBanner = document.getElementById('edicion-info-banner');
        if (infoBanner) {
            infoBanner.remove();
        }

        // Restaurar comportamiento original del formulario
        const form = document.getElementById('platoForm');
        if (form) {
            const newForm = form.cloneNode(true);
            form.parentNode.replaceChild(newForm, form);

            // Restaurar listener original (crear producto)
            newForm.addEventListener('submit', function(e) {
                e.preventDefault();
                // TODO: Implementar guardarProducto() después
                alert('Función de crear producto se implementará después');
            });
        }

        console.log('✅ Edición cancelada - Formulario restaurado');

    } catch (error) {
        console.error('❌ Error al cancelar edición:', error);
    }
}

function removeSelectedIngredient() {
    if (selectedIngredientRow) {
        const index = parseInt(selectedIngredientRow.dataset.index);
        currentPlatoInsumos.splice(index, 1);
        selectedIngredientRow = null;
        updateInsumosTablePlatos();
    } else {
        // NOTA: Reemplazar 'alert' con un modal personalizado.
        window.alert('Selecciona un insumo para eliminar.');
    }
}


// --- Funciones de Pedido (desde pedido.html) ---
let orderItems = [];

function addMenuItem(button) {
    const menuItem = button.closest('.menu-item');
    const name = menuItem.dataset.name;
    const price = parseFloat(menuItem.dataset.price);

    const existingItem = orderItems.find(item => item.name === name);

    if (existingItem) {
        existingItem.quantity += 1;
        existingItem.subtotal = existingItem.quantity * existingItem.price;
    } else {
        orderItems.push({
            name: name,
            quantity: 1,
            price: price,
            subtotal: price,
            comment: ''
        });
    }
    updateOrderTable();
    updateTotal();
}

function updateOrderTable() {
    const tbody = document.getElementById('orderItemsTable');
    if (!tbody) return;

    tbody.innerHTML = '';
    orderItems.forEach((item, index) => {
        const row = document.createElement('tr');
        row.onclick = () => selectRow(row, index);
        row.innerHTML = `
            <td>${item.name}</td>
            <td>${item.quantity}</td>
            <td>S/. ${item.price.toFixed(2)}</td>
            <td>S/. ${item.subtotal.toFixed(2)}</td>
            <td>${item.comment}</td>
        `;
        tbody.appendChild(row);
    });
}

let selectedRow = null;

function selectRow(row, index) {
    if (selectedRow) {
        selectedRow.classList.remove('table-active');
    }
    selectedRow = row;
    row.classList.add('table-active');
}

function removeSelectedItem() {
    if (selectedRow) {
        const index = Array.from(selectedRow.parentNode.children).indexOf(selectedRow);
        orderItems.splice(index, 1);
        updateOrderTable();
        updateTotal();
        selectedRow = null;
    } else {
        // NOTA: Reemplazar 'alert' con un modal personalizado.
        window.alert('Selecciona un item para eliminar.');
    }
}

function updateTotal() {
    const totalAmountElement = document.getElementById('totalAmount');
    if (!totalAmountElement) return;

    const total = orderItems.reduce((sum, item) => sum + item.subtotal, 0);
    totalAmountElement.textContent = `S/. ${total.toFixed(2)}`;
}

function saveOrder() {
    if (orderItems.length === 0) {
        // NOTA: Reemplazar 'alert' con un modal personalizado.
        window.alert('Debe agregar al menos un item al pedido');
        return;
    }
    // NOTA: Reemplazar 'alert' con un modal personalizado.
    window.alert('Pedido guardado exitosamente');
}


// --- Funciones de Historial de Pedidos (desde historial-pedidos.html) ---
function setupSearchInput(inputId, tableSelector) {
    const searchInput = document.getElementById(inputId);
    if (searchInput) {
        searchInput.addEventListener('input', function() {
            const searchTerm = this.value.toLowerCase();
            const rows = document.querySelectorAll(tableSelector + ' tbody tr');

            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                if (text.includes(searchTerm)) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        });
    }
}

// --- Funciones de Mesas (desde mesas.html) ---
let selectedTables = [];
let longPressTimer;
const longPressDuration = 500; // milisegundos

// Variable para controlar si un long press ha ocurrido
let isLongPressActive = false;

/**
 * Gestiona la selección de una mesa (clic normal o Ctrl+clic en PC, tap o long press en móvil).
 * @param {HTMLElement} element - El elemento DOM de la tarjeta de la mesa.
 * @param {number} tableNumber - El número de la mesa.
 * @param {Event} event - El evento (mouse o touch) que disparó la función.
 */
function toggleTableSelection(element, tableNumber, event) {
    if (isLongPressActive) {
        // Si ya estamos en modo de selección por long press, alternar la mesa
        const index = selectedTables.indexOf(tableNumber);
        if (index > -1) {
            selectedTables.splice(index, 1);
            element.classList.remove('selected');
        } else {
            selectedTables.push(tableNumber);
            element.classList.add('selected');
        }
    } else if (event.ctrlKey) { // En PC, si Ctrl está presionado
        const index = selectedTables.indexOf(tableNumber);
        if (index > -1) {
            selectedTables.splice(index, 1);
            element.classList.remove('selected');
        } else {
            selectedTables.push(tableNumber);
            element.classList.add('selected');
        }
    } else { // Clic normal en PC o tap corto en móvil
        clearSelection(); // Deseleccionar todo lo demás
        selectedTables.push(tableNumber);
        element.classList.add('selected');
    }
    updateButtons();
}

/**
 * Comienza el temporizador para detectar un "mantener presionado" (long press).
 * @param {HTMLElement} element - El elemento DOM de la tarjeta de la mesa.
 * @param {number} tableNumber - El número de la mesa.
 */
function handleTableMouseDown(element, tableNumber) {
    // Solo si es un dispositivo táctil O si no estamos en modo Ctrl (para evitar conflicto en PC)
    if ('ontouchstart' in window || !event.ctrlKey) {
        longPressTimer = setTimeout(() => {
            isLongPressActive = true;
            // Si no hay nada seleccionado, la primera pulsación larga lo selecciona
            if (selectedTables.length === 0) {
                clearSelection(); // Asegurarse de que no haya nada accidentalmente seleccionado
                selectedTables.push(tableNumber);
                element.classList.add('selected');
            } else if (selectedTables.length > 0 && !selectedTables.includes(tableNumber)) {
                 // Si ya hay selección y la actual no está, agregarla
                selectedTables.push(tableNumber);
                element.classList.add('selected');
            } else if (selectedTables.length > 0 && selectedTables.includes(tableNumber)) {
                // Si ya está seleccionada, la long press la deselecciona
                const index = selectedTables.indexOf(tableNumber);
                selectedTables.splice(index, 1);
                element.classList.remove('selected');
            }
            updateButtons();
            // NOTA: Podrías añadir un feedback visual aquí, como vibración o un borde temporal
        }, longPressDuration);
    }
}

/**
 * Finaliza el evento de clic/toque, limpiando el temporizador de long press
 * y ejecutando la selección si no fue un long press.
 * @param {HTMLElement} element - El elemento DOM de la tarjeta de la mesa.
 * @param {number} tableNumber - El número de la mesa.
 * @param {Event} event - El evento (mouse o touch) que disparó la función.
 */
function handleTableMouseUp(element, tableNumber, event) {
    clearTimeout(longPressTimer);
    if (!isLongPressActive && event.detail !== 2) { // event.detail === 2 es doble click
        toggleTableSelection(element, tableNumber, event);
    }
    // Si fue un long press, la selección ya se manejó en handleTableMouseDown
    isLongPressActive = false; // Resetear el estado de long press
}

/**
 * Cancela el long press si el mouse/dedo se mueve significativamente.
 */
function handleTableMove() {
    clearTimeout(longPressTimer);
    isLongPressActive = false; // Asegura que el modo long press se desactive si hay movimiento
}
// ===================== ACTIVAR MODO EDICIÓN =====================
function activarModoEdicion(producto) {
    try {
        // Cambiar título del botón principal
        const submitBtn = document.querySelector('#platoForm button[type="submit"]');
        if (submitBtn) {
            submitBtn.innerHTML = '<i class="bi bi-pencil-square me-2"></i>ACTUALIZAR PRODUCTO';
            submitBtn.classList.remove('btn-primary');
            submitBtn.classList.add('btn-warning');
        }

        // Agregar indicador visual de edición
        const nombreInput = document.getElementById('nombrePlato');
        if (nombreInput) {
            nombreInput.style.borderLeft = '4px solid #ffc107';
        }

        // Mostrar información del producto en edición
        mostrarInfoEdicion(producto);

        // Cambiar el comportamiento del formulario
        const form = document.getElementById('platoForm');
        if (form) {
            // Remover listener anterior si existe
            const newForm = form.cloneNode(true);
            form.parentNode.replaceChild(newForm, form);

            // Agregar nuevo listener para actualización
            newForm.addEventListener('submit', function(e) {
                e.preventDefault();
                actualizarProducto();
            });
        }

        console.log('🔄 Modo edición activado');

    } catch (error) {
        console.error('❌ Error al activar modo edición:', error);
    }
}

function updateButtons() {
    const orderBtn = document.getElementById('orderBtn');
    const joinBtn = document.getElementById('joinTablesBtn');

    if (!orderBtn || !joinBtn) return;

    if (selectedTables.length === 1) {
        orderBtn.style.display = 'inline-block';
        joinBtn.style.display = 'none';
    } else if (selectedTables.length > 1) {
        orderBtn.style.display = 'inline-block';
        joinBtn.style.display = 'inline-block';
    } else {
        orderBtn.style.display = 'none';
        joinBtn.style.display = 'none';
    }
}

function goToOrderSelected() {
    if (selectedTables.length >= 1) {
        window.location.href = `/pedido/${selectedTables[0]}`;
        clearSelection();
    }
}

function goToOrder(tableNumber) {
    window.location.href = `/pedido/${tableNumber}`;
    clearSelection();
}

function joinTables() {
    if (selectedTables.length > 1) {
        // NOTA: Reemplazar 'alert' con un modal personalizado.
        window.alert(`Uniendo mesas: ${selectedTables.join(', ')}`);
        selectedTables.forEach(tableNumber => {
            const tableElements = document.querySelectorAll('.table-card');
            tableElements.forEach(element => {
                // Asumiendo que el número de mesa está dentro de un strong para el HTML proporcionado
                // Es mejor usar un data-attribute en el HTML para esto: <div data-table-number="1">
                const elementTableNumber = parseInt(element.dataset.tableNumber); // Usar data-table-number
                if (elementTableNumber === tableNumber) {
                    element.classList.add('occupied');
                    element.classList.remove('available');
                    // Iniciar el temporizador para la mesa si es la primera vez que se ocupa
                    const timerElement = element.querySelector('.table-timer');
                    if (timerElement && timerElement.textContent === '00:00:00') {
                        startTableTimer(timerElement);
                    }
                }
            });
        });
        clearSelection();
    } else {
        // NOTA: Reemplazar 'alert' con un modal personalizado.
        window.alert("Selecciona al menos dos mesas para unir.");
    }
}

function clearSelection() {
    const selectedElements = document.querySelectorAll('.table-card.selected');
    selectedElements.forEach(element => {
        element.classList.remove('selected');
    });
    selectedTables = [];
    updateButtons();
    isLongPressActive = false; // Asegura que el modo long press se desactive
}

function startTableTimer(timerElement) {
    let seconds = 0;
    // Si ya existe un timerId, limpiarlo para evitar múltiples intervalos
    if (timerElement.dataset.timerId) {
        clearInterval(parseInt(timerElement.dataset.timerId));
    }
    // Si la mesa ya estaba ocupada, parsear el tiempo existente
    if (timerElement.closest('.table-card').classList.contains('occupied')) {
        const currentTimeParts = timerElement.textContent.split(':');
        if (currentTimeParts.length === 3) {
            seconds = parseInt(currentTimeParts[0]) * 3600 +
                      parseInt(currentTimeParts[1]) * 60 +
                      parseInt(currentTimeParts[2]);
        }
    }
    const timerId = setInterval(() => {
        seconds++;
        const hours = Math.floor(seconds / 3600);
        const minutes = Math.floor((seconds % 3600) / 60);
        const secs = seconds % 60;
        timerElement.textContent = `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
    }, 1000);
    timerElement.dataset.timerId = timerId; // Guardar el ID para poder limpiarlo
}


// --- Funciones de Pago Fraccionado (desde resumen-pedido.html) ---
let splitPaymentPeople = [];

function openSplitPaymentModal() {
    const totalAmountElement = document.getElementById('totalPedidoAmount');
    let totalAmount = parseFloat(totalAmountElement.value.replace('S/. ', '')) || 0;

    document.getElementById('splitTotalAmount').value = `S/. ${totalAmount.toFixed(2)}`;
    document.getElementById('numPeopleSplit').value = '1';
    splitPaymentPeople = [];
    updateSplitPaymentRows();
}

function changeSplitPeople(change) {
    const numPeopleInput = document.getElementById('numPeopleSplit');
    let currentNum = parseInt(numPeopleInput.value) || 1;
    currentNum += change;
    if (currentNum < 1) currentNum = 1;
    numPeopleInput.value = currentNum;
    updateSplitPaymentRows();
}

function updateSplitPaymentRows() {
    const numPeople = parseInt(document.getElementById('numPeopleSplit').value);
    const splitPaymentRowsContainer = document.getElementById('splitPaymentRows');
    splitPaymentRowsContainer.innerHTML = '';

    const totalAmountElement = document.getElementById('totalPedidoAmount');
    let totalAmount = parseFloat(totalAmountElement.value.replace('S/. ', '')) || 0;
    const amountPerPerson = (totalAmount / numPeople).toFixed(2);

    splitPaymentPeople = [];

    for (let i = 0; i < numPeople; i++) {
        const rowDiv = document.createElement('div');
        rowDiv.classList.add('row', 'mb-3', 'align-items-center');
        rowDiv.innerHTML = `
            <div class="col-md-5">
                <input type="text" class="form-control" placeholder="Nombre Persona ${i + 1}" id="personName${i}">
            </div>
            <div class="col-md-5">
                <div class="input-group">
                    <span class="input-group-text">S/.</span>
                    <input type="number" class="form-control person-amount" value="${amountPerPerson}" step="0.01">
                </div>
            </div>
            <div class="col-md-2">
                <button class="btn btn-sm btn-info w-100" onclick="generateIndividualBoleta(${i})">Boleta</button>
            </div>
        `;
        splitPaymentRowsContainer.appendChild(rowDiv);
        splitPaymentPeople.push({
            name: '',
            amount: parseFloat(amountPerPerson)
        });
    }
    document.querySelectorAll('#splitPaymentRows .person-amount').forEach(input => {
        input.addEventListener('input', validateSplitAmounts);
    });
    validateSplitAmounts();
}

function validateSplitAmounts() {
    const totalAmountElement = document.getElementById('totalPedidoAmount');
    const totalPedido = parseFloat(totalAmountElement.value.replace('S/. ', '')) || 0;
    let currentSum = 0;
    const amountInputs = document.querySelectorAll('#splitPaymentRows .person-amount');

    amountInputs.forEach(input => {
        currentSum += parseFloat(input.value) || 0;
    });

    const diff = Math.abs(totalPedido - currentSum);
    const footer = document.querySelector('#splitPaymentModal .modal-footer');
    let warningElement = document.getElementById('splitAmountWarning');

    if (diff > 0.01) {
        if (!warningElement) {
            warningElement = document.createElement('div');
            warningElement.id = 'splitAmountWarning';
            warningElement.classList.add('alert', 'alert-warning', 'w-100', 'text-center', 'mb-2');
            footer.insertBefore(warningElement, footer.firstChild);
        }
        warningElement.textContent = `La suma de los pagos (${currentSum.toFixed(2)}) no coincide con el total del pedido (${totalPedido.toFixed(2)}). Diferencia: ${diff.toFixed(2)}`;
    } else {
        if (warningElement) {
            warningElement.remove();
        }
    }
}
// ===================== LLENAR FORMULARIO PARA EDICIÓN =====================
function llenarFormularioParaEdicion(producto) {
    try {
        console.log('📝 Llenando formulario con producto:', producto);

        // Llenar campos básicos
        document.getElementById('nombrePlato').value = producto.nomProducto || '';

        // El precio se bloquea en modo edición
        const precioInput = document.getElementById('precioPlato');
        precioInput.value = (producto.precioActual || 0).toFixed(2);
        precioInput.disabled = true;
        precioInput.style.backgroundColor = '#e9ecef';
        precioInput.style.color = '#6c757d';

        // Limpiar insumos actuales del formulario
        currentPlatoInsumos = [];

        // Cargar insumos del producto con corrección de idTipoCantidad
        if (producto.insumos && Array.isArray(producto.insumos)) {
            console.log('🥬 Procesando insumos del producto:', producto.insumos);

            producto.insumos.forEach((insumo, index) => {
                console.log(`📋 Insumo ${index + 1} original:`, insumo);

                // Validar que el insumo tiene los datos mínimos necesarios
                if (!insumo.idInsumo || !insumo.nomInsumo) {
                    console.warn(`⚠️ Insumo ${index + 1} incompleto, saltando:`, insumo);
                    return;
                }

                // CORRECCIÓN ESPECÍFICA: Obtener idTipoCantidad correcto
                let idTipoCantidad = null;

                // Método 1: Si viene directamente en el insumo
                if (insumo.idTipoCantidad) {
                    idTipoCantidad = parseInt(insumo.idTipoCantidad);
                    console.log(`✅ idTipoCantidad directo: ${idTipoCantidad}`);
                }

                // Método 2: Buscar por nombre en tiposCantidadDisponibles
                if (!idTipoCantidad && insumo.nomTipoCantidad) {
                    console.log(`🔍 Buscando idTipoCantidad para: "${insumo.nomTipoCantidad}"`);
                    console.log('📋 Tipos disponibles:', tiposCantidadDisponibles);

                    const tipoCantidad = tiposCantidadDisponibles.find(tipo => {
                        const nombreTipo = tipo.nomCantidad || tipo.nombre || '';
                        const nombreInsumo = insumo.nomTipoCantidad || '';

                        return nombreTipo.toLowerCase().trim() === nombreInsumo.toLowerCase().trim();
                    });

                    if (tipoCantidad) {
                        idTipoCantidad = parseInt(tipoCantidad.idTipoCantidad || tipoCantidad.id);
                        console.log(`✅ Encontrado idTipoCantidad por nombre: ${idTipoCantidad}`);
                    } else {
                        console.warn(`⚠️ No se encontró tipo para "${insumo.nomTipoCantidad}"`);
                    }
                }

                // Método 3: Buscar en insumosDisponibles (como respaldo)
                if (!idTipoCantidad && insumo.idInsumo) {
                    console.log(`🔍 Buscando en insumosDisponibles para idInsumo: ${insumo.idInsumo}`);

                    const insumoDisponible = insumosDisponibles.find(disponible =>
                        parseInt(disponible.idInsumo) === parseInt(insumo.idInsumo)
                    );

                    if (insumoDisponible && insumoDisponible.idTipoCantidad) {
                        idTipoCantidad = parseInt(insumoDisponible.idTipoCantidad);
                        console.log(`✅ Encontrado idTipoCantidad en insumosDisponibles: ${idTipoCantidad}`);
                    }
                }

                // Método 4: Valor por defecto (último recurso)
                if (!idTipoCantidad) {
                    console.warn(`⚠️ No se pudo determinar idTipoCantidad, usando valor por defecto`);
                    idTipoCantidad = 1; // Asume que 1 es un tipo de cantidad válido
                }

                // Crear el objeto insumo para el formulario
                const insumoFormulario = {
                    idInsumo: parseInt(insumo.idInsumo),
                    type: insumo.nomInsumo,
                    quantity: parseFloat(insumo.cantPorInsumo || insumo.quantity || 0),
                    unit: insumo.nomTipoCantidad || 'unidad',
                    idTipoCantidad: idTipoCantidad // Ya es un número entero
                };

                console.log(`✅ Insumo ${index + 1} procesado:`, insumoFormulario);

                // Validación final antes de agregar
                if (insumoFormulario.idInsumo && insumoFormulario.quantity > 0 && insumoFormulario.idTipoCantidad) {
                    currentPlatoInsumos.push(insumoFormulario);
                } else {
                    console.error(`❌ Insumo ${index + 1} no válido:`, insumoFormulario);
                }
            });
        }

        // Actualizar tabla de insumos
        updateInsumosTablePlatos();

        console.log(`📝 Formulario llenado con ${currentPlatoInsumos.length} insumos válidos`);
        console.log('🔍 Insumos finales:', currentPlatoInsumos);

        // Validación de depuración
        currentPlatoInsumos.forEach((insumo, index) => {
            if (!insumo.idTipoCantidad) {
                console.error(`❌ CRÍTICO: Insumo ${index + 1} SIN idTipoCantidad:`, insumo);
            }
        });

    } catch (error) {
        console.error('❌ Error al llenar formulario:', error);
        mostrarError('Error al llenar el formulario de edición: ' + error.message);
    }
}
function processSplitPayment() {
    const totalAmountElement = document.getElementById('totalPedidoAmount');
    const totalPedido = parseFloat(totalAmountElement.value.replace('S/. ', '')) || 0;
    let currentSum = 0;
    const amountInputs = document.querySelectorAll('#splitPaymentRows .person-amount');
    const nameInputs = document.querySelectorAll('#splitPaymentRows input[type="text"]');

    splitPaymentPeople = [];
    amountInputs.forEach((input, index) => {
        const name = nameInputs[index].value || `Persona ${index + 1}`;
        const amount = parseFloat(input.value) || 0;
        splitPaymentPeople.push({ name, amount });
        currentSum += amount;
    });

    const diff = Math.abs(totalPedido - currentSum);

    if (diff > 0.01) {
        // NOTA: Reemplazar 'alert' con un modal personalizado.
        window.alert(`Error: La suma de los pagos (${currentSum.toFixed(2)}) no coincide con el total del pedido (${totalPedido.toFixed(2)}). Ajusta los montos.`);
        return;
    }

    let confirmationMessage = "Se procesarán los siguientes pagos:\n";
    splitPaymentPeople.forEach(p => {
        confirmationMessage += `- ${p.name}: S/. ${p.amount.toFixed(2)}\n`;
    });
    confirmationMessage += "\n¿Confirmar pagos?";

    // NOTA: Reemplazar 'confirm' y 'alert' con modales personalizados.
    if (window.confirm(confirmationMessage)) {
        window.alert('Pagos procesados exitosamente!');
        const modal = bootstrap.Modal.getInstance(document.getElementById('splitPaymentModal'));
        modal.hide();
    }
}

function generateIndividualBoleta(personIndex) {
    const nameInput = document.getElementById(`personName${personIndex}`);
    const amountInput = document.querySelectorAll('#splitPaymentRows .person-amount')[personIndex];

    const personName = nameInput.value || `Persona ${personIndex + 1}`;
    const personAmount = parseFloat(amountInput.value) || 0;

    // NOTA: Reemplazar 'alert' con un modal personalizado.
    window.alert(`Generando boleta para ${personName} por S/. ${personAmount.toFixed(2)}. (Simulado)`);
}

// --- Funciones de Reportes (desde reportes.html) ---
function openReport(reportId) {
    // NOTA: Reemplazar 'alert' con un modal personalizado.
    window.alert(`Abriendo reporte #${reportId}. Aquí se integrará con PowerBI en el futuro.`);
}
// ===================== AGREGAR ESTAS NUEVAS FUNCIONES =====================
// (Agregar después de las funciones existentes de platos)

// ===================== FUNCIONES DE INICIALIZACIÓN =====================
async function inicializarPaginaPlatos() {
    console.log('🚀 Inicializando página de platos...');

    try {
        mostrarCargandoTabla(true);

        await Promise.all([
            cargarProductos(),
            cargarInsumosDisponibles(),
            cargarTiposCantidadDisponibles()
        ]);

        populateIngredientTypes();
        console.log('✅ Página de platos inicializada correctamente');

    } catch (error) {
        console.error('❌ Error al inicializar página de platos:', error);
        mostrarCargandoTabla(false);
        alert('Error al cargar los datos iniciales. Verifique su conexión con el servidor.');
    }
}

// ===================== FUNCIONES DE CARGA DE DATOS =====================
async function cargarProductos() {
    try {
        console.log('📦 Cargando productos desde la API...');

        const response = await fetch(`${PRODUCTO_API_URL}/listar`);
        if (!response.ok) throw new Error(`Error HTTP: ${response.status}`);

        const data = await response.json();
        if (!Array.isArray(data)) throw new Error('Respuesta inválida de la API');

        productosData = data;
        console.log(`✅ Cargados ${productosData.length} productos`);

        actualizarTablaProductos();
        return productosData;

    } catch (error) {
        console.error('❌ Error al cargar productos:', error);
        productosData = [];
        actualizarTablaProductos();

        if (error.message.includes('Failed to fetch')) {
            alert('No se pudo conectar con el servidor. Verifique que Spring Boot esté ejecutándose en http://localhost:8080');
        } else {
            alert(`Error al cargar productos: ${error.message}`);
        }
        throw error;
    }
}

async function cargarInsumosDisponibles() {
    try {
        console.log('🥬 Cargando insumos disponibles...');

        const response = await fetch(`${INSUMO_API_URL}/listar`);
        if (!response.ok) throw new Error('Error al cargar insumos');

        insumosDisponibles = await response.json();
        console.log(`✅ Cargados ${insumosDisponibles.length} insumos disponibles`);

    } catch (error) {
        console.error('❌ Error al cargar insumos:', error);
        insumosDisponibles = [];
    }
}

// ===================== FUNCIÓN DE CARGA MEJORADA =====================
// Asegurar que tiposCantidadDisponibles se carga antes de editar
async function cargarTiposCantidadDisponibles() {
    try {
        console.log('📏 Cargando tipos de cantidad...');

        const response = await fetch(`${TIPO_CANTIDAD_API_URL}/listar`);
        if (!response.ok) throw new Error('Error al cargar tipos de cantidad');

        tiposCantidadDisponibles = await response.json();
        console.log(`✅ Cargados ${tiposCantidadDisponibles.length} tipos de cantidad:`, tiposCantidadDisponibles);

    } catch (error) {
        console.error('❌ Error al cargar tipos de cantidad:', error);
        tiposCantidadDisponibles = [];
        throw error;
    }
}
// ===================== FUNCIÓN PARA VERIFICAR DATOS CARGADOS =====================
function verificarDatosCargados() {
    console.log('🔍 VERIFICACIÓN DE DATOS CARGADOS:');
    console.log('- Productos:', productosData.length);
    console.log('- Insumos disponibles:', insumosDisponibles.length);
    console.log('- Tipos de cantidad:', tiposCantidadDisponibles.length);

    if (tiposCantidadDisponibles.length === 0) {
        console.error('❌ CRÍTICO: No hay tipos de cantidad cargados');
        mostrarError('Error: No se han cargado los tipos de cantidad. Refresque la página.');
        return false;
    }

    console.log('📋 Tipos de cantidad disponibles:');
    tiposCantidadDisponibles.forEach(tipo => {
        console.log(`  - ID: ${tipo.idTipoCantidad}, Nombre: "${tipo.nomCantidad}"`);
    });

    return true;
}

// ===================== FUNCIÓN PRINCIPAL PARA GUARDAR PRODUCTO =====================
async function guardarProducto() {
    try {
        console.log('💾 Iniciando creación de nuevo producto...');

        // Obtener datos del formulario
        const nombre = document.getElementById('nombrePlato').value.trim();
        const precio = parseFloat(document.getElementById('precioPlato').value);

        console.log('📝 Datos del formulario:', {
            nombre,
            precio,
            insumos: currentPlatoInsumos.length
        });

        // Validaciones del lado del cliente
        const errores = validarFormularioCreacion();
        if (errores.length > 0) {
            mostrarError('Errores de validación:\n' + errores.join('\n'));
            return;
        }

        // Mostrar confirmación al usuario
        const confirmacion = `¿Está seguro de crear el producto "${nombre}"?

💰 Precio: S/. ${precio.toFixed(2)}
🥬 Insumos: ${currentPlatoInsumos.length}

Esta acción no se puede deshacer.`;

        if (!confirm(confirmacion)) {
            console.log('👤 Usuario canceló la creación');
            return;
        }

        // Mostrar indicador de carga
        const submitBtn = document.querySelector('#platoForm button[type="submit"]');
        const textoOriginal = submitBtn.innerHTML;
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Creando...';

        try {
            // Preparar datos para la API
            const productoDTO = construirProductoDTO(nombre, precio, currentPlatoInsumos);
            console.log('📤 Enviando a la API:', productoDTO);

            // Llamar al endpoint de creación
            const response = await fetch(`${PRODUCTO_API_URL}/crear`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(productoDTO)
            });

            console.log('📨 Response status:', response.status);

            if (!response.ok) {
                const errorData = await response.json();
                console.error('❌ Error response:', errorData);
                throw new Error(errorData.mensaje || errorData.error || `Error HTTP: ${response.status}`);
            }

            const nuevoProducto = await response.json();
            console.log('✅ Producto creado exitosamente:', nuevoProducto);

            // Mostrar éxito
            mostrarExito(`Producto "${nombre}" creado exitosamente con ID: ${nuevoProducto.idProducto}`);

            // Limpiar formulario
            limpiarFormularioCreacion();

            // Recargar lista de productos para mostrar el nuevo
            await cargarProductos();

            console.log('🔄 Lista de productos actualizada');

        } catch (error) {
            throw error;
        }

    } catch (error) {
        console.error('❌ Error al crear producto:', error);
        mostrarError(`Error al crear el producto: ${error.message}`);

    } finally {
        // Restaurar botón
        const submitBtn = document.querySelector('#platoForm button[type="submit"]');
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.innerHTML = '<i class="bi bi-floppy me-2"></i>GUARDAR';
        }
    }
}

// ===================== FUNCIÓN DE ACTUALIZACIÓN DE TABLA =====================
function actualizarTablaProductos() {
    const tbody = document.getElementById('platosTable');
    if (!tbody) {
        console.error('❌ No se encontró la tabla de productos');
        return;
    }

    tbody.innerHTML = '';
    mostrarCargandoTabla(false);

    if (productosData.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="2" class="text-center text-muted py-4">
                    <i class="bi bi-inbox fs-1"></i><br>
                    <strong>No hay productos registrados</strong><br>
                    <small>Cree su primer producto usando el formulario de la izquierda</small>
                </td>
            </tr>
        `;

        const contador = document.getElementById('productos-contador');
        if (contador) contador.textContent = '0 productos';
        return;
    }

    productosData.forEach(producto => {
        const row = document.createElement('tr');

        row.dataset.id = producto.idProducto;
        row.dataset.name = producto.nomProducto;
        row.dataset.price = producto.precioActual || '0.00';

        row.style.cursor = 'pointer';
        row.classList.add('table-row-hover');

        row.addEventListener('click', (e) => {
            if (!e.target.closest('.price-cell')) {
                editarProducto(producto.idProducto);
            }
        });

        const precioActual = producto.precioActual || 0;
        const precioColor = precioActual > 0 ? '#28a745' : '#dc3545';
        const precioText = precioActual > 0 ? `S/. ${precioActual.toFixed(2)}` : 'Sin precio';

        row.innerHTML = `
            <td class="align-middle">
                <div>
                    <strong>${escapeHtml(producto.nomProducto)}</strong>
                    ${producto.insumos && producto.insumos.length > 0 ?
                        `<br><small class="text-muted">
                            <i class="bi bi-list-ul me-1"></i>
                            ${producto.insumos.length} insumo(s)
                        </small>` :
                        '<br><small class="text-warning">⚠️ Sin insumos</small>'
                    }
                </div>
            </td>
            <td class="price-cell align-middle text-center"
                style="cursor: pointer; color: ${precioColor}; font-weight: bold;"
                onclick="event.stopPropagation(); mostrarHistorialPrecios(${producto.idProducto}, '${escapeHtml(producto.nomProducto)}')">
                <div>${precioText}</div>
                <small class="text-muted">
                    <i class="bi bi-clock-history me-1"></i>
                    Ver historial
                </small>
            </td>
        `;

        tbody.appendChild(row);
    });

    const contador = document.getElementById('productos-contador');
    if (contador) {
        contador.textContent = `${productosData.length} productos`;
    }

    console.log(`📊 Tabla actualizada con ${productosData.length} productos`);
}
function limpiarFormularioCambioPrecio() {
    document.getElementById('cambiarPrecioForm').reset();
    document.getElementById('nombreProductoPrecio').value = '';
    document.getElementById('idProductoPrecio').value = '';
    document.getElementById('precioActualProducto').value = '';
    document.getElementById('nuevoPrecioProducto').value = '';
    document.getElementById('motivoCambioPrecio').value = '';
    productoSeleccionadoPrecio = null;

    console.log('🧹 Formulario de cambio de precio limpiado');
}

// ===================== FUNCIÓN PARA VALIDAR PRECIO EN TIEMPO REAL =====================
// Validar precio en tiempo real
function validarPrecioEnTiempoReal() {
    const precioInput = document.getElementById('precioPlato');
    if (!precioInput) return;

    precioInput.addEventListener('input', function() {
        if (this.disabled) return; // No validar si está deshabilitado (modo edición)

        const precio = parseFloat(this.value);

        // Remover clases de validación previas
        this.classList.remove('is-valid', 'is-invalid');

        if (isNaN(precio) || precio <= 0) {
            this.classList.add('is-invalid');
        } else if (precio > 9999.99) {
            this.classList.add('is-invalid');
        } else {
            this.classList.add('is-valid');
        }
    });
}

// ===================== CONFIGURACIÓN DE EVENT LISTENERS =====================
function configurarEventListenersEdicion() {
    console.log('⚙️ Configurando event listeners para edición...');

    // Validación en tiempo real
    validarNombreEnTiempoReal();
    validarPrecioEnTiempoReal();

    // Event listener para el modal de insumos cuando se cierra
    const addIngredientModal = document.getElementById('addIngredientModal');
    if (addIngredientModal) {
        addIngredientModal.addEventListener('hidden.bs.modal', function() {
            updateDisplayIngredientUnit();
        });
    }

    // Prevenir envío accidental del formulario
    const form = document.getElementById('platoForm');
    if (form) {
        form.addEventListener('keydown', function(e) {
            if (e.key === 'Enter' && e.target.tagName !== 'TEXTAREA') {
                e.preventDefault();
            }
        });
    }

    console.log('✅ Event listeners de edición configurados');
}
// ===================== FUNCIÓN PARA CONFIGURAR EVENT LISTENERS =====================
function configurarEventListenersCambioPrecio() {
    // Event listener para el modal cuando se abre
    const modal = document.getElementById('cambiarPrecioModal');
    if (modal) {
        modal.addEventListener('shown.bs.modal', function() {
            // Focus en el input de nuevo precio
            const nuevoPrecioInput = document.getElementById('nuevoPrecioProducto');
            if (nuevoPrecioInput) {
                nuevoPrecioInput.focus();
                nuevoPrecioInput.select();
            }
        });

        modal.addEventListener('hidden.bs.modal', function() {
            // Limpiar cuando se cierra
            limpiarFormularioCambioPrecio();
        });
    }

    // Event listener para validación en tiempo real
    const nuevoPrecioInput = document.getElementById('nuevoPrecioProducto');
    if (nuevoPrecioInput) {
        nuevoPrecioInput.addEventListener('input', validarPrecioEnTiempoReal);
        nuevoPrecioInput.addEventListener('blur', validarPrecioEnTiempoReal);
    }

    // Event listener para el formulario (prevenir submit default)
    const form = document.getElementById('cambiarPrecioForm');
    if (form) {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            confirmarCambioPrecio();
        });
    }

    console.log('⚙️ Event listeners de cambio de precio configurados');
}
// ===================== FUNCIONES DE INTERFAZ =====================
function mostrarCargandoTabla(mostrar) {
    const tbody = document.getElementById('platosTable');
    if (!tbody) return;

    if (mostrar) {
        tbody.innerHTML = `
            <tr>
                <td colspan="2" class="text-center py-4">
                    <div class="spinner-border spinner-border-sm me-2 text-primary"></div>
                    <span>Cargando productos desde la base de datos...</span>
                </td>
            </tr>
        `;
    }
}

function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// ===================== VERSIÓN MEJORADA DE editarProducto =====================
// REEMPLAZAR la función editarProducto existente:
async function editarProducto(idProducto) {
    try {
        console.log(`✏️ Iniciando edición del producto ID: ${idProducto}`);

        // Verificar que los datos estén cargados
        if (!verificarDatosCargados()) {
            return;
        }

        // Mostrar indicador de carga en el formulario
        mostrarCargandoFormulario(true);

        // Cargar datos completos del producto desde la API
        const response = await fetch(`${PRODUCTO_API_URL}/${idProducto}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`Error HTTP: ${response.status} - ${response.statusText}`);
        }

        const producto = await response.json();
        console.log('✅ Producto cargado para edición:', producto);

        // Verificar que el producto tenga datos válidos
        if (!producto || !producto.idProducto) {
            throw new Error('Datos del producto incompletos');
        }

        // Establecer modo edición
        productoEnEdicion = producto;

        // Llenar formulario con datos del producto (AQUÍ SE CORRIGE idTipoCantidad)
        llenarFormularioParaEdicion(producto);

        // Cambiar interfaz a modo edición
        activarModoEdicion(producto);

        console.log(`🎯 Producto "${producto.nomProducto}" listo para editar`);

    } catch (error) {
        console.error('❌ Error al cargar producto para edición:', error);
        mostrarError(`Error al cargar el producto: ${error.message}`);

    } finally {
        mostrarCargandoFormulario(false);
    }
}

async function mostrarHistorialPrecios(idProducto, nombreProducto) {
    try {
        console.log(`📊 Cargando historial de precios para: ${nombreProducto} (ID: ${idProducto})`);

        // Configurar el modal
        document.getElementById('productNameHistory').textContent = nombreProducto;
        const historyTableBody = document.getElementById('priceHistoryTableBody');

        // Mostrar cargando en la tabla
        historyTableBody.innerHTML = `
            <tr>
                <td colspan="3" class="text-center py-3">
                    <div class="spinner-border spinner-border-sm me-2 text-primary"></div>
                    <span>Cargando historial de precios...</span>
                </td>
            </tr>
        `;

        // Abrir modal inmediatamente para mostrar loading
        const modal = new bootstrap.Modal(document.getElementById('priceHistoryModal'));
        modal.show();

        // Llamar al endpoint del controlador
        const response = await fetch(`${PRODUCTO_API_URL}/${idProducto}/historial-precios`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`Error HTTP: ${response.status} - ${response.statusText}`);
        }

        const historialData = await response.json();
        console.log('✅ Historial cargado:', historialData);

        // Limpiar tabla
        historyTableBody.innerHTML = '';

        // Verificar si hay datos
        if (!Array.isArray(historialData) || historialData.length === 0) {
            historyTableBody.innerHTML = `
                <tr>
                    <td colspan="3" class="text-center text-muted py-4">
                        <i class="bi bi-info-circle fs-4"></i><br>
                        <strong>No hay historial de precios</strong><br>
                        <small>Este producto aún no tiene cambios de precio registrados</small>
                    </td>
                </tr>
            `;
            return;
        }

        // Agregar filas del historial
        historialData.forEach((precio, index) => {
            const row = document.createElement('tr');

            // Formatear fecha
            const fecha = new Date(precio.fechaInicio);
            const fechaFormateada = fecha.toLocaleDateString('es-ES', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit'
            });

            // Determinar si es el precio actual (primer elemento, generalmente)
            const esActual = index === 0;
            const estadoBadge = esActual ?
                '<span class="badge bg-success">Actual</span>' :
                '<span class="badge bg-secondary">Histórico</span>';

            // Crear fila
            row.innerHTML = `
                <td>
                    <strong>${fechaFormateada}</strong>
                    ${precio.fechaFin ?
                        `<br><small class="text-muted">Hasta: ${new Date(precio.fechaFin).toLocaleDateString('es-ES')}</small>` :
                        ''
                    }
                </td>
                <td class="text-center">
                    <strong class="text-success">S/. ${precio.precio.toFixed(2)}</strong>
                </td>
                <td class="text-center">
                    ${estadoBadge}
                </td>
            `;

            // Agregar clase especial al precio actual
            if (esActual) {
                row.classList.add('table-success');
            }

            historyTableBody.appendChild(row);
        });

        console.log(`📊 Historial mostrado: ${historialData.length} registros`);

    } catch (error) {
        console.error('❌ Error al cargar historial de precios:', error);

        // Mostrar error en la tabla
        const historyTableBody = document.getElementById('priceHistoryTableBody');
        historyTableBody.innerHTML = `
            <tr>
                <td colspan="3" class="text-center text-danger py-4">
                    <i class="bi bi-exclamation-triangle fs-4"></i><br>
                    <strong>Error al cargar el historial</strong><br>
                    <small>${error.message}</small>
                </td>
            </tr>
        `;

        // También abrir el modal para mostrar el error
        if (!document.getElementById('priceHistoryModal').classList.contains('show')) {
            const modal = new bootstrap.Modal(document.getElementById('priceHistoryModal'));
            modal.show();
        }

        // Mostrar notificación de error
        mostrarError(`Error al cargar historial: ${error.message}`);
    }
}

// ===================== FUNCIÓN DE PRUEBA =====================
async function probarConectividad() {
    try {
        console.log('🧪 Probando conectividad con la API...');

        const response = await fetch(`${API_BASE_URL}/api/producto/listar`);

        if (response.ok) {
            const data = await response.json();
            console.log('✅ Conectividad exitosa');
            console.log(`📊 API respondió con ${Array.isArray(data) ? data.length : 0} productos`);
            alert('✅ Conexión exitosa con la API');
            return true;
        } else {
            throw new Error(`Error HTTP: ${response.status}`);
        }
    } catch (error) {
        console.error('❌ Error de conectividad:', error);

        let mensajeError = 'No se pudo conectar con el servidor. Verifique que Spring Boot esté ejecutándose en http://localhost:8080';
        if (!error.message.includes('Failed to fetch')) {
            mensajeError = `Error de conexión: ${error.message}`;
        }

        alert('❌ ' + mensajeError);
        return false;
    }
}

// ===================== FUNCIONES DE DESARROLLO =====================
window.verificarEstado = function() {
    console.log('🔍 ESTADO DE LA APLICACIÓN:');
    console.log('- Productos cargados:', productosData.length);
    console.log('- Insumos disponibles:', insumosDisponibles.length);
    console.log('- Tipos de cantidad:', tiposCantidadDisponibles.length);
    console.log('- Producto editando:', currentPlatoEditando);
    console.log('- Insumos actuales:', currentPlatoInsumos.length);

    return {
        productos: productosData.length,
        insumos: insumosDisponibles.length,
        tipos: tiposCantidadDisponibles.length,
        editando: currentPlatoEditando,
        insumosFormulario: currentPlatoInsumos.length
    };
};

window.cargarDatosPrueba = function() {
    console.log('🧪 Cargando datos de prueba...');

    productosData = [
        {
            idProducto: 1,
            nomProducto: "Producto de Prueba 1",
            precioActual: 15.50,
            insumos: [{nombre: "Insumo 1"}, {nombre: "Insumo 2"}]
        },
        {
            idProducto: 2,
            nomProducto: "Producto de Prueba 2",
            precioActual: 25.75,
            insumos: [{nombre: "Insumo 3"}]
        }
    ];

    actualizarTablaProductos();
    alert('✅ Datos de prueba cargados');
};
// Validar en tiempo real el nombre del producto
function validarNombreEnTiempoReal() {
    const nombreInput = document.getElementById('nombrePlato');
    if (!nombreInput) return;

    nombreInput.addEventListener('input', function() {
        const nombre = this.value.trim();

        // Remover clases de validación previas
        this.classList.remove('is-valid', 'is-invalid');

        if (nombre.length === 0) {
            // Campo vacío - sin validación visual
            return;
        } else if (nombre.length < 2) {
            // Muy corto
            this.classList.add('is-invalid');
        } else if (nombre.length > 100) {
            // Muy largo
            this.classList.add('is-invalid');
        } else {
            // Válido
            this.classList.add('is-valid');
        }
    });
}
// Función mejorada para mostrar errores con contador
// ACTUALIZAR la función mostrarError existente:
function mostrarError(mensaje) {
    console.error('❌ ' + mensaje);

    // Crear alerta más visible
    const alertDiv = document.createElement('div');
    alertDiv.className = 'alert alert-danger alert-dismissible fade show position-fixed';
    alertDiv.style.cssText = `
        top: 20px;
        right: 20px;
        z-index: 1050;
        max-width: 400px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    `;
    alertDiv.innerHTML = `
        <strong><i class="bi bi-exclamation-triangle-fill me-2"></i>Error:</strong>
        ${mensaje.replace(/\n/g, '<br>')}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

    document.body.appendChild(alertDiv);

    // Auto-remover después de 8 segundos
    setTimeout(() => {
        if (alertDiv.parentNode) {
            alertDiv.remove();
        }
    }, 8000);

    // Fallback: también mostrar alert tradicional para compatibilidad
    alert('❌ ' + mensaje);
}

// ACTUALIZAR la función mostrarExito existente:
function mostrarExito(mensaje) {
    console.log('✅ ' + mensaje);

    // Crear alerta de éxito
    const alertDiv = document.createElement('div');
    alertDiv.className = 'alert alert-success alert-dismissible fade show position-fixed';
    alertDiv.style.cssText = `
        top: 20px;
        right: 20px;
        z-index: 1050;
        max-width: 400px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    `;
    alertDiv.innerHTML = `
        <strong><i class="bi bi-check-circle-fill me-2"></i>Éxito:</strong>
        ${mensaje}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

    document.body.appendChild(alertDiv);

    // Auto-remover después de 5 segundos
    setTimeout(() => {
        if (alertDiv.parentNode) {
            alertDiv.remove();
        }
    }, 5000);

    // Fallback: también mostrar alert tradicional
    alert('✅ ' + mensaje);
}

// Función para mostrar información
function mostrarInfo(mensaje) {
    console.log('ℹ️ ' + mensaje);
    alert('ℹ️ ' + mensaje);
}

// --- Funciones de Gestión de Insumos (desde configuracion.html - NUEVA SECCIÓN) ---
let insumosData = []; // Almacenará los insumos cargados desde la API
let tiposCantidadData = []; // Almacenará los tipos de cantidad
let insumoEditandoId = null; // Para controlar si estamos editando

// Cargar insumos desde la API de Spring Boot
async function loadInsumos() {
    try {
        const response = await fetch('http://localhost:8080/api/insumo/listar');
        if (!response.ok) throw new Error('Error al cargar insumos');

        const data = await response.json(); // Recibe GETInsumoDTO[]
        insumosData = data;
        updateInsumosManagementTable();

    } catch (error) {
        console.error('Error al cargar insumos:', error);
        alert('No se pudieron cargar los insumos desde el servidor.');
        // Fallback: mostrar tabla vacía
        insumosData = [];
        updateInsumosManagementTable();
    }
}

function updateInsumosManagementTable() {
    const tbody = document.getElementById('insumosManagementTableBody');
    if (!tbody) return;

    tbody.innerHTML = '';

    if (insumosData.length === 0) {
        tbody.innerHTML = '<tr><td colspan="3" class="text-center text-muted">No hay insumos registrados.</td></tr>';
        return;
    }

    insumosData.forEach(insumo => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${insumo.nomInsumo}</td>
            <td>${insumo.nomTipoCantidad}</td>
            <td>
                <div class="action-buttons">
                    <button class="btn btn-edit btn-sm" data-id="${insumo.idInsumo}" data-action="edit" title="Editar">
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-delete btn-sm" data-id="${insumo.idInsumo}" data-action="delete" title="Eliminar">
                        <i class="bi bi-trash"></i>
                    </button>
                </div>
            </td>
        `;
        tbody.appendChild(row);
    });

    // Agregar event listeners a los botones DESPUÉS de crear las filas
    tbody.addEventListener('click', function(e) {
        const button = e.target.closest('button');
        if (!button) return;

        const id = parseInt(button.dataset.id);
        const action = button.dataset.action;

        if (action === 'edit') {
            editInsumo(id);
        } else if (action === 'delete') {
            deleteInsumo(id);
        }
    });
}

async function confirmarCambioPrecio() {
    try {
        console.log('💰 Iniciando proceso de cambio de precio...');

        // Obtener datos del formulario
        const idProducto = parseInt(document.getElementById('idProductoPrecio').value);
        const nuevoPrecio = parseFloat(document.getElementById('nuevoPrecioProducto').value);
        const motivo = document.getElementById('motivoCambioPrecio').value.trim();

        // Validaciones
        if (!idProducto || idProducto <= 0) {
            mostrarError('ID de producto inválido');
            return;
        }

        if (!nuevoPrecio || nuevoPrecio <= 0) {
            mostrarError('Por favor, ingrese un precio válido mayor a 0');
            return;
        }

        if (productoSeleccionadoPrecio && nuevoPrecio === productoSeleccionadoPrecio.precioActual) {
            mostrarError('El nuevo precio debe ser diferente al precio actual');
            return;
        }

        // Confirmar el cambio con el usuario
        const mensaje = `¿Está seguro de cambiar el precio de "${productoSeleccionadoPrecio.nomProducto}"?

Precio actual: S/. ${(productoSeleccionadoPrecio.precioActual || 0).toFixed(2)}
Nuevo precio: S/. ${nuevoPrecio.toFixed(2)}
${motivo ? `Motivo: ${motivo}` : ''}`;

        if (!confirm(mensaje)) {
            console.log('👤 Usuario canceló el cambio de precio');
            return;
        }

        // Deshabilitar botón mientras se procesa
        const botonConfirmar = document.querySelector('#cambiarPrecioModal .btn-primary');
        const textoOriginal = botonConfirmar.innerHTML;
        botonConfirmar.disabled = true;
        botonConfirmar.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Actualizando...';

        // Preparar datos para la API
        const cambiarPrecioDTO = {
            idProducto: idProducto,
            nuevoPrecio: nuevoPrecio,
            idEmpleado: 1, // TODO: Obtener del usuario logueado
            motivo: motivo || 'Actualización de precio desde interfaz web'
        };

        console.log('📤 Enviando cambio de precio:', cambiarPrecioDTO);

        // Llamar al endpoint
        const response = await fetch(`${PRODUCTO_API_URL}/cambiar-precio`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(cambiarPrecioDTO)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.mensaje || `Error HTTP: ${response.status}`);
        }

        const resultado = await response.json();
        console.log('✅ Precio cambiado exitosamente:', resultado);

        // Mostrar éxito
        mostrarExito(`Precio actualizado exitosamente a S/. ${nuevoPrecio.toFixed(2)}`);

        // Cerrar modal
        const modal = bootstrap.Modal.getInstance(document.getElementById('cambiarPrecioModal'));
        if (modal) {
            modal.hide();
        }

        // Limpiar formulario
        limpiarFormularioCambioPrecio();

        // Recargar datos de productos para reflejar el cambio
        await cargarProductos();

        console.log('🔄 Datos actualizados después del cambio de precio');

    } catch (error) {
        console.error('❌ Error al cambiar precio:', error);
        mostrarError(`Error al cambiar el precio: ${error.message}`);

    } finally {
        // Restaurar botón
        const botonConfirmar = document.querySelector('#cambiarPrecioModal .btn-primary');
        if (botonConfirmar) {
            botonConfirmar.disabled = false;
            botonConfirmar.innerHTML = '<i class="bi bi-check-lg me-1"></i>Actualizar Precio';
        }
    }
}
function mostrarError(mensaje) {
    console.error('❌ ' + mensaje);
    alert('❌ ' + mensaje);
}

function mostrarExito(mensaje) {
    console.log('✅ ' + mensaje);
    alert('✅ ' + mensaje);
}

function mostrarInfo(mensaje) {
    console.log('ℹ️ ' + mensaje);
    alert('ℹ️ ' + mensaje);
}
function abrirModalCambiarPrecio() {
    try {
        console.log('💰 Abriendo modal de cambio de precio...');

        // Obtener datos del modal de historial
        const nombreProducto = document.getElementById('productNameHistory').textContent;

        // Buscar el producto en los datos cargados
        const producto = productosData.find(p => p.nomProducto === nombreProducto);

        if (!producto) {
            mostrarError('No se pudo encontrar el producto seleccionado');
            return;
        }

        // Guardar referencia del producto
        productoSeleccionadoPrecio = producto;

        // Llenar el modal con los datos del producto
        document.getElementById('nombreProductoPrecio').value = producto.nomProducto;
        document.getElementById('idProductoPrecio').value = producto.idProducto;
        document.getElementById('precioActualProducto').value = `S/. ${(producto.precioActual || 0).toFixed(2)}`;
        document.getElementById('nuevoPrecioProducto').value = (producto.precioActual || 0).toFixed(2);
        document.getElementById('motivoCambioPrecio').value = '';

        // Cerrar modal de historial
        const historialModal = bootstrap.Modal.getInstance(document.getElementById('priceHistoryModal'));
        if (historialModal) {
            historialModal.hide();
        }

        // Abrir modal de cambio de precio después de un pequeño delay
        setTimeout(() => {
            const cambiarPrecioModal = new bootstrap.Modal(document.getElementById('cambiarPrecioModal'));
            cambiarPrecioModal.show();
        }, 300);

        console.log('✅ Modal de cambio de precio abierto para:', producto.nomProducto);

    } catch (error) {
        console.error('❌ Error al abrir modal de cambio de precio:', error);
        mostrarError('Error al preparar el cambio de precio');
    }
}

function clearInsumoForm() {
    document.getElementById('insumoId').value = '';
    document.getElementById('insumoName').value = '';
    document.getElementById('insumoUnit').value = '';
    document.getElementById('insumoModalLabel').textContent = 'Nuevo Insumo';
    insumoEditandoId = null;
}

async function saveInsumo() {
    const nombre = document.getElementById('insumoName').value.trim();
    const idTipoCantidad = parseInt(document.getElementById('insumoUnit').value);

    // Validaciones básicas
    if (!nombre) {
        alert('Por favor, ingrese el nombre del insumo.');
        return;
    }

    if (!idTipoCantidad) {
        alert('Por favor, seleccione un tipo de unidad.');
        return;
    }

    const esEdicion = !!insumoEditandoId;

    try {
        let response;

        if (esEdicion) {
            // Actualizar insumo existente
            const actualizarDTO = {
                idInsumo: insumoEditandoId,
                nomInsumo: nombre,
                idTipoCantidad: idTipoCantidad
            };

            response = await fetch('http://localhost:8080/api/insumo/actualizar', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(actualizarDTO)
            });
        } else {
            // Crear nuevo insumo
            const crearDTO = {
                nomInsumo: nombre,
                idTipoCantidad: idTipoCantidad
            };

            response = await fetch('http://localhost:8080/api/insumo/crear', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(crearDTO)
            });
        }

        if (response.ok) {
            const resultado = await response.json(); // GETInsumoDTO
            alert(esEdicion ?
                `Insumo '${resultado.nomInsumo}' actualizado exitosamente.` :
                `Insumo '${resultado.nomInsumo}' agregado exitosamente.`
            );

            // Cerrar modal y recargar datos
            const modal = bootstrap.Modal.getInstance(document.getElementById('insumoModal'));
            modal.hide();
            clearInsumoForm();
            await loadInsumos(); // Recargar la lista

        } else {
            const errorData = await response.json().catch(() => ({}));
            alert(`Error: ${errorData.message || 'No se pudo guardar el insumo.'}`);
        }

    } catch (error) {
        console.error('Error al guardar insumo:', error);
        alert('Hubo un problema al conectarse con el servidor.');
    }
}

// FUNCIÓN EDITINSUMO - ÚNICA Y CORRECTA
async function editInsumo(id) {
    try {
        console.log('Editando insumo ID:', id);

        const insumo = insumosData.find(i => i.idInsumo === id);
        if (!insumo) {
            alert('Insumo no encontrado.');
            return;
        }

        // Cambiar el título del modal
        document.getElementById('insumoModalLabel').textContent = 'Editar Insumo';

        // Llenar los campos
        document.getElementById('insumoId').value = insumo.idInsumo;
        document.getElementById('insumoName').value = insumo.nomInsumo;

        // Establecer la variable global
        insumoEditandoId = id;

        // Cargar tipos de cantidad
        const response = await fetch('http://localhost:8080/api/tipo-cantidad/listar');
        if (response.ok) {
            const data = await response.json();
            const select = document.getElementById('insumoUnit');
            select.innerHTML = '<option value="">Seleccionar...</option>';

            data.forEach(tipo => {
                const option = document.createElement('option');
                option.value = tipo.idTipoCantidad;
                option.textContent = tipo.nomCantidad;
                select.appendChild(option);
            });

            // Seleccionar el tipo correcto
            select.value = insumo.idTipoCantidad;
        }

        // Abrir el modal
        const modal = new bootstrap.Modal(document.getElementById('insumoModal'));
        modal.show();

    } catch (error) {
        console.error('Error al editar insumo:', error);
        alert('Error al cargar los datos del insumo.');
    }
}

// FUNCIÓN DELETEINSUMO - NUEVA
async function deleteInsumo(id) {
    const insumo = insumosData.find(i => i.idInsumo === id);

    if (!insumo) {
        alert('Insumo no encontrado.');
        return;
    }

    if (!confirm(`¿Está seguro de eliminar el insumo '${insumo.nomInsumo}'?`)) {
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/insumo/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            alert(`Insumo '${insumo.nomInsumo}' eliminado exitosamente.`);
            await loadInsumos(); // Recargar la lista
        } else {
            alert('No se pudo eliminar el insumo.');
        }

    } catch (error) {
        console.error('Error al eliminar insumo:', error);
        alert('Hubo un problema al conectarse con el servidor.');
    }
}

// Hacer funciones globales para insumos - MOVER AL FINAL DEL DOMCONTENTLOADED




// --- Event Listeners Globales (DOMContentLoaded) ---
document.addEventListener("DOMContentLoaded", () => {
    // Carga el tema al cargar la página
    loadTheme();

    updateDateTime();
    setInterval(updateDateTime, 1000);

    const sidebar = document.getElementById("sidebar");
    const dashboardContainer = document.querySelector(".dashboard-container");
    const navLinks = document.querySelectorAll(".sidebar-nav a"); // Asegúrate de que esta línea esté presente
    const toggleBtnIcon = sidebar ? sidebar.querySelector(".sidebar-toggle-btn i") : null;

    if (sidebar && dashboardContainer && toggleBtnIcon) {
        // Lógica de reseteo del sidebar en redimensionamiento de ventana
        window.addEventListener("resize", () => {
            if (window.innerWidth > 991) { // Usa 991px para desktop
                sidebar.classList.remove("show"); // Quita la clase 'show' de móvil
                if (!sidebar.classList.contains('collapsed')) {
                    dashboardContainer.classList.remove('sidebar-collapsed');
                    toggleBtnIcon.classList.remove("bi-chevron-right");
                    toggleBtnIcon.classList.add("bi-chevron-left");
                } else {
                    dashboardContainer.classList.add('sidebar-collapsed'); // Asegura que se active si está colapsado
                    toggleBtnIcon.classList.remove("bi-chevron-left");
                    toggleBtnIcon.classList.add("bi-chevron-right");
                }
            } else {
                sidebar.classList.remove('collapsed'); // Quita la clase 'collapsed' de desktop
                dashboardContainer.classList.remove('sidebar-collapsed'); // Quita la clase de desktop
                sidebar.classList.remove('show'); // Asegura que no esté visible por defecto en móvil
            }
        });

        // Asegúrate de que el sidebar esté en el estado correcto al cargar la página
        if (window.innerWidth > 991) { // Desktop
            // Si el sidebar no tiene la clase 'collapsed', quita 'sidebar-collapsed' del contenedor
            if (!sidebar.classList.contains('collapsed')) {
                dashboardContainer.classList.remove('sidebar-collapsed');
                toggleBtnIcon.classList.remove("bi-chevron-right");
                toggleBtnIcon.classList.add("bi-chevron-left");
            } else {
                // Si el sidebar tiene 'collapsed', asegúrate de que el contenedor también lo tenga
                dashboardContainer.classList.add('sidebar-collapsed');
                toggleBtnIcon.classList.remove("bi-chevron-left");
                toggleBtnIcon.classList.add("bi-chevron-right");
            }
        } else { // Mobile
            sidebar.classList.remove('collapsed');
            dashboardContainer.classList.remove('sidebar-collapsed');
            sidebar.classList.remove('show');
        }
    }


    const currentPath = window.location.pathname;
    navLinks.forEach((link) => {
        link.classList.remove("active", "disabled");
        // Ajuste para que /configuracion sea activo si está en cualquiera de sus subpestañas
        if (currentPath.startsWith('/configuracion') || currentPath.startsWith('/usuarios')) {
            if (link.getAttribute("href") === '/configuracion') { // Asume que /configuracion es la raíz de la sección
                link.classList.add("active");
            }
        } else if (link.getAttribute("href") === currentPath) {
            link.classList.add("active");
        }
    });


    // Y REEMPLAZARLA con esto:
    if (document.getElementById('insumosTableBody')) { // Para la tabla de insumos en el formulario de platos
        console.log('🍽️ Detectada página de platos - inicializando...');

        // Inicializar inmediatamente la página de platos
        setTimeout(() => {
            if (typeof inicializarPaginaPlatos === 'function') {
                inicializarPaginaPlatos();
            } else {
                console.error('❌ Función inicializarPaginaPlatos no encontrada');
                updateInsumosTablePlatos();
            }
        }, 500);

        // Configurar event listeners para edición
        configurarEventListenersEdicion();

        // Configurar event listeners para cambio de precio
        configurarEventListenersCambioPrecio();

        // Listener para el formulario de platos (modo crear)
        const platoForm = document.getElementById('platoForm');
        if (platoForm) {
            platoForm.addEventListener('submit', function(e) {
                e.preventDefault();

                // Verificar si estamos en modo edición o creación
                if (productoEnEdicion) {
                    // Modo edición
                    actualizarProducto();
                } else {
                    // Modo creación
                    if (!verificarPrerequisitosCreacion()) {
                        return; // No continuar si faltan prerequisitos
                    }
                    guardarProducto();
                }
            });

            console.log('✅ Listener del formulario configurado para crear/editar');
            configurarValidacionTiempoReal();
        }

        // Para el modal de "Añadir Insumo"
        const addIngredientModal = document.getElementById('addIngredientModal');
        if (addIngredientModal) {
            addIngredientModal.addEventListener('show.bs.modal', function() {
                populateIngredientTypes();
                updateDisplayIngredientUnit();
            });

            const ingredientTypeSelect = document.getElementById('ingredientType');
            if (ingredientTypeSelect) {
                ingredientTypeSelect.addEventListener('change', updateDisplayIngredientUnit);
            }
        }

        // Listener para búsqueda mejorada
        const searchInput = document.getElementById('searchPlatos');
        if (searchInput) {
            searchInput.addEventListener('input', function() {
                const searchTerm = this.value.toLowerCase().trim();
                const rows = document.querySelectorAll('#platosTable tr[data-id]');

                let productosVisibles = 0;
                rows.forEach(row => {
                    const nombre = (row.dataset.name || '').toLowerCase();
                    const precio = (row.dataset.price || '').toLowerCase();

                    if (!searchTerm || nombre.includes(searchTerm) || precio.includes(searchTerm)) {
                        row.style.display = '';
                        productosVisibles++;
                    } else {
                        row.style.display = 'none';
                    }
                });

                // Actualizar contador
                const contador = document.getElementById('productos-contador');
                if (contador) {
                    const totalProductos = rows.length;
                    if (searchTerm) {
                        contador.textContent = `${productosVisibles} de ${totalProductos} productos (filtrado)`;
                    } else {
                        contador.textContent = `${totalProductos} productos`;
                    }
                }
            });
        }
    }

    if (document.getElementById('orderItemsTable')) { // Para la tabla de pedidos en pedido.html
        updateOrderTable();
        updateTotal();
        setupSearchInput('menuSearch', '.menu-items');
    }

    if (document.getElementById('splitPaymentModal')) { // Para el modal de pago fraccionado
        document.getElementById('splitPaymentModal').addEventListener('show.bs.modal', openSplitPaymentModal);
    }

   if (document.getElementById('insumosManagementTableBody')) { // Para la tabla de gestión de insumos en configuracion.html
       loadInsumos(); // Carga los insumos al abrir la pestaña
       // Listener para el modal de insumos (limpiar al abrir)
       document.getElementById('insumoModal').addEventListener('show.bs.modal', async function(event) {
           // Solo limpiar si NO es un botón de editar
           if (!insumoEditandoId) {
               clearInsumoForm();
               await cargarTiposCantidad();
           }
           // Si es editar, no hacer nada aquí porque editInsumo ya maneja todo
       });

       // AGREGAR ESTAS LÍNEAS AQUÍ ↓
       document.getElementById('insumoModal').addEventListener('hidden.bs.modal', function() {
           insumoEditandoId = null;
           document.getElementById('insumoModalLabel').textContent = 'Nuevo Insumo';
       });
   }

    if (document.getElementById('searchInput')) { // Para la tabla de historial de pedidos
        setupSearchInput('searchInput', '.table');
    }

    // Lógica para mesas.html (Selección múltiple, long press, doble clic)
    const tableCards = document.querySelectorAll('.table-card');
    if (tableCards.length > 0) {
        tableCards.forEach(card => {
            const tableNumber = parseInt(card.dataset.tableNumber);
            let clickTimer = null;
            let lastClickTime = 0;
            const DOUBLE_CLICK_THRESHOLD = 300; // ms

            // Eventos de ratón para PC
            card.addEventListener('mousedown', (event) => {
                if (event.button === 0) { // Solo clic izquierdo
                    handleTableMouseDown(card, tableNumber);
                }
            });

            card.addEventListener('mouseup', (event) => {
                if (event.button === 0) { // Solo clic izquierdo
                    const currentTime = new Date().getTime();
                    // Detectar doble click
                    if (currentTime - lastClickTime < DOUBLE_CLICK_THRESHOLD) {
                        clearTimeout(clickTimer);
                        goToOrder(tableNumber); // Doble clic va directo al pedido
                        clearSelection(); // Limpiar selección al ir a pedido
                    } else {
                        clickTimer = setTimeout(() => {
                            handleTableMouseUp(card, tableNumber, event); // Clic simple o Ctrl+Clic
                        }, DOUBLE_CLICK_THRESHOLD);
                    }
                    lastClickTime = currentTime;
                }
            });

            // Evitar menú contextual en long press para touch
            card.addEventListener('contextmenu', (event) => {
                if ('ontouchstart' in window) { // Solo para dispositivos táctiles
                    event.preventDefault();
                }
            });


            // Eventos táctiles para móvil
            card.addEventListener('touchstart', (event) => {
                // Prevenir el comportamiento por defecto de "doble tap para zoom" si es necesario
                // event.preventDefault(); // Cuidado: esto puede interferir con el scroll
                handleTableMouseDown(card, tableNumber);
            }, { passive: true }); // Usar passive para no bloquear el scroll

            card.addEventListener('touchend', (event) => {
                clearTimeout(longPressTimer);
                if (!isLongPressActive && event.cancelable) { // Si no fue un long press y el evento es cancelable
                    // Aquí se simula el click para que el toggleTableSelection funcione
                    const simulatedEvent = new MouseEvent('click', {
                        bubbles: true,
                        cancelable: true,
                        view: window,
                        detail: 1, // Indicar que es un clic simple
                        ctrlKey: event.ctrlKey || false // Propagar ctrlKey si existe
                    });
                    card.dispatchEvent(simulatedEvent);
                }
                isLongPressActive = false; // Resetear el estado de long press
                event.preventDefault(); // Prevenir el clic fantasma si se usó long press
            });

            card.addEventListener('touchmove', handleTableMove, { passive: true }); // Usar passive para no bloquear el scroll
        });
    }
    // Cargar tipos de cantidad desde Spring Boot
    async function cargarTiposCantidad() {
        try {
            const response = await fetch('http://localhost:8080/api/tipo-cantidad/listar');
            if (!response.ok) throw new Error('Error al cargar tipos de cantidad');

            const data = await response.json(); // Recibe GETTipoCantidadDTO[]
            const select = document.getElementById('insumoUnit');
            select.innerHTML = '<option value="">Seleccionar...</option>'; // Limpiar opciones previas

            data.forEach(tipo => {
                const option = document.createElement('option');
                option.value = tipo.idTipoCantidad; // Usamos el ID del DTO
                option.textContent = tipo.nomCantidad; // Nombre del tipo de cantidad
                select.appendChild(option);
            });

        } catch (error) {
            console.error('Error:', error);
            alert('No se pudieron cargar los tipos de unidad.');
        }
    }
    // ===================== FUNCIONES DE DEPURACIÓN =====================
    window.debugProductoEnEdicion = function() {
        console.log('🐛 DEBUG - Estado de edición:');
        console.log('- Producto en edición:', productoEnEdicion);
        console.log('- Insumos actuales:', currentPlatoInsumos);
        console.log('- Insumo seleccionado:', selectedIngredientRow);

        return {
            productoEnEdicion,
            currentPlatoInsumos,
            selectedIngredientRow,
            totalInsumos: currentPlatoInsumos.length
        };
    };


    window.abrirModalCambiarPrecio = abrirModalCambiarPrecio;
    window.confirmarCambioPrecio = confirmarCambioPrecio;
    window.limpiarFormularioCambioPrecio = limpiarFormularioCambioPrecio;
    window.configurarEventListenersCambioPrecio = configurarEventListenersCambioPrecio;
    // ===================== EXPORTAR FUNCIONES =====================
    window.inicializarPaginaPlatos = inicializarPaginaPlatos;
    window.cargarProductos = cargarProductos;
    window.editarProducto = editarProducto;
    window.mostrarHistorialPrecios = mostrarHistorialPrecios;
    window.probarConectividad = probarConectividad;
    window.mostrarHistorialPrecios = mostrarHistorialPrecios;
    window.abrirModalCambiarPrecio = abrirModalCambiarPrecio;
    window.editarProducto = editarProducto;
    window.actualizarProducto = actualizarProducto;
    window.cancelarEdicion = cancelarEdicion;
    window.saveIngredient = saveIngredient;
    const animateElements = document.querySelectorAll(".room-card, .table-card, .card, .table-responsive, .config-tabs");
    animateElements.forEach((el) => observer.observe(el));
   window.loadInsumos = loadInsumos;
   window.saveInsumo = saveInsumo;
   window.editInsumo = editInsumo;
   window.deleteInsumo = deleteInsumo;
   window.clearInsumoForm = clearInsumoForm;
    // ===================== EXPORTAR FUNCIONES NUEVAS =====================
    window.eliminarInsumoDirecto = eliminarInsumoDirecto;
    window.limpiarTodosInsumos = limpiarTodosInsumos;
    window.clearPlatoForm = clearPlatoForm; // Reemplazar la existente
    window.actualizarInfoAyuda = actualizarInfoAyuda;
    window.configurarEventListenersEdicion = configurarEventListenersEdicion;

    window.llenarFormularioParaEdicion = llenarFormularioParaEdicion;
    window.editarProducto = editarProducto;
    window.verificarDatosCargados = verificarDatosCargados;
    window.cargarTiposCantidadDisponibles = cargarTiposCantidadDisponibles;
    // ===================== EXPORTAR FUNCIONES =====================
    window.guardarProducto = guardarProducto;
    window.construirProductoDTO = construirProductoDTO;
    window.validarFormularioCreacion = validarFormularioCreacion;
    window.limpiarFormularioCreacion = limpiarFormularioCreacion;
    window.configurarValidacionTiempoReal = configurarValidacionTiempoReal;
    window.verificarPrerequisitosCreacion = verificarPrerequisitosCreacion;
    window.saveIngredient = saveIngredient; // Reemplazar la existente
window.clearPlatoForm = clearPlatoForm;
window.mostrarError = mostrarError;
window.mostrarExito = mostrarExito;
window.actualizarTituloFormulario = actualizarTituloFormulario;
window.mostrarAyudaContextual = mostrarAyudaContextual;


});
