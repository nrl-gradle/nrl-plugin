package nrlssc.gradle.helpers

import org.gradle.api.Project
import org.slf4j.Logger
import org.slf4j.LoggerFactory

enum NRLPropertyName {
    groupCode('groupCode')


    private static Logger logger = LoggerFactory.getLogger(NRLPropertyName.class)
    private List<String> names = new ArrayList<>()

    NRLPropertyName(String... names){
        for(String s : names){
            this.names.add(s)
        }
    }

    boolean getAsBoolean(Project project)
    {
        return getAsBoolean(project, names)
    }
    
    static boolean getAsBoolean(Project project, String... names)
    {
        return getAsBoolean(project, names.toList())
    }
    
    static boolean getAsBoolean(Project project, List<String> names){
        boolean retVal = false
        for(String name : names){
            if(project.hasProperty(name)){
                retVal = project.property(name).toString().toBoolean()
                break
            }
        }
        return retVal
    }
    
    
    String getAsString(Project project)
    {
        return getAsString(project, names)
    }

    static String getAsString(Project project, String... names)
    {
        return getAsString(project, names.toList())
    }
    
    static String getAsString(Project project, List<String> names){
        String retVal = ''
        for(String name : names){
            if(project.hasProperty(name)){
                retVal = project.property(name)
                break
            }
        }
        return retVal
    }

    boolean exists(Project project)
    {
        return exists(project, names)
    }
    
    static boolean exists(Project project, String... names)
    {
        return exists(project, names.toList())    
    }
    
    static boolean exists(Project project, List<String> names){
        boolean retVal = false
        for(String name : names){
            if(project.properties.containsKey(name)){
                retVal = true
                break
            }
        }
        return retVal
    }

}