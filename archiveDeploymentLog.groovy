def archiveIEPDeploymentLog(ProjectName, , targetEnv, ApproverID, pipelineEnv["changeRequest"], timestamp)

def call(Name, appVersion, targetEnv, approvers, crNumber, time)

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


sh """
      set +x
      echo "DeployDate: $date \tAppName: $name \tAppVersion: $appVersion \tDeployedto: $targetEnv \tApprovedBy: ${approver} \tKISAM CR: $crNumber" >> $fileName
      cp -R $fileName deployment.log
      echo \$(date "+%a %d %b %Y %T %p") "\t${name} \t${appVersion} \t${targetEnv} \t${approver} \t${crNumber} \t${env.BUILD_TAG}" >> $dashboardFileName
   """

 archiveArtifacts artifacts: 'deployment.log', fingerprint: true
}
