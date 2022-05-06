def filename = 'Jenkins.yaml'


node {
  stage('Read YAML') {
    def data = readYAML(file: filename)
    return data
  }

  stage('Write Yaml') {
    def datas = readYAML file: filename
    datas.deployment.type = 'container' 
    writeYAML file: filename, data: datas, overwrite: true
  }
}



