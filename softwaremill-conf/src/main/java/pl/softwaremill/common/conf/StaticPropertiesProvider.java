package pl.softwaremill.common.conf;

import com.google.common.collect.ImmutableMap;

import java.util.concurrent.ConcurrentMap;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class StaticPropertiesProvider implements PropertiesProvider {
    private static ConcurrentMap<String, ImmutableMap<String, String>> configurations;
    
    @Override
    public ImmutableMap<String, String> lookupProperties(String name) {
        return configurations.get(name);
    }
    
    public void setProperties(String configurationName, ImmutableMap<String, String> properties) {
        configurations.put(configurationName, properties);
    }

    @Override
    public boolean providerAvailable() {
        return true;
    }
}
