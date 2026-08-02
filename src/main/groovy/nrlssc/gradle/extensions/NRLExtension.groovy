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

    String legalVersion = "1.1"
    String sectionCode = "7340"
    String poc = "Naval Research Laboratory"

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

    void legal(String legalVersion, String sectionCode, String poc)
    {
        this.legalVersion = legalVersion
        this.sectionCode = sectionCode
        this.poc = poc
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
        return true;//Always enabled now.  CCB is no longer centrally managed, each project must manage their own artifact approvals.  //!project.hgit.isCI() && PropertyName.resolveRemote.getAsBoolean(project)
    }

    String legalText = """* =============================================================================
* CONTROLLED UNCLASSIFIED INFORMATION // SPECIFIED - TECHNICAL INFORMATION
* =============================================================================
*
* DESIGNATION INDICATOR: 
* Controlled by: Department of the Navy
* Controlled by: U.S. Naval Research Laboratory (NRL), Code [SECTION_CODE]
* CUI Category: CUI//SP-CTI
* Distribution Statement: Distribution Statement D
* POC: [POC]
*
* PATENT & LICENSE NOTICE:
* This software is a Work of the United States Government and implements
* technology patented by the Department of the Navy.
*
* Where applicable, please refer to the LICENSE.txt file located in the
* root directory of this project.
*
* =============================================================================
* NAVAL RESEARCH LABORATORY PROPRIETARY SOURCE CODE
* =============================================================================
*
* NOTICE:  All information contained herein is, and remains
* the property of the U.S. Government as represented by the Secretary
* of the Navy. The intellectual and technical concepts contained
* herein are proprietary to the U.S. Government and may be covered
* by U.S. and Foreign Patents and patents in process.
*
* Dissemination of this information or reproduction of this material
* outside the Department of Defense or other authorized U.S. Government
* agency is strictly forbidden. Usage rights for this Software are
* freely granted to the Department of Defense and other authorized U.S.
* Government agencies, and any commercial sale of this Software or
* derivative works of this Software to these agencies is prohibited.
*
* Released under the DoD Community Source Usage Agreement Version 1.1 by
* the Naval Research Laboratory (NRL); you may not use this file except
* in compliance with the terms and conditions of DFARS 227.7103-7 (Use
* and non-disclosure agreement), 252.204-7000 (Disclosure of Information)
* and 252.227-7025 (Limitations on the Use or Disclosure of
* Government-Furnished Information Marked with Restrictive Legends). Other
* requests must be referred to the NRL. This Software is distributed on
* an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND.
*
* Distribution authorized to U.S. Government Agencies and their
* contractors. Other requests for this document shall be referred to
* the NRL. Disseminate in accordance with provisions of DoD Directive
* 5230.25.
*
* See the DCS Usage Agreement for the specific language governing
* permissions and limitations. You may obtain a copy of the Agreement
* at https://software.forge.mil/sf/go/doc1216
"""

    String getLegalText(){
        return legalText
    }

    void setLegalText(String text){
        this.legalText = text
    }
}
