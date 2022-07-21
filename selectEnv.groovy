def env = 'dev'
def buildID = '001'
def Recipients = "examplename1@exampleemail.com, examplename2@exampleemail.com"
def skipStageA = "false"
def skipStageB = "false"
stageA = "true"
stageB = "true"
promotionalSteps = "true"
List<String> envTestList = []

if (skipStageA == "true")
{
    stageA = false
    skipStageB = "true"
}

if (skipStageB == "true")
{
    stageB = false
    promotionalSteps = false
}

node{
    stage('start') {
        echo 'start'
        createManifest('dev',BUILD_ID,"ABCD","abcd",Recipients)
    }
    
    stage('Test Stage A') {
        if (stageA != true) {
            echo "Stage A"
        } else {
            echo "Skipped"
        }
    }
    stage('Test Stage B') {
        if (stageB != true) {
            echo "Stage B"
        } else {
            echo "Skipped"
        }
    }
    stage('Promotion'){
        if (promotionalSteps != false){
    def pipelineENV = [
	    "environment": "",
	    "changeRequest": ""
    ]
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
        else{
            echo "Skip Stage Promotion"
        }
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
	archiveArtifacts artifacts: 'manifest.yaml' fingerprint: true
}

def updateManifest(pipelineENV) {
	def file = readYaml file: "manifest.yaml"
	
	println(pipelineENV)
	
	file['environment'] = pipelineENV["environment"].toLowerCase()
	
	def changeRequest = pipelineENV["changeRequest"].toString()
	file["changeRequest"] = pipelineENV["changeRequest"]
	
	if("${env.JOB_NAME}".endsWith(".Test")){
		file['kind'] = "Artifact Deployment"
	}else {
		file['kind'] = "Promotions"
	}
	
	sh """
	      if [ -f manifest.yaml ] ; then
		      rm -f manifest.yaml
	      fi
       	    """
	 writeYaml file: 'manifest.yaml', data: file, overwrite: true
	       
         sh 'cat manifest.yaml'
	archiveArtifacts artifacts: 'manifest.yaml' fingerprint: true
}

def environmentalTesting(env) {
    stage("${env} Environmental Testing"){
        echo "$env Environmental Testing"
    }

}

def envSelect (){
	stage('environment select') {
		envTestList = ["DSIT","SITE","PTE","EITE","END"]
		
		def req = ""
		
		timeout(time:1, unit:'DAYS') {
			req = input message: "Select an environment to deploy Artifact?",
				id: 'envResponse',
			    	parameters: [string(description: 'Change Request number from KISAM', name: 'changeRequest'),
					     choice(name: 'environment',
						    choices: envTestList)]
			println(req)
		}
		
		if (req.environment == 'END') {
			return req
			break;
		}
		
		if (req.environment != 'PROD') {
			while ( req.changeRequest == "" ) {
				req = input message: "Select an environment to deploy Artifact?",
			   		id: 'DeployPackage',
			   		parameters: [string(description: 'Missing KISAM change request number', name: 'changeRequest'),
						     choice(name: 'environment',
				      			    choices: envTestList)]
				println(req)
				if (req.environtment == 'END') {
					return req
					break;
				}
			}
		}


        if (req != 'PROD') {
            environmentalTesting(req.environment)
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


