pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo '🔨 Building the project...'
                sh 'make build'
            }
        }

        stage('Test') {
            steps {
                echo '🧪 Running tests...'
                sh 'make test'
            }
        }

        stage('Deploy') {
            steps {
                echo '🚀 Deploying application...'
                sh 'make deploy'
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline completed successfully!'
        }
        failure {
            echo '❌ Pipeline failed. Check logs for details.'
        }
    }
}
