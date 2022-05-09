def apiVersion = 'v1'
def env = 'dev'
def version = '1.0.0'
def buildID = 'xyz'
def appFamily = 'Test Family'
def appName = 'Test Application'
def jar_file = 'application.jar'
def yaml_file = 'Jenkins.yml'
def docker = 'dockerfile'
def MD5 = 'MD5NUM'
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
        - Test
        - Test
        - Test
        - Test
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
    datas.manifest[0] = jar_file
    datas.manifest[1] = yaml_file
    datas.manifest[2] = docker
    datas.manifest[3] = MD5
    datas.deployment.type = DEPLOY_TYPE
    datas.deployment.target = DEPLOY_TARGET
    datas.deployment.style = DEPLOY_STYLE
    datas.deployment.window = DEPLOY_WINDOW
    writeYaml file: 'Jenkins.yml', data: datas, overwrite: true
    sh 'cat Jenkins.yml'
  }
    
    stage('Test Build Manifest') {
        def folder = findFiles(glob: '../Test\ Folder/*.txt')
        echo "finding files: ${folder}"
            }
        }   

// def folder = new File('jenkins-aws-upload/Test Folder')
//         folder.eachFileecurse FileType.FILES,   { file ->
//             if(!file.name.endsWith(".txt")) {
//                 println "Listing Files ${file.absolutePath}"


