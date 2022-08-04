node {
  stage('Archive Git Log') {
    withCredentials([[$class: 'StringBinding', credentialsID: 'testID', variable: 'testID']]) {
      
      git branch: 'main', credentialsID: ${testID}, poll: false, url: 'git@github.com:eleone61/Cloned_repo.git'
      sh """
           ls -al
           git log "https://github.com/eleone61/jenkins-aws-upload.git" > git.log
          """
    }

      archiveArtifacts artifacts: "git.log", fingerprint: true
  }
}
