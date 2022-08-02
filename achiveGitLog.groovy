node {
  stage('Archive Git Log') {
//     git changelog: true,
//       url: "https://github.com/eleone61/jenkins-aws-upload.git"
    sh """
          ls -al
          git log git@github.com:eleone61/jenkins-aws-upload.git >> git.log
       """
    
    archiveArtifacts artifacts: "git.log", fingerprint: true
  }
}
