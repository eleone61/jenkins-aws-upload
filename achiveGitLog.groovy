node {
  stage('Archive Git Log') {
    withCredentials([usernameColonPassword(credentialsId: 'Test_Cred', variable: 'testID')]) {
//       git branch: 'main', credentialsId: 'Test_Cred', poll: false, url: 'git@github.com:eleone61/Cloned_repo.git'
      git branch: "*", poll: false, url: 'https://github.com/eleone61/Cloned_repo.git'
      sh """
           ls -al
           git log "https://github.com/eleone61/jenkins-aws-upload.git" > git.log
          """
    }

      archiveArtifacts artifacts: "git.log", fingerprint: true
  }
}
