package top.woodwhale.taobaounion.utils;

public class UrlUtils {
    public static String createHomePagerUrl(int categoryId, int page) {
        return "discovery/"+categoryId+"/"+page;
    }

    public static String getCoverPath(String pictUrl) {
        return "https:" + pictUrl;
    }
}
