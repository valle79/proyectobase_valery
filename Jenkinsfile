pipeline {
    agent any
    
    environment {
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_TOKEN = credentials('sonarqube-token')
        SLACK_WEBHOOK = credentials('slack-webhook-url')
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Obteniendo código del repositorio...'
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo 'Compilando el proyecto con Maven...'
                bat 'mvn clean install'
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                echo 'Ejecutando análisis de calidad de código con SonarQube...'
                withSonarQubeEnv('SonarQube') {
                    bat 'mvn sonar:sonar -Dsonar.projectKey=vallegrande-project -Dsonar.host.url=%SONAR_HOST_URL% -Dsonar.login=%SONAR_TOKEN%'
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                echo 'Verificando Quality Gate de SonarQube...'
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: false
                }
            }
        }
        
        stage('Run Application') {
            steps {
                echo 'Iniciando aplicación Spring Boot...'
                script {
                    bat 'start /B mvn spring-boot:run'
                    sleep 30
                }
            }
        }
        
        stage('JMeter Load Test') {
            steps {
                echo 'Ejecutando pruebas de carga con JMeter...'
                bat 'jmeter -n -t jmeter-test-plan.jmx -l results.jtl -e -o jmeter-report'
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline ejecutado exitosamente'
            script {
                def message = """
                {
                    "text": "✅ *Pipeline Exitoso*",
                    "blocks": [
                        {
                            "type": "section",
                            "text": {
                                "type": "mrkdwn",
                                "text": "*Proyecto:* ${env.JOB_NAME}\\n*Build:* #${env.BUILD_NUMBER}\\n*Estado:* Exitoso ✅\\n*Duración:* ${currentBuild.durationString}"
                            }
                        }
                    ]
                }
                """
                bat "curl -X POST -H \"Content-Type: application/json\" -d \"${message}\" %SLACK_WEBHOOK%"
            }
        }
        failure {
            echo 'Pipeline falló'
            script {
                def message = """
                {
                    "text": "❌ *Pipeline Fallido*",
                    "blocks": [
                        {
                            "type": "section",
                            "text": {
                                "type": "mrkdwn",
                                "text": "*Proyecto:* ${env.JOB_NAME}\\n*Build:* #${env.BUILD_NUMBER}\\n*Estado:* Fallido ❌\\n*Duración:* ${currentBuild.durationString}"
                            }
                        }
                    ]
                }
                """
                bat "curl -X POST -H \"Content-Type: application/json\" -d \"${message}\" %SLACK_WEBHOOK%"
            }
        }
        always {
            echo 'Limpiando recursos...'
            bat 'taskkill /F /IM java.exe /T || exit 0'
        }
    }
}
