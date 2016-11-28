package com.aliens.hipster.config;

import com.aliens.msg.config.HazelcastConfig;
import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.instance.HazelcastInstanceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.InputStreamReader;


@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class, DatabaseConfiguration.class,HazelcastConfig.class})
public class CacheConfiguration {

    private final Logger log = LoggerFactory.getLogger(CacheConfiguration.class);


    @Autowired
    HazelcastConfig hazelcastConfig;

    private static HazelcastInstance hazelcastInstance;



    private CacheManager cacheManager;

    @PreDestroy
    public void destroy() {
        log.info("Closing Cache Manager");
        Hazelcast.shutdownAll();
    }

    @Bean
    public CacheManager cacgheManager(HazelcastInstance hazelcastInstance) {
        log.info("Starting HazelcastCacheManager");
        cacheManager = new com.hazelcast.spring.cache.HazelcastCacheManager(hazelcastInstance);
        return cacheManager;
    }

    @Bean
    public HazelcastInstance hazelcastInstance()
    {
        Config config = getConfig();
        log.info("Creating Hazelcast instance");
        hazelcastInstance = HazelcastInstanceFactory.newHazelcastInstance(config);
        return hazelcastInstance;
    }

    public static HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }


    private Config getConfig() {
        log.info("Configuring Hazelcast");
        Config config = new Config();
        config.setInstanceName(hazelcastConfig.getInstanceName());
        config.getNetworkConfig().setPort(hazelcastConfig.getPort());
        config.getNetworkConfig().setPortAutoIncrement(false);


        if (hazelcastConfig.isAws()) {

            config.getNetworkConfig().setPublicAddress(getAwsMetaData(buildCommand(hazelcastConfig.getAwsUrl(),"local-ipv4")));

            //config.getNetworkConfig().setPublicAddress("localhost");
            config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
            config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
            AwsConfig awsCondif = config.getNetworkConfig().getJoin().getAwsConfig();
            awsCondif.setEnabled(true);
            awsCondif.setAccessKey(hazelcastConfig.getAccessKey());
            awsCondif.setSecretKey(hazelcastConfig.getSecretKey());
            awsCondif.setRegion(hazelcastConfig.getRegionCode());
            awsCondif.setSecurityGroupName(getAwsMetaData(buildCommand(hazelcastConfig.getAwsUrl(),"security-groups")));

            //awsCondif.setSecurityGroupName("test");

            awsCondif.setTagKey(hazelcastConfig.getTagKey());
            awsCondif.setTagValue(hazelcastConfig.getTagValue());
        } else {
            System.setProperty("hazelcast.local.localAddress", "127.0.0.1");
            config.getNetworkConfig().getJoin().getAwsConfig().setEnabled(false);
            config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
            config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
        }

        config.getMapConfigs().put("default", initializeDefaultMapConfig());
        //config.getMapConfigs().put("com.aliens.msg.models.*", initializeDomainMapConfig(jHipsterProperties));

        return config;
    }

    private MapConfig initializeDefaultMapConfig() {
        MapConfig mapConfig = new MapConfig();

        /*
            Number of backups. If 1 is set as the backup-count for example,
            then all entries of the map will be copied to another JVM for
            fail-safety. Valid numbers are 0 (no backup), 1, 2, 3.
         */
        mapConfig.setBackupCount(0);

        /*
            Valid values are:
            NONE (no eviction),
            LRU (Least Recently Used),
            LFU (Least Frequently Used).
            NONE is the default.
         */
        mapConfig.setEvictionPolicy(EvictionPolicy.NONE);

        /*
            Maximum size of the map. When max size is reached,
            map is evicted based on the policy defined.
            Any integer between 0 and Integer.MAX_VALUE. 0 means
            Integer.MAX_VALUE. Default is 0.
         */
        mapConfig.setMaxSizeConfig(new MaxSizeConfig(0, MaxSizeConfig.MaxSizePolicy.USED_HEAP_SIZE));

        /*
            When max. size is reached, specified percentage of
            the map will be evicted. Any integer between 0 and 100.
            If 25 is set for example, 25% of the entries will
            get evicted.
         */
        mapConfig.setEvictionPercentage(25);

        return mapConfig;
    }




    private String buildCommand(String ...params)
    {
        String command="curl %s/latest/meta-data/%s";
        return String.format(command,params);
    }


    private String getAwsMetaData(String command) {
        StringBuffer output = new StringBuffer();

        Process p;
        try {
            log.info("Running command {}",command);
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();
    }

}
