package couponRest.companyWS;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import couponSys.beans.Company;
import couponSys.beans.Coupon;
import couponSys.beans.Coupon.CouponType;
import couponSys.beans.Coupon.Status;
import couponSys.couponsystem.CouponSystem;
import couponSys.couponsystem.CouponSystem.ClientType;
import couponSys.exception.ConException;
import couponSys.exception.CouponsSysException;
import couponSys.exception.FacadeExeption;
import couponSys.facade.CompanyFacade;
import wrapBeans.*;

@Path("/company")
public class CompanyWS {
	
	@Context
	private HttpServletRequest req;
	
	@GET
	public String start(){
		HttpSession session = req.getSession(false);
		if(session != null){
			session.invalidate();
		}
		
		session = req.getSession(true);
		return session.getId();
	}
	
	// TODO  NEW METHOD - 7.12.2016
	@GET
	@Path("/getcoupontypes/")
	@Produces(MediaType.APPLICATION_JSON)
	public CouponType[] getCouponTypes(){
		CouponType[] couponTypes = CouponType.class.getEnumConstants();
		return couponTypes;
	}
	
	

	
	// TODO 
//	@GET
//	@Path("/login/{username}/{password}")
//	@Produces(MediaType.TEXT_PLAIN)
//	public String login(@PathParam ("username") String username, @PathParam ("password") String password){
//		
//		HttpSession session =req.getSession(false);
//		if (session!=null){
//			session.invalidate();
//		}
//		
//		try {
//		session = req.getSession(true);
//		CouponSystem cs = CouponSystem.getCouponSystem();
//		CompanyFacade facade;
//			facade = (CompanyFacade)cs.login(username, password, ClientType.COMPANY);
//		
//			session.setAttribute("companyFacade",facade);
//			System.out.println("Company "+username+" is logged in.");
//			return "Company "+username+" is logged in.";
//		} catch (CouponsSysException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "Company "+username+" is not logged in.";
//		
//	}
	
	//http://localhost:8080/EKLWS/rest/company/addcoup/f34/20150413/20170413/955/TRAVELLING/eran/8000.00/eran/true/COUPON_USABLE
	@POST
	@Path("/addcoup/{title}/{startDate}/{endDate}/{amount}/{type}/{message}/{price}/")
	@Produces(MediaType.TEXT_PLAIN)
	public String createCoupon(
			@PathParam ("title") String title,
			@PathParam ("startDate") String startDate,
			@PathParam ("endDate") String endDate,
			@PathParam ("amount") String amount,
			@PathParam ("type") String type,
			@PathParam ("message") String message,
			@PathParam ("price") String price) throws CouponsSysException{
		HttpSession session =req.getSession(false);
		if (session!=null){
			
			Coupon coupon = setUpCoupon(title, startDate, endDate, amount, type, message, price);
			
			CompanyFacade cf = (CompanyFacade) session.getAttribute("companyFacade");
			cf.createCoupon(coupon);
			return "Coupon was created";
		}
		return "Coupon was not created";
	}
	
	//http://localhost:8080/EKLWS/rest/company/remove/40
	@PUT
	@Path("/remove/{couponId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String removeCoupon(@PathParam("couponId") int couponId)  throws CouponsSysException{
		HttpSession session = req.getSession(false);
		if(session!=null){
			CompanyFacade cf = (CompanyFacade)session.getAttribute("companyFacade");
			Coupon coupon;
			coupon = cf.getCoupon(couponId);
			cf.removeCoupon(coupon);
			return "Coupon "+couponId+" was removed";
		}
		return "Coupon "+couponId+" was not removed";
		
	}
	
	//http://localhost:8080/EKLWS/rest/company/updatecoupon/40/ghk78/20150613/20170613/325/TRAVELLING/eran/3454.00/eran/true/COUPON_USABLE/
	@PUT
	@Path("/updatecoupon/{id}/")
	@Produces(MediaType.TEXT_PLAIN)
	public String updateCoupon(
			@PathParam ("id") long id,
			@QueryParam ("endDate") long endDate,
			@QueryParam ("amount") int amount,
			@QueryParam ("message") String message,
			@QueryParam ("price") double price)  throws CouponsSysException{
		
		HttpSession session = req.getSession(false);
		if(session!=null){
			CompanyFacade cf = (CompanyFacade)session.getAttribute("companyFacade");
			Coupon coupon = new Coupon();
			coupon = cf.getCoupon(id);
			coupon.setEndDate(new Date(endDate));
			coupon.setAmount(amount);
			coupon.setMessage(message);
			coupon.setPrice(price);;
			cf.updateCoupon(coupon);
			return "Coupon was updated";
		}
		return "Coupon was not updated";
	}
	
	//http://localhost:8080/EKLWS/rest/company/activatecoupon/40
	@PUT
	@Path("/activatecoupon/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String activateCoupon(@PathParam("id") long id)  throws CouponsSysException{
		HttpSession session = req.getSession(false);
		if (session!=null){
			CompanyFacade cf = (CompanyFacade)session.getAttribute("companyFacade");
			Coupon coupon = cf.getCoupon(id);
			cf.activateCoupon(coupon);
			return "Coupon activated";
		}
		return "Coupon was not activated";
	}
	// TODO ///////////////DONE ANGJS
	@GET
	@Path("/getcompany")
	@Produces(MediaType.APPLICATION_JSON)
	public CompanyWrap getCompany()  throws CouponsSysException{
		
		HttpSession session = req.getSession(false);
		CompanyWrap cw = new CompanyWrap();
		if (session!=null){
			CompanyFacade cf = (CompanyFacade)session.getAttribute("companyFacade");
			cw.convert(cf.getCompany());
		}
		return cw;
	}
	
	@PUT
	@Path("/updatecompany")
	@Produces(MediaType.TEXT_PLAIN)
	public String updateCompany(
			@QueryParam("mail") String mail,
			@QueryParam("pass") String pass)  throws CouponsSysException{
		HttpSession session = req.getSession(false);
		if (session!=null){
			CompanyFacade cf = (CompanyFacade)session.getAttribute("companyFacade");
			Company comp = cf.getCompany();
			if (mail != null) comp.setEmail(mail);
			if (pass != null) comp.setPassword(pass);
			cf.updateCompany(comp);
			return "Company updated";
		}
		return "Company was not updated";
	}
	
	// TODO ///////////////DONE ANGJS
	@GET
	@Path("/allcoupons")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getAllCoupons() throws CouponsSysException{
		HttpSession session = req.getSession(false);
		Collection<Coupon> coupons = new ArrayList<>();
		Collection<Coupon> cw = new ArrayList<>();
		if (session!=null){
			CompanyFacade cf = (CompanyFacade)session.getAttribute("companyFacade");
			coupons = cf.getAllCoupons();
			for (Coupon coupon : coupons) {
				cw.add(coupon);
			}
		}
		return cw;
	}
	
	//http://localhost:8080/EKLWS/rest/company/couponbytype/TRAVELLING
	@GET
	@Path("/couponbytype/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getCouponByType(@PathParam("type") String type) throws CouponsSysException{
		HttpSession session = req.getSession(false);
		Collection<Coupon> coupons = new ArrayList<>();
		Collection<Coupon> cw = new ArrayList<>();
		
		if (session!=null){
			CompanyFacade cf = (CompanyFacade)session.getAttribute("companyFacade");
			CouponType ct = CouponType.valueOf(CouponType.class, type);
			coupons = cf.getCouponByType(ct);
			for (Coupon coupon : coupons) {
				cw.add(coupon);
			}
		}
		return cw;
	}
	
	
	@GET
	@Path("/couponbyprice/{price}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getCouponByTopPrice(@PathParam("price") double price) throws CouponsSysException{
		HttpSession session = req.getSession(false);
		Collection<Coupon> coupons = new ArrayList<>();
		Collection<Coupon> cw = new ArrayList<>();
		if (session!=null){
			CompanyFacade cf = (CompanyFacade)session.getAttribute("companyFacade");
			coupons = cf.getCouponByTopPrice(price);
			for (Coupon coupon : coupons) {
				cw.add(coupon);
			}
		}
		return cw;
	}
	
	//http://localhost:8080/EKLWS/rest/company/couponbydate/20170413/
	@GET
	@Path("/couponbydate/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getCouponByDate(@PathParam("date") String date) throws CouponsSysException{
		HttpSession session = req.getSession(false);
		Collection<Coupon> coupons = new ArrayList<>();
		Collection<Coupon> cw = new ArrayList<>();
		if (session!=null){
			CompanyFacade cf = (CompanyFacade)session.getAttribute("companyFacade");
				Date sqleDate = new Date(Long.parseLong(date));
				coupons = cf.getCouponByDate(sqleDate);
				for (Coupon coupon : coupons) {
					cw.add(coupon);
				}
		}
		return cw;
	}
	
	@GET
	@Path("/activecoupons")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getActiveCoupons() throws CouponsSysException{
		HttpSession session = req.getSession(false);
		Collection<Coupon> coupons = new ArrayList<>();
		Collection<Coupon> cw = new ArrayList<>();
		if (session!=null){
			CompanyFacade cf = (CompanyFacade)session.getAttribute("companyFacade");
			coupons = cf.getActiveCoupons();
			for (Coupon coupon : coupons) {
				cw.add(coupon);
			}
		}
		return cw;
	}
	
	// TODO ///////////////DONE ANGJS
	// TODO BUG - RETURNED COUPONS: Active = TRUE
	@GET
	@Path("/inactcoupons")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getInactiveCoupons() throws CouponsSysException{
		HttpSession session = req.getSession(false);
		Collection<Coupon> coupons = new ArrayList<>();
		List<Coupon> cw = new ArrayList<>();
		if (session!=null){
			CompanyFacade cf = (CompanyFacade)session.getAttribute("companyFacade");
			coupons = cf.getInactiveCoupons();
			for (Coupon coupon : coupons) {
				cw.add(coupon);
			}
		}
		return cw;
	}
	
	
	@GET
	@Path("/getcoupon/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon getCoupon(@PathParam("id") long id) throws CouponsSysException{
		HttpSession session = req.getSession(false);
		Coupon coupon = null;

		if (session!=null){
			CompanyFacade cf = (CompanyFacade)session.getAttribute("companyFacade");
			coupon = cf.getCoupon(id);
		}
		return coupon;
	}
	
	private Coupon setUpCoupon(
			String title,
			String startDate,
			String endDate,
			String amount,
			String type,
			String message,
			String price){
		
		Coupon coupon = new Coupon();
		CouponType cType = CouponType.valueOf(CouponType.class, type);


		
		coupon.setTitle(title);
		coupon.setType(cType);
		coupon.setAmount(Integer.parseInt(amount));
		coupon.setPrice(Double.parseDouble(price));
		coupon.setMessage(message);
		
		
		// TODO - added handling dates from millis to sql date
		Date sqlsDate = new Date(Long.parseLong(startDate));
		Date sqleDate = new Date(Long.parseLong(endDate));
		
		coupon.setStartDate(sqlsDate);
		coupon.setEndDate(sqleDate);
		
		//TODO
				//		
				//		
				//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
				//		java.util.Date parsed;
				//		java.sql.Date sql = null;
				//		try {
				//
				//			parsed = format.parse(startDate);
				//			sql = new java.sql.Date(parsed.getTime());
				//			coupon.setStartDate(sql);
				//			parsed = format.parse(endDate);
				//			sql = new java.sql.Date(parsed.getTime());
				//			coupon.setEndDate(sql);
				//		} catch (ParseException e) {
				//			// TODO Auto-generated catch block
				//			e.printStackTrace();
				//		}
				////		coupon.setStartDate(setDate(startDate));
				////		coupon.setStartDate(setDate(endDate));
		
		return coupon;
	}
	
	
//	public java.sql.Date setDate(String date){
//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//		java.util.Date parsed;
//		java.sql.Date sql = null;
//		try {
//			parsed = format.parse(date);
//			sql = new java.sql.Date(parsed.getTime());
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return sql;
//	}
//	
}