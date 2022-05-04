def createYAML(){
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
node {
  stage('Create YAML'){
     createYAML()
    sh "cat Jenkins.yaml"
  }
}



