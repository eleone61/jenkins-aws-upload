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
    stage('Find DATA') {
//         sh 'cd testzip'
//         def folder = unzip zipFile: 'testzip.zip', glob: '', read: true String version = v['test1.txt']                    
//         echo "finding files: ${folder}"
// //         echo "${folder[0]}"
        sh 'pwd'
        def folder = sh script: """
                                zip -sf test.zip | sed '1d;\$d' | sed -r 's/\\s+//g' > file_list.txt
                                """
        sh 'ls && pwd'
        file = readFile('file_list.txt')
        def lines = file.readLines()
        println (lines[0])
//         temp = []
//         for (int i = 0; i < lines.size(); i++) {
//         println lines[i]
//         value = lines[i].toString()
//         temp.add(value) 
//         echo 'step 1'
//      }
        
        Yamldata.manifest = lines
        
    }
    
        
    stage('Write Yaml') {
        if ('manifest.yaml') {
            sh 'rm manifest.yaml'
        }
        writeYaml file: 'manifest.yaml', data: Yamldata
        sh 'cat manifest.yaml'
//         def datas = readYaml file: 'manifest.yaml'
// //         datas.environment = env
// //         datas.buildID = buildID
        
        
    }
}
