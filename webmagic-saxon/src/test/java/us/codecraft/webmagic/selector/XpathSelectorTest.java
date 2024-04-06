package us.codecraft.webmagic.selector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.json.JSONObject;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import org.w3c.dom.Node;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.xsoup.XPathEvaluator;
import us.codecraft.xsoup.Xsoup;

import javax.xml.transform.TransformerException;

/**
 * @author code4crafter@gmail.com <br> Date: 13-4-21 Time: 上午10:06
 */
public class XpathSelectorTest {


    private String html = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/java/us/codecraft/webmagic/selector/testSrc/html.json")))).getString("myString");

    /**
     * New api test
     *
     * @author hooy
     * @since 8.0
     */
    private String rank = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/java/us/codecraft/webmagic/selector/testSrc/rank.json")))).getString("myString");



    public XpathSelectorTest() throws IOException {
    }


    @Test
    public void test() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("src/test/java/us/codecraft/webmagic/selector/testSrc/text.json")));
        JSONObject readJsonObject = new JSONObject(content);
        String readString = readJsonObject.getString("myString");
        XpathSelector xpathSelector = new XpathSelector(
                "//div[@id='main']/div[@class='blog_main']/div[@class='blog_title']/h3/a/text()");
        String select = xpathSelector.select(readString);
        Assert.assertEquals("jsoup 解析页面商品信息", select);
    }

    @Test
    public void testOschina() {
        Html html1 = new Html(html);
        Assert.assertEquals("再次吐槽easyui", html1.xpath("//*[@class='QTitle']/h1/a/text()").toString());
        Assert.assertNotNull(html1.$("a[href]").xpath("//@href").all());
        Selectors.xpath("/abc/").select("");
    }

    @Test
    public void testXPath2() {
        String text = "<h1>眉山：扎实推进农业农村工作 促农持续增收<br>\n" +
                "<span>2013-07-31 23:29:45&nbsp;&nbsp;&nbsp;来源：<a href=\"http://www.mshw.net\" target=\"_blank\" style=\"color:#AAA\">眉山网</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;责任编辑：张斯炜</span></h1>";
        Xpath2Selector xpathSelector = new Xpath2Selector("//h1/text()");
        Assert.assertEquals("眉山：扎实推进农业农村工作 促农持续增收", xpathSelector.select(text));
    }

    @Test
    public void testXpath2Selector() {
        Xpath2Selector xpath2Selector = new Xpath2Selector("//xhtml:a/@href");
        String select = xpath2Selector.select(html);
        Assert.assertEquals("http://www.oschina.net/", select);

        List<String> selectList = xpath2Selector.selectList(html);
        Assert.assertEquals(113, selectList.size());
        Assert.assertEquals("http://www.oschina.net/", selectList.get(0));
    }

    @Ignore("take long time")
    @Test
    public void performanceTest() {
        Xpath2Selector xpath2Selector = new Xpath2Selector("//a");
        long time = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            xpath2Selector.selectList(html);
        }
        System.out.println(System.currentTimeMillis() - time);

        XpathSelector xpathSelector = new XpathSelector("//a");
        time = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            xpathSelector.selectList(html);
        }
        System.out.println(System.currentTimeMillis() - time);

        time = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            xpath2Selector.selectList(html);
        }
        System.out.println(System.currentTimeMillis() - time);

        CssSelector cssSelector = new CssSelector("a");
        time = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            cssSelector.selectList(html);
        }
        System.out.println("css " + (System.currentTimeMillis() - time));
    }

    @Ignore("take long time")
    @Test
    public void parserPerformanceTest() throws XPatherException {
        System.out.println(html.length());

        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode tagNode = htmlCleaner.clean(html);
        Document document = Jsoup.parse(html);

        long time = System.currentTimeMillis();
        for (int i = 0; i < 2000; i++) {
            htmlCleaner.clean(html);
        }
        System.out.println(System.currentTimeMillis() - time);

        time = System.currentTimeMillis();
        for (int i = 0; i < 2000; i++) {
            tagNode.evaluateXPath("//a");
        }
        System.out.println(System.currentTimeMillis() - time);

        System.out.println("=============");

        time = System.currentTimeMillis();
        for (int i = 0; i < 2000; i++) {
            Jsoup.parse(html);
        }
        System.out.println(System.currentTimeMillis() - time);

        time = System.currentTimeMillis();
        for (int i = 0; i < 2000; i++) {
            document.select("a");
        }
        System.out.println(System.currentTimeMillis() - time);

        System.out.println("=============");

        time = System.currentTimeMillis();
        for (int i = 0; i < 2000; i++) {
            htmlCleaner.clean(html);
        }
        System.out.println(System.currentTimeMillis() - time);

        time = System.currentTimeMillis();
        for (int i = 0; i < 2000; i++) {
            tagNode.evaluateXPath("//a");
        }
        System.out.println(System.currentTimeMillis() - time);

        System.out.println("=============");

        XPathEvaluator compile = Xsoup.compile("//a");
        time = System.currentTimeMillis();
        for (int i = 0; i < 2000; i++) {
            compile.evaluate(document);
        }
        System.out.println(System.currentTimeMillis() - time);

    }

    @Test
    public void testStringAPI() {
        // testAPI: selectList(String) -> selectList(Node)
        List<String> items = new Xpath2Selector("//div[@class=\"bd\"]//tbody/tr").selectList(rank);
        Assert.assertSame(100, items.size());
        // testAPI: select(String) -> select(Node)
        String name = new Xpath2Selector("//td[3]/div/a[1]/text()").select(items.get(10));
        Assert.assertEquals("深宫安容传", name);
    }

    @Test
    public void testNodeAPI() {
        // testAPI: selectNodes(String) -> selectNodes(Node)
        List<Node> items = new Xpath2Selector("//div[@class=\"bd\"]//tbody/tr").selectNodes(rank);
        Assert.assertSame(100, items.size());
        // testAPI: selectNode(Node)
        Node item = new Xpath2Selector("./td[3]/div/a[1]").selectNode(items.get(10));
        String name = new Xpath2Selector("./text()").select(item);
        Assert.assertEquals("深宫安容传", name);
    }

    @Test
    public void testUtilAPI() throws TransformerException {
        Node item = Xpath2Selector.newInstance("//div[@class=\"bd\"]//tbody/tr[11]/td[3]/div/a[1]/text()").selectNode(rank);
        // testAPI: nodeToString(Node) -> nodesToStrings(List<Node>)
        String name = JaxpSelectorUtils.nodeToString(item);
        Assert.assertEquals("深宫安容传", name);
    }

}
