/**
 * SENA Spring Boot - JavaScript Principal
 * ========================================
 */

// Esperar a que el DOM esté completamente cargado
document.addEventListener('DOMContentLoaded', function() {
    console.log('SENA Spring Boot - Aplicación inicializada');
    
    // Inicializar tooltips de Bootstrap
    initializeTooltips();
    
    // Auto-cerrar alertas después de 5 segundos
    autoCloseAlerts();
    
    // Confirmar acciones de eliminación
    setupDeleteConfirmations();
    
    // Validación de formularios
    setupFormValidation();
    
    // Animaciones de entrada
    addFadeInAnimations();
});

/**
 * Inicializar tooltips de Bootstrap
 */
function initializeTooltips() {
    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => 
        new bootstrap.Tooltip(tooltipTriggerEl)
    );
}

/**
 * Auto-cerrar alertas después de 5 segundos
 */
function autoCloseAlerts() {
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });
}

/**
 * Configurar confirmaciones de eliminación
 */
function setupDeleteConfirmations() {
    const deleteButtons = document.querySelectorAll('[data-action="delete"]');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (!confirm('¿Está seguro de que desea eliminar este registro?')) {
                e.preventDefault();
            }
        });
    });
}

/**
 * Configurar validación de formularios
 */
function setupFormValidation() {
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
}

/**
 * Añadir animaciones de entrada a los elementos
 */
function addFadeInAnimations() {
    const elements = document.querySelectorAll('.card, .table');
    elements.forEach((element, index) => {
        element.classList.add('fade-in');
        element.style.animationDelay = `${index * 0.1}s`;
    });
}

/**
 * Función auxiliar para formatear fechas
 */
function formatDate(dateString) {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
}

/**
 * Función auxiliar para validar email
 */
function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(String(email).toLowerCase());
}

/**
 * Función auxiliar para validar teléfono
 */
function validatePhone(phone) {
    const re = /^[0-9]{7,15}$/;
    return re.test(String(phone));
}

/**
 * Mostrar notificación toast (requiere librería adicional o implementación custom)
 */
function showToast(message, type = 'info') {
    // Implementación básica con alert (puede mejorarse con librería toast)
    console.log(`[${type.toUpperCase()}] ${message}`);
    
    // Crear toast personalizado
    const toastContainer = document.getElementById('toast-container');
    if (toastContainer) {
        const toast = document.createElement('div');
        toast.className = `toast align-items-center text-white bg-${type} border-0`;
        toast.setAttribute('role', 'alert');
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">${message}</div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        `;
        toastContainer.appendChild(toast);
        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();
        
        // Eliminar el toast después de que se oculte
        toast.addEventListener('hidden.bs.toast', () => {
            toast.remove();
        });
    }
}

/**
 * Función para confirmar navegación con cambios sin guardar
 */
function confirmNavigation() {
    let formChanged = false;
    const forms = document.querySelectorAll('form');
    
    forms.forEach(form => {
        form.addEventListener('change', () => {
            formChanged = true;
        });
        
        form.addEventListener('submit', () => {
            formChanged = false;
        });
    });
    
    window.addEventListener('beforeunload', (e) => {
        if (formChanged) {
            e.preventDefault();
            e.returnValue = '';
        }
    });
}

/**
 * Filtro de búsqueda en tablas
 */
function setupTableSearch(tableId, searchInputId) {
    const searchInput = document.getElementById(searchInputId);
    const table = document.getElementById(tableId);
    
    if (searchInput && table) {
        searchInput.addEventListener('keyup', function() {
            const filter = this.value.toLowerCase();
            const rows = table.getElementsByTagName('tbody')[0].getElementsByTagName('tr');
            
            Array.from(rows).forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(filter) ? '' : 'none';
            });
        });
    }
}

/**
 * Configurar DataTables con opciones en español
 */
function initializeDataTable(tableId) {
    if ($.fn.DataTable) {
        $(`#${tableId}`).DataTable({
            language: {
                url: '//cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json'
            },
            pageLength: 10,
            responsive: true,
            order: [[1, 'asc']],
            dom: 'Bfrtip',
            buttons: ['copy', 'csv', 'excel', 'pdf', 'print']
        });
    }
}

// Exportar funciones para uso global
window.senaApp = {
    showToast,
    formatDate,
    validateEmail,
    validatePhone,
    setupTableSearch,
    initializeDataTable,
    confirmNavigation
};

console.log('SENA App utilities loaded successfully');
