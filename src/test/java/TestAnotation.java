import com.cyl.court.anotation.Resolver;

public class TestAnotation {

  public static void main(String[] args) {
    System.out.println(Resolver.class.getAnnotations()[0].toString());
  }

}
