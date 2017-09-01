package vertx.system;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.vertx.core.Vertx;

/**
 * 启动类
 * @author windows7
 *
 */
public class UserRunner
{
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		final Vertx vertx = Vertx.vertx();
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
		vertx.deployVerticle(new UserVerticle(context));
	    System.out.println("Deployment done");
	}

}

