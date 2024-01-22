node("ci-node") {

    stage("checkout") {
        checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/mchekini-check-consulting/pro-epargne-api.git']])
    }

    stage("unit tests"){
        sh "chmod 700 ./mvnw && ./mvnw test"
    }

    stage("build"){
        sh "chmod 700 ./mvnw && ./mvnw clean install -DskipTests"
    }

    stage("build image"){
        sh "sudo docker build -t pro-epargne-api ."
    }

    stage("build image"){
        withCredentials([gitUsernamePassword(credentialsId: 'mchekini', passwordVariable: 'password')]) {
            sh "sudo docker login -u mchekini -p $password"
            sh "sudo docker tag pro-epargne-api mchekini/pro-epargne-api:1.0"
            sh "sudo docker push mchekini/pro-epargne-api:1.0"
            sh "sudo docker rmi mchekini/pro-epargne-api:1.0"
            sh "sudo docker rmi pro-epargne-api"
            stash include: 'docker-compose.yml', name: 'utils'
        }
    }

    stage("deploy"){
        node("deploy-node"){
            unstash "utils"
            try{
                sh "sudo docker-compose down"
                sh "sudo docker-compose pull"
                sh "sudo docker-compose up -d"
            }catch (Exception e){
                 println "No Docker Container Running"
            }
        }
    }


}