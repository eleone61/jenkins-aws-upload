def pipelineENV = ["env":"",
                   "changeRequest":""]
 def crValid = ""

node{
    stage('start') {
        echo 'start'
    }
	
    stage('Simulate Build and Test') {
	echo 'Build'
	echo 'Test'
    }

    stage('Simulate Push and Pull to Nexus') {
		echo 'Push and Pull to repo'
    }

    while (pipelineENV != 'PREPROD') {
        pipelineENV = envSelect()
// 	println(pipelineENV["changeRequest"])
        buildDescript(pipelineENV)
    }

    stage('Finished') {
        echo 'finished'
    }   
}

def envSelect (){
	stage('environment select') {
		envTestList = ["DSIT","SITE","PTE","PROD","END"]
		
		def req = ""
// 		crValid = false
		
		timeout(time:1, unit:'DAYS') {
			req = input message: "Select an environment to deploy Artifact?",
				id: 'envResponse',
			    	parameters: [string(description: 'Change Request number from KISAM', name: 'changeRequest'),
					     choice(name: 'env',
						    choices: envTestList)]
			println(req)
		}
		
		if (req.environment == 'END') {
			return req
			break;
		}
		
		if (req.environment != 'PROD') {
 			crValid = crCheck(req["changeRequest"])
 			println (crValid)
			while ( crValid == "false" ) {
				req = input message: "Select an environment to deploy Artifact?",
					id: 'DeployPackage',
					parameters: [string(description: 'Missing KISAM change request number', name: 'changeRequest'),
						     choice(name: 'env',
							    choices: envTestList)]
				println(req)
				crValid = ""
				crValid = crCheck(req["changeRequest"])
				
				if (req.env == 'END') {
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
    def valid = sh returnStdout: true, script: """
							if [[ ${CR} =~ N/A ]] || [[ ${CR} =~ n/a ]] || [[ ${CR} =~ N/a ]] || [[ ${CR} =~ n/A ]]
							then
 								#echo "${CR} is valid"
								echo -n "true"
								break
							else
								if [ ${crLen} -ge 5 ]
								then
									if [[ ${CR} =~ [0-9] ]] || [[ ${CR} =~ [a..z] ]]
									then
									    #echo "${CR} is valid!"
									    echo -n "true"
									else
 									    #echo "${CR} is not valid"
									    echo -n "false"
									    break
									fi
								else
 									#echo "${CR} is less than 5 characters"
									echo -n "false"
									break
								fi
							fi
						 """
	println(valid)
 	return valid
}

def buildDescript(pipelineENV) {
    CR = pipelineENV["changeRequest"].toString()
    println(CR)
	currentBuild.description = "The current build is in environment: ${pipelineENV["env"]} \nChange Request Value is: ${pipelineENV["changeRequest"]}"
}

