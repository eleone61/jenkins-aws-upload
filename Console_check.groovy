node {
def consoleCheck = sh returnStdout: true, script:  """  
                                                      if ( [ -s console.log ] )
                                                      then
                                                          echo "true" \ sed '\$d'
                                                      elif [ -f console.log ]
                                                      then
                                                        echo "null" | sed '\$d'
                                                    else 
                                                        echo "false" | sed '\$d'
                                                    fi
                                                  """

  
  
  

  stage('Console Check') {
    println(consoleCheck)
    if (consoleCheck == "false") {
      echo "Console Log exists"
    } else {
        error "No Console Log exists";
    }
  }
}
