package nrlssc.gradle.extensions

import nrlssc.gradle.helpers.NRLPropertyName
import nrlssc.gradle.helpers.PropertyName
import org.gradle.api.Project
import org.gradle.internal.impldep.com.fasterxml.jackson.databind.annotation.JsonAppend
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class NRLExtension {
    private static Logger logger = LoggerFactory.getLogger(NRLExtension.class)

    String artiURL = 'http://mingus.nrlssc.navy.mil/artifactory'
    String nexusURL = 'https://nexus.di2e.net/nexus/content/repositories'
    String nexus3URL = 'https://nexus.di2e.net/nexus3/repository'
    boolean publishArti = true
    boolean resolveArti = true
    boolean publishNexus = false
    boolean resolveNexus = false
    
    String artiUsername = null
    String artiPassword = null
    String nexusUsername = null
    String nexusPassword = null
    
    String groupCode = '7432'
    
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
    
    String getGroupCode(){
        return groupCode
    }
    
    void loadProperties()
    {
        if(PropertyName.artiURL.exists(project))
        {
            this.artiURL = PropertyName.artiURL.getAsString(project)
        }
        if(PropertyName.nexusURL.exists(project))
        {
            this.nexusURL = PropertyName.nexusURL.getAsString(project)
        }
        if(PropertyName.artiUsername.exists(project))
        {
            this.artiUsername = PropertyName.artiUsername.getAsString(project)
        }
        if(PropertyName.artiPassword.exists(project))
        {
            this.artiPassword = PropertyName.artiPassword.getAsString(project)
        }
        if(PropertyName.nexusUsername.exists(project))
        {
            this.nexusUsername = PropertyName.nexusUsername.getAsString(project)
        }
        if(PropertyName.nexusPassword.exists(project))
        {
            this.nexusPassword = PropertyName.nexusPassword.getAsString(project)
        }
        if(PropertyName.resolveArti.exists(project))
        {
            this.resolveArti = PropertyName.resolveArti.getAsBoolean(project)
        }
        if(PropertyName.resolveNexus.exists(project))
        {
            this.resolveNexus = PropertyName.resolveNexus.getAsBoolean(project)
        }
        if(PropertyName.publishArti.exists(project))
        {
            this.publishArti = PropertyName.publishArti.getAsBoolean(project)
        }
        if(PropertyName.publishNexus.exists(project))
        {
            this.publishNexus = PropertyName.publishNexus.getAsBoolean(project)
        }
        if(NRLPropertyName.groupCode.exists(project))
        {
            this.groupCode = NRLPropertyName.groupCode.getAsString(project)
        }
    }
    
    boolean remoteAllowed()
    {
        return !project.hgit.isCI() && PropertyName.resolveRemote.getAsBoolean(project)
    }
    
    
    
    //region passthrough
    void setMajorVersion(int majorVersion) {
        project.hgit.setMajorVersion(majorVersion)
    }

    void setMajorVersion(String majorVersion) {
        project.hgit.setMajorVersion(majorVersion)
    }

    void setMinorVersion(int minorVersion) {
        project.hgit.setMinorVersion(minorVersion)
    }

    void setMinorVersion(String minorVersion) {
        project.hgit.setMinorVersion(minorVersion)
    }
    //endregion passthrough
}
