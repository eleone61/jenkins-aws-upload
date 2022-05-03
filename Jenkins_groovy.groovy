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

def createYAML(apiVersion, environment, appFamily, appName, version, buildID, deploymentType)
node { 
    stage('Build') { 
        echo 'We are Building' 
    }
    stage('Test') {
        echo 'Testing'
    }
    stage('Create yaml') {
                        sh """
                        cat << EOF > Jenkins.yaml 
                         apiVersion: v1
                         environment: dev
                         version: 1.0.0
                         buildID: xyz
                         kind: deployment
                         manifest:
                          - application.jar
                          - Jenkins.yaml
                          - dockerfile
                          - MD5NUM
                         deployment:
                          type: container
                          target: EKS
                          style: non-intrusive
                          window: asap
                        #Optional Fields
                          healthcheckURI: ":80/irmod/test.html"
                        EOF
                    """
    }
    stage('Check Yaml') {
        cat Jenkins.yaml
}
}



