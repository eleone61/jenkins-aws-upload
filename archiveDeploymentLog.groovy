node {
def date="72022"
def name="Elijah"
def appVersion="1.0"
def targetEnv="dev"
def approver="LDHVB"
def crNumber="1234"
def home = "Home"

def fileName= "/var/log/deployment_log.txt"
def dashboardFileName = "${env.JENKINS_HOME}" + "/logs/ecm_deployment_log.txt"


sh """
      set +x
      echo "DeployDate: $date \tAppName: $name \tAppVersion: $appVersion \tDeployedto: $targetEnv \tApprovedBy: ${approver} \tKISAM CR: $crNumber" >> $fileName
      cp $fileName deployment.log
      echo \$(date "+%a %d %b %Y %T %p") "\t${name} \t${appVersion} \t${targetEnv} \t${approver} \t${crNumber} \t${env.BUILD_TAG}" >> $dashboardFileName
   """
      
}
