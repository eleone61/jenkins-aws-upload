def filename = 'Jenkins.yaml'



node {
  stage('Read YAML') {
    datas = readYAML file: filename
    return datas
  }

  stage('Write Yaml') {
    datas.deployment.type = 'container' 
    writeYAML file: filename, data: datas, overwrite: true
  }
}



