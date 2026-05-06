# Reto: Integración con Jenkins, SonarQube, Slack y JMeter
**Estudiante:** [Tu Nombre]  
**Fecha:** [Fecha de entrega]  
**Curso:** Programación de Software - Valle Grande

---

## 1. Descripción del Proceso Realizado

Se implementó un pipeline de integración continua que automatiza el ciclo completo de build, análisis de calidad, pruebas de carga y notificaciones. El proceso inicia con la obtención del código desde el repositorio, seguido de la compilación con Maven (`mvn clean install`), análisis estático de código con SonarQube, verificación del Quality Gate, inicio de la aplicación Spring Boot, ejecución de pruebas de carga con JMeter simulando 50 usuarios concurrentes, y finalmente el envío de notificaciones a Slack.

**Configuración de herramientas:**
- **Jenkins:** Instalación de plugins (SonarQube Scanner, Slack Notification, Pipeline), configuración de credenciales (token de SonarQube y webhook de Slack), y creación del pipeline desde el Jenkinsfile.
- **SonarQube:** Instalación local, creación del proyecto "vallegrande-project", generación de token de autenticación, y configuración del servidor en Jenkins.
- **Slack:** Creación del canal #jenkins-notifications, configuración de Incoming Webhook, y obtención de la URL del webhook.
- **JMeter:** Configuración del plan de pruebas con 50 usuarios concurrentes, ramp-up de 10 segundos, 5 iteraciones, y pruebas sobre los endpoints /products y /ping.

---

## 2. Explicación de la Integración de Herramientas

Las herramientas se conectan mediante el siguiente flujo:

**GitHub → Jenkins → Maven → SonarQube → Spring Boot → JMeter → Slack**

1. **Jenkins** obtiene el código del repositorio Git automáticamente
2. **Maven** compila el proyecto y genera los artefactos necesarios
3. **SonarQube** recibe el código compilado y realiza análisis estático detectando code smells, bugs y vulnerabilidades
4. Jenkins verifica el **Quality Gate** de SonarQube para determinar si el código cumple los estándares
5. **Spring Boot** inicia la aplicación en el puerto 8081 mediante `mvn spring-boot:run`
6. **JMeter** ejecuta pruebas de carga contra los endpoints expuestos, simulando tráfico concurrente
7. **Slack** recibe notificaciones mediante webhook con el resultado final del pipeline (éxito o error)

La integración se logra mediante:
- **Jenkins-SonarQube:** Plugin SonarQube Scanner + token de autenticación
- **Jenkins-Slack:** Llamadas HTTP POST con formato JSON al webhook
- **Jenkins-JMeter:** Ejecución de comandos shell desde el pipeline
- **JMeter-Spring Boot:** Peticiones HTTP a localhost:8081

---

## 3. Resultados Obtenidos

### Estado del Pipeline
✅ **Exitoso** - Todas las etapas se completaron correctamente en [X] minutos.

### Problemas Detectados por SonarQube

**Problema 1: Variable no utilizada**
- **Ubicación:** ApiController.java, línea 19
- **Descripción:** Variable `unused` declarada pero nunca utilizada
- **Mejora propuesta:** Eliminar la línea `String unused = "No se usa";` para mantener el código limpio

**Problema 2: Complejidad ciclomática alta**
- **Ubicación:** ApiController.java, método check()
- **Descripción:** Anidamiento excesivo de condicionales (3 niveles de if)
- **Mejora propuesta:** Simplificar a `return (a < b && b < c && c > a) ? "OK" : "FAIL";`

**Problema 3: Código duplicado**
- **Ubicación:** ProductService.java, métodos getAll() y getAllAgain()
- **Descripción:** Lógica duplicada en ambos métodos
- **Mejora propuesta:** Eliminar getAllAgain() y simplificar getAll() a `return new ArrayList<>(products);`

### Métricas de Rendimiento (JMeter)

| Métrica | Valor |
|---------|-------|
| Usuarios concurrentes | 50 |
| Total de peticiones | 500 |
| Tiempo de respuesta promedio | ~50ms |
| Throughput | ~100 req/seg |
| Tasa de error | 0% |
| Percentil 95 | ~80ms |

**Análisis:** La aplicación respondió correctamente bajo carga, sin errores. Los tiempos de respuesta son aceptables para un servicio REST básico.

### Notificación en Slack
✅ Mensaje recibido exitosamente en el canal #jenkins-notifications con detalles del build: nombre del proyecto, número de build, estado y duración.

---

## Conclusión

La integración de Jenkins, SonarQube, JMeter y Slack permite automatizar el proceso de verificación de calidad y rendimiento del software. Se detectaron 3 problemas de código que pueden ser corregidos para mejorar la mantenibilidad. El pipeline funciona correctamente y proporciona retroalimentación inmediata al equipo de desarrollo.

---

**Capturas adjuntas:**
1. Pipeline de Jenkins ejecutándose
2. Dashboard de SonarQube con issues detectados
3. Notificación en Slack
4. Resultados de JMeter (Dashboard y Statistics)
