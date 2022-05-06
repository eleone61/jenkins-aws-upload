def filename = 'Jenkins.yaml'


node {
  stage('Read YAML') {
    def data = readYaml(file: filename)
    return data
  }

  stage('Write Yaml') {
    def datas = readYaml file: filename
    datas.deployment.type = 'container' 
    writeYaml file: filename, data: datas, overwrite: true
  }
}



