pipeline {
  agent {
    docker {
      image 'maven:3.5.2-jdk-8'
    }
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
