pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "23MIS0432/food-delivery-order-system"
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
<<<<<<< HEAD
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
=======
                bat "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
>>>>>>> a458ca0cc8d0b5217ce3858ee77a78cc8eeb8a63
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
<<<<<<< HEAD
                    bat "echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin"
=======
                    bat "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
>>>>>>> a458ca0cc8d0b5217ce3858ee77a78cc8eeb8a63
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
