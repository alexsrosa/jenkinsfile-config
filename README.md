# Git tag automation by Jenkinsfile (Automatização de git tag pelo Jenkinsfile)

## Control of the git tag when committing to the `master` that is derived from pull request

This implementation follows the SEMVER `https://semver.org/`

If it is a hotfix, increment the PATCH level (0.0.X), if it is a release source, increment the MINOR level (0.X.0), or if the source branch contains the name or part of the `RELEASE- MAJOR-VERSION`, increments MAJOR (X.0.0).

## (PT_BR) Controle da tag do git ao efetuar commit na `master` que tenha origem de pull request

Esta implementação segue o padrão SEMVER `https://semver.org/`

Caso seja de um hotfix, incrementa o nível PATCH (0.0.X), caso seja de origem de release, incrementa o nível MINOR (0.X.0) ou se o branch de origem contiver o nome ou part do nome `RELEASE-MAJOR-VERSION`, incrementa MAJOR (X.0.0).