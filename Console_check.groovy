
def consoleCheck = sh script:  """  
                                  if ( [ -s console.log ] )
                                  then
                                      echo "File exists"
                                  elif [ -f console.log ]
                                  then
                                    echo "File exists but is Null"
                                else 
                                    echo "File does not exist"
                                fi
                              """

  
  
  
  
node {
  stage('Console Check') {
    println(consoleCheck)
  }
}
