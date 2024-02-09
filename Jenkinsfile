node("ci-node") {

    def GIT_COMMIT_HASH = ""

    stage("Checkout") {
        checkout scmGit(branches: [[name: '*/develop']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/mchekini-check-consulting/pro-epargne-api.git']])
        GIT_COMMIT_HASH = sh (script: "git log -n 1 --pretty=format:'%H'", returnStdout: true)
    }

    stage("Unit Tests") {
        sh "chmod 700 mvnw && ./mvnw test"
    }

    // stage("Quality Analyses"){
    //     sh "./mvnw clean verify sonar:sonar \\\n" +
    //             "  -Dsonar.projectKey=pro-epargne-api \\\n" +
    //             "  -Dsonar.projectName='pro-epargne-api' \\\n" +
    //             "  -Dsonar.host.url=http://15.236.158.221:11001 \\\n" +
    //             "  -Dsonar.token=sqp_b8aeb87695f8c71e1de1cbcd46e313ecfb874e3c"
    // }

    stage("Build") {
        sh "./mvnw package -DskipTests"
    }

    stage("Build Docker Image") {
        sh "sudo docker build -t pro-epargne-api ."
    }

    stage("Push Docker Image To Registry") {
        sh "sudo docker tag pro-epargne-api mchekini/pro-epargne-api:$GIT_COMMIT_HASH"
        sh "sudo docker tag pro-epargne-api mchekini/pro-epargne-api:latest"
        withCredentials([usernamePassword(credentialsId: 'mchekini', passwordVariable: 'password', usernameVariable: 'username')]) {
            sh "sudo docker login -u $username -p $password"
            sh "sudo docker push mchekini/pro-epargne-api:$GIT_COMMIT_HASH"
            sh "sudo docker push mchekini/pro-epargne-api:latest"
            sh "sudo docker rmi mchekini/pro-epargne-api:$GIT_COMMIT_HASH"
            sh "sudo docker rmi mchekini/pro-epargne-api:latest"
            sh "sudo docker rmi pro-epargne-api"
            stash include: 'docker-compose.yml', name: 'utils'
        }
    }

}