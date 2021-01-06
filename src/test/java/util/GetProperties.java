package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class GetProperties {

    private Properties prop = new Properties();
    private String configPropPath;

    public GetProperties() throws IOException {
        String configPath = readEnvironment();
        loadConfigProperties(configPath);
    }

    private String readEnvironment() {
        return configPropPath = getClass().getClassLoader().getResource("base_url.properties").getPath();
    }

    private void loadConfigProperties(String path) throws IOException {
        InputStream in = new FileInputStream(path);
        prop.load(new InputStreamReader(in, StandardCharsets.UTF_8));
    }

    public String getBaseUrl() throws IOException {
        return prop.getProperty("BASE_URL");
    }


}
