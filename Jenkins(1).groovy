


node {
  stage('Read YAML') {
    def data = readYaml('Jenkins.yml')
    return data
  }

  stage('Write Yaml') {
    def datas = readYaml('Jenkins.yml')
    datas.deployment.type = 'container' 
    writeYaml file: 'Jenkins.yml', data: datas, overwrite: true
  }
}



