package vertx.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.UserService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.shiro.ShiroAuth;
import io.vertx.ext.auth.shiro.ShiroAuthRealmType;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.FormLoginHandler;
import io.vertx.ext.web.handler.RedirectAuthHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.UserSessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

/**
 * UserVerticle
 * @author liuxincheng
 *
 */
public class UserVerticle extends AbstractVerticle
{
	Router router = Router.router(vertx);

	public static final String ALL_PRODUCTS_ADDRESS = "example.all.products";

	// Reuse the Vert.x Mapper, alternatively you can use your own.
	private final ObjectMapper mapper = Json.mapper;
	
	@SuppressWarnings("deprecation")
	AuthProvider authProvider = ShiroAuth.create(vertx, ShiroAuthRealmType.PROPERTIES, new JsonObject());

	/**
	 * 用户服务
	 */
	@Autowired
	private UserService service;

	public UserVerticle(final ApplicationContext context)
	{
		service = (UserService) context.getBean("userService");
	}

	@Override
	public void start() throws Exception
	{
		router = Router.router(vertx);
		UserVerticle that = this;

		router.route().handler(CookieHandler.create());
		router.route().handler(BodyHandler.create());
		router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));

		router.route().handler(UserSessionHandler.create(authProvider));

		/**
		 * 登陆系统
		 */
		router.post("/login").handler(that::LoginSystem);
		router.route("/welcome").handler(RedirectAuthHandler.create(authProvider, "/page2.html"));
		/**
		 * 注册用户
		 */
		router.post("/register").handler(that::Register);

		// 退出系统
		router.route("/logout").handler(context ->
		{
			context.clearUser();
			// Redirect back to the index page
			context.response().putHeader("location", "/").setStatusCode(302).end();
		});

		// Serve the non private static pages
		router.route().handler(StaticHandler.create());

		vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	}

	/**
	 * 登陆系统
	 * 
	 * @param routingContext
	 */
	private void LoginSystem(RoutingContext routingContext)
	{
		HttpServerResponse response = routingContext.response();

		// SQLConnection conn = routingContext.get("conn");
		JsonObject bodyContent = routingContext.getBodyAsJson();

		String userName = bodyContent.getString("userName");
		String passWord = bodyContent.getString("passWord");

		/**
		 * 通过条件查询数据库
		 */
		
		response.putHeader("location", "/welcome").setStatusCode(200);
		System.out.println("in login block ........................");
		response.end();
	}

	/**
	 * 注册用户
	 * 
	 * @param routingContext
	 */
	private void Register(RoutingContext routingContext)
	{

		HttpServerResponse response = routingContext.response();

		SQLConnection conn = routingContext.get("conn");
		JsonObject userContent = routingContext.getBodyAsJson();

		/*
		 * conn.
		 * updateWithParams("INSERT INTO products (name, price, weight) VALUES (?, ?, ?)"
		 * , new JsonArray()
		 * .add(userContent.getString("name")).add(userContent.getFloat("price")).add(
		 * userContent.getInteger("weight")), query -> { if (query.failed()) {
		 * sendError(500, response); } else { response.end(); } });
		 */

		System.out.println("add running ..................");
	}

	/**
	 * 发送错误信息
	 * 
	 * @param statusCode
	 * @param response
	 */
	private void sendError(int statusCode, HttpServerResponse response)
	{
		response.setStatusCode(statusCode).end();
	}
}
