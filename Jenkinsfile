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
        
        stage('Build2') {
             steps {
                  echo "Hello World"
                     echo "Multiline shell steps works too"
             }
         }      
        
        stage('Upload to AWS') {
              steps {
                  s3Upload(file:'HelloWorld.py', bucket:'shred-jenkins-test', path:'jenkins-aws-upload/HelloWorld.py')
                  }
              }
         }
}


