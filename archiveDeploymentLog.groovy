// def archiveIEPDeploymentLog(ProjectName, , targetEnv, ApproverID, pipelineEnv["changeRequest"], timestamp)

// def call(Name, appVersion, targetEnv, approvers, crNumber, time)
node {
  
def date="72022"
def name="Elijah"
def appVersion="1.0"
def targetEnv="dev"
def approver="LDHVB"
def crNumber="1234"
def home = "Home"

def fileName= "/home/jenkins/workspace/log/deployment.log" 
def metricContent = "/home/jenkins/workspace/log/metrics.log"
def time = timestamp()

def exist = fileExists 'deployment.log'

if (exist != 'true') {
      sh """
            echo 'DeployDate: \tAppName: \tAppVersion: \tDeployedto: \tApprovedBy: \tKISAM CR:' > $fileName
            echo 'file Does not exist, Creating'
         """
}

println(exist)      
      
      
sh """
      set +x
  #   echo "DeployDate: $time \tAppName: $name \tAppVersion: $appVersion \tDeployedto: $targetEnv \tApprovedBy: ${approver} \tKISAM CR: $crNumber" >> $fileName
     # echo "\t${time} \t${name} \t${appVersion} \t${targetEnv} \t${approver} \t${crNumber} \t${env.BUILD_TAG}" >> $metricContent
      
      sed '1 a\\t${time} \t${name} \t${appVersion} \t${targetEnv} \t${approver} \t${crNumber} \t${env.BUILD_TAG}' $fileName
      cat $fileName
      echo "\n"
      cat 'deployment.log'
   """

 archiveArtifacts artifacts: fileName, fingerprint: true
}

def timestamp () {
    def now = new Date()
    return now.format("E MMM dd HH:mm:ss z yyy", TimeZone.getTimeZone("America/New_York"))
}
