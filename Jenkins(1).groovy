node{
    stage('start') {
        echo 'start'
    }

    def pipelineENV =''
    while (pipelineENV != 'PREPROD') {
        pipelineENV = envSelect()
    }

    approvalGate('PROD')
    stagePROD()

    stage('Finished') {
        echo 'finished'
    }   
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
                    choices: "DEV\nDSIT\nPTE\nPREPROD\nPROD"
                    )]

        if (req != 'PROD') {
            approvalGate(req)
        }

        switch(req) {
            case 'DEV': 
                stageDEV()
                break;
            case 'DSIT': 
                stageDSIT()
                break;
            case 'PTE':
                stagePTE()
                break;
            case 'PREPROD':
                stagePREPROD()
                break;
            case 'PROD':
                prodError()
                break;
            
        }
        return req
}
}

def stageDEV (){
    stage('deploying to DEV'){
        echo 'DEV'
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

def stagePREPROD (){
    stage('deploying to PREPROD') {
        echo 'PREPROD'
    }
}

def prodError() {
    stage('Attempted to go to PROD, need to go to PREPROD first') {
        catchError(stageResult: 'FAILURE') {
            sh "exit 1"
        }
        echo 'must go to pre-prod first'
        echo 'go to PREPROD first'
    }
}

def stagePROD (){
    stage('deploying to PROD') {
        echo 'PROD'
    }
}

