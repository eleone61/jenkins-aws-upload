node {
def consoleCheck = sh script:  """  
                                  if ( [ -s console.log ] )
                                  then
                                      echo "File exists"
                                      return 1
                                  elif [ -f console.log ]
                                  then
                                    echo "File exists but is Null"
                                    false
                                else 
                                    echo "File does not exist"
                                    false
                                fi
                              """

  
  
  

  stage('Console Check') {
    println(consoleCheck)
  }
}
