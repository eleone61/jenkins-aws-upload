node {
  stage('Archive Git Log') {
        git branch: 'main', poll: false, url: 'https://github.com/eleone61/jenkins-aws-upload.git'
        sh """
             ls -al
             git log >> git.log
            """
    
    archiveArtifacts artifacts: "git.log", fingerprint: true
  }
}
