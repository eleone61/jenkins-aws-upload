def env = '$(env)'
def buildID = '$(buildID)'
def Recipients = "examplename1@exampleemail.com, examplename2@exampleemail.com"

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
    ],
    'email' : ''
]



node {  
    stage('Find DATA') {
        if ('test1.txt' || 'test2.txt') {
            sh 'rm test1.txt'
            sh 'rm test2.txt'
            sh 'touch test1.txt' 
        }
        tee('test1.txt') {
            unzip dir: '/var/jenkins_home/workspace/Jenkins Test 5/test', zipFile: 'test.zip', read: true String
        }
        sh """
           cat test1.txt
           sed -r -e 's/Reading://g' -e '1d;\$d' -e 's/\\s+//g' 'test1.txt' > 'test2.txt'
           """
       sh 'cat test2.txt'
       sh 'touch test.md5'
//         sh 'pwd'
//         def folder = sh script: """
//                                 zip -sf test.zip | sed '1d;\$d' | sed -r 's/\\s+//g' > file_list.txt
//                                 """
//         sh 'ls && pwd'
        file = readFile('test2.txt')
        def lines = file.readLines()
        lines.add('test.md5')
        println (lines[0])
//         temp = []
//         for (int i = 0; i < lines.size(); i++) {
//         println lines[i]
//         value = lines[i].toString()
//         temp.add(value) 
//         echo 'step 1'
//      }
        
        Yamldata.manifest = lines
//         Yamldata.manifest = lines.add('test.md5')
        
        sh 'Email = $(echo "$Recipients" | sed "s/ //g")'
        def emails = Email.tokenize(",")
        println (emails)
        
        
        Yamldata.manifest = lines
        Yamldata.email = emails
        
    }
    
        
    stage('Write Yaml') {
        writeYaml file: 'manifest.yaml', data: Yamldata, overwrite: true
        sh 'cat manifest.yaml'
//         def datas = readYaml file: 'manifest.yaml'
// //         datas.environment = env
// //         datas.buildID = buildID
        
        
    }
}
