@GrabResolver(name='Maven Central', root='http://repo1.maven.org/')
@Grab(group='redis.clients', module='jedis', version='2.1.0')
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub

@NonCPS
def call() {

    def log = ""
    def rev
    def tagUser

    Jedis jedis = new Jedis("ip", 6379, 0)
    jedis.connect();

    def ln = System.getProperty('line.separator')
        def changeLogSets = currentBuild.rawBuild.changeSets
        for (int i = 0; i < changeLogSets.size(); i++) {
            def entries = changeLogSets[i].items
            for (int j = 0; j < entries.length; j++) {
                def entry = entries[j]
                rev = entry.revision
                if ( rev instanceof String ) {
                    rev = entry.revision.take(8)
                }
                def msg = entry.msg.replaceAll("\n"," ")
                tagUser = jedis.get(entry.committerEmail)
                log += "[$rev] [$msg] [${entry.author}] [@$tagUser] ${ln}"
            }
        }
        return log;

}