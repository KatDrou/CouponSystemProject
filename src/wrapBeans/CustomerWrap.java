package wrapBeans;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import couponSys.beans.Coupon;
import couponSys.beans.Customer;

@XmlRootElement
public class CustomerWrap {
	
	private long id;
	private String custName;
	private Collection<Coupon> coupons;
	
	public CustomerWrap() {
		super();
	}
		
	@XmlElement
	public long getId() {
	return id;
	}
	
	@XmlElement
	public String getCustName() {
		return custName;
	}
	
	@XmlElement
	public Collection<Coupon> getCoupons() {
		return coupons;
	}

	public CustomerWrap convert(Customer cs){
		
			CustomerWrap csw = new CustomerWrap();
			this.id = cs.getId();
			this.custName = cs.getCustName();
			this.coupons = cs.getCoupons();
			
			return csw;
		}
		



}
