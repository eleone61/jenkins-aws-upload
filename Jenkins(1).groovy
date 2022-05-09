def apiVersion = 'v1'
def env = 'dev'
def version = '1.0.0'
def buildID = 'xyz'
def appFamily = 'Test Family'
def appName = 'Test Application'
def DEPLOY_TYPE = 'container'
def DEPLOY_TARGET = 'EKS'
def DEPLOY_STYLE = 'non-intrusive'
def DEPLOY_WINDOW = 'asap'

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
        target: test
        style: test
        window: test
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
    datas.apiVersion = apiVersion
    datas.environment = env
    datas.version = version
    datas.appFamily = appFamily
    datas.appName = appName
    datas.buildID = buildID
    datas.deployment.type = DEPLOY_TYPE
    datas.deployment.target = DEPLOY_TARGET
    datas.deployment.style = DEPLOY_STYLE
    datas.deployment.window = DEPLOY_WINDOW
    writeYaml file: 'Jenkins.yml', data: datas, overwrite: true
    sh 'cat Jenkins.yml'
  }
}



