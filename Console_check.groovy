node {
  def consoleCheck = sh returnStdout: true, script:  """  
                                                      if ( [ -s console.log ] )
                                                      then
                                                      true
                                                      fi
   
                                                  """
  stage('Console Check') {
    println(consoleCheck)
     if (consoleCheck == "true") 
     {
       echo "Console Log exists"
     } 
  }
}
