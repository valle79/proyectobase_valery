pipeline {
    agent any
    
    environment {
        SONAR_HOST_URL = 'http://sonarqube:9000'
        SONAR_TOKEN = credentials('sonarqube-token')
        SLACK_WEBHOOK = credentials('slack-webhook-url')
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
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
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: false
                }
            }
        }
        
        stage('Run Application') {
            steps {
                sh 'nohup mvn spring-boot:run > app.log 2>&1 &'
                sleep 30
            }
        }
        
        stage('JMeter Load Test') {
            steps {
                sh 'jmeter -n -t jmeter-test-plan.jmx -l results.jtl -e -o jmeter-report'
            }
        }
    }
    
    post {
        success {
            sh """
            curl -X POST -H 'Content-Type: application/json' \
            -d '{"text":"✅ Pipeline Exitoso - ${env.JOB_NAME} #${env.BUILD_NUMBER}"}' \
            ${env.SLACK_WEBHOOK}
            """
        }
        
        failure {
            sh """
            curl -X POST -H 'Content-Type: application/json' \
            -d '{"text":"❌ Pipeline Fallido - ${env.JOB_NAME} #${env.BUILD_NUMBER}"}' \
            ${env.SLACK_WEBHOOK}
            """
        }
        
        always {
            sh 'pkill -f spring-boot || true'
        }
    }
}