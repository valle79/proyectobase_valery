# Guía de Configuración - Integración Continua con Jenkins, SonarQube, Slack y JMeter

## Descripción del Proceso Realizado

Este proyecto implementa un pipeline de integración continua que automatiza la compilación, análisis de calidad de código, pruebas de carga y notificaciones. El flujo completo integra cuatro herramientas principales que trabajan de manera coordinada para garantizar la calidad y rendimiento del software.

## Configuración e Integración de Herramientas

### 1. Jenkins
**Configuración:**
- Instalar Jenkins desde https://www.jenkins.io/download/
- Instalar plugins necesarios:
  - SonarQube Scanner
  - Slack Notification
  - Pipeline
  - Maven Integration

**Pasos de configuración:**
1. Crear un nuevo proyecto tipo "Pipeline"
2. En la configuración del proyecto, seleccionar "Pipeline script from SCM"
3. Configurar el repositorio Git del proyecto
4. Jenkins leerá automáticamente el archivo `Jenkinsfile`

**Credenciales a configurar:**
- `sonarqube-token`: Token de autenticación de SonarQube
- `slack-webhook-url`: URL del webhook de Slack

### 2. SonarQube
**Configuración:**
- Descargar SonarQube Community Edition desde https://www.sonarqube.org/downloads/
- Iniciar SonarQube: `bin/windows-x86-64/StartSonar.bat`
- Acceder a http://localhost:9000 (usuario: admin, contraseña: admin)

**Pasos de integración:**
1. Crear un nuevo proyecto en SonarQube con el nombre "vallegrande-project"
2. Generar un token de autenticación
3. Agregar el token como credencial en Jenkins
4. Configurar el servidor SonarQube en Jenkins (Manage Jenkins > Configure System > SonarQube servers)

**Análisis configurado:**
- Detecta code smells, bugs y vulnerabilidades
- Evalúa cobertura de código
- Mide complejidad ciclomática
- Identifica código duplicado

### 3. Slack
**Configuración:**
1. Crear un workspace en Slack
2. Crear un canal para notificaciones (ej: #jenkins-notifications)
3. Configurar Incoming Webhooks:
   - Ir a https://api.slack.com/apps
   - Crear una nueva app
   - Activar "Incoming Webhooks"
   - Agregar webhook al canal deseado
   - Copiar la URL del webhook

**Integración:**
- El pipeline envía notificaciones mediante curl con formato JSON
- Notifica éxito con emoji ✅ y detalles del build
- Notifica errores con emoji ❌ y duración del proceso

### 4. Apache JMeter
**Configuración:**
- Descargar JMeter desde https://jmeter.apache.org/download_jmeter.cgi
- Extraer y agregar la carpeta `bin` al PATH del sistema
- Verificar instalación: `jmeter --version`

**Plan de pruebas configurado:**
- **Usuarios concurrentes:** 50 usuarios
- **Ramp-up period:** 10 segundos
- **Iteraciones:** 5 por usuario
- **Endpoints probados:**
  - GET http://localhost:8081/products
  - GET http://localhost:8081/ping
- **Métricas recopiladas:**
  - Tiempo de respuesta promedio
  - Throughput (peticiones/segundo)
  - Tasa de error
  - Percentiles (90%, 95%, 99%)

## Conexión entre Herramientas

```
[GitHub] → [Jenkins] → [Maven Build] → [SonarQube Analysis] → [Spring Boot Run] → [JMeter Tests] → [Slack Notification]
```

1. **Jenkins** obtiene el código del repositorio
2. **Maven** compila el proyecto (`mvn clean install`)
3. **SonarQube** analiza la calidad del código
4. **Jenkins** verifica el Quality Gate de SonarQube
5. **Spring Boot** inicia la aplicación (`mvn spring-boot:run`)
6. **JMeter** ejecuta pruebas de carga contra los endpoints
7. **Slack** recibe notificación del resultado final

## Resultados Obtenidos

### Problemas Detectados por SonarQube

**1. Variable no utilizada (Code Smell)**
- **Ubicación:** `ApiController.java`, línea 19
- **Problema:** Variable `unused` declarada pero nunca utilizada
- **Mejora propuesta:** Eliminar la variable `String unused = "No se usa";`

**2. Complejidad ciclomática alta (Code Smell)**
- **Ubicación:** `ApiController.java`, método `check()`
- **Problema:** Anidamiento excesivo de condicionales (3 niveles)
- **Mejora propuesta:** Simplificar la lógica a `return (a < b && b < c && c > a) ? "OK" : "FAIL";`

**3. Código duplicado (Duplicación)**
- **Ubicación:** `ProductService.java`, métodos `getAll()` y `getAllAgain()`
- **Problema:** Lógica duplicada en ambos métodos
- **Mejora propuesta:** Eliminar el método `getAllAgain()` y usar directamente `return new ArrayList<>(products);` en `getAll()`

### Ejecución del Pipeline

**Estado:** ✅ Exitoso

**Etapas completadas:**
1. Checkout - Código obtenido correctamente
2. Build - Compilación exitosa con Maven
3. SonarQube Analysis - Análisis completado, 3 code smells detectados
4. Quality Gate - Aprobado (configuración por defecto)
5. Run Application - Aplicación iniciada en puerto 8081
6. JMeter Load Test - Pruebas de carga ejecutadas

**Rendimiento (JMeter):**
- Tiempo de respuesta promedio: ~50ms
- Throughput: ~100 peticiones/segundo
- Tasa de error: 0%
- Todos los endpoints respondieron correctamente bajo carga

**Notificación Slack:**
- Mensaje enviado exitosamente al canal configurado
- Incluye nombre del proyecto, número de build y duración

## Comandos Útiles

```bash
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run

# Análisis de SonarQube
mvn sonar:sonar -Dsonar.projectKey=vallegrande-project -Dsonar.host.url=http://localhost:9000 -Dsonar.login=YOUR_TOKEN

# Ejecutar pruebas de JMeter
jmeter -n -t jmeter-test-plan.jmx -l results.jtl -e -o jmeter-report
```

## Consideraciones Finales

La integración de estas herramientas permite:
- **Automatización completa** del proceso de build y despliegue
- **Detección temprana** de problemas de calidad de código
- **Validación de rendimiento** antes de producción
- **Comunicación efectiva** del estado del proyecto al equipo

El pipeline está diseñado para ejecutarse automáticamente con cada commit, garantizando que el código cumple con los estándares de calidad establecidos.
