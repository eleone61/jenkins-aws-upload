def filename = 'Jenkins.yaml'
def datas = readYAML file: filename


node {
  stage('Read YAML') {
    def data = readYAML file: filename
    return data
  }

  stage('Write Yaml') {
    def datas = readYAML file: filename
    datas.deployment.type = 'container' 
    writeYAML file: filename, data: datas, overwrite: true
  }
}



