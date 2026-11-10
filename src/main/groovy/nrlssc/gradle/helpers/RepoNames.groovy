package nrlssc.gradle.helpers

enum RepoNames {
    LocalMavenRepo(['NRLSSCGEO': 'libs-all-local']), RemoteMavenRepo(['NRLSSCGEO': 'libs-all']),
    LocalIvyRepo(['NRLSSCGEO': 'ivy-all-local']), RemoteIvyRepo(['NRLSSCGEO': 'ivy-all']),

    ReleasePublishRepo(['NRLSSCGEO': 'libs-release']),
    DevPublishRepo(['NRLSSCGEO': 'libs-snapshot']),

    LegacyReleasePublishRepo(['NRLSSCGEO': 'ivy-release', '7441': 'ivy-release-7441']),
    LegacyDevPublishRepo(['NRLSSCGEO': 'ivy-dev', '7441': 'ivy-dev-7441']),

    YumPublishRepo(['NRLSSCGEO': 'rpm-release']),

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