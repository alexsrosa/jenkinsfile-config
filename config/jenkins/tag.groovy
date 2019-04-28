def generateTag(){
    newTag = getNewTag(getTags(), getVersionIncrement())
    tagRepo(newTag)
}

enum VersionIncrement { MAJOR, MINOR, PATCH }

def getVersionIncrement() {

    def Repo = load pwd() + "/config/jenkins/repo.groovy"
    def lastCommitMessage = Repo.getLastCommit()
    Repo.validateLastCommit(lastCommitMessage)

    def versionIncrement

    if(lastCommitMessage.contains('RELEASE-MAJOR-VERSION') && lastCommitMessage.contains('pull request')){
        versionIncrement = VersionIncrement.MAJOR
    } else if(lastCommitMessage.contains('Merged in release') && lastCommitMessage.contains('pull request')){
        versionIncrement = VersionIncrement.MINOR
    } else if(lastCommitMessage.contains('Merged in hotfix') && lastCommitMessage.contains('pull request')){
        versionIncrement = VersionIncrement.PATCH
    }else{
        throw new Exception("Não foi identificado o tipo de versionamento no commit: ${lastCommitMessage}")
    }

    return versionIncrement
}

def getTags() {
    def gitTagOutput = sh(script: "git tag", returnStdout: true)
    def tags = gitTagOutput.split("\n").findAll{ it =~ /^\d+\.\d+\.\d+$/ }
    return tags
}

def getNewTag(List tags, VersionIncrement increment) {
    def Common = load pwd() + "/config/jenkins/common.groovy"
    Common.debug("getNewTag( tags: {$tags}, increment: ${increment} )")

    tags.sort{ x, y -> compareTags(x, y)}
    def mostRecentTag = tags.last()
    def mostRecentTagParts = mostRecentTag.tokenize('.')

    def newTagMajor = mostRecentTagParts[0].toInteger()
    def newTagMinor = mostRecentTagParts[1].toInteger()
    def newTagPatch = mostRecentTagParts[2].toInteger()

    switch(increment) {
        case VersionIncrement.MAJOR:
            newTagMajor++
            newTagMinor = 0
            newTagPatch = 0
            break
        case VersionIncrement.MINOR:
            newTagMinor++
            newTagPatch = 0
            break
        case VersionIncrement.PATCH:
            newTagPatch++
            break
    }

    def newTag = "${newTagMajor}.${newTagMinor}.${newTagPatch}"
    return newTag
}

def compareTags(String tag1, String tag2) {
    def tag1Split = tag1.tokenize('.')
    def tag2Split = tag2.tokenize('.')

    for(def index in (0..2)) {
        def result = tag1Split[index].compareTo(tag2Split[index])
        if(result != 0) {
            return result
        }
    }

    return 0
}

def tagRepo(String newTag) {
    def tagStatus = sh(script: "git tag ${newTag}", returnStatus: true)

    if( tagStatus != 0) {
        throw new Exception("Não foi possível gerar a tag: '${newTag}'")
    }

    try {
        withCredentials(
            [[$class: 'UsernamePasswordMultiBinding', credentialsId: '<INCLUDE_CREDENTIALS_ID>', 
            usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD']]) {

            sh("git config credential.username ${env.GIT_USERNAME}")
            sh("git config credential.helper '!echo password=\$GIT_PASSWORD; echo'")
            sh("GIT_ASKPASS=true git push origin --tags")
        }
    } catch(Exception ex) {
        error "[ERROR] ${ex}"
    } finally {
        sh("git config --unset credential.username")
        sh("git config --unset credential.helper")
    }
}

return this