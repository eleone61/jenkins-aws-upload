node {
stage('DSL') {
        withEnv(["jobName=${params.JobName}", "jobDescription=${params.JobDescription}", "jobTrigger=${params.JobTrigger}", "jobScriptpath=${params.JobScriptPath}"]) {
            echo jobName
            echo jobDescription
            echo jobTrigger
            echo jobScriptpath
            jobDsl scriptText: """
                                pipelineJob('$jobName') { 
                                    definition {
                                            cpsScm {
                                                lightweight(true)
                                                scm {
                                                    rtc {
                                                        buildDefinition('Test-BD')
                                                        connection('None','Test-Cred','https://localhost:9443/ccm', 480)
                                                        }
                                                    }
                                                scriptPath('$jobScriptpath')
                                                }
                                            }
                                    triggers {
                                            scm('$jobTrigger')
                                        }
                                    logRotator {
                                        daysToKeep(365)
                                        }
                                    description('$jobDescription')
                                    properties {
                                                copyArtifactPermissionProperty {
                                                    projectNames('onboarding-test') }
                                                disableConcurrentBuilds()
                                                 }
                                          }
                                """
        }
}
}
