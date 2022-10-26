def pipelineENV = ["env":"",
                   "changeRequest":""]

node{
    stage('start') {
        echo 'start'
    }

    while (pipelineENV != 'PREPROD') {
        pipelineENV = envSelect()
	println(pipelineENV["changeRequest"])
        crCheck(pipelineENV["changeRequest"])
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
		return req
	}
}


def crCheck(changeRequest) {
    def CR = changeRequest.toString()
    println(CR)
    sh """
    	if [[ ${CR} =~ N/A ]] || [[ ${CR} =~ n/a ]] || [[ ${CR} =~ N/a ]] || [[ ${CR} =~ n/A ]]
	then
		echo "${CR} is valid"
    		break
	else
		if [ {#${CR}} -ge 5 ]
		then
			if [[ ${CR} =~ [0-9] ]] || [[ ${CR} =~ [a..z] ]]
			then
			    echo "${CR} is valid!"
			else
			    echo "${CR} is not valid"
			fi
		else
			echo "${CR} is less than 5 characters"
		fi
	fi
	"""
}

def buildDescript(changeRequest) {
    CR = changeRequest.toString()
    println(CR)
    currentBuild.description = CR
}

