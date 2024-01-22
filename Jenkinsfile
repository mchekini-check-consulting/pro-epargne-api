node("ci-node") {

    stage("checkout") {
        checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/mchekini-check-consulting/pro-epargne-api.git']])
    }

    stage("build"){
        sh "chmod 700 ./mvnw && ./mvnw clean install -DskipTests"
    }

}