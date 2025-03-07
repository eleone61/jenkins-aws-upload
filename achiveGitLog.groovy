node {
  stage('Archive Git Log') {
    withCredentials([sshUserPrivateKey(credentialsId: 'testCred', keyFileVariable: '')]) {
      git branch: 'main', credentialsId: 'testCred', poll: false, url: 'git@github.com:eleone61/Cloned_repo.git'
      sh """
           ls -al
           git log > git.log
          """
    }
    

      archiveArtifacts artifacts: "git.log", fingerprint: true
  }
}
