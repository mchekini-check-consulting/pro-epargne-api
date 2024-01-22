node("ci-node") {

    stage("checkout") {
        checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/mchekini-check-consulting/pro-epargne-api.git']])
    }

    stage("unit tests"){
        sh "chmod 700 ./mvnw && ./mvnw test"
    }

    stage("Quality Analyses"){
        sh "./mvnw clean verify sonar:sonar \\\n" +
                "  -Dsonar.projectKey=test \\\n" +
                "  -Dsonar.projectName='test' \\\n" +
                "  -Dsonar.host.url=http://15.237.58.46:11001 \\\n" +
                "  -Dsonar.token=sqp_be97ca1c861440747c6ad3171fed78507dcca365"
    }

    stage("build"){
        sh "chmod 700 ./mvnw && ./mvnw clean install -DskipTests"
    }

    stage("build image"){
        sh "sudo docker build -t pro-epargne-api ."
    }

    stage("push image"){
        withCredentials([usernamePassword(credentialsId: 'mchekini', usernameVariable: 'username',
                passwordVariable: 'password')]) {
            sh "sudo docker login -u $username -p $password"
            sh "sudo docker tag pro-epargne-api $username/pro-epargne-api:1.0"
            sh "sudo docker push $username/pro-epargne-api:1.0"
            sh "sudo docker rmi $username/pro-epargne-api:1.0"
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