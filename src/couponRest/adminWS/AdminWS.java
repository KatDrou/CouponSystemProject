package couponRest.adminWS;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import couponSys.beans.Customer;
import couponSys.couponsystem.CouponSystem;
import couponSys.couponsystem.CouponSystem.ClientType;
import couponSys.exception.ConException;
import couponSys.exception.CouponsSysException;
import couponSys.exception.FacadeExeption;
import couponSys.facade.AdminFacade;
import couponSys.facade.CouponClientFacade;
import wrapBeans.CompanyWrap;
import wrapBeans.CustomerWrap;

@Path("admin")
public class AdminWS {

	@Context
	private HttpServletRequest req;

	@GET
	public String start() {
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		session = req.getSession(true);
		return "" + session.getId();
	}

	@POST
	@Path("addcomp/{compName}/{password}/{compEmail}")
	@Produces(MediaType.TEXT_PLAIN)
	public String addCompany(@PathParam("compName") String compName, @PathParam("password") String password,
			@PathParam("compEmail") String compEmail) throws CouponsSysException {
		HttpSession session = req.getSession(false);

		if (session != null) {
			AdminFacade admin = (AdminFacade) session.getAttribute("adminFacade");
			Company c = new Company();
			c.setCompName(compName);
			c.setEmail(compEmail);
			c.setPassword(password);
			admin.createCompany(c);			
		}
		return "company added";
	}

	@DELETE
	@Path("delcomp/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String delCompany(@PathParam("id") long id) throws CouponsSysException {

		HttpSession session = req.getSession(false);
		if (session != null) {

			System.out.println("here");
			AdminFacade admin = (AdminFacade) session.getAttribute("adminFacade");
			Company c = admin.getCompany(id);
			admin.removeCompany(c);

		}
		return "company deleted";

	}

	@PUT
	@Path("updcomp/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String updCompany(@PathParam("id") long id, @QueryParam("password") String password,
			@QueryParam("email") String compEmail)throws CouponsSysException {
		HttpSession session = req.getSession(false);
		if (session != null) {

				AdminFacade admin = (AdminFacade) session.getAttribute("adminFacade");
				Company c = admin.getCompany(id);

					c.setPassword(password);
					c.setEmail(compEmail);
				admin.updateCompany(c);
				}
		return "company updated";
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getcomp/{id}")
	public CompanyWrap getCompany(@PathParam("id") Long id)throws CouponsSysException {
		HttpSession session = req.getSession(false);
		if (session != null) {

		AdminFacade admin = (AdminFacade) req.getSession(false).getAttribute("adminFacade");
		CompanyWrap cw = new CompanyWrap();
			cw.convert(admin.getCompany(id));
			
		return cw;
		}
		return null;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getallcomp")

	public Collection<CompanyWrap> getAllCompanies() throws CouponsSysException {
		HttpSession session = req.getSession(false);
		if (session != null) {
			AdminFacade admin = (AdminFacade) req.getSession(false).getAttribute("adminFacade");
			Collection<Company> comps;
			comps = admin.getAllCompanies();

			Collection<CompanyWrap> compsw = new ArrayList();
			for (Company curr : comps) {
				CompanyWrap cw = new CompanyWrap();
				cw.convert(curr);
				compsw.add(cw);
			}
				return compsw;
		}
			return null;

	}

	@POST
	@Path("addcust/{custName}/{password}/")
	@Produces(MediaType.TEXT_PLAIN)
	public String addCustomer(@PathParam("custName") String custName, @PathParam("password") String password) throws CouponsSysException{
		HttpSession session = req.getSession(false);

		if (session != null) {

			AdminFacade admin = (AdminFacade) session.getAttribute("adminFacade");
			Customer c = new Customer();
			c.setCustName(custName);
			c.setPassword(password);
				admin.createCustomer(c);
		}
		return "customer added";
	}

	@DELETE
	@Path("delcust/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String delCustomer(@PathParam("id") long id) throws CouponsSysException {

		HttpSession session = req.getSession(false);
		if (session != null) {

			AdminFacade admin = (AdminFacade) session.getAttribute("adminFacade");
				Customer c = admin.getCustomer(id);
				System.out.println(c);
				admin.removeCustomer(c);
				System.out.println("customer deleted");
		}

		return "customer deleted";

	}

	@PUT
	@Path("updcust/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String updCustomer(@PathParam("id") long id, @QueryParam("name") String custName,
			@QueryParam("password") String password) throws CouponsSysException {
		HttpSession session = req.getSession(false);
		if (session != null) {

			AdminFacade admin = (AdminFacade) session.getAttribute("adminFacade");
			Customer c = admin.getCustomer(id);
				c.setPassword(password);
				c.setCustName(custName);
				admin.updateCustomer(c);
		}
		return "customer updated";
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getcust/{id}")
	public CustomerWrap getCustomer(@PathParam("id") Long id) throws CouponsSysException {
		HttpSession session = req.getSession(false);
		if (session != null) {
		AdminFacade admin = (AdminFacade) req.getSession(false).getAttribute("adminFacade");
		CustomerWrap csw = new CustomerWrap();
			csw.convert(admin.getCustomer(id));
		return csw;
		}
		return null;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getallcust")
	public Collection<CustomerWrap> getAllCustomers() throws CouponsSysException{
		HttpSession session = req.getSession(false);
		if (session != null) {
			AdminFacade admin = (AdminFacade) req.getSession(false).getAttribute("adminFacade");
			Collection<Customer> csts;
				csts = admin.getAllCustomers();
				Collection<CustomerWrap> cstsw = new ArrayList();
				for (Customer curr : csts) {
					CustomerWrap csw = new CustomerWrap();
	
					csw.convert(curr);
					cstsw.add(csw);
				}
				return cstsw;
			} 
		return null;
	}
}
