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
        sh 'pwd'
        tee('test1.txt') {
            unzip dir: '/var/jenkins_home/workspace/Jenkins Test 5/test', zipFile: 'test.zip', read: true String
        }
        sh """
           cat test1.txt
           sed -e 's/\<Reading\>//g' | sed '1d;$d' > 'test1.txt'
           """
       sh 'cat test1.txt'
//         sh 'pwd'
//         def folder = sh script: """
//                                 zip -sf test.zip | sed '1d;\$d' | sed -r 's/\\s+//g' > file_list.txt
//                                 """
//         sh 'ls && pwd'
//         file = readFile('file_list.txt')
//         def lines = file.readLines()
//         println (lines[0])
//         temp = []
//         for (int i = 0; i < lines.size(); i++) {
//         println lines[i]
//         value = lines[i].toString()
//         temp.add(value) 
//         echo 'step 1'
//      }
        
//         Yamldata.manifest = lines
        
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
