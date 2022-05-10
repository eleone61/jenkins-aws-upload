def apiVersion = 'v1'
def env = 'dev'
def version = '1.0.0'
def buildID = 'xyz'
def appFamily = 'Test Family'
def appName = 'Test Application'
def jar_file = 'application.jar'
def yaml_file = 'Jenkins.yml'
def docker = 'dockerfile'
def MD5 = 'MD5NUM'
def DEPLOY_TYPE = 'container'
def DEPLOY_TARGET = 'EKS'
def DEPLOY_STYLE = 'non-intrusive'
def DEPLOY_WINDOW = 'asap'
def ext = '.txt'
def ext2 = '.yaml'
def ext3 = '.jar'

def createYAML(){
    sh """
      cat << EOF > Jenkins.yml 
       apiVersion: test
       environment: test
       version: test
       appFamily: test
       appName: test
       buildID: test
       kind: deployment
       manifest:
        - Test
        - Test
        - Test
        - Test
       deployment:
        type: test
        target: test
        style: test
        window: test
      #Optional Fields
        healthcheckURI: ":80/irmod/test.html"
    """
}

def createFolder(){
    sh """
            mkdir test-folder
           
            touch test-folder/file1.txt
            touch test-folder/file2.yaml
            touch test-folder/file3.jar
            
            
           """
}

node {
  stage('Create YAML') {
    createYAML() 
    sh 'cat Jenkins.yml'
  }
  stage('Read YAML') {
    sh 'pwd'
    sh 'ls'
    def data = readYaml file: 'Jenkins.yml'
    return data
  }

  stage('Write Yaml') {
     def folder = findFiles(glob: 'test-folder/*')
     echo "finding files: ${folder}"
    def datas = readYaml file: 'Jenkins.yml'
    datas.apiVersion = apiVersion
    datas.environment = env
    datas.version = version
    datas.appFamily = appFamily
    datas.appName = appName
    datas.buildID = buildID
    datas.deployment.type = DEPLOY_TYPE
    datas.deployment.target = DEPLOY_TARGET
    datas.deployment.style = DEPLOY_STYLE
    datas.deployment.window = DEPLOY_WINDOW
    datas.manifest = folder  
    
//     for (int i = 0; i < folder.size(); i++) {
//         println folder[i]
//         datas.manifest[i] = folder[i]
//         echo 'step 1'
    
//         for (files in folder[i]) {
//             if (files.path.endsWith(ext)) {
//                 datas.manifest[i] = files
//                 println files
//             }
//             else if(files.path.endsWith(ext2)) {
//                 datas.manifest[i] = files
//                 println files
//             }
//                  else {
//                 datas.manifest[i] = files
//                 println files
//             }
//        }
//     } 

//  println datas.manifest
// //       for (i = 0; i < datas.manifest.size(); i++) {
// //        writeYaml file: 'Jenkins.yml', data: datas.add(datas.manifest[i]), overwrite: true 
// //       }
    
    writeYaml file: 'Jenkins.yml', data: datas, overwrite: true
    sh 'cat Jenkins.yml'
  }
    
        }   

// def folder = new File('jenkins-aws-upload/Test Folder')
//         folder.eachFileecurse FileType.FILES,   { file ->
//             if(!file.name.endsWith(".txt")) {
//                 println "Listing Files ${file.absolutePath}"


