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
  stage('Create YAML'){
     createYAML('v1', 'dev', 'TestFam', 'Test Application', '1.0.0', 'xyz', 'container') 
                sh "cat Jenkins.yaml"
  }
}



