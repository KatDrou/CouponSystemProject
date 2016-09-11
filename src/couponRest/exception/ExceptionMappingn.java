package couponRest.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.derby.tools.sysinfo;

import com.sun.jersey.spi.container.ExceptionMapperContext;

import couponSys.exception.CouponsSysException;

@Provider
public class ExceptionMappingn implements ExceptionMapper<CouponsSysException> {

	@Override
	public Response toResponse(CouponsSysException e) {
		System.out.println("ExceptionMapper");
		return Response.status(500).entity(e.getMessage()).type(MediaType.TEXT_HTML).build();
	}

}
