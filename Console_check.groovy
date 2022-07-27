node {
  def consoleCheck = sh returnStdout: true, script:  """  
                                                      if ( [ -s console.log ] )
                                                      then
                                                          echo 'true' 
                                                      elif [ -f console.log ]
                                                      then
                                                        echo 'null' 
                                                    else 
                                                        echo 'false' 
                                                    fi
                                                  """
  stage('Console Check') {
    println(consoleCheck.trim())
    if (consoleCheck.trim() == "true") 
    {
      echo "Console Log exists"
    } 
  }
}
