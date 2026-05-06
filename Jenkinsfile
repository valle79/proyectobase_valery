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
                sh 'mvn clean install'
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                echo 'Ejecutando análisis con SonarQube...'
                withSonarQubeEnv('SonarQube') {
                    sh '''
                    mvn sonar:sonar \
                    -Dsonar.projectKey=vallegrande-project \
                    -Dsonar.host.url=$SONAR_HOST_URL \
                    -Dsonar.login=$SONAR_TOKEN
                    '''
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
                sh 'nohup mvn spring-boot:run &'
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
            sh """
            curl -X POST -H 'Content-Type: application/json' \
            -d '{"text":"✅ Pipeline Exitoso - ${env.JOB_NAME} #${env.BUILD_NUMBER}"}' \
            $SLACK_WEBHOOK
            """
        }
        
        failure {
            echo 'Pipeline falló'
            sh """
            curl -X POST -H 'Content-Type: application/json' \
            -d '{"text":"❌ Pipeline Fallido - ${env.JOB_NAME} #${env.BUILD_NUMBER}"}' \
            $SLACK_WEBHOOK
            """
        }
        
        always {
            echo 'Limpiando recursos...'
            sh 'pkill -f java || true'
        }
    }
}