Gestor de Clientes CRM - Aplicación Android
Aplicación móvil de gestión de clientes desarrollada en Kotlin y XML para Android, implementando un sistema CRUD completo con persistencia en SQLite.

Descripción
Esta aplicación permite gestionar una base de datos de clientes de forma sencilla e intuitiva. Los usuarios pueden:

Agregar nuevos clientes
Buscar clientes en tiempo real
Editar información de clientes existentes
Eliminar clientes con confirmación
Ver el contador total de clientes


Características Principales
Funcionalidades Implementadas

CRUD Completo: Create, Read, Update y Delete de clientes
Persistencia de Datos: SQLite con SQLiteOpenHelper
Búsqueda en Tiempo Real: Filtro por nombre o correo electrónico
Validaciones de Formulario:

Campos obligatorios
Formato de email válido
Teléfono con mínimo 9 dígitos

Interfaz Moderna: Material Design con RecyclerView y CardViews
Confirmación de Eliminación: AlertDialog antes de borrar
Contador de Clientes: Muestra el total en tiempo real
Gestos Intuitivos:

Toque simple para editar
Toque prolongado para eliminar

Arquitectura del Proyecto
Estructura de Archivos
com.ejemplo.crmclientes/
├── MainActivity.kt                    # Pantalla principal con lista
├── FormularioClienteActivity.kt      # Formulario agregar/editar
├── Cliente.kt                         # Modelo de datos
├── ClienteAdapter.kt                  # Adaptador RecyclerView
└── DatabaseHelper.kt                  # Gestión SQLite
Modelo de Datos
Tabla: clientes
CampoTipoDescripciónidINTEGERClave primaria (AUTO)nombreTEXTNombre del clienteemailTEXTCorreo electrónicotelefonoTEXTNúmero de teléfono

Instalación y Ejecución
Requisitos Previos

Android Studio Hedgehog (2023.1.1) o superior
Kotlin 1.9.0 o superior
SDK mínimo: API 24 (Android 7.0)
SDK objetivo: API 34 (Android 14)

Pasos para Ejecutar

Clonar o descargar el proyecto

bash   git clone <URL_DEL_REPOSITORIO>

Abrir en Android Studio

File → Open → Seleccionar la carpeta del proyecto

Sincronizar Gradle

Esperar a que Android Studio descargue las dependencias
Si hay errores, hacer click en "Sync Now"


Configurar el dispositivo

Conectar un dispositivo Android con USB debugging activado
O crear un emulador (AVD) desde Device Manager


Ejecutar la aplicación

Hacer click en el botón Run
Seleccionar el dispositivo/emulador
Esperar a que se instale y ejecute

Tecnologías Utilizadas

Lenguaje: Kotlin 1.9
UI: XML Layouts
Base de Datos: SQLite con SQLiteOpenHelper
Componentes Android:

RecyclerView con Adapter personalizado
Material Design Components
CardView para items
FloatingActionButton
AlertDialog
TextInputLayout

Patrones:

Data Class para modelo
ViewHolder Pattern
CRUD completo

Funcionalidades Extras Implementadas
Búsqueda en tiempo real - Filtra mientras escribes
Contador de clientes - Actualización automática
AlertDialog - Confirmación antes de eliminar
Validaciones completas - Formato email, longitud teléfono
Interfaz profesional - Material Design, CardViews
Gestos intuitivos - Click y long click

Desarrollo
Validaciones Implementadas
kotlin// Nombre: Campo obligatorio
// Email: Campo obligatorio + formato válido
// Teléfono: Campo obligatorio + mínimo 9 dígitos + solo números
Operaciones CRUD

Create: insertarCliente(cliente) → Agrega a SQLite
Read: obtenerTodosLosClientes() → Lista completa
Update: actualizarCliente(cliente) → Modifica registro
Delete: eliminarCliente(id) → Elimina con confirmación


Ciclo de Vida
La aplicación maneja correctamente el ciclo de vida de las Activities:

onCreate(): Inicialización de vistas y base de datos
onResume(): Recarga de datos al volver a la pantalla
onActivityResult(): Manejo de resultados de formulario

Verificar que hay clientes en la base de datos
Revisar logs en Logcat
