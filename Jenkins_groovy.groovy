def APIVER = 'v1'
def ENV = 'dev'
def VER = '1.0.0'
def BUILDID = 'xyz'
def JAR_FILE = 'application'
def YAML = 'iep-deployment'
def DEPLOY_TYPE = 'container'
def DEPLOY_TARGET = 'EKS'
def DEPLOY_STYLE = 'non-intrusive'
def DEPLOY_WINDOW = 'asap'
def YAML_path = 'dir/Jenkins.yaml'

def data
node { 
    stage('Build') { 
        echo 'We are Building' 
    }
    stage('Test') {
        echo 'Testing'
    }
    stage('read yaml') {
            steps {
                script{data = readYaml(file: 'Jenkins.yaml')}
                echo data.deployment.window.toString()
            }
        }
}


