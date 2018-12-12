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
          sh 'ant -f deploy.xml -Dis.deploy=true'
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
