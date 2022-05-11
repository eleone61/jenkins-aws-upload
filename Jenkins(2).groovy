def env = '$(env)'
def buildID = '$(buildID)'

def createYAML(){
    sh """
      cat << EOF > manifest.yaml 
       apiVersion: v1
       environment: test
       version: 1.0
       appFamily: FBP
       appName: FBP-product-app
       buildID: test
       kind: deployment
       manifest:
        - 
        - 
        - 
        - 
       deployment:
        type: container
        target: EKS
        style: non-intrusive
        window: asap
    """
}

node {
    stage('Create YAML') {
        createYAML()
        sh 'cat manifest.yaml'
    }
    stage('Write YAML') {
        def datas = readYaml file: 'manifest.yaml'
        datas.environment = env
        datas.buildID = buildID
        writeYaml file: 'manifest.yaml', data: datas, overwrite: true
        sh 'cat manifest.yaml'
    }
}