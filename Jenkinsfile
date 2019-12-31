pipeline {
  agent {
    dockerfile true
  }
  triggers {
    cron '@midnight'
    pollSCM '@midnight'
  }
  options {
    buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '5'))
  }
  stages {
    stage('build') {      
      steps {        
        script {
          build('verify -Dtest=!*GaugeData*')
        }
      }
      post {
        success {
          archiveArtifacts 'target/*.nbm'
          junit 'target/surefire-reports/**/*.xml' 
        }
      }
    }

    stage('deploy') {
      when {
        branch 'master'
        not {
          triggeredBy 'TimerTrigger'
        }
      }
      steps {
        script {
          build('deploy -Dmaven.test.skip=true')
        }
      }
    }

    stage('sonar') {
      when {
        branch 'master'
        expression {
          currentBuild.result == 'SUCCESS' 
        }
      }    
      steps {        
        script {
          maven cmd: 'sonar:sonar -Dsonar.host.url=https://sonar.ivyteam.io'
        }
      }
    }
  }
}

def build(phase) {
  withCredentials([
    string(credentialsId: 'keystore-password-visual-vm-plugin', variable: 'KEYSTORE_PASSWORD'),
    file(credentialsId: 'keystore-visual-vm-plugin', variable: 'KEYSTORE_FILE')]){
    maven cmd: "clean ${phase} " +
                "-P sign " +
                "-Dcert.location=${env.KEYSTORE_FILE} -Dcert.password=${env.KEYSTORE_PASSWORD}"
  }
}
