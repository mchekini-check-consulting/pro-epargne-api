node("ci-node"){

    stage("Checkout"){
        checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/mchekini-check-consulting/pro-epargne-api.git']])
    }

    stage("Unit Tests"){
        sh "chmod 700 mvnw && ./mvnw test"
    }

    stage("Build"){
        sh "./mvnw package -DskipTests"
    }

    stage("Build Docker Image"){
        sh "sudo docker build -t pro-epargne-api ."
    }
}