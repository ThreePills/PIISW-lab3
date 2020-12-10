pipeline {
  agent any
    
  tools {java "jdk", maven "maven"}
    
  stages {
        
    stage('Cloning Git') {
      steps {
        git 'https://github.com/ThreePills/PIISW-lab3'
      }
    }
        
    stage('Build jar') {
      steps {
        sh 'mvn clean install -DskipTests'
      }
    }
     
    stage('Test') {
      steps {
         sh 'mvn test'
      }
    }      
  }
}