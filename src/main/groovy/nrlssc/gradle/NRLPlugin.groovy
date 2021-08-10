package nrlssc.gradle

import nrlssc.gradle.extensions.NRLExtension
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

                        if (nrl.artiPassword != null && nrl.artiUsername != null) {
                            credentials {
                                username = nrl.artiUsername
                                password = nrl.artiPassword
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

                        if (nrl.artiPassword != null && nrl.artiUsername != null) {
                            credentials {
                                username = nrl.artiUsername
                                password = nrl.artiPassword
                            }
                        }
                    }
                }
                if (nrl.resolveNexus) {
                    maven {
                        url "$nrl.nexusURL/${RepoNames.NexusRelPubRepo.getName(nrl.groupCode)}"
                        if (nrl.nexusPassword != null && nrl.nexusUsername != null) {
                            credentials {
                                username = nrl.nexusUsername
                                password = nrl.nexusPassword
                            }
                        }
                    }
                    if (!project.hgit.isReleaseBranch(project.hgit.fetchBranch())) {
                        maven {
                            url "$nrl.nexusURL/${RepoNames.NexusSnapPubRepo.getName(nrl.groupCode)}"
                            if (nrl.nexusPassword != null && nrl.nexusUsername != null) {
                                credentials {
                                    username = nrl.nexusUsername
                                    password = nrl.nexusPassword
                                }
                            }
                        }
                    }
                    maven{
                        url "$nrl.nexus3URL/repo1.maven.org"
                        if (nrl.nexusPassword != null && nrl.nexusUsername != null) {
                            credentials {
                                username = nrl.nexusUsername
                                password = nrl.nexusPassword
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
        NRLExtension nrl = project.extensions.getByType(NRLExtension)
        project.pub {
            if (nrl.publishArti) {
                repo {
                    name = "arti"
                    url = nrl.artiURL
                    username = nrl.artiUsername
                    password = nrl.artiPassword
                    snapshot {
                        key = RepoNames.DevPublishRepo.getName(nrl.groupCode)
                    }
                    release {
                        key = RepoNames.ReleasePublishRepo.getName(nrl.groupCode)
                    }
                }
            }
            if (nrl.publishNexus) {
                repo {
                    name = "nexus"
                    url = nrl.nexusURL
                    username = nrl.nexusUsername
                    password = nrl.nexusPassword

                    release {
                        key = RepoNames.NexusRelPubRepo.getName(nrl.groupCode)
                        maven = true
                    }
                    snapshot {
                        key = RepoNames.NexusSnapPubRepo.getName(nrl.groupCode)
                        maven = true
                    }
                }
                repo{
                    name = "nexusYum"
                    url = nrl.nexus3URL
                    username = nrl.nexusUsername
                    password = nrl.nexusPassword

                    yum{
                        key = RepoNames.NexusYumPubRepo.getName(nrl.groupCode)
                    }
                }
            }
        }
    }
    
   
}
