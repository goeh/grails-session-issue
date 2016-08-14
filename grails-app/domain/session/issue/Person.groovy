package session.issue

import grails.gorm.MultiTenant

class Person implements MultiTenant<Person> {

    String tenantId
    String firstName
    String lastName

    static constraints = {
    }
}
