package net.cworks.app.web.filter.gzip;

import javax.servlet.ServletException;

public class GzipResponseHeadersNotModifiableException
    extends ServletException {

    public GzipResponseHeadersNotModifiableException(String message) {
        super(message);
    }
}
