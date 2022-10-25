def pipelineENV = ["env":"",
                   "CR":""]

node{
    stage('start') {
        echo 'start'
    }

    while (pipelineENV != 'PREPROD') {
        pipelineENV = envSelect()
        crCheck(pipelineENV["CR"])
        buildDescrpt(pipelineENV["CR"])
    }

    stage('Finished') {
        echo 'finished'
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
    }
}


def crCheck(changeRequest) {
    def CR = changeRequest
    sh """
            if [[ "$1" =~ N/A ]] || [[ "$1" =~ n/a ]] || [[ "$1" =~ N/a ]] || [[ "$1" =~ n/A ]]; then # $1 because it corresponds to the posistion of the parameter after the function name
    		echo "$1 is valid"
    		break
	    else
		    if [ ${#1} -ge 5 ]; then
			if [[ "$1" =~ [0-9] ]] || [[ "$1" =~ [a..z] ]]; then
			    echo "$1 is valid!"
			else
			    echo "$1 is not valid"
			fi
		    else
			echo "$1 is less than 5 characters"
		    fi
		fi
	"""
}

def buildDescript(changeRequest) {
    CR = changeRequest.toString()
    println(CR)
    currentBuild.description = CR
}

