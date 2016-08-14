package session.issue

import grails.gorm.multitenancy.Tenants
import grails.transaction.Transactional
import groovy.transform.CompileStatic

@CompileStatic
@Transactional
class PersonService {

    def save(Person person) {
        if (!person.tenantId) {
            person.tenantId = Tenants.currentId().toString()
        }
        person.save()
    }
}
