# Instrucciones Paso a Paso - Reto de Integración Continua

## Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:
- Java JDK 11 o superior
- Maven 3.6+
- Jenkins
- SonarQube Community Edition
- Apache JMeter
- Cuenta de Slack

---

## PASO 1: Configurar SonarQube

### 1.1 Iniciar SonarQube
```bash
cd [RUTA_SONARQUBE]
bin\windows-x86-64\StartSonar.bat
```

### 1.2 Acceder a SonarQube
- Abrir navegador: http://localhost:9000
- Usuario: `admin`
- Contraseña: `admin` (cambiar en primer acceso)

### 1.3 Crear Proyecto
1. Click en "Create Project" → "Manually"
2. Project key: `vallegrande-project`
3. Display name: `Valle Grande - Proyecto Base`
4. Click "Set Up"

### 1.4 Generar Token
1. Click en "Locally"
2. Generate token → Copiar y guardar el token
3. Este token se usará en Jenkins

---

## PASO 2: Configurar Slack

### 2.1 Crear Canal
1. Abrir Slack workspace
2. Crear canal: `#jenkins-notifications`

### 2.2 Configurar Webhook
1. Ir a: https://api.slack.com/apps
2. Click "Create New App" → "From scratch"
3. Nombre: `Jenkins Integration`
4. Seleccionar workspace
5. En "Incoming Webhooks" → Activar
6. Click "Add New Webhook to Workspace"
7. Seleccionar canal `#jenkins-notifications`
8. Copiar la Webhook URL (empieza con https://hooks.slack.com/...)

---

## PASO 3: Configurar Jenkins

### 3.1 Instalar Plugins
1. Ir a: Manage Jenkins → Manage Plugins
2. Instalar los siguientes plugins:
   - SonarQube Scanner
   - Slack Notification
   - Pipeline
   - Maven Integration

### 3.2 Configurar SonarQube en Jenkins
1. Ir a: Manage Jenkins → Configure System
2. Buscar sección "SonarQube servers"
3. Click "Add SonarQube"
   - Name: `SonarQube`
   - Server URL: `http://localhost:9000`
   - Server authentication token: Agregar credencial con el token de SonarQube

### 3.3 Agregar Credenciales
1. Ir a: Manage Jenkins → Manage Credentials
2. Click en "(global)" → "Add Credentials"

**Credencial 1: Token de SonarQube**
- Kind: Secret text
- Secret: [PEGAR TOKEN DE SONARQUBE]
- ID: `sonarqube-token`
- Description: `SonarQube Authentication Token`

**Credencial 2: Webhook de Slack**
- Kind: Secret text
- Secret: [PEGAR WEBHOOK URL DE SLACK]
- ID: `slack-webhook-url`
- Description: `Slack Webhook URL`

### 3.4 Crear Pipeline
1. Click "New Item"
2. Nombre: `Valle-Grande-Pipeline`
3. Seleccionar "Pipeline"
4. Click "OK"
5. En configuración:
   - Pipeline → Definition: "Pipeline script from SCM"
   - SCM: Git
   - Repository URL: [URL de tu repositorio]
   - Script Path: `Jenkinsfile`
6. Click "Save"

---

## PASO 4: Ejecutar el Pipeline

### 4.1 Iniciar Build
1. En el proyecto Jenkins, click "Build Now"
2. Observar la ejecución en "Build History"
3. Click en el número de build → "Console Output" para ver logs

### 4.2 Capturar Evidencias

**Para Jenkins:**
- Captura de pantalla del pipeline ejecutándose (vista de stages)
- Captura del "Console Output" mostrando éxito

**Para SonarQube:**
- Ir a http://localhost:9000
- Click en el proyecto "vallegrande-project"
- Capturar pantalla mostrando:
  - Overview con métricas
  - Issues detectados (Code Smells)

**Para Slack:**
- Abrir canal `#jenkins-notifications`
- Capturar mensaje de notificación del pipeline

---

## PASO 5: Ejecutar Pruebas de JMeter

### 5.1 Verificar que la aplicación esté corriendo
```bash
curl http://localhost:8081/products
```

### 5.2 Ejecutar JMeter desde línea de comandos
```bash
jmeter -n -t jmeter-test-plan.jmx -l results.jtl -e -o jmeter-report
```

### 5.3 Ver Resultados
1. Abrir carpeta `jmeter-report`
2. Abrir `index.html` en navegador
3. Capturar pantallas de:
   - Dashboard con gráficos
   - Statistics (tiempo de respuesta, throughput)

### 5.4 Ejecutar JMeter con GUI (alternativa)
```bash
jmeter
```
1. File → Open → Seleccionar `jmeter-test-plan.jmx`
2. Click en botón verde "Start" (▶)
3. Ver resultados en "Summary Report" y "View Results Tree"
4. Capturar pantallas

---

## PASO 6: Identificar Problemas de SonarQube

En el reporte de SonarQube, identificar estos 3 problemas:

### Problema 1: Variable no utilizada
- **Archivo:** `ApiController.java`
- **Línea:** 19
- **Código:** `String unused = "No se usa";`
- **Mejora:** Eliminar la variable innecesaria

### Problema 2: Complejidad ciclomática alta
- **Archivo:** `ApiController.java`
- **Método:** `check()`
- **Problema:** Anidamiento excesivo de if
- **Mejora:** Simplificar a: `return (a < b && b < c && c > a) ? "OK" : "FAIL";`

### Problema 3: Código duplicado
- **Archivo:** `ProductService.java`
- **Métodos:** `getAll()` y `getAllAgain()`
- **Problema:** Lógica duplicada
- **Mejora:** Eliminar `getAllAgain()` y simplificar `getAll()` a: `return new ArrayList<>(products);`

---

## PASO 7: Preparar Entregables

### Documento Final (máximo 1 página)

Incluir:

1. **Descripción del proceso realizado**
   - Explicar cómo se configuraron las 4 herramientas
   - Mencionar los pasos de integración

2. **Explicación de la integración de herramientas**
   - Describir el flujo: GitHub → Jenkins → Maven → SonarQube → Spring Boot → JMeter → Slack
   - Explicar cómo se comunican entre sí

3. **Resultados obtenidos**
   - Estado del pipeline (exitoso/fallido)
   - Problemas detectados por SonarQube
   - Métricas de JMeter (tiempo de respuesta, throughput)
   - Confirmación de notificación en Slack

### Capturas Requeridas

✅ **Jenkins** (4 puntos)
- Pipeline ejecutándose con todas las etapas visibles

✅ **SonarQube** (3 puntos)
- Dashboard del proyecto con métricas
- Lista de issues detectados
- Identificar 3 problemas con propuestas de mejora

✅ **Slack** (3 puntos)
- Mensaje de notificación del pipeline

✅ **JMeter** (4 puntos)
- Dashboard con gráficos de rendimiento
- Tabla de estadísticas (tiempo de respuesta, throughput, errores)

✅ **Documento** (6 puntos)
- Máximo 1 página con descripción, integración y resultados

---

## Solución de Problemas Comunes

### Error: "SonarQube server not found"
- Verificar que SonarQube esté corriendo en http://localhost:9000
- Revisar configuración en Jenkins → Configure System → SonarQube servers

### Error: "Port 8081 already in use"
- Detener procesos Java: `taskkill /F /IM java.exe`
- Verificar que no haya otra instancia corriendo

### Error: JMeter no encuentra el plan de pruebas
- Verificar que `jmeter-test-plan.jmx` esté en la raíz del proyecto
- Usar ruta absoluta: `jmeter -n -t C:\ruta\completa\jmeter-test-plan.jmx`

### Notificación de Slack no llega
- Verificar que la Webhook URL sea correcta
- Probar manualmente con curl:
```bash
curl -X POST -H "Content-Type: application/json" -d "{\"text\":\"Test\"}" [WEBHOOK_URL]
```

---

## Comandos Rápidos de Referencia

```bash
# Compilar proyecto
mvn clean install

# Ejecutar aplicación
mvn spring-boot:run

# Análisis SonarQube manual
mvn sonar:sonar -Dsonar.projectKey=vallegrande-project -Dsonar.host.url=http://localhost:9000 -Dsonar.login=[TOKEN]

# JMeter en modo no-GUI
jmeter -n -t jmeter-test-plan.jmx -l results.jtl -e -o jmeter-report

# JMeter con GUI
jmeter

# Detener aplicación Java
taskkill /F /IM java.exe
```

---

## Checklist Final

- [ ] SonarQube corriendo en puerto 9000
- [ ] Token de SonarQube generado y guardado
- [ ] Webhook de Slack configurado
- [ ] Jenkins con plugins instalados
- [ ] Credenciales agregadas en Jenkins
- [ ] Pipeline creado y configurado
- [ ] Build ejecutado exitosamente
- [ ] Captura de Jenkins tomada
- [ ] Captura de SonarQube tomada
- [ ] 3 problemas identificados con mejoras propuestas
- [ ] Captura de Slack tomada
- [ ] JMeter ejecutado con 50 usuarios
- [ ] Captura de JMeter tomada
- [ ] Documento de 1 página completado

---

**¡Listo para entregar el reto!** 🚀
