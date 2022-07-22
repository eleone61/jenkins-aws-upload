// def archiveIEPDeploymentLog(ProjectName, , targetEnv, ApproverID, pipelineEnv["changeRequest"], timestamp)

// def call(Name, appVersion, targetEnv, approvers, crNumber, time)
if (!(deployment_log.txt)) {
      sh 'echo "DeployDate: \tAppName: \tAppVersion: \tDeployedto: \tApprovedBy: \tKISAM CR:" > "deployment_log.txt"'
}


node {
def date="72022"
def name="Elijah"
def appVersion="1.0"
def targetEnv="dev"
def approver="LDHVB"
def crNumber="1234"
def home = "Home"

def fileName= "/home/jenkins/workspace/log/deployment_log.txt"
def dashboardFileName = "/home/jenkins/workspace/log/ecm_deployment_log.txt"
def time = timestamp()

sh """
      set +x
      echo "DeployDate: $time \tAppName: $name \tAppVersion: $appVersion \tDeployedto: $targetEnv \tApprovedBy: ${approver} \tKISAM CR: $crNumber" >> $fileName
      cp -R $fileName deployment.log
      echo "\t${time} \t${name} \t${appVersion} \t${targetEnv} \t${approver} \t${crNumber} \t${env.BUILD_TAG}" >> $dashboardFileName
   """

 archiveArtifacts artifacts: 'deployment.log', fingerprint: true
}

def timestamp () {
    def now = new Date()
    return now.format("E MMM dd HH:mm:ss z yyy", TimeZone.getTimeZone("America/New_York"))
}
