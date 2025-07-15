package api.endpoints;

public class Routes {

    public static String base_url = "https://api.restful-api.dev";

    //Device module
    public static String post_url = base_url+ "/objects";
    public static String get_url = base_url+ "/objects/{Id}";
    public static String update_url = base_url+ "/objects/{Id}";
    public static String delete_url = base_url+ "/objects/{Id}";
}
