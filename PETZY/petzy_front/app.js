// Dirección del backend
const API_URL = "http://localhost:8080/api/v1";

const $ = (id) => document.getElementById(id);

// Listas que se llenan con los datos del backend
let productos = [];
let categorias = [];

// Funciones de apoyo

// Evita que un nombre con HTML se interprete como código
function esc(texto) {
    return String(texto ?? "").replace(/[&<>"']/g, (c) => ({
        "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;"
    }[c]));
}

function formatoCOP(valor) {
    return Number(valor).toLocaleString("es-CO", {
        style: "currency",
        currency: "COP",
        maximumFractionDigits: 0
    });
}

let temporizadorMensaje;
function mostrarMensaje(texto, tipo = "ok") {
    const caja = $("mensaje");
    caja.textContent = texto;
    caja.className = `toast ${tipo === "error" ? "error" : ""}`;
    caja.hidden = false;

    clearTimeout(temporizadorMensaje);
    temporizadorMensaje = setTimeout(() => { caja.hidden = true; }, 4500);
}

function marcarConexion(ok) {
    const estado = $("estadoConexion");
    estado.textContent = ok ? "Backend conectado" : "Backend sin conexión";
    estado.className = `conexion ${ok ? "ok" : "error"}`;
}

// Todas las peticiones al backend pasan por aquí
async function api(ruta, opciones = {}) {
    const config = { ...opciones };

    if (config.body) {
        config.headers = { "Content-Type": "application/json" };
    }

    let respuesta;
    try {
        respuesta = await fetch(`${API_URL}${ruta}`, config);
    } catch {
        marcarConexion(false);
        throw new Error(
            "No hay conexión con el backend. Verifica que esté ejecutándose en el puerto 8080."
        );
    }

    marcarConexion(true);

    if (!respuesta.ok) {
        let detalle = "";
        try {
            const cuerpo = await respuesta.json();
            detalle = cuerpo.message || "";
        } catch {
            // la respuesta no tenía JSON
        }

        throw new Error(
            detalle ||
            `El servidor no pudo completar la operación (error ${respuesta.status}). ` +
            "Revisa los datos, por ejemplo nombres o correos repetidos."
        );
    }

    return respuesta.status === 204 ? null : respuesta.json();
}

// Cambiar entre Inicio y Productos

const VISTAS = ["inicio", "productos"];

function mostrarVista() {
    const hash = location.hash.slice(1);
    const vista = VISTAS.includes(hash) ? hash : "inicio";

    document.querySelectorAll(".view").forEach((seccion) => {
        seccion.hidden = seccion.id !== `vista-${vista}`;
    });

    document.querySelectorAll(".nav-link").forEach((enlace) => {
        enlace.classList.toggle("active", enlace.dataset.vista === vista);
    });
}

window.addEventListener("hashchange", mostrarVista);

// Ventanas emergentes

document.querySelectorAll("dialog").forEach((dialogo) => {
    // Botón cancelar
    dialogo.querySelectorAll("[data-cerrar]").forEach((boton) => {
        boton.addEventListener("click", () => dialogo.close());
    });

    // Cierra la ventana al hacer clic en el fondo
    dialogo.addEventListener("click", (evento) => {
        // Ignora los clics dentro de la ventana (campos y listas)
        if (evento.target !== dialogo) return;

        const caja = dialogo.getBoundingClientRect();
        const fuera =
            evento.clientX < caja.left || evento.clientX > caja.right ||
            evento.clientY < caja.top || evento.clientY > caja.bottom;
        if (fuera) dialogo.close();
    });
});

// Categorías

async function cargarCategorias() {
    try {
        // Esta ruta está en ProductController, no en CatalogController
        categorias = await api("/products/product-categories");
    } catch (error) {
        categorias = [];
        mostrarMensaje(error.message, "error");
    }

    $("totalCategorias").textContent = categorias.length;
    pintarSelectoresCategoria();
}

function pintarSelectoresCategoria() {
    const selector = $("categoria");
    const filtro = $("filtroCategoria");
    const filtroActual = filtro.value;

    const opciones = categorias
        .map((c) => `<option value="${c.id}">${esc(c.name)}</option>`)
        .join("");

    selector.innerHTML = categorias.length
        ? `<option value="">Selecciona una categoría</option>${opciones}`
        : `<option value="">No hay categorías disponibles</option>`;

    filtro.innerHTML = `<option value="">Todas las categorías</option>${opciones}`;
    filtro.value = filtroActual; // mantiene el filtro elegido

    $("hintCategoria").hidden = categorias.length > 0;
}

$("btnNuevaCategoria").addEventListener("click", () => {
    $("formCategoria").reset();
    $("modalCategoria").showModal();
});

$("formCategoria").addEventListener("submit", async (evento) => {
    evento.preventDefault();

    const categoria = {
        name: $("categoriaNombre").value.trim(),
        description: $("categoriaDescripcion").value.trim()
    };

    if (!categoria.name) {
        mostrarMensaje("El nombre de la categoría es obligatorio.", "error");
        return;
    }

    try {
        await api("/catalogs/product-categories", {
            method: "POST",
            body: JSON.stringify(categoria)
        });

        $("modalCategoria").close();
        mostrarMensaje("Categoría creada correctamente.");
        await cargarCategorias();
    } catch (error) {
        mostrarMensaje(error.message, "error");
    }
});

// Productos

async function cargarProductos() {
    try {
        productos = await api("/products");
        $("totalProductos").textContent = productos.length;
        renderizarProductos();
    } catch (error) {
        productos = [];
        $("tablaProductos").innerHTML =
            `<tr><td colspan="5" class="empty">No se pudieron cargar los productos.</td></tr>`;
        mostrarMensaje(error.message, "error");
    }
}

function etiquetaStock(stock) {
    if (stock === 0) return `<span class="badge badge-danger">Agotado</span>`;
    if (stock <= 5) return `${stock} <span class="badge badge-warn">Bajo</span>`;
    return stock;
}

function renderizarProductos() {
    const categoriaId = $("filtroCategoria").value;

    const lista = categoriaId
        ? productos.filter((p) => String(p.category?.id) === categoriaId)
        : productos;

    if (lista.length === 0) {
        $("tablaProductos").innerHTML = `
            <tr><td colspan="5" class="empty">No hay productos registrados.</td></tr>`;
        return;
    }

    $("tablaProductos").innerHTML = lista.map((p) => `
        <tr>
            <td>${esc(p.name)}</td>
            <td>${esc(p.category?.name ?? "Sin categoría")}</td>
            <td>${formatoCOP(p.price)}</td>
            <td>${etiquetaStock(p.stock)}</td>
            <td>
                <button class="btn-secondary" data-editar="${p.id}">Editar</button>
                <button class="btn-danger" data-desactivar="${p.id}">Desactivar</button>
            </td>
        </tr>
    `).join("");
}

$("filtroCategoria").addEventListener("change", renderizarProductos);

$("btnNuevoProducto").addEventListener("click", () => {
    $("formProducto").reset();
    $("productoId").value = "";
    $("tituloFormulario").textContent = "Nuevo producto";
    $("modalProducto").showModal();
});

$("formProducto").addEventListener("submit", async (evento) => {
    evento.preventDefault();

    const id = $("productoId").value;

    const producto = {
        name: $("nombre").value.trim(),
        price: Number($("precio").value),
        stock: Number($("stock").value),
        categoryId: Number($("categoria").value)
    };

    if (!producto.name || !Number.isFinite(producto.price) || producto.price <= 0 ||
        !Number.isInteger(producto.stock) || producto.stock < 0 || !producto.categoryId) {
        mostrarMensaje("Revisa los datos del producto (incluida la categoría).", "error");
        return;
    }

    try {
        await api(id ? `/products/${id}` : "/products", {
            method: id ? "PUT" : "POST",
            body: JSON.stringify(producto)
        });

        $("modalProducto").close();
        mostrarMensaje(id ? "Producto actualizado." : "Producto creado correctamente.");
        await cargarProductos();
    } catch (error) {
        mostrarMensaje(error.message, "error");
    }
});

$("tablaProductos").addEventListener("click", async (evento) => {
    const botonEditar = evento.target.closest("[data-editar]");
    const botonDesactivar = evento.target.closest("[data-desactivar]");

    if (botonEditar) {
        const producto = productos.find((p) => String(p.id) === botonEditar.dataset.editar);
        if (!producto) return;

        $("productoId").value = producto.id;
        $("nombre").value = producto.name;
        $("precio").value = producto.price;
        $("stock").value = producto.stock;
        $("categoria").value = producto.category?.id ?? "";
        $("tituloFormulario").textContent = "Editar producto";
        $("modalProducto").showModal();
    }

    if (botonDesactivar) {
        if (!confirm("¿Deseas desactivar este producto?")) return;

        try {
            await api(`/products/${botonDesactivar.dataset.desactivar}`, { method: "DELETE" });
            mostrarMensaje("Producto desactivado correctamente.");
            await cargarProductos();
        } catch (error) {
            mostrarMensaje(error.message, "error");
        }
    }
});


// Inicio de la página

async function iniciarAplicacion() {
    mostrarVista();

    await cargarCategorias();
    await cargarProductos();
}

iniciarAplicacion();
