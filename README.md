# 🛡️ Mishi Mentor Pro v3.0: Tu Auditor Técnico Modular en Java

Mishi Mentor Pro es una suite interactiva de comandos (CLI) de grado profesional diseñada para la auditoría técnica automatizada, detección de vulnerabilidades y refactorización defensiva de código Java. Esta herramienta integra múltiples modelos de inteligencia artificial (locales y cloud) y un motor de base de datos documental propio para transformar el análisis estático de código en un ecosistema agéntico local, seguro y eficiente.


## ✨ Características Principales de la v3.0

* **MishiVault™ (JSON-QL Database Base):** Sistema de persistencia inmutable basado en JSON que organiza de forma automática un árbol genealógico cronológico de tus archivos auditados mediante el mapeo dinámico de linajes (ParentId).

* **MishiHealth™ (Signos Vitales y Resiliencia):** Guardián de infraestructura que ejecuta validaciones síncronas de conectividad a internet en bajo consumo (HTTP de 2 segundos sin descarga de datos) y verifica el estado de Ollama antes de la ejecución de hilos.

* **Consciencia Operativa Híbrida (Motores en Caliente):** Panel de control dinámico que te permite intercambiar los cerebros de IA desde la terminal según la disponibilidad del entorno real:

* * **Locales (Ollama):** Llama 3, Codestral, Phi 3.

* * Simulación: **MockBrain** (Entorno offline interactivo con respuestas inteligentes para pruebas de desarrollo local sin consumo de tokens).

* * Cloud: Google Gemini Pro, OpenAI GPT-4o mini, DeepSeek Chat v3, Anthropic Claude (Bloqueados defensivamente de forma dinámica si no hay red o API keys).

* **El Mishiómetro de Seguridad:** Motor de evaluación basado en expresiones regulares (Regex) nativas que analiza el reporte final y calcula una puntuación de pulgas/vulnerabilidades en tiempo real con alertas interactivas en consola.

* **Formatters de Gala:** Generación paralela de reportes técnicos detallados en Markdown y reportes comerciales formales estructurados en PDF.

## 💡 Casos de Uso

Mishi Mentor Pro optimiza el flujo de trabajo de desarrolladores independientes, freelancers en plataformas como Fiverr, o auditores de software:

* **Auditorías de Código en Campo:** Detecta fallos lógicos, inyecciones (SQL, XSS) y riesgos OWASP de forma segura.

* **Refactorización Senior Orientada a SOLID:** Obtén propuestas de Clean Code listas para producción.

* **Entregables Comerciales Inmediatos:** Genera reportes PDF firmados para tus clientes de consultoría técnica sin configuraciones tediosas.

## 🚀 Instalación y Uso (Versión 3.0)

### Configuración Inicial Defensiva
Para garantizar la seguridad de tus credenciales corporativas, el archivo de propiedades se genera de forma local utilizando el asistente de consola síncrono, asegurando que tus llaves nunca se expongan en repositorios públicos.

1. Clona este repositorio en tu máquina de desarrollo.

2. Compila el proyecto con Maven: mvn clean install.

3. Al arrancar por primera vez, ConsoleWizard detectará la falta del entorno y te guiará en la terminal para generar tu archivo:
   ```~/.mishi_vault/config.properties```
 ```properties
gemini.api.key=TuApiKeyGemini
openai.api.key=TuApiKeyOpenAI
deepseek.api.key=TuApiKeyDeepSeek
claude.api.key=TuApiKeyClaude
```

### Ejecución de la Suite
1. Ejecuta la aplicación desde la consola.

2. MishiHealth validará silenciosamente tu red y tus servicios locales.

3. Utiliza la interfaz interactiva para cargar tus scripts .java.

4. Accede al menú dinámico para cambiar de cerebro en caliente de manera segura.

5. Al finalizar el análisis, tus reportes (Markdown y PDF) se exportarán automáticamente a la carpeta centralizada ```Documentos/Mishi_Entregables. ```

# 🛡️ 🛠️ Arquitectura de la Suite v3.0

El software está desacoplado bajo principios SOLID estrictos para asegurar el aislamiento de responsabilidades:

* ```com.bugotruco.MishiVault:``` **Motor central de base de datos documental que gestiona la persistencia de los recuerdos y la resolución de linajes históricos.**

* ```com.bugotruco.MishiHealth:``` **Capa médica del sistema; aislamiento y diagnóstico de red de alto rendimiento.**

* ```com.bugotruco.MishiConsole:``` **Orquestador de interfaz de comandos con menús adaptativos basados en estados de hardware.**

* ```com.bugotruco.brains.MockBrain:``` **Clon lógico e interactivo para emulación y pruebas unitarias de persistencia en entornos de desconexión.**

* ```com.bugotruco.MishiEvaluador:``` **Componente analítico encargado del parseo Regex y ejecución del Mishiómetro.**


## 🤝 Contribuciones, Soporte y Comunidad

Este software es un entorno vivo de experimentación técnica enfocado en llevar la automatización con IA al metal real en entornos de desarrollo de software. ¡Las ideas para la futura versión 4.0 (Cifrado militar AES-GCM de credenciales y Thread-Pools con ratones concurrentes) ya están en marcha!

Si esta suite te ha servido para optimizar tus proyectos o tus entregables profesionales y deseas apoyar su mantenimiento:


| Plataforma | Enlace |
| :--- | :--- |
| **☕ Cafecito** | [Invítame un café](https://ko-fi.com/bugotruco) |
| **📺 YouTube** | [Canal Oficial Bugotruco](https://www.youtube.com/@Bugotruco) |
| **💼 LinkedIn** | [Conectar con Salvador](https://www.linkedin.com/in/salvador-granados-god%C3%ADnez-699a122aa) |
| **💸 Donar** | [Apoya el búnker vía PayPal](https://www.paypal.com/paypalme/ChavaGranados) |

Creado con 💖 por Salvador (Autilius) Granados Godínez Java Developer Senior enfocado en Clean Code, Software Architecture y Seguridad. Supervisado rigurosamente por Mimi-chan (Senior Project Manager 🍊🐈).
