pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "23mis0432/food-delivery-order-system"
        DOCKER_TAG   = "latest"
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Vijay-Kumar2309/food-delivery-order-system.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    // Check if Docker daemon is running
                    def dockerStatus = bat(script: 'docker ps > nul 2>&1', returnStatus: true)
                    if (dockerStatus == 0) {
                        bat "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                    } else {
                        echo "⚠️ Warning: Docker daemon is not running. Skipping Docker build..."
                        currentBuild.result = 'UNSTABLE'
                    }
                }
            }
        }

        stage('Docker Push') {
            when {
                expression { currentBuild.result != 'UNSTABLE' }
            }
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    bat "echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin"
                    bat "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            when {
                expression { currentBuild.result != 'UNSTABLE' }
            }
            steps {
                bat 'kubectl apply -f k8s/deployment.yaml'
                bat 'kubectl apply -f k8s/service.yaml'
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline completed successfully!'
        }
        failure {
            echo '❌ Pipeline failed. Check logs above.'
        }
        unstable {
            echo '⚠️ Pipeline completed with warnings. Docker daemon may not be running.'
        }
    }
}
