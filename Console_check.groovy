node {
def consoleCheck = sh returnStdout: true, script:  """  
                                                      if ( [ -s console.log ] )
                                                      then
                                                          echo "TRUE"
                                                      elif [ -f console.log ]
                                                      then
                                                        echo "NULL"
                                                    else 
                                                        echo "FALSE"
                                                    fi
                                                  """

  
  
  

  stage('Console Check') {
    println(consoleCheck)
    if (consoleCheck != "FALSE") {
      echo "Console Log exists"
    }else {
      break;
    }
  }
}
