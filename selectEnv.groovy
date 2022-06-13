def env = 'dev'
def buildID = '001'
def Recipients = "examplename1@exampleemail.com, examplename2@exampleemail.com"
def envTestList = [
    'a',
    'b',
    'c',
    'd']

node{
    stage('start') {
        echo 'start'
        createManifest('dev',BUILD_ID,"ABCD","abcd",Recipients)
    }

    def pipelineENV =''
    def goPROD = 'false'
    while (pipelineENV != 'PROD' && goPROD != 'true') {
        pipelineENV = envSelect()
        updateManifest(pipelineENV)
    }

    approvalGate('PROD')
    stagePROD()

    stage('Finished') {
        echo 'finished'
    }   
}



def createManifest(env,buildID,ProgramName,ProjectName,Recipients) {

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
        writeYaml file: 'manifest.yaml', data: Yamldata, overwrite: true
}

def updateManifest(pipelineENV) {

}

def approvalGate(env) {
    stage("${env} approval gate"){
        def req = input message: "Approve to deploy to ${env}", 
            parameters: [string(description: 'Change request from KISAM', name: 'changeRequest')],
            id: 'approver'
        echo req
    }

}

def envSelect (){
    stage('environment select') {
        def req = input message: 'Select an environment',
        id: 'envResponse',
        parameters: [choice(name: 'Branch to deploy',
                            choices: "${envTestList}"
                    )]

        if (req != 'PROD') {
            approvalGate(req)
        }

        switch(req) {
            case 'DSIT': 
                stageDSIT()
                break;
            case 'SITE': 
                stageSITE()
                break;
            case 'PTE':
                stagePTE()
                break;
            case 'EITE':
                stageEITE()
                break;
            case 'PROD':
                prodError()
                
                break;
            
        }
        return req
    }
}

def stageSITE (){
    stage('deploying to SITE'){
        echo 'SITE'
    }
}

def stageDSIT (){
    stage('deploying to DSIT'){
        echo 'DSIT'
    }
}

def stagePTE (){
    stage('deploying to PTE'){
        echo 'PTE'
    }
}

def stageEITE (){
    stage('deploying to EITE') {
        echo 'EITE'
    }
}

def prodError() {
    stage('Attempted to go to PROD, need to go to SAT/EITE/SITE first') {
        catchError(stageResult: 'FAILURE') {
            sh "exit 1"
        }
        echo 'must go to pre-prod first'
        echo 'go to EITE first'
    }
}

def stagePROD (){
    stage('deploying to PROD') {
        echo 'PROD'
    }
}

