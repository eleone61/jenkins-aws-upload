node {
  stage('Archive Git Log') {
    sh """
          ls -al
          git log "https://github.com/eleone61/jenkins-aws-upload.git" >> git.log
       """
    
    archiveArtifacts artifacts: "git.log", fingerprint: true
  }
}
