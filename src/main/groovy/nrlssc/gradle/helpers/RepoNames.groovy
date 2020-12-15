package nrlssc.gradle.helpers

enum RepoNames {
    LocalMavenRepo(['7432': 'libs-all-local']), RemoteMavenRepo(['7432': 'libs-all']),
    LocalIvyRepo(['7432': 'ivy-all-local']), RemoteIvyRepo(['7432': 'ivy-all']),

    ReleasePublishRepo(['7432': 'ivy-release-local', '7431': 'ivy-release-local-7441']), 
    DevPublishRepo(['7432': 'ivy-dev-local', '7431': 'ivy-dev-local-7441']),

    NexusRelPubRepo(['7432': 'Private_NRLSSCGEO_Releases', 'tileserver': 'Private_TILESERVER_Releases', 'chartserver': 'Private_VECTILESVR_Releases']),
    NexusSnapPubRepo(['7432': 'Private_NRLSSCGEO_Snapshots', 'tileserver': 'Private_TILESERVER_Snapshots', 'chartserver': 'Private_VECTILESVR_Snapshots']),
    NexusYumPubRepo(['7432': 'Private_NRLSSCGEO_Yum'])

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
            return codeMap.get('7432')
        }
    }
}