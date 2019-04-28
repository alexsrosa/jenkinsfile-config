def validateLastCommit(String lastCommitMessage){
    if(lastCommitMessage == null){
        lastCommitMessage = getLastCommit()
    }
    
    def Common = load pwd() + "/config/jenkins/common.groovy"
    Common.debug("Último mensagem de commit: ${lastCommitMessage}")

    if(!lastCommitMessage.contains('pull request')){
        throw new Exception("Commit não tem origem de um PULL REQUEST: ${lastCommitMessage}")
    }
}

String getLastCommit(){
    def lastCommitMessage = sh returnStdout: true, script: 'git log --pretty=format:"%s"  -1'
    return lastCommitMessage
}

return this