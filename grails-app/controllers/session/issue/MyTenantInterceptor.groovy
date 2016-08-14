package session.issue

import groovy.transform.CompileStatic
import org.grails.datastore.mapping.multitenancy.resolvers.SystemPropertyTenantResolver
import org.grails.datastore.mapping.multitenancy.web.SessionTenantResolver

/**
 * Set a fixed tenantId for testing.
 */
@CompileStatic
class MyTenantInterceptor {

    int order = HIGHEST_PRECEDENCE + 100

    MyTenantInterceptor() {
        matchAll()
    }

    boolean before() {
        if(params.tenant) {
            request.session[SessionTenantResolver.ATTRIBUTE] = params.tenant
        }
        true
    }

    boolean after() {
        true
    }

    void afterView() {
        //request.session.removeAttribute(SessionTenantResolver.ATTRIBUTE)
    }
}
