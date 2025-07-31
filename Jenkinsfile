pipeline {
    agent {
        label 'windows' // Make sure your Jenkins agent is labeled 'windows'
    }

    tools {
            allure 'myallure' // Name configured in Global Tool Configuration
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
                echo '🔨 Cleaning and building the project...'
                bat "if exist %BUILD_DIR% (del /Q %BUILD_DIR%\\*) else (mkdir %BUILD_DIR%)"
                bat "echo Build step executed > %BUILD_DIR%\\build.log"
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
        stage('Allure Report') {
                   steps {
                       echo '📊 Generating Allure report...'
                       allure([
                           includeProperties: false,
                           jdk: '',
                           properties: [],
                           reportBuildPolicy: 'ALWAYS',
                           results: [[path: 'target/allure-results']]
                       ])
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
