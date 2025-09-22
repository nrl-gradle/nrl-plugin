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
    String dockerURL = 'art.nrlssc.org/docker' //only works with Arti right now
    String gitlabDockerURL = 'registry.nrlssc.org'
    boolean publishArti = true
    boolean resolveArti = true
    boolean resolveArtiLegacy = false
    boolean publishArtiLegacy = false
    boolean publishGitlab = false
    boolean resolveGitlab = false
    String gitlabProject = ""  //now used for main project instead of secondary


    boolean publishDocker = false
    boolean publishGitlabDocker = false

    boolean pubSecondaryGitlab = false
    List<String> extraGitlabProjects = new ArrayList<>()
    Map<String, String> extraGitlabUsers = new HashMap<>()
    Map<String, String> extraGitlabPws = new HashMap<>()

    boolean pubSecondaryDocker = false
    List<String> extraDocker = new ArrayList<>()
    Map<String, String> extraDockerUsers = new HashMap<>()
    Map<String, String> extraDockerPws = new HashMap<>()

    boolean legacyPublish = false
    
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

    void setExtraGitlabProjects(String... gitlabProjects){
        this.extraGitlabProjects.addAll(gitlabProjects)
        pubSecondaryGitlab = true
    }

    List<String> getExtraGitlabProjects(){
        return this.extraGitlabProjects
    }

    void gitlabPublish(String projID){
        this.extraGitlabProjects.add(projID)
        pubSecondaryGitlab = true
    }

    void gitlabPublish(String projID, String un, String pw){
        this.extraGitlabProjects.add(projID)
        this.extraGitlabUsers.put(projID, un)
        this.extraGitlabPws.put(projID, pw)
        pubSecondaryGitlab = true
    }

    void dockerPublish(String registryURL, String un, String pw){
        this.extraDocker.add(registryURL)
        this.extraDockerUsers.put(registryURL, un)
        this.extraDockerPws.put(registryURL, pw)
        pubSecondaryDocker = true
    }

    List<String> getExtraDockerRegistries(){
        return this.extraDocker
    }

    void setGitlabProject(String gitlabProject){
        this.gitlabProject = gitlabProject
    }

    String getGitlabProject(){
        return gitlabProject
    }

    void setPublishDocker(boolean publishDocker){
        this.publishDocker = publishDocker;
    }

    boolean isPublishDocker(){
        return this.publishDocker
    }

    void setPublishGitlabDocker(boolean publishDocker){
        this.publishGitlabDocker = publishDocker
    }

    boolean isPublishGitlabDocker(){
        return this.publishGitlabDocker
    }

    boolean isPublishSecondary(){
        return pubSecondaryGitlab
    }

    boolean isLegacyPublish(){
        return legacyPublish
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
        if(PropertyName.gitlabProject.exists(project))
        {
            this.gitlabProject = PropertyName.gitlabProject.getAsString(project)
        }
    }
    
    boolean remoteAllowed()
    {
        return !project.hgit.isCI() && PropertyName.resolveRemote.getAsBoolean(project)
    }
}
