package couponRest.customerWS;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import couponSys.beans.Coupon;
import couponSys.beans.Coupon.CouponType;
import couponSys.beans.Customer;
import couponSys.couponsystem.CouponSystem;
import couponSys.couponsystem.CouponSystem.ClientType;
import couponSys.exception.ConException;
import couponSys.exception.CouponsSysException;
import couponSys.exception.FacadeExeption;
import couponSys.facade.AdminFacade;
import couponSys.facade.CouponClientFacade;
import couponSys.facade.CustomerFacade;
import wrapBeans.CustomerWrap;

@Path("/customer")
public class CustomerWS {

	@Context
	private HttpServletRequest req;
	
	
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
	@Path("purchase/{couponID}")
	@Produces(MediaType.TEXT_PLAIN)
	public String purchase(@PathParam("couponID") long id) throws CouponsSysException  {
		HttpSession session = req.getSession(false);
		if (session != null) {
				CustomerFacade customer = (CustomerFacade) session.getAttribute("customerFacade");
				Coupon c =new Coupon();
				c = customer.getCoupon(id);
				customer.purchaseCoupon(c);
	

			

				return "purchase successfull";
		}
		return null;
	}
	
	@GET
	@Path("purchased")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getAllPurchasedCoupons()throws CouponsSysException {
		HttpSession session = req.getSession(false);
		if (session != null){
			CustomerFacade customer = (CustomerFacade) session.getAttribute("customerFacade");
			
				return customer.getALLPurchasedCoupons();
				
		}
		return null;
	}

	
	@GET
	@Path("customerInfo")
	@Produces(MediaType.APPLICATION_JSON)
	public CustomerWrap getCustomer()throws CouponsSysException{
		HttpSession session = req.getSession(false);
		if (session != null){
			CustomerFacade customerF = (CustomerFacade) session.getAttribute("customerFacade");
			CustomerWrap custW = new CustomerWrap();
			Customer customerB= customerF.getCustomer();
			custW.convert(customerB);
			return custW;
		}
		return null;
	}
	@PUT
	@Path("update")
	@Produces(MediaType.TEXT_PLAIN)
	public String updCustomer(@QueryParam ("username")String custName, @QueryParam ("password") String password ) throws  CouponsSysException{
		HttpSession session =req.getSession(false);
		if (session!=null){
			CustomerFacade customerFacade = (CustomerFacade) session.getAttribute("customerFacade");
			Customer c = customerFacade.getCustomer();
			System.out.printf(custName,password);
			if(custName !=null) c.setCustName(custName);
			if(password !=null) c.setPassword(password);
			customerFacade.updateCustomer(c);
			return "updated";
		}
		return null;
	}
	

	@GET
	@Path("entire")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Coupon> entireCoupons()throws CouponsSysException{
		HttpSession session = req.getSession(false);
		if (session != null){
			CustomerFacade customer = (CustomerFacade) session.getAttribute("customerFacade");
				return customer.entireCoupons();
		}
		return null;
	}
	
	@GET
	@Path("entire/{price}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Coupon> entireCouponsPrice(@PathParam("price") double price)throws CouponsSysException{
		HttpSession session = req.getSession(false);
		if (session != null){
			
			CustomerFacade customer = (CustomerFacade) session.getAttribute("customerFacade");
				return customer.entireCouponsByPrice(price);
		}
		return null;
	}
	
	@GET
	@Path("entirebytype/{type}")
	@Consumes (MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Coupon> entireCouponsType(@PathParam("type") String type)throws CouponsSysException{
		System.out.println("here for sale");
		HttpSession session = req.getSession(false);
		if (session != null){
	
			CustomerFacade customer = (CustomerFacade) session.getAttribute("customerFacade");
			
				return customer.entireCouponsByType(CouponType.valueOf(type));
			}

		return null;
	}
}
