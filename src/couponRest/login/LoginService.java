package couponRest.login;

import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import couponSys.couponsystem.CouponSystem;
import couponSys.couponsystem.CouponSystem.ClientType;
import couponSys.exception.ConException;
import couponSys.exception.CouponsSysException;
import couponSys.facade.AdminFacade;
import couponSys.facade.CompanyFacade;
import couponSys.facade.CouponClientFacade;
//
//@Path("/login")
//public class LoginService {
//
//	@Context
//	private HttpServletRequest req;
//	
//	@GET
//	public String start(){
//		HttpSession session =req.getSession(false);
//		if (session!=null){
//			session.invalidate();
//		}
//		
//		session = req.getSession(true);
//		return ""+session.getId();
//	}
//	
//	
//	
//	//http://localhost:8080/EKLWS/rest/login/admin/1234/admin/
//	@GET
//	@Path("/{username}/{password}/{clientType}")
//	@Produces(MediaType.TEXT_PLAIN)
//	public String login(@PathParam("username") String user,@PathParam("password") String pass,@PathParam("clientType") String type ){
//		HttpSession session =req.getSession(false);
//		if (session!=null){
//			session.invalidate();
//		}
//		
//		session = req.getSession(true);
//		try {
//			CouponClientFacade facade  = CouponSystem.getCouponSystem().login(user, pass, ClientType.ADMIN);
//			session.setAttribute("facade", facade);
//			AdminFacade a = (AdminFacade) session.getAttribute("facade");
//			System.out.println(a.getAllCompanies());
//			return a.getAllCompanies().toString();
//		} catch (ConException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (CouponsSysException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "not logged";
//		
//	}
import couponSys.facade.CustomerFacade;

@Path ("/login")
public class LoginService {

	@Context
	private HttpServletRequest req;
	@Context
	private HttpServletResponse res;
	
	@GET
	public String start(){
		HttpSession session =req.getSession(false);
		if (session!=null){
			session.invalidate();
		}
		
		session = req.getSession(true);
		return ""+session.getId();
	}
	
		
	@GET
	@Path("/{username}/{password}/{clientType}")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@PathParam("username") String userName,@PathParam("password") String password,@PathParam("clientType") String type )throws CouponsSysException{
	
			
		HttpSession session =req.getSession(false);
		
		if (session!=null){
			session.invalidate();
		}

		session = req.getSession(true);
		
		CouponSystem cSys=CouponSystem.getCouponSystem();
		ClientType ct = ClientType.valueOf(type);
		session.setAttribute("type", type); 
		switch (ct){
		
		case ADMIN:	
						AdminFacade clientAdmin= (AdminFacade) cSys.login(userName, password, ct);
						session.setAttribute("adminFacade", clientAdmin);
						session.setAttribute("admin", userName); 

						break;
				
		case COMPANY:	CompanyFacade clientComp= (CompanyFacade) cSys.login(userName, password, ct);
						session.setAttribute("companyFacade", clientComp);
						session.setAttribute("company", userName);
						break;
		case CUSTOMER:	CustomerFacade clientCust= (CustomerFacade) cSys.login(userName, password, ct);
						session.setAttribute("customerFacade", clientCust);
						session.setAttribute("customer", userName);
						break;	
						
		default: 
			
				return "You choose an unvalid option" ;
					
			
		}
			
		
		
		session.setAttribute("couponSystem", cSys);
		return type;
		
		}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("gohome")
	public Response goHome() throws URISyntaxException  {
		HttpSession session =req.getSession(false);
		String loc=null;
		String type;
		System.out.println(session);
		type= (String)session.getAttribute("type");
		System.out.println(type);
		if (session == null || type == null ){
			 loc = "http://localhost:8080/EKLWS";
			 
		} else {
			ClientType ct = ClientType.valueOf(type);
			
			switch (ct) {
				case ADMIN:
					loc = "http://localhost:8080/EKLWS/admin.html";
					break;
				case COMPANY:
					loc = "http://localhost:8080/EKLWS/company.html";
					break;
				case CUSTOMER:
					loc = "http://localhost:8080/EKLWS/customer.html";
					break;	
				default: 
					break;
					
			}
		}
		
		java.net.URI location = new java.net.URI(loc);
	    return Response.temporaryRedirect(location).build();

	}


	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("off")
	public void logout()  {
		req.getSession(false).invalidate();
	}
}
