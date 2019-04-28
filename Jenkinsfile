node {
    stage("Checkout"){
       deleteDir()
       checkout([
            $class: 'GitSCM',
            branches: scm.branches,
            doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
            extensions: [[$class: 'CloneOption', noTags: false, shallow: false, depth: 0, reference: '']],
            userRemoteConfigs: scm.userRemoteConfigs,
        ])
    }
    stage("Validate last commit"){
        // Validate if the branch is master and if it is
        if(env.BRANCH_NAME == 'master'){

            // Validate if the last commit is in the default
            def Repo = load pwd() + "/config/jenkins/repo.groovy"
            Repo.validateLastCommit(null)
        }
    }
    stage("Build and Install"){
        echo 'Build and Install'
    }
    stage("Archive"){
        echo 'Archive'
    }
    stage("Deploy"){      
        if(env.BRANCH_NAME == 'master'){
            echo 'é a master'
        }else{
            echo 'é outro branch'
        }
    }
    stage("Increment Tag"){ 
        // Validate if the branch is master and if it is
        if(env.BRANCH_NAME == 'master'){
            // Automatically generate a new tag
            def Tag = load pwd() + "/config/jenkins/tag.groovy"
            Tag.generateTag()
        }
    }
    stage("CleanUp"){ 
        deleteDir()
    }
}