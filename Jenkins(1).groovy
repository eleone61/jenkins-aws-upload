def createYAML(apiVersion, environment, appFamily, appName, version, buildID, deploymentType){
                        sh """
                        cat << EOF > Jenkins.yaml 
                         apiVersion: ${apiVersion}
                         environment: ${environment}
                         version: ${version}
                         appFamily: ${appFamily}
                         appName: ${appName}
                         buildID: ${buildID}
                         kind: deployment
                         manifest:
                          - application.jar
                          - Jenkins.yaml
                          - dockerfile
                          - MD5NUM
                         deployment:
                          type: ${deploymentType}
                          target: EKS
                          style: non-intrusive
                          window: asap
                        #Optional Fields
                          healthcheckURI: ":80/irmod/test.html"
                        EOF
                    """
    }

node {
  stage('Create YAML') {
    createYAML('v1', 'dev', 'TestFam', 'Test Application', '1.0.0', 'xyz', 'test') 
                sh "cat Jenkins.yml"
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
  }
}



