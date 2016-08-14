package session.issue

import grails.rest.RestfulController
import grails.transaction.Transactional
import grails.web.http.HttpHeaders
import org.grails.datastore.mapping.multitenancy.web.SessionTenantResolver
import org.springframework.beans.factory.annotation.Autowired

import static org.springframework.http.HttpStatus.CREATED
import static grails.gorm.multitenancy.Tenants.withCurrent

class PersonController extends RestfulController<Person> {

    static responseFormats = ['json', 'xml']

    @Autowired
    PersonService personService

    PersonController() {
        super(Person)
    }

    /**
     * This action work as expected. Note that @Transactional is commented.
     *
     * @return
     */
    @Override
    //@Transactional
    def save() {
        withCurrent {
            Person person = createResource()
            person = personService.save(person) // NOTE The service is marked @Transactional
            if (person.hasErrors()) {
                respond person.errors, view: 'create' // STATUS CODE 422
            } else {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link(resource: this.controllerName, action: 'show', id: person.id, absolute: true))
                respond person, [status: CREATED, view: 'show']
            }
        }
    }

    /**
     * This action failed with:
     * Caused by IllegalTransactionStateException: Pre-bound JDBC Connection found! HibernateTransactionManager
     * does not support running within DataSourceTransactionManager if told to manage the DataSource itself.
     * It is recommended to use a single HibernateTransactionManager for all transactions on a single DataSource,
     * no matter whether Hibernate or JDBC access.
     * -&gt;&gt;   93 | execute   in grails.transaction.GrailsTransactionTemplate
     *
     * @return
     */
    @Transactional
    def save2() {
        withCurrent {
            Person person = createResource()
            person = personService.save(person) // NOTE The service is ALSO marked @Transactional
            if (person.hasErrors()) {
                respond person.errors, view: 'create' // STATUS CODE 422
            } else {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link(resource: this.controllerName, action: 'show', id: person.id, absolute: true))
                respond person, [status: CREATED, view: 'show']
            }
        }
    }

    /**
     * This action works. Note that GORMs multi-tenancy support is not used here. We set tenantId manually.
     *
     * @return
     */
    @Transactional
    def save3() {
        //withCurrent {
            Person person = createResource()
            person.tenantId = request.session[SessionTenantResolver.ATTRIBUTE]
            person = personService.save(person)
            if (person.hasErrors()) {
                respond person.errors, view: 'create' // STATUS CODE 422
            } else {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link(resource: this.controllerName, action: 'show', id: person.id, absolute: true))
                respond person, [status: CREATED, view: 'show']
            }
        //}
    }
}
