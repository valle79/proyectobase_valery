pipeline {
    agent any
    
    environment {
        SONAR_HOST_URL = 'http://sonarqube:9000' // cambiar si no usas red docker
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
                sh 'mvn clean install -DskipTests'
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                echo 'Ejecutando análisis con SonarQube...'
                withSonarQubeEnv('SonarQube') {
                    sh """
                    mvn sonar:sonar \
                    -Dsonar.projectKey=vallegrande-project \
                    -Dsonar.host.url=${env.SONAR_HOST_URL} \
                    -Dsonar.login=${env.SONAR_TOKEN}
                    """
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                echo 'Verificando Quality Gate...'
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: false
                }
            }
        }
        
        stage('Run Application') {
            steps {
                echo 'Iniciando aplicación...'
                sh 'nohup mvn spring-boot:run > app.log 2>&1 &'
                sleep 30
            }
        }
        
        stage('JMeter Load Test') {
            steps {
                echo 'Ejecutando pruebas con JMeter...'
                sh 'jmeter -n -t jmeter-test-plan.jmx -l results.jtl -e -o jmeter-report'
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline exitoso'
            node {
                sh """
                curl -X POST -H 'Content-Type: application/json' \
                -d '{"text":"✅ Pipeline Exitoso - ${env.JOB_NAME} #${env.BUILD_NUMBER}"}' \
                ${env.SLACK_WEBHOOK}
                """
            }
        }
        
        failure {
            echo 'Pipeline falló'
            node {
                sh """
                curl -X POST -H 'Content-Type: application/json' \
                -d '{"text":"❌ Pipeline Fallido - ${env.JOB_NAME} #${env.BUILD_NUMBER}"}' \
                ${env.SLACK_WEBHOOK}
                """
            }
        }
        
        always {
            echo 'Limpiando recursos...'
            node {
                sh 'pkill -f spring-boot || true'
            }
        }
    }
}