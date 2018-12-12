pipeline {
  agent {
    dockerfile true
  }
  triggers {
    cron '@midnight'
  }
  options {
    buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '5'))
  }
  stages {
    stage('build and deploy') {      
      steps {        
        script {
          maven cmd: 'clean verify -Dtest=!*GaugeData*'

          withCredentials([
            string(credentialsId: 'keystore-PASSWORD-visual-vm-plugin', variable: 'KEYSTORE_PASSWORD'),
            file(credentialsId: 'keystore-visual-vm-plugin', variable: 'KEYSTORE_FILE')]) {
            sh "ant -f deploy.xml -Dcert.location=${env.KEYSTORE_FILE} -Dcert.password=${env.KEYSTORE_PASSWORD}"
          }
        }
      }
      post {
        success {
          archiveArtifacts 'target/nbm/*.nbm'
          junit 'target/surefire-reports/**/*.xml' 
        }
      }
    }
  }
}
