def env = '$(env)'
def buildID = '$(buildID)'
def Recipients = "examplename1@exampleemail.com, examplename2@exampleemail.com"
def skipStageC = 'false'
def skipStageD = 'false'
def skipStageE = 'false'

def failPipeline() {
   stage('Must Complete Testing') {
       script {
           catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
            sh "exit 1"
        }
       }
   }
}
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

        file = readFile('test2.txt')
        def lines = file.readLines()
        lines.add('test.md5')
        println (lines[0])
        
        
       def Email = sh returnStdout: true, script: """
                                                    echo -n ${Recipients}|sed 's/ //g' 
                                                  """
        println (Email)
        def emails = Email.tokenize(',')
        println (emails)
        
        
        Yamldata.manifest = lines
        Yamldata.email = emails
        
    }
    
    stage('Check Test') {
        if (skipStageC == 'true' || skipStageD == 'true' || skipStageE == 'true') {
                failPipeline()
        }
    }
        
        
    stage('Write Yaml') {
        writeYaml file: 'manifest.yaml', data: Yamldata, overwrite: true
        sh 'cat manifest.yaml'
        
    }
    
    stage('SHA-256 Test') {
        tee('test3.txt') {
            sh 'sha256sum shatest.txt'
        }
        sh """
            sed -r -e '1d' -e 's/shatest.txt//g' 'test3.txt' > 'test4.txt'
            cat test4.txt
           """
    }
}
