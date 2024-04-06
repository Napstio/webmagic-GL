package us.codecraft.webmagic;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class SiteTest {

    //this test function seems to set the default charset of the site and test if it was set correctly
    @Test
    public void defaultCharsetCorrectlySetTest() {
        Site site = Site.me().setDefaultCharset(StandardCharsets.UTF_8.name());
        assertEquals(StandardCharsets.UTF_8.name(), site.getDefaultCharset());
    }

}
