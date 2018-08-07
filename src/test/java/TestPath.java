import java.io.File;
import java.io.UnsupportedEncodingException;

public class TestPath {

    public static void main(String[] args) {

//         String pathName = "com/cyl/court/resource/";

        String jarWholePath = TestPath.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            jarWholePath = java.net.URLDecoder.decode(jarWholePath, "UTF-8");
        } catch (UnsupportedEncodingException e) { System.out.println(e.toString()); }
        String jarPath = new File(jarWholePath).getParentFile().getAbsolutePath();
        System.out.println(jarPath);

//        System.out.println(TestPath.class.getClassLoader().getResource(""));
    }

}
