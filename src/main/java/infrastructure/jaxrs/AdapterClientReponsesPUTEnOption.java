package infrastructure.jaxrs;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

import infrastructure.langage.Types;

@Priority(Priorities.HEADER_DECORATOR + 2)
public class AdapterClientReponsesPUTEnOption implements ReaderInterceptor {

	@Override
	public Object aroundReadFrom(ReaderInterceptorContext contexte) throws IOException, WebApplicationException {
		if(contexte.getType() == Optional.class) {
			Type t = contexte.getGenericType();
			ParameterizedType gt = (ParameterizedType)t;
			Type ta = gt.getActualTypeArguments()[0];
			contexte.setType(Types.convertirTypeEnClasse(ta));
			return Optional.of(contexte.proceed());
		}
		return contexte.proceed();
	}

}
