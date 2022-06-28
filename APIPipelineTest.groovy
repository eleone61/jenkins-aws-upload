if("${env.JOB_NAME}".endsWith("Deploy")){
iep_Deployment()
}


def iep_Deployment()
{
    node(buildNode)
    {
        performPreStage ()
        withEnv([
            "JAVA_HOME=${JAVA_HOME}",
            "ANT_HOME=${ANT_HOME}",
            "M2_HOME=${MAVEN_HOME}",
            "MAVEN_HOME=${MAVEN_HOME}",
            "MVN_HOME=${MAVEN_HOME}",
            "SONAR_HOME=${SONAR_HOME}",
            "ASA_HOME=${ASA_HOME}",
            "CC_HOME=${CC_HOME}"
        ]) {
            performStageA ()
            performStageB ()
            performStageH_iep ()
        }
            completeCDStage ()
        }


    }
}


def performStageA (){
	stage(A)
	       {
	
	              
	              CurrentStage = A
	              echo "${A} - start"
	
	              if (PullFromSCM)
	              {
	                      try
	                      {
	                      
	                      
	       clearWorkspace()      
	       switch(scmType.toUpperCase()) {
	          case "GIT" :
	                        Git_source_pull (Credential, gitRepoURL, gitRepoBranch)
	             break; 
	          
	          case "RTC" :
	          case "RTC_TRAINING" :
	          
	                     if( scmType.toUpperCase() == "RTC" )
	                             RTCServerURL = RTCLiveURL
	                     else
	                             RTCServerURL = RTCTrainURL
	          
	                             RTC_source_pull (BuildToolKit, BuildDefinition, Credential, RTCServerURL)
	                             RTC_source_pull_change_set(ProjectName, buildLabel, BuildToolKit, BuildDefinition, RTCServerURL, Credential, buildNode, rtcWorkspaceName, loadRTCDirectory, createFoldersForRTCComponents)
	             break; 
	          
	          case "BASE" :
	                             CCBase_source_pull_change_set (ProjectName, currentBuild.displayName, env.NODE_NAME, ccConfigSpec, ccLoadRules, viewname) 
	             break; 
	
	          case "UCM" :
	                CC_ucm_source_pull(ccStream, ccLoadRules, viewname)
	                        getChangeSet()
	             break; 
	
	          default : 
	                throw new Exception("Invalid repository parameter value specified.");
	                             
	                      sh "chmod -R u+w ."
	
	                      }
	              }
	                      catch (Exception exc)
	                      {
	                             echo "${A} Failed"
	                             sendEmail(ErrorSubject+"$CurrentStage\n\n${exc}", ErrorBody, ErrorRecipient, ReplyTo)
	                             throw(exc)
	                      }
	                      
	              }
	              else
	                      echo "Skipping ${A}"
	
	              echo "${A} - complete"
	              
	              try {          
	                      patchSandbox(ProjectName, project_directory, patchJob)
	                      }
	              catch (Exception exc)
	                      {
	                             echo "Patching Failed"
	                             sendEmail(ErrorSubject+" Patching Stage\n\n${exc}", ErrorBody, ErrorRecipient, ReplyTo)
	                             throw(exc)
	                      }
	              
	              
	       }
	       
	return this
	
	}
	



def performStageB (){
	       stage(B)
	       {
	              CurrentStage = B
	              echo "${B} - start"
	                      
	              if (BuildAndCompile)
	              {
	                      try
	                      {
	                      
	                      
	                      
	       switch(buildTool.toUpperCase()) {
	          case "ANT" :
	                      containerAntBuild(ProjectName, project_root, buildArgs, antTarget, packageName, ecpConfig, ProjectNameLower, deployType )
	             break; 
	          case "MAVEN" :
	//                    containerMavenBuild (ProjectName, project_root, junit_root, buildArgs, mvnGoals, packageName,ecpConfig, ProjectNameLower, deployType, buildManifest, buildManifestNonFlatten)
	                      iepMavenBuild (ProjectName, project_root, junit_root, buildArgs, mvnGoals, packageName,ecpConfig, ProjectNameLower, deployType, buildManifest, buildManifestNonFlatten)
	//                    IEPMavenBuild (ProjectName, project_root, junit_root, buildArgs, mvnGoals, packageName,ecpConfig, ProjectNameLower, deployType, buildManifest, buildManifestNonFlatten, BUILD_ID, targetEnv)
	             break; 
	        case "NONE" :
	                    echo "No Build is needed"
	            break;
	          
	          default : 
	                throw new Exception("Invalid build tool value specified.");
	                             
	                      
	                      }
	                      
	                             //createBuildArtifact()
	                      
	              // Run the Nexus IQ scan
	
	    // print NexusIQProfileName
	    //           if (!NexusIQProfileName.equals("NA") )
	    //           {
	    //                          print NexusIQProfileName
	    //                          performNexusIQScan(NexusIQProfileName, ErrorSubject, Recipient, ReplyTo, NexusIQThreshold)
	    //           }
	              
	            }
	                      catch (Exception exc)
	                      {
	                             echo "${B} Failed"
	                             sendEmail(ErrorSubject+"$CurrentStage\n\n${exc}", ErrorBody, ErrorRecipient, ReplyTo)
	                             throw(exc)
	                      }
	                      finally
	                      {
	                      }
	              }
	              else
	                      echo "Skipping ${B}"
	
	              echo "${B} - complete"
	       }
	       
	return this
	
	}
  
  
  def iepUpdateManifest(pipelineEnv,iepUser,ProgramName,ProjectName) {
	       def file = readYaml file: "manifest.yaml"

           
	       file['environment'] = pipelineEnv.toLowerCase()
	       file['kind']= "promotion"
	       
	       sh """
	              if [ -f manifest.yaml ] ; then
	                      rm -f manifest.yaml
	              fi
	       """
	       
	       writeYaml file: 'manifest.yaml', data: file, overwrite: true
	       
	       sh 'cat manifest.yaml'
	       
	       sh """
	       scp -q -o LogLevel=QUIET manifest.yaml ${iepUser}@va1mgtltepi1.rup.afsiep.net:/opt/cloud-partial/${ProgramName}/${ProjectName}

	       echo 'mv /opt/cloud-partial/${ProgramName}/${ProjectName}/* /opt/cloud/${ProgramName}/${ProjectName}/' | ssh ${iepUser}@va1mgtltepi1.rup.afsiep.net

	       """
	}
