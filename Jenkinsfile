pipeline {
    agent any

    stages {
        stage('Pull source code from SCM') {
            steps {
                echo 'Hello World'
            }
        }
    
        stage('Build/Compile and Unit Test') {
            steps {
                echo 'Build'
            }
        }
        
        stage('Code Coverage') {
            steps {
                echo 'Code Coverage'
            }
        }
        
        stage('Quality Scan') {
            steps {
                echo 'Quality Scan'
            }
        }
        
        stage('Security Scan') {
            steps {
                echo 'Security Scan'
            }
        }
        
        stage('Push to S3') {
            steps {
                echo 'Push'
            }
        }
        
        stage('Upload to AWS') {
              steps {
                  withAWS(region:'us-east-1',credentials:'elijah_leone') {
                  sh 'echo "Uploading content with AWS creds"'
                      s3Upload(pathStyleAccessEnabled: true, payloadSigningEnabled: true, file:'HelloWorld.py', bucket:'shred-jenkins-test')
                  }
              }
         }
}
}

