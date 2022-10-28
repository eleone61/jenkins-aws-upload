def pipelineENV = ["env":"",
                   "changeRequest":""]
 crValid = false

node{
    stage('start') {
        echo 'start'
    }

    while (pipelineENV != 'PREPROD') {
        pipelineENV = envSelect()
	println(pipelineENV["changeRequest"])
        buildDescript(pipelineENV["changeRequest"])
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
			crCheck(req["changeRequest"])
			while ( req.changeRequest == "" || crValid == false) {
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
		return req
	}
}


def crCheck(changeRequest) {
    def CR = changeRequest.toString()
    crLen = CR.length()
    println(CR)
    sh """
    	set -a 
    	if [[ ${CR} =~ N/A ]] || [[ ${CR} =~ n/a ]] || [[ ${CR} =~ N/a ]] || [[ ${CR} =~ n/A ]]
	then
		echo "${CR} is valid"
		crValid = "true"
    		break
	else
		if [ ${crLen} -ge 5 ]
		then
			if [[ ${CR} =~ [0-9] ]] || [[ ${CR} =~ [a..z] ]]
			then
			    echo "${CR} is valid!"
			else
			    echo "${CR} is not valid"
			    crValid = "false"
			fi
		else
			echo "${CR} is less than 5 characters"
			crValid = "false"
		fi
	fi
	"""
}

def buildDescript(changeRequest) {
    CR = changeRequest.toString()
    println(CR)
    currentBuild.description = CR
}

