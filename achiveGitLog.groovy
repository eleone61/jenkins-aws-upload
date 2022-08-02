node {
  stage('Archive Git Log') {
    sh """
          git log >> git.log
       """
    
    archiveArtifacts artifacts: "git.log", fingerprint: true
  }
}
