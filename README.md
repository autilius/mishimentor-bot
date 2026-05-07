🛡️ Mishi Mentor v2.0
Auditoría Técnica de Java con Elegancia Felina.

Mishi Mentor es una herramienta de orquestación diseñada para desarrolladores que buscan elevar la calidad de su código. No solo refactoriza; audita, detecta vulnerabilidades y genera reportes comerciales listos para entregar. Este proyecto representa la evolución técnica hacia la versión 2.0 de la herramienta, enfocada en la automatización de procesos de auditoría.

🚀 Características Principales
MishiVault™: Sistema de persistencia basado en JSON para mantener un historial de auditorías inmutable sin alterar los archivos originales.

Análisis de Seguridad: Identificación de hallazgos (Findings) clasificados por severidad (High, Medium, Low) mediante auditorías técnicas rigurosas.

Exportación Multiformato: Generación automática de reportes de gala en PDF y documentación técnica en Markdown a partir de logs en JSON.

Clean Code Architecture: Construido bajo principios SOLID, patrones de diseño y optimización de rendimiento para garantizar un mantenimiento sencillo y profesional.

🛠️ Stack Tecnológico
Lenguaje: Java 17+ (especializado en arquitectura de software y seguridad).

Gestión de Datos: Jackson para la serialización y conversión de datos JSON a formatos comerciales.

Motor de PDF: OpenPDF para la generación de reportes con estética profesional.

Arquitectura: Modular / Service-Oriented.

📦 Instalación y Uso
Clonar el repositorio: git clone ...

Compilar: mvn clean install

Ejecutar: Inicia el MishiOrchestrator y sigue las instrucciones de la mishi-consola.

📂 Estructura del Reporte
Cada auditoría genera un objeto MishiNode que actúa como contenedor de la información recuperada para el motor de reportes e incluye:

Verdict: El análisis cualitativo y veredicto de la auditoría.

Refactored Code: El código optimizado y limpio siguiendo los más altos estándares de desarrollo.

Security Findings: Lista detallada de riesgos y vulnerabilidades detectadas.

⚙️ Configuración (config.properties)
El sistema requiere de una configuración externa para gestionar las llaves de API y los prompts de sistema que definen el comportamiento de la IA durante la auditoría.

Properties
# Credenciales de API
gemini.api.key=TuApiKeyGemini
gemini.api.url=TuUrlGemini
tavily.api.key=TuApiKeyTavily
tavily.api.url=TuUrlTavily

# Zona de Prompts generales

# Prompt principal de la aplicación (System Prompt)
mishi.system.prompt=Aquí va tu prompt de sistema

Este proyecto forma parte de un portafolio profesional en GitHub desarrollado por un especialista en Java y seguridad técnica. Para servicios de consultoría, auditorías de proyectos o desarrollo de herramientas a medida, consulte el perfil profesional en Fiverr.

Desarrollado con un enfoque en la monetización de software open-source y calidad técnica.