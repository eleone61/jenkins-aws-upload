def Yamldata = [
    'apiVersion': 'v1',
    'environment': 'test',
    'version': 1.0,
    'appFamily': 'FBP',
    'appName': 'FBP-product-app',
    'buildID': 'test',
    'kind': 'deployment',
    'manifest':
    'deployment':
        'type': 'container',
        'target': 'EKS',
        'style': 'non-intrusive',
        'window': 'asap'
]

node {
    stage('Write Yaml') {
        writeYaml file: 'manifest.yaml', data: Yamldata
        sh 'cat manifest.yaml'
    }
}