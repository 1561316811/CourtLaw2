import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestForRegex {

    public static void main(String[] args) {
       String str = " 1.1  1.1.1  1.1.1.1  1.1.1.2  1.2  1.2.1  1.2.1.1  1.2.1.2 ";

        String regEx = "\\s\\d{1}\\.\\d{1}\\s";
//        String regEx = "\\s\\d{1}\\.\\d{1}\\.\\d{1}\\s";
//        String regEx = "\\s\\d{1}\\.\\d{1}\\.\\d{1}\\.\\d{1}\\s";

//        regEx = new Scanner(System.in).nextLine();
        System.out.println(regEx);
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        // 字符串是否与正则表达式相匹配
//        boolean rs = matcher.matches();


        while(matcher.find()) {
//        while(matcher.lookingAt())


//            matcher.find(60);
            System.out.println("matcher.find() '" + matcher.group() +
                    "' start = " + matcher.start() + " end = " + matcher.end());
        }

    }

    public static void main1(String[] args) {
        // 要验证的字符串
        String str = "service@xsoftlab.dasfdasdf.sdsdfl.net     service@xsoftlab.dasfdasdf.sdsdfl.net   service@xsoftlab.dasfdasdf.sdsdfl.net";
        // 邮箱验证规则
//        str  = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        String regEx = "[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-Z\\-]{1,}";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        // 字符串是否与正则表达式相匹配
//        boolean rs = matcher.matches();


        while(matcher.find()) {
//        while(matcher.lookingAt())


            matcher.find(60);
            System.out.println("matcher.find() '" + matcher.group() +
                    "' start = " + matcher.start() + " end = " + matcher.end());
        }
//        while(matcher.find()) {
//            for(int j = 0; j <= matcher.groupCount(); j++)
//                System.out.println(matcher.group(j));
//        }
//        System.out.println(matcher.region(1,50).matches());
//        System.out.println(rs);
    }

}
