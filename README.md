# S11 | AP5 | Reto: Integración con Jenkins, SonarQube, Slack y JMeter

## 📋 Descripción del Proyecto

Proyecto base de Spring Boot para implementar un flujo de integración continua utilizando Jenkins, SonarQube, Slack y JMeter. El objetivo es automatizar la compilación, análisis de calidad de código, pruebas de carga y notificaciones.

## 🛠️ Tecnologías Utilizadas

- **Java 11+** - Lenguaje de programación
- **Spring Boot 2.7.5** - Framework de aplicación
- **Maven** - Gestión de dependencias y build
- **Jenkins** - Servidor de integración continua
- **SonarQube** - Análisis de calidad de código
- **Apache JMeter** - Pruebas de carga y rendimiento
- **Slack** - Notificaciones del pipeline

## 🚀 Endpoints Disponibles

La aplicación expone los siguientes endpoints en `http://localhost:8081`:

- `GET /` - Mensaje de bienvenida
- `GET /products` - Lista de productos
- `GET /check` - Verificación de estado
- `GET /ping` - Health check

## 📁 Estructura del Proyecto

```
.
├── src/
│   └── main/
│       ├── java/vallegrande/edu/pe/
│       │   ├── DemoApplication.java
│       │   ├── controller/
│       │   │   └── ApiController.java
│       │   ├── model/
│       │   │   └── Product.java
│       │   └── service/
│       │       └── ProductService.java
│       └── resources/
│           └── application.properties
├── Jenkinsfile                      # Pipeline de Jenkins
├── sonar-project.properties         # Configuración de SonarQube
├── jmeter-test-plan.jmx            # Plan de pruebas de JMeter
├── GUIA_CONFIGURACION.md           # Guía detallada de configuración
├── INSTRUCCIONES_PASO_A_PASO.md    # Instrucciones paso a paso
└── pom.xml                          # Configuración de Maven
```

## 📖 Documentación

- **[INSTRUCCIONES_PASO_A_PASO.md](INSTRUCCIONES_PASO_A_PASO.md)** - Guía completa paso a paso para ejecutar el reto
- **[GUIA_CONFIGURACION.md](GUIA_CONFIGURACION.md)** - Documento técnico con configuración detallada y resultados

## ⚡ Inicio Rápido

### 1. Compilar el proyecto
```bash
mvn clean install
```

### 2. Ejecutar la aplicación
```bash
mvn spring-boot:run
```

### 3. Verificar funcionamiento
```bash
curl http://localhost:8081/products
```

## 🔄 Pipeline de Integración Continua

El pipeline de Jenkins ejecuta las siguientes etapas:

1. **Checkout** - Obtiene el código del repositorio
2. **Build** - Compila el proyecto con Maven
3. **SonarQube Analysis** - Analiza la calidad del código
4. **Quality Gate** - Verifica los estándares de calidad
5. **Run Application** - Inicia la aplicación Spring Boot
6. **JMeter Load Test** - Ejecuta pruebas de carga
7. **Slack Notification** - Envía notificación del resultado

## 📊 Análisis de Calidad (SonarQube)

El proyecto incluye intencionalmente problemas de código para demostrar las capacidades de SonarQube:

### Problemas Detectados

1. **Variable no utilizada** (`ApiController.java`)
   - Variable `unused` declarada pero nunca usada
   - **Mejora:** Eliminar la variable

2. **Complejidad ciclomática alta** (`ApiController.java`)
   - Método `check()` con anidamiento excesivo
   - **Mejora:** Simplificar la lógica condicional

3. **Código duplicado** (`ProductService.java`)
   - Métodos `getAll()` y `getAllAgain()` con lógica duplicada
   - **Mejora:** Eliminar duplicación y simplificar

## 🧪 Pruebas de Carga (JMeter)

Configuración del plan de pruebas:
- **Usuarios concurrentes:** 50
- **Ramp-up period:** 10 segundos
- **Iteraciones:** 5 por usuario
- **Endpoints probados:** `/products` y `/ping`

### Métricas Evaluadas
- Tiempo de respuesta promedio
- Throughput (peticiones/segundo)
- Tasa de error
- Percentiles (90%, 95%, 99%)

## 📢 Notificaciones (Slack)

El pipeline envía notificaciones automáticas a Slack:
- ✅ **Éxito:** Mensaje con detalles del build exitoso
- ❌ **Error:** Mensaje con información del fallo

## 📦 Entregables del Reto

1. ✅ Captura del pipeline en Jenkins (4 puntos)
2. ✅ Captura del análisis en SonarQube (3 puntos)
   - Identificar 3 problemas con propuestas de mejora
3. ✅ Captura de notificación en Slack (3 puntos)
4. ✅ Captura de resultados de JMeter (4 puntos)
5. ✅ Documento de 1 página (6 puntos)
   - Descripción del proceso
   - Integración de herramientas
   - Resultados obtenidos

## 🔧 Requisitos del Sistema

- Java JDK 11 o superior
- Maven 3.6+
- Jenkins con plugins: SonarQube Scanner, Slack Notification, Pipeline
- SonarQube Community Edition
- Apache JMeter 5.5+
- Cuenta de Slack con permisos para crear webhooks

## 📝 Comandos Útiles

```bash
# Compilar
mvn clean install

# Ejecutar aplicación
mvn spring-boot:run

# Análisis SonarQube
mvn sonar:sonar -Dsonar.projectKey=vallegrande-project -Dsonar.host.url=http://localhost:9000 -Dsonar.login=YOUR_TOKEN

# JMeter (modo no-GUI)
jmeter -n -t jmeter-test-plan.jmx -l results.jtl -e -o jmeter-report

# JMeter (con interfaz)
jmeter

# Detener aplicación
taskkill /F /IM java.exe
```

## 🎯 Objetivos de Aprendizaje

- Implementar un pipeline de integración continua
- Configurar y utilizar herramientas de DevOps
- Analizar calidad de código con SonarQube
- Realizar pruebas de carga con JMeter
- Automatizar notificaciones con Slack
- Comprender el flujo completo de CI/CD

## 👥 Autor

Proyecto base proporcionado por Valle Grande para el curso de Programación de Software.

## 📄 Licencia

Este proyecto es con fines educativos.

---

**Nota:** Este proyecto contiene intencionalmente problemas de código para demostrar las capacidades de análisis de SonarQube. No modificar el código fuente, el enfoque es la integración de herramientas.
