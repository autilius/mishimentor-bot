# 🛡️ MishiMentor v4.0: Tu Auditor Técnico Modular en Java

MishiMentor es una suite interactiva de comandos (CLI) de grado profesional diseñada para la auditoría técnica automatizada, detección de vulnerabilidades y refactorización defensiva de código Java. Esta herramienta integra múltiples modelos de inteligencia artificial (locales y cloud), un motor de base de datos documental propio y un núcleo de concurrencia avanzada para transformar el análisis estático de código en un ecosistema agéntico local, seguro y eficiente.

Bajo la estricta supervisión micro-operativa de **Mimi-chan (The Orange Boss)**, la versión 4.0 eleva la suite a un entorno multiproceso blindado.

---

## ✨ Características Principales de la v4.0

*   **⚙️ Multitarea y Concurrencia Avanzada (Nuevo):** Integración de un `FixedThreadPool` (Thread-Pool controlado de fondo) que delega las auditorías pesadas de las APIs y búsquedas web a hilos secundarios. La consola interactiva nunca se congela, permitiéndote seguir navegando por los menús mientras el búnker procesa código en segundo plano.
*   **🔒 Núcleo Criptográfico AES-GCM (Nuevo):** Resguardo blindado y cifrado simétrico robusto de tus credenciales y API Keys locales en el archivo de propiedades. Tus llaves corporativas están protegidas nativamente contra accesos no autorizados.
*   **🧠 Consciencia Operativa Híbrida (8 Motores en Caliente):** Panel de control dinámico que te permite intercambiar los cerebros de IA en caliente desde la terminal según la disponibilidad del entorno real:
   *   **Locales (Ollama):** Llama 3, Codestral (Especialista en código), Phi 3 (Ultra-ligero).
   *   **Simulación:** *MockBrain* (Entorno offline interactivo con respuestas inteligentes para pruebas de desarrollo sin consumo de tokens).
   *   **Cloud:** Google Gemini Pro, OpenAI GPT-4o mini, DeepSeek Chat v3, Anthropic Claude. *(Bloqueados defensivamente de forma dinámica por MishiHealth si no hay red o API keys)*.
*   **🗄️ MishiVault™ (JSON-QL Database Base):** Sistema de persistencia inmutable basado en JSON que organiza de forma automática un árbol genealógico cronológico de tus archivos auditados mediante el mapeo dinámico de linajes (`ParentId`).
*   **📶 MishiHealth™ (Signos Vitales y Resiliencia):** Guardián de infraestructura que ejecuta validaciones síncronas de conectividad a internet en bajo consumo (HTTP de 2 segundos sin descarga de datos) para activar contingencias off-line automáticas.
*   **📊 El Mishiómetro de Seguridad:** Motor de evaluación basado en expresiones regulares (Regex) nativas que analiza el reporte final y calcula una puntuación de pulgas/vulnerabilidades en tiempo real con alertas interactivas en consola.
*   **📄 Formatters de Gala:** Generación paralela de reportes técnicos detallados en Markdown y reportes comerciales formales estructurados en PDF.

---

## 💡 Casos de Uso

MishiMentor optimiza el flujo de trabajo de desarrolladores independientes, freelancers en plataformas como Fiverr, o auditores de software:

*   **Auditorías de Código en Campo:** Detecta fallos lógicos, inyecciones (SQL, XSS) y riesgos OWASP de forma asíncrona y segura.
*   **Refactorización Senior Orientada a SOLID:** Obtén propuestas de Clean Code listas para producción mientras mantienes el búnker operando.
*   **Entregables Comerciales Inmediatos:** Genera reportes PDF firmados para tus clientes de consultoría técnica sin configuraciones tediosas.

---

## 🚀 Instalación y Uso (Versión 4.0)

### Configuración Inicial Defensiva
El asistente de consola síncrono generará de forma local tu entorno en la ruta: `~/.mishi_vault/config.properties`. Con la v4.0, las llaves introducidas se almacenan bajo **cifrado AES-GCM**.

```properties
gemini.api.key=TuApiKeyGemini
openai.api.key=TuApiKeyOpenAI
deepseek.api.key=TuApiKeyDeepSeek
claude.api.key=TuApiKeyClaude
```

### Ejecución de la Suite
1. Clona este repositorio en tu máquina de desarrollo.

2. Compila el proyecto con Maven: ```mvn clean install```

3. Ejecuta el binario compilado. ```MishiHealth``` validará silenciosamente tu red y tus servicios locales.

4. Utiliza la interfaz interactiva para cargar tus scripts ```.java```. El análisis se enviará al pool de hilos de fondo.

5. Al finalizar el análisis, tus reportes (Markdown y PDF) se exportarán automáticamente a la carpeta centralizada ```Documentos/Mishi_Entregables. ```

# 🛡️ 🛠️ Arquitectura de la Suite v4.0

El software está desacoplado bajo principios SOLID estrictos para asegurar el aislamiento de responsabilidades:

* ```com.bugotruco.mishimentor.MishiConsole```**: Orquestador de interfaz de comandos con menús adaptativos basados en hilos de ejecución concurrentes.**

* ```com.bugotruco.mishimentor.MishiOrchestrator```**: Flujo central encargado de despachar las tareas de análisis de manera asíncrona al pool.**

* ```com.bugotruco.mishimentor.MishiVault```**: Motor central de base de datos documental que gestiona la persistencia de los recuerdos y la resolución de linajes históricos.**

* ```com.bugotruco.mishimentor.MishiHealth```**: Capa médica del sistema; aislamiento y diagnóstico de red de alto rendimiento.**

* ```com.bugotruco.mishimentor.MishiEvaluador```**: Componente analítico encargado del parseo Regex y ejecución del Mishiómetro.**


## 🤝 Contribuciones, Soporte y Comunidad

Este software es un entorno vivo de experimentación técnica enfocado en llevar la automatización con IA al metal real en entornos de desarrollo de software. Si esta suite te ha servido para optimizar tus proyectos o tus entregables profesionales y deseas apoyar su mantenimiento en el búnker:

| Plataforma | Enlace |
| :--- | :--- |
| **☕ Cafecito** | [Invítame un café](https://ko-fi.com/bugotruco) |
| **📺 YouTube** | [Canal Oficial Bugotruco](https://www.youtube.com/@Bugotruco) |
| **💸 Donar** | [Apoya el búnker vía PayPal](https://www.paypal.com/paypalme/ChavaGranados) |

Creado con 💖 por Salvador (Autilius) Granados Godínez Java Developer Senior enfocado en Clean Code, Software Architecture y Seguridad. Supervisado rigurosamente por Mimi-chan (Senior Project Manager 🍊🐈).
