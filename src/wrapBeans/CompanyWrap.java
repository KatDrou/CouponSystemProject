package wrapBeans;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import couponSys.beans.Company;
import couponSys.beans.Coupon;

@XmlRootElement
public class CompanyWrap {
	
	private long id;
	private String compName;
	private String email;
	private Collection<Coupon> coupons;
	
	public CompanyWrap() {
		super();
	}
	@XmlElement
	public Collection<Coupon> getCoupons() {
		return coupons;
	}



	@XmlElement
	public long getId() {
		return id;
	}


	@XmlElement
	public String getCompName() {
		return compName;
	}


	@XmlElement
	public String getEmail() {
		return email;
	}

	

	

	@Override
	public String toString() {
		return "CompanyWrap [id=" + id + ", compName=" + compName + ", email=" + email + "]";
	}

	public CompanyWrap convert(Company c){
		
		CompanyWrap cw = new CompanyWrap();
		this.id = c.getId();
		this.compName = c.getCompName();
		this.email = c.getEmail();
		this.coupons = c.getCoupons();
		return cw;
	}
	
	
	
}