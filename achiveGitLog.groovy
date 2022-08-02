node {
  stage('Archive Git Log') {
    sh """
          ls -al
          git log >> git.log
       """
    
    archiveArtifacts artifacts: "git.log", fingerprint: true
  }
}
