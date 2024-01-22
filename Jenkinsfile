node("ci-node"){

    stage("Checkout"){
        checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/mchekini-check-consulting/pro-epargne-api.git']])
    }

    stage("Unit Tests"){
        sh "chmod 700 mvnw && ./mvnw test"
    }
}