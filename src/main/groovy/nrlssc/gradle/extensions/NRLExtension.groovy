package nrlssc.gradle.extensions

import nrlssc.gradle.helpers.PropertyName
import org.gradle.api.Project
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class NRLExtension {
    private static Logger logger = LoggerFactory.getLogger(NRLExtension.class)

    String artiURL = 'https://art.nrlssc.org/artifactory'
    String artiLegacyURL = 'http://mingus.nrlssc.navy.mil/artifactory/list'
    String gitlabURL = 'https://gitlab.nrlssc.org'
    boolean publishArti = true
    boolean resolveArti = true
    boolean resolveArtiLegacy = true
    boolean publishArtiLegacy = true
    boolean publishGitlab = false
    boolean resolveGitlab = true

    boolean pubSecondaryGitlab = false
    String gitlabProject = ""

    
    String groupCode = 'NRLSSCGEO'
    
    private Project project
    NRLExtension(Project project)
    {
        this.project = project
    }
    
    void setGroupCode(String code){
        this.groupCode = code
    }
    
    void setGroupCode(int code){
        groupCode = code.toString()
    }

    void setGitlabProject(String gitlabProject){
        this.gitlabProject = gitlabProject
        pubSecondaryGitlab = true
    }

    String getGitlabProject(){
        return gitlabProject
    }

    boolean isPublishSecondary(){
        return pubSecondaryGitlab
    }
    
    String getGroupCode(){
        return groupCode
    }
    
    void loadProperties()
    {
        if(PropertyName.artiURL.exists(project))
        {
            this.artiURL = PropertyName.artiURL.getAsString(project)
        }
        if(PropertyName.gitlabURL.exists(project)) {
            this.gitlabURL = PropertyName.gitlabURL.getAsString(project)
        }
        if(PropertyName.resolveArti.exists(project))
        {
            this.resolveArti = PropertyName.resolveArti.getAsBoolean(project)
        }
        if(PropertyName.resolveGitlab.exists(project))
        {
            this.resolveGitlab = PropertyName.resolveGitlab.getAsBoolean(project)
        }
        if(PropertyName.publishArti.exists(project))
        {
            this.publishArti = PropertyName.publishArti.getAsBoolean(project)
        }
        if(PropertyName.publishGitlab.exists(project))
        {
            this.publishGitlab = PropertyName.publishGitlab.getAsBoolean(project)
        }
        if(PropertyName.groupCode.exists(project))
        {
            this.groupCode = PropertyName.groupCode.getAsString(project)
        }
    }
    
    boolean remoteAllowed()
    {
        return !project.hgit.isCI() && PropertyName.resolveRemote.getAsBoolean(project)
    }
}
