pipeline {
    agent any

    environment {
<<<<<<< HEAD
<<<<<<< HEAD
        TARGET_BRANCH = 'develop-fe'  // 목표 브랜치 이름 설정
=======
        TARGET_BRANCH = 'master'  // 목표 브랜치 이름 설정
>>>>>>> master
=======
        TARGET_BRANCH = 'develop'  // 목표 브랜치 이름 설정
>>>>>>> develop
    }

    stages {
        stage('Check Branch') { // 내가 원하는 브랜치인지 필터링 진행
            when {
                expression {
                    return env.GIT_BRANCH == "origin/${TARGET_BRANCH}"
                }
            }
            steps {
                echo "Building branch: ${env.GIT_BRANCH}"
            }
        }
        stage('Cleanup Workspace') {
            steps {
                deleteDir() // 작업 공간 초기화
            }
        }
        stage('Checkout') {
            steps {
                script {
                    checkout([$class: 'GitSCM',
<<<<<<< HEAD
<<<<<<< HEAD
                              branches: [[name: '*/develop-fe']],
=======
                              branches: [[name: '*/master']],
>>>>>>> master
=======
                              branches: [[name: '*/develop']],
>>>>>>> develop
                              doGenerateSubmoduleConfigurations: false,
                              extensions: [[$class: 'CleanCheckout']],
                              userRemoteConfigs: [[url: 'https://lab.ssafy.com/s11-webmobile2-sub2/S11P12B308.git',
                              credentialsId: 'gitlab-token']]  // credentialsId를 올바르게 설정
                    ])
                }
            }
        }
<<<<<<< HEAD
=======
        stage('Copy .env') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'firebase-env', variable: 'ENV_FILE')]) {
                        // .env 파일 복사
                        sh 'cp $ENV_FILE frontend/.env'

                        // 복사된 파일 내용 확인
                        sh 'cat frontend/.env'
                    }
                }
            }
        }
>>>>>>> master
        stage('Build with npm') {
            steps {
                dir('frontend') {
                    sh 'npm install'
                    sh 'CI=false npm run build'  // CI 환경 변수를 false로 설정
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    // 빌드된 파일을 이용하여 Docker 이미지 빌드
                    sh 'docker build -t frontend:latest -f frontend/Dockerfile frontend/.'
                }
            }
        }
        stage('Deploy with Docker Compose') {
            steps {
                script {
                    // SSH 키를 사용하여 원격 서버에 연결하고 명령을 실행
                    sh '''
                        ssh -o StrictHostKeyChecking=no -i /var/jenkins_home/.ssh/jenkins_rsa ubuntu@i11b308.p.ssafy.io '
                        cd /home/ubuntu/plog &&
                        docker-compose stop frontend &&
                        docker-compose rm -f frontend &&
                        docker-compose up -d frontend
                        '
                    '''
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/frontend/build/**/*', fingerprint: true
        }
        success {
            echo 'Deployment successful!'
        }
        failure {
            echo 'Deployment failed!'
        }
    }
}
