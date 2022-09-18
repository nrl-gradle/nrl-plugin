package nrlssc.gradle.helpers

enum RepoNames {
    LocalMavenRepo(['NRLSSCGEO': 'libs-all-local']), RemoteMavenRepo(['NRLSSCGEO': 'libs-all']),
    LocalIvyRepo(['NRLSSCGEO': 'ivy-all-local']), RemoteIvyRepo(['NRLSSCGEO': 'ivy-all']),

    ReleasePublishRepo(['NRLSSCGEO': 'ivy-release-local', '7441': 'ivy-release-local-7441']),
    DevPublishRepo(['NRLSSCGEO': 'ivy-dev-local', '7441': 'ivy-dev-local-7441']),

    YumPublishRepo(['NRLSSCGEO': 'rpm-release-local']),

    GitlabMavenRelease(['NRLSSCGEO': '48']), GitlabMavenSnapshot(['NRLSSCGEO': '247'])

    private Map<String, String> codeMap

    RepoNames(Map<String, String> codeMap)
    {
        this.codeMap = codeMap
    }

    String getName(String groupCode)
    {
        if(codeMap != null && codeMap.containsKey(groupCode))
        {
            return codeMap.get(groupCode)
        }
        else
        {
            return codeMap.get('NRLSSCGEO')
        }
    }
}