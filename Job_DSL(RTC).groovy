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
                                                        buildDefinition('fbp-product-app-CICD-DAILY-BD')
                                                        connection('Build Toolkit 7.0.2','credentials','https://localhost:9443/ccm', 480)
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