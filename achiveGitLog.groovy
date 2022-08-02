node {
  stage('Archive Git Log') {
    sh """
          ls -al
          git log "jenkins-aws-upload.git" >> git.log
       """
    
    archiveArtifacts artifacts: "git.log", fingerprint: true
  }
}
