def env = '$(env)'
def buildID = '$(buildID)'

def Yamldata = [
    'apiVersion': 'v1',
    'environment': env,
    'version': 1.0,
    'appFamily': 'FBP',
    'appName': 'FBP-product-app',
    'buildID': buildID,
    'kind': 'deployment',
    'manifest': '',
    'deployment': [ 
        'type': 'container',
        'target': 'EKS',
        'style': 'non-intrusive',
        'window': 'asap'
    ]
]

node {
    stage('Write Yaml') {
        writeYaml file: 'manifest.yaml', data: Yamldata
        sh 'cat manifest.yaml'
        def folder = findFiles(glob: 'test-folder/*')
            echo "finding files: ${folder}"
        def datas = readYaml file: 'manifest.yaml'
        datas.environment = env
        datas.buildID = buildID
        
        temp = []
        for (int i = 0; i < folder.size(); i++) {
        println folder[i]
        value = folder[i].toString()
        temp.add(value) 
        echo 'step 1'
        }
        
        datas.manifest = temp
        
        writeYaml file: 'manifest.yaml', data: datas, overwrite: true
        sh 'cat manifest.yaml'
    }
}
