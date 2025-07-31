pipeline {
    agent {
        label 'windows' // Make sure your Jenkins agent is labeled 'windows'
    }

    environment {
        BUILD_DIR = 'D:\\Automation\\RestAssuredProjects\\build'
    }

    stages {
        stage('Checkout') {
            steps {
                echo '📥 Checking out source code...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo '🔨 Building the project...'
                bat "mkdir %BUILD_DIR% && echo Build step executed > %BUILD_DIR%\\build.log"
            }
        }

        stage('Test') {
            steps {
                echo '🧪 Running tests...'
                bat "mvn clean test"
            }
        }

        stage('Deploy') {
            steps {
                echo '🚀 Deploying application...'
                bat "echo Deploying... > %BUILD_DIR%\\deploy.log"
            }
        }
    }

    post {
        always {
                    echo '📦 Pipeline finished — publishing report...'
                    publishHTML(target: [
                        reportDir: 'target/surefire-reports',
                        reportFiles: 'index.html',
                        reportName: 'TestNG Report'
                    ])
        }
        success {
            echo '✅ Pipeline completed successfully!'
        }
        failure {
            echo '❌ Pipeline failed. Check logs for details.'
        }
    }
}
