package nrlssc.gradle

import nrlssc.gradle.extensions.NRLExtension
import nrlssc.gradle.helpers.PropertyName
import nrlssc.gradle.helpers.RepoNames
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.Copy
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class NRLPlugin implements Plugin<Project>{
    private static Logger logger = LoggerFactory.getLogger(NRLPlugin.class)

    
    @Override
    void apply(Project project) {
        
        NRLExtension nrlext = project.extensions.create("nrl", NRLExtension, project)
        nrlext.loadProperties()
        
        project.pluginManager.apply(JavaPlugin)
        project.pluginManager.apply(AppDistPlugin)
        project.pluginManager.apply(HGitPlugin)
        project.pluginManager.apply(PubPlugin)
        project.pluginManager.apply(UtilPlugin)


        project.gradle.taskGraph.whenReady {
            for (Task tsk : project.gradle.taskGraph.getAllTasks()) {
                if (tsk.name.equalsIgnoreCase("xsd-dependency-tree")) {
                    tsk.outputs.upToDateWhen { false }
                    tsk.setOnlyIf { true }
                }
            }
        }
        project.group = 'mil.navy.nrlssc'

        project.tasks.withType(Copy.class){
            it.duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
        
        project.afterEvaluate {
            NRLExtension nrl = project.extensions.getByType(NRLExtension)

            String artiUN = PropertyName.artiUsername.getAsString(project)
            String artiPW = PropertyName.artiPassword.getAsString(project)
            String glUN = PropertyName.gitlabUsername.getAsString(project)
            String glPW = PropertyName.gitlabPassword.getAsString(project)


            project.repositories {
                if (nrl.resolveArti) {
                    ivy {
                        if (nrl.remoteAllowed()) {
                            url "$nrl.artiURL/list/${RepoNames.RemoteIvyRepo.getName(nrl.groupCode)}/"
                        } else {
                            url "$nrl.artiURL/list/${RepoNames.LocalIvyRepo.getName(nrl.groupCode)}/"
                        }
                        allowInsecureProtocol = true

                        patternLayout {
                            ivy "[organisation]/[module]/[revision]/ivys/ivy-[revision].xml"
                            artifact "[organisation]/[module]/[revision]/[type]s/[artifact]-[revision](-[classifier]).[ext]"
                        }

                        if (artiUN != null && artiPW != null) {
                            credentials {
                                username = artiUN
                                password = artiPW
                            }
                        }
                    }
                    maven {
                        if (nrl.remoteAllowed()) {
                            url "$nrl.artiURL/list/${RepoNames.RemoteMavenRepo.getName(nrl.groupCode)}"
                        } else {
                            url "$nrl.artiURL/list/${RepoNames.LocalMavenRepo.getName(nrl.groupCode)}"
                        }
                        allowInsecureProtocol = true

                        if (artiUN != null && artiPW != null) {
                            credentials {
                                username = artiUN
                                password = artiPW
                            }
                        }
                    }
                }
                if (nrl.resolveGitlab) {
                    maven {
                        url "$nrl.gitlabURL/api/v4/projects/${RepoNames.GitlabMavenRelease.getName(nrl.groupCode)}/packages/maven"
                        if (glUN != null && glPW != null) {
                            credentials {
                                username = glUN
                                password = glPW
                            }
                        }
                    }
                    if (!project.hgit.isReleaseBranch(project.hgit.fetchBranch())) {
                        maven {
                            url "$nrl.gitlabURL/api/v4/projects/${RepoNames.GitlabMavenSnapshot.getName(nrl.groupCode)}/packages/maven"
                            if (glUN != null && glPW != null) {
                                credentials {
                                    username = glUN
                                    password = glPW
                                }
                            }
                        }
                    }
                }
            }

            configurePub(project)
        }
        project.configurations.all {
            it.resolutionStrategy {
                preferProjectModules()
            }
        }
        
        
    }

    static void configurePub(Project project)
    {
        String artiPubUN = PropertyName.artiPublishUsername.getAsString(project)
        String artiPubPW = PropertyName.artiPublishPassword.getAsString(project)
        String glPubUN = PropertyName.gitlabPublishUsername.getAsString(project)
        String glPubPW = PropertyName.gitlabPublishPassword.getAsString(project)

        NRLExtension nrl = project.extensions.getByType(NRLExtension)
        project.pub {
            if (nrl.publishArti) {
                repo {
                    name = "arti"
                    url = nrl.artiURL
                    username = artiPubUN
                    password = artiPubPW
                    snapshot {
                        key = RepoNames.DevPublishRepo.getName(nrl.groupCode)
                    }
                    release {
                        key = RepoNames.ReleasePublishRepo.getName(nrl.groupCode)
                    }
                    yum{
                        key = RepoNames.YumPublishRepo.getName(nrl.groupCode)
                    }
                }
            }
            if (nrl.publishGitlab) {
                repo {
                    name = "gitlab"
                    url = nrl.gitlabURL
                    pattern = "{url}/api/v4/projects/{key}/packages/maven"
                    username = glPubUN
                    password = glPubPW

                    release {
                        key = RepoNames.GitlabMavenRelease.getName(nrl.groupCode)
                        maven = true
                    }
                    snapshot {
                        key = RepoNames.GitlabMavenSnapshot.getName(nrl.groupCode)
                        maven = true
                    }
                }
            }
        }
    }
    
   
}
