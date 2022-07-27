node {
def consoleCheck = sh returnStdout: true, script:  """  
                                                      if ( [ -s console.log ] )
                                                      then
                                                          true
                                                      elif [ -f console.log ]
                                                      then
                                                        false
                                                    else 
                                                        false
                                                    fi
                                                  """

  
  
  

  stage('Console Check') {
    println(consoleCheck)
  }
}
