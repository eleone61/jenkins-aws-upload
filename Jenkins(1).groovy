def filename = 'Jenkins.yaml'
def datas = readYAML file: filename


node {
  stage('Read YAML') {
    return datas
  }

  stage('Write Yaml') {
    datas.deployment.type = 'container' 
    writeYAML file: filename, data: datas, overwrite: true
  }
}



