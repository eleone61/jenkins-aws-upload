def apiVersion = 'v1'
def environment = 'dev'
def AppFamily = 'TestFam'
def appName = 'Test Application'
def version = '1.0.0'
def buildID = 'xyz'
def deploymentType = 'container'

def createYAML(apiVersion, environment, appFamily, appName, version, buildID, deploymentType){
                        sh """
                        cat << EOF > Jenkins.yaml 
                         apiVersion: ${apiVersion}
                         environment: ${environment}
                         version: ${version}
                         appFamily: ${AppFamily}
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
  stage('Create YAML'){
     createYAML()
    sh "cat Jenkins.yaml"
  }
}



