def createYAML(){
    sh """
      cat << EOF > Jenkins.yml 
       apiVersion: test
       environment: test
       version: test
       appFamily: test
       appName: test
       buildID: test
       kind: deployment
       manifest:
        - application.jar
        - Jenkins.yaml
        - dockerfile
        - MD5NUM
       deployment:
        type: test
        target: EKS
        style: non-intrusive
        window: asap
      #Optional Fields
        healthcheckURI: ":80/irmod/test.html"
    """
}


node {
  stage('Create YAML') {
    createYAML() 
    sh 'cat Jenkins.yml'
  }
  stage('Read YAML') {
    sh 'pwd'
    sh 'ls'
    def data = readYaml file: 'Jenkins.yml'
    return data
  }

  stage('Write Yaml') {
    def datas = readYaml file: 'Jenkins.yml'
    datas.deployment.type = 'container' 
    writeYaml file: 'Jenkins.yml', data: datas, overwrite: true
    sh 'cat Jenkins.yml'
  }
}



