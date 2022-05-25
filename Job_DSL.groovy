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
                                                    git{
                                                        branch('*/main')
                                                        remote{ url('https://github.com/eleone61/jenkins-aws-upload.git') }
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
                                            properties ([
                                                 copyArtifactPermission('*'),
                                                 disableConcurrentBuilds()
                                                 ])
                                          }
                             """
        }
    }
}
